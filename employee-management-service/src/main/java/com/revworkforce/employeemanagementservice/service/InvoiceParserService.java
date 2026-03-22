package com.revworkforce.employeemanagementservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import com.revworkforce.employeemanagementservice.dto.InvoiceParseResponse;
import com.revworkforce.employeemanagementservice.integration.OllamaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class InvoiceParserService {
    private static final Logger log = LoggerFactory.getLogger(InvoiceParserService.class);

    @Autowired
    private OllamaClient ollamaClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Set<String> VALID_CATEGORIES = Set.of(
            "TRAVEL", "MEALS", "ACCOMMODATION", "OFFICE_SUPPLIES", "EQUIPMENT",
            "SOFTWARE", "TRAINING", "CLIENT_ENTERTAINMENT", "COMMUNICATION",
            "MEDICAL", "TRANSPORTATION", "OTHER"
    );

    public InvoiceParseResponse parseUploadedFile(String base64Data, String fileType) {
        if (base64Data == null || base64Data.isBlank()) {
            return InvoiceParseResponse.builder()
                    .success(false).errorMessage("No file provided").build();
        }

        String cleanBase64 = base64Data.contains(",")
                ? base64Data.substring(base64Data.indexOf(",") + 1)
                : base64Data;

        boolean isPdf = fileType != null && fileType.toLowerCase().contains("pdf");

        if (isPdf) {
            return parsePdf(cleanBase64);
        } else {
            return parseImage(cleanBase64);
        }
    }

    private InvoiceParseResponse parsePdf(String base64Pdf) {
        try {
            byte[] pdfBytes = Base64.getDecoder().decode(base64Pdf);
            String extractedText;

            try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
                PDFTextStripper stripper = new PDFTextStripper();
                extractedText = stripper.getText(doc);
            }

            if (extractedText == null || extractedText.isBlank()) {
                return InvoiceParseResponse.builder()
                        .success(false).errorMessage("Could not extract text from PDF. The PDF might be image-based.").build();
            }

            log.info("PDF text extracted ({} chars). Running instant regex parser first...", extractedText.length());
            log.debug("Extracted PDF text (first 1500 chars):\n{}", extractedText.substring(0, Math.min(extractedText.length(), 1500)));

            InvoiceParseResponse regexResult = regexParse(extractedText);
            if (regexResult.isSuccess()) {
                boolean hasUsefulData = regexResult.getTotalAmount() != null
                        || regexResult.getVendorName() != null
                        || regexResult.getInvoiceNumber() != null;
                if (hasUsefulData) {
                    log.info("✅ Regex parser extracted (instant): vendor={}, amount={}, date={}, invoice={}, items={}",
                            regexResult.getVendorName(), regexResult.getTotalAmount(),
                            regexResult.getInvoiceDate(), regexResult.getInvoiceNumber(),
                            regexResult.getItems() != null ? regexResult.getItems().size() : 0);
                    return regexResult;
                }
            }

            if (ollamaClient.isAvailable()) {
                log.info("Regex incomplete — trying AI-powered extraction (truncated text)...");
                try {
                    String truncatedText = truncateForAi(extractedText, 1500);
                    InvoiceParseResponse aiResult = parseInvoice(truncatedText);
                    if (aiResult.isSuccess()) {
                        log.info("AI extraction succeeded: vendor={}, amount={}", aiResult.getVendorName(), aiResult.getTotalAmount());
                        return aiResult;
                    }
                } catch (Exception e) {
                    log.warn("AI extraction failed: {}", e.getMessage());
                }
            }

            if (regexResult.isSuccess()) {
                return regexResult;
            }

            log.warn("Both regex and AI could not extract enough data from PDF.");
            return InvoiceParseResponse.builder()
                    .success(false)
                    .errorMessage("Could not extract data from this PDF. Please fill the form manually.")
                    .rawText(extractedText)
                    .build();
        } catch (IllegalArgumentException e) {
            log.error("Invalid base64 data: {}", e.getMessage());
            return InvoiceParseResponse.builder()
                    .success(false).errorMessage("Invalid file data. Please re-upload the PDF.").build();
        } catch (Exception e) {
            log.error("PDF processing failed: {}", e.getMessage(), e);
            return InvoiceParseResponse.builder()
                    .success(false).errorMessage("PDF processing failed: " + e.getMessage()).build();
        }
    }

    private String truncateForAi(String text, int maxChars) {
        if (text.length() <= maxChars) return text;

        int headerLen = (int) (maxChars * 0.6);
        int footerLen = maxChars - headerLen;
        String header = text.substring(0, headerLen);
        String footer = text.substring(text.length() - footerLen);
        return header + "\n...[truncated]...\n" + footer;
    }

    private InvoiceParseResponse parseImage(String base64Image) {
        if (!ollamaClient.isAvailable()) {
            log.warn("Ollama is not reachable — cannot process image invoice");
            return InvoiceParseResponse.builder()
                    .success(false)
                    .errorMessage("AI vision service is not available. Please upload a PDF instead for instant extraction, or start the Ollama service (ollama serve) and pull the vision model (ollama pull llava).")
                    .build();
        }

        try {
            String prompt = "Read this invoice/receipt. Reply with JSON only, no explanation.\n" +
                    "{\"title\":\"short title\",\"vendorName\":\"str\",\"invoiceNumber\":\"str\",\"invoiceDate\":\"YYYY-MM-DD\"," +
                    "\"totalAmount\":number,\"category\":\"TRAVEL|MEALS|ACCOMMODATION|OFFICE_SUPPLIES|EQUIPMENT|SOFTWARE|TRAINING|MEDICAL|TRANSPORTATION|OTHER\"," +
                    "\"description\":\"brief\",\"items\":[{\"description\":\"str\",\"amount\":number,\"quantity\":number}]}\nJSON:";

            String aiResponse = ollamaClient.generateWithImage(prompt, base64Image);

            if (aiResponse != null && !aiResponse.startsWith("Error")) {
                return parseAiResponse(aiResponse, "image-upload");
            }

            return InvoiceParseResponse.builder()
                    .success(false)
                    .errorMessage("Vision model could not read this image. Try uploading a PDF instead, or ensure the llava model is installed (ollama pull llava).")
                    .build();
        } catch (Exception e) {
            log.error("Image analysis failed: {}", e.getMessage());
            return InvoiceParseResponse.builder()
                    .success(false).errorMessage("Image analysis failed: " + e.getMessage()).build();
        }
    }

    public InvoiceParseResponse parseInvoice(String invoiceText) {
        if (invoiceText == null || invoiceText.isBlank()) {
            return InvoiceParseResponse.builder()
                    .success(false)
                    .errorMessage("No invoice text provided")
                    .build();
        }

        try {
            String prompt = buildExtractionPrompt(invoiceText);
            String aiResponse = ollamaClient.generate(prompt, 300);

            if (aiResponse == null || aiResponse.startsWith("Error")) {
                log.error("AI processing failed: {}", aiResponse);
                return InvoiceParseResponse.builder()
                        .success(false)
                        .errorMessage("AI processing failed: " + aiResponse)
                        .rawText(invoiceText)
                        .build();
            }

            return parseAiResponse(aiResponse, invoiceText);
        } catch (Exception e) {
            log.error("Failed to parse invoice: {}", e.getMessage());
            return InvoiceParseResponse.builder()
                    .success(false)
                    .errorMessage("Failed to parse invoice: " + e.getMessage())
                    .rawText(invoiceText)
                    .build();
        }
    }

    private String buildExtractionPrompt(String invoiceText) {
        return "Extract invoice data as JSON only. No explanation.\n" +
               "{\"title\":\"short title\",\"vendorName\":\"str\",\"invoiceNumber\":\"str\",\"invoiceDate\":\"YYYY-MM-DD\"," +
               "\"totalAmount\":number,\"category\":\"TRAVEL|MEALS|ACCOMMODATION|OFFICE_SUPPLIES|EQUIPMENT|SOFTWARE|TRAINING|MEDICAL|TRANSPORTATION|OTHER\"," +
               "\"description\":\"brief\",\"items\":[{\"description\":\"str\",\"amount\":number,\"quantity\":number}]}\n\n" +
               "Invoice:\n" + invoiceText + "\n\nJSON:";
    }

    private InvoiceParseResponse parseAiResponse(String aiResponse, String rawText) {
        try {
            String json = extractJson(aiResponse);
            log.info("Extracted JSON from AI response: {}", json.substring(0, Math.min(json.length(), 500)));
            JsonNode root = objectMapper.readTree(json);

            List<InvoiceParseResponse.ParsedItem> items = new ArrayList<>();
            if (root.has("items") && root.get("items").isArray()) {
                for (JsonNode itemNode : root.get("items")) {
                    items.add(InvoiceParseResponse.ParsedItem.builder()
                            .description(getTextOrNull(itemNode, "description"))
                            .amount(getDecimalOrNull(itemNode, "amount"))
                            .quantity(itemNode.has("quantity") ? itemNode.get("quantity").asInt(1) : 1)
                            .build());
                }
            }

            String category = getTextOrNull(root, "category");
            if (category != null) {
                category = category.toUpperCase().replace(" ", "_");
                if (!VALID_CATEGORIES.contains(category)) {
                    category = guessCategory(rawText);
                }
            }

            return InvoiceParseResponse.builder()
                    .success(true)
                    .title(getTextOrNull(root, "title"))
                    .vendorName(getTextOrNull(root, "vendorName"))
                    .invoiceNumber(getTextOrNull(root, "invoiceNumber"))
                    .invoiceDate(getTextOrNull(root, "invoiceDate"))
                    .totalAmount(getDecimalOrNull(root, "totalAmount"))
                    .currency(root.has("currency") ? root.get("currency").asText("INR") : "INR")
                    .category(category)
                    .description(getTextOrNull(root, "description"))
                    .items(items)
                    .rawText(rawText)
                    .build();
        } catch (Exception e) {
            log.error("Could not parse AI response as JSON: {}", e.getMessage());
            return InvoiceParseResponse.builder()
                    .success(false)
                    .errorMessage("Could not parse AI response as structured data. Please fill fields manually.")
                    .rawText(rawText)
                    .build();
        }
    }

    private InvoiceParseResponse regexParse(String text) {
        log.info("Running regex-based extraction on text ({} chars)", text.length());

        String vendorName = extractVendorName(text);
        String invoiceNumber = extractInvoiceNumber(text);
        String invoiceDate = extractDate(text);
        BigDecimal totalAmount = extractTotalAmount(text);
        String category = guessCategory(text);
        List<InvoiceParseResponse.ParsedItem> items = extractLineItems(text);

        if (invoiceNumber == null) {
            Matcher orderMatcher = Pattern.compile("(?i)(?:ORDER[_\\s-]*(?:INVOICE)?[_\\s-]*)([A-Z0-9]{5,30})")
                    .matcher(text);
            if (orderMatcher.find()) {
                invoiceNumber = orderMatcher.group(1);
            }
        }

        if (totalAmount == null && !items.isEmpty()) {
            totalAmount = items.stream()
                    .map(i -> i.getAmount() != null ? i.getAmount().multiply(BigDecimal.valueOf(i.getQuantity() != null ? i.getQuantity() : 1)) : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        String title = generateTitle(vendorName, category, totalAmount);

        String description = generateDescription(text, vendorName);

        int fieldsFound = 0;
        if (vendorName != null) fieldsFound++;
        if (invoiceNumber != null) fieldsFound++;
        if (invoiceDate != null) fieldsFound++;
        if (totalAmount != null) fieldsFound++;
        if (!items.isEmpty()) fieldsFound++;

        log.info("Regex extracted {} fields: vendor={}, invoice={}, date={}, amount={}, items={}",
                fieldsFound, vendorName, invoiceNumber, invoiceDate, totalAmount, items.size());

        if (fieldsFound == 0) {
            return InvoiceParseResponse.builder()
                    .success(false)
                    .errorMessage("Could not extract data from this PDF. Please fill the fields manually.")
                    .rawText(text)
                    .build();
        }

        return InvoiceParseResponse.builder()
                .success(true)
                .title(title)
                .vendorName(vendorName)
                .invoiceNumber(invoiceNumber)
                .invoiceDate(invoiceDate)
                .totalAmount(totalAmount)
                .currency("INR")
                .category(category)
                .description(description)
                .items(items)
                .rawText(text)
                .build();
    }

    private String generateDescription(String text, String vendorName) {
        String[] lines = text.split("\\r?\\n");
        List<String> meaningful = new ArrayList<>();
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.length() > 5 && trimmed.length() < 100 && meaningful.size() < 3
                    && !trimmed.matches("^[\\d\\s./-]+$")
                    && (vendorName == null || !trimmed.equals(vendorName))) {
                meaningful.add(trimmed);
            }
        }
        return meaningful.isEmpty() ? null : String.join("; ", meaningful);
    }

    private String extractVendorName(String text) {
        String lower = text.toLowerCase();
        if (lower.contains("rapido")) return "Rapido";
        if (lower.contains("amazon")) return "Amazon";
        if (lower.contains("flipkart")) return "Flipkart";
        if (lower.contains("myntra")) return "Myntra";
        if (lower.contains("swiggy")) return "Swiggy";
        if (lower.contains("zomato")) return "Zomato";
        if (lower.contains("uber eats")) return "Uber Eats";
        if (lower.contains("uber")) return "Uber";
        if (lower.contains("makemytrip") || lower.contains("make my trip")) return "MakeMyTrip";
        if (lower.contains("bigbasket") || lower.contains("big basket")) return "BigBasket";
        if (lower.contains("dunzo")) return "Dunzo";
        if (lower.contains("zepto")) return "Zepto";
        if (lower.contains("blinkit") || lower.contains("grofers")) return "Blinkit";
        if (lower.contains("ola cabs") || (lower.contains("ola") && containsAny(lower, "ride", "cab", "trip", "fare"))) return "Ola";
        if (lower.contains("phonepe")) return "PhonePe";
        if (lower.contains("paytm")) return "Paytm";
        if (lower.contains("meesho")) return "Meesho";
        if (lower.contains("nykaa")) return "Nykaa";
        if (lower.contains("jiomart")) return "JioMart";
        if (lower.contains("reliance")) return "Reliance";
        if (lower.contains("irctc")) return "IRCTC";
        if (lower.contains("redbus")) return "RedBus";
        if (lower.contains("cleartrip")) return "Cleartrip";
        if (lower.contains("goibibo")) return "Goibibo";
        if (lower.contains("dominos") || lower.contains("domino's")) return "Dominos";
        if (lower.contains("mcdonald")) return "McDonald's";
        if (lower.contains("starbucks")) return "Starbucks";

        String[] vendorPatterns = {
                "(?i)(?:sold\\s*by|seller\\s*(?:name)?|vendor\\s*(?:name)?|bill\\s*from|billed?\\s*by|merchant)\\s*[:\\-]?\\s*([^\\r\\n]+)",
                "(?i)(?:from|company|store|shop|restaurant|retailer)\\s*[:\\-]\\s*([^\\r\\n]+)"
        };

        for (String pattern : vendorPatterns) {
            Matcher m = Pattern.compile(pattern).matcher(text);
            if (m.find()) {
                String name = m.group(1).trim();
                name = name.split(",")[0].trim();
                name = name.replaceAll("\\s*\\(.*\\)\\s*$", "").trim();
                name = name.replaceAll("(?i)\\s*GST.*$", "").trim();

                if (!name.isEmpty() && name.length() >= 2 && name.length() < 60
                        && !name.toLowerCase().contains("not by")
                        && !name.toLowerCase().contains("private limited")
                        && !name.toLowerCase().contains("services")) {
                    return name;
                }
            }
        }

        String[] lines = text.split("\\r?\\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.length() > 3 && trimmed.length() < 80
                    && !trimmed.matches("(?i).*(?:invoice|receipt|bill\\s*no|tax|date|order\\s*id|order\\s*no|page|gst|total|amount|qty|hsn|payment|summary|\\d{2}/\\d{2}/\\d{4}).*")
                    && !trimmed.matches("^[\\d\\s.,/-]+$")
                    && !trimmed.matches("(?i)^(tax\\s+invoice|original|duplicate|copy|ride\\s*id|time|distance|duration).*")) {
                return trimmed;
            }
        }

        return null;
    }

    private String extractInvoiceNumber(String text) {
        Set<String> rejected = Set.of("details", "date", "summary", "amount", "total",
                "charge", "charges", "fee", "fees", "payment", "address", "name", "type",
                "number", "invoice", "bill", "receipt", "tax", "order", "ride", "booking",
                "category", "supply", "state", "using");

        Matcher m1 = Pattern.compile("(?i)invoice\\s+no\\.?\\s*[:\\-]?\\s*([0-9][A-Za-z0-9]{4,49})").matcher(text);
        while (m1.find()) {
            String num = m1.group(1).trim();
            if (isValidInvoiceNumber(num, rejected)) return num;
        }

        Matcher m2 = Pattern.compile("(?i)(?:ride|order|booking|txn)\\s*id\\s*[:\\-]?\\s*([A-Za-z0-9\\-/.]{5,50})").matcher(text);
        while (m2.find()) {
            String num = m2.group(1).trim();
            if (isValidInvoiceNumber(num, rejected)) return num;
        }

        Matcher m3 = Pattern.compile("(?i)(?:bill|receipt|ref|reference|txn)\\s*(?:no|number|num|#|id)\\.?\\s*[:\\-]?\\s*([A-Za-z0-9\\-/]{3,50})").matcher(text);
        while (m3.find()) {
            String num = m3.group(1).trim();
            if (isValidInvoiceNumber(num, rejected)) return num;
        }

        Matcher m4 = Pattern.compile("(?m)^\\s*([0-9]{2,}[A-Z]+[A-Z0-9]{3,})\\s*$").matcher(text);
        while (m4.find()) {
            String num = m4.group(1).trim();
            if (num.length() >= 8 && num.length() <= 50) return num;
        }

        return null;
    }

    private boolean isValidInvoiceNumber(String num, Set<String> rejected) {
        return num.length() >= 3 && num.length() <= 50
                && num.matches(".*\\d.*")
                && !rejected.contains(num.toLowerCase())
                && !num.matches("(?i)^(details|date|summary|amount|total|charge|invoice|bill|tax|Feb|Mar|Jan).*");
    }

    private String extractDate(String text) {
        String dateContext = text;
        Matcher dateLine = Pattern.compile("(?i)(?:date|dated|invoice\\s*date|bill\\s*date|order\\s*date|time\\s*of\\s*ride)[:\\s]*(.{0,80})").matcher(text);
        if (dateLine.find()) {
            dateContext = dateLine.group(1);
        }

        String result = tryExtractDate(dateContext);
        if (result != null) return result;

        if (!dateContext.equals(text)) {
            result = tryExtractDate(text);
        }
        return result;
    }

    private String tryExtractDate(String text) {
        Matcher mOrd1 = Pattern.compile(
                "(?i)(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[a-z]*\\s+(\\d{1,2})(?:st|nd|rd|th)?[,]?\\s+(\\d{4})"
        ).matcher(text);
        if (mOrd1.find()) {
            try {
                int day = Integer.parseInt(mOrd1.group(2));
                int month = monthToNumber(mOrd1.group(1));
                int year = Integer.parseInt(mOrd1.group(3));
                if (day >= 1 && day <= 31 && month >= 1 && month <= 12 && year >= 2000) {
                    return String.format("%d-%02d-%02d", year, month, day);
                }
            } catch (Exception ignored) {}
        }

        Matcher mOrd2 = Pattern.compile(
                "(?i)(\\d{1,2})(?:st|nd|rd|th)?\\s+(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[a-z]*[,]?\\s+(\\d{4})"
        ).matcher(text);
        if (mOrd2.find()) {
            try {
                int day = Integer.parseInt(mOrd2.group(1));
                int month = monthToNumber(mOrd2.group(2));
                int year = Integer.parseInt(mOrd2.group(3));
                if (day >= 1 && day <= 31 && month >= 1 && month <= 12 && year >= 2000) {
                    return String.format("%d-%02d-%02d", year, month, day);
                }
            } catch (Exception ignored) {}
        }

        Matcher m1 = Pattern.compile("(\\d{1,2})[/\\-](\\d{1,2})[/\\-](\\d{4})").matcher(text);
        if (m1.find()) {
            try {
                int a = Integer.parseInt(m1.group(1));
                int b = Integer.parseInt(m1.group(2));
                int year = Integer.parseInt(m1.group(3));
                if (year >= 2000) {
                    if (a > 12) return String.format("%d-%02d-%02d", year, b, a);
                    else if (b > 12) return String.format("%d-%02d-%02d", year, a, b);
                    else return String.format("%d-%02d-%02d", year, b, a);
                }
            } catch (Exception ignored) {}
        }

        Matcher m2 = Pattern.compile("(\\d{4})[/\\-](\\d{1,2})[/\\-](\\d{1,2})").matcher(text);
        if (m2.find()) {
            int year = Integer.parseInt(m2.group(1));
            if (year >= 2000) {
                return String.format("%d-%02d-%02d", year, Integer.parseInt(m2.group(2)), Integer.parseInt(m2.group(3)));
            }
        }

        return null;
    }

    private int monthToNumber(String monthStr) {
        return switch (monthStr.substring(0, 3).toLowerCase()) {
            case "jan" -> 1; case "feb" -> 2; case "mar" -> 3; case "apr" -> 4;
            case "may" -> 5; case "jun" -> 6; case "jul" -> 7; case "aug" -> 8;
            case "sep" -> 9; case "oct" -> 10; case "nov" -> 11; case "dec" -> 12;
            default -> -1;
        };
    }

    private BigDecimal extractTotalAmount(String text) {
        String[] tier1Patterns = {
                "(?i)(?:grand\\s*total|total\\s*amount|total\\s*payable|order\\s*total|invoice\\s*total|bill\\s*total|final\\s*amount|you\\s*pay)[^\\d₹]{0,20}(?:₹|Rs\\.?|INR)\\s*([\\d,]+\\.\\d{2})",
                "(?i)(?:grand\\s*total|total\\s*amount|total\\s*payable|order\\s*total|invoice\\s*total|bill\\s*total|final\\s*amount|you\\s*pay)[^\\d₹]{0,20}([\\d,]+\\.\\d{2})",
        };

        String[] tier2Patterns = {
                "(?i)(?:^|\\n)\\s*total[^\\d₹]{0,15}(?:₹|Rs\\.?|INR)\\s*([\\d,]+\\.\\d{2})",
                "(?i)(?:^|\\n)\\s*total[^\\d₹]{0,15}([\\d,]+\\.\\d{2})",
        };

        String tier3Pattern = "(?:₹|Rs\\.?)\\s*([\\d,]+\\.\\d{2})";

        BigDecimal best = findBestAmount(text, tier1Patterns);
        if (best != null) return best;

        best = findBestAmount(text, tier2Patterns);
        if (best != null) return best;

        BigDecimal maxAmount = null;
        Matcher m3 = Pattern.compile(tier3Pattern).matcher(text);
        while (m3.find()) {
            try {
                BigDecimal amt = parseAmount(m3.group(1));
                if (amt != null && isReasonableAmount(amt)) {
                    int endPos = m3.end();
                    if (endPos < text.length() && text.charAt(endPos) == '/') continue;
                    if (maxAmount == null || amt.compareTo(maxAmount) > 0) {
                        maxAmount = amt;
                    }
                }
            } catch (Exception ignored) {}
        }

        return maxAmount;
    }

    private BigDecimal findBestAmount(String text, String[] patterns) {
        BigDecimal best = null;
        for (String pattern : patterns) {
            Matcher m = Pattern.compile(pattern).matcher(text);
            while (m.find()) {
                try {
                    BigDecimal amt = parseAmount(m.group(1));
                    if (amt != null && isReasonableAmount(amt)) {
                        int endPos = m.end();
                        if (endPos < text.length() && text.charAt(endPos) == '/') continue;
                        if (best == null || amt.compareTo(best) > 0) {
                            best = amt;
                        }
                    }
                } catch (Exception ignored) {}
            }
            if (best != null) return best;
        }
        return best;
    }

    private BigDecimal parseAmount(String amtStr) {
        if (amtStr == null || amtStr.isBlank()) return null;
        String cleaned = amtStr.replace(",", "").trim();
        if (cleaned.isEmpty()) return null;
        try {
            return new BigDecimal(cleaned);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isReasonableAmount(BigDecimal amt) {
        return amt.compareTo(BigDecimal.ZERO) > 0 && amt.compareTo(new BigDecimal("1000000")) < 0;
    }

    private String guessCategory(String text) {
        String lower = text.toLowerCase();

        if (containsAny(lower, "flight", "airline", "airport", "travel", "ticket", "railway", "train", "irctc"))
            return "TRAVEL";
        if (containsAny(lower, "restaurant", "food", "meal", "lunch", "dinner", "breakfast", "cafe", "catering", "zomato", "swiggy"))
            return "MEALS";
        if (containsAny(lower, "hotel", "accommodation", "stay", "room", "lodge", "resort", "oyo", "airbnb"))
            return "ACCOMMODATION";
        if (containsAny(lower, "stationery", "office supply", "office supplies", "pen", "paper", "printer"))
            return "OFFICE_SUPPLIES";
        if (containsAny(lower, "laptop", "computer", "monitor", "keyboard", "mouse", "equipment", "hardware"))
            return "EQUIPMENT";
        if (containsAny(lower, "software", "license", "subscription", "saas", "aws", "azure", "cloud"))
            return "SOFTWARE";
        if (containsAny(lower, "training", "course", "seminar", "workshop", "conference", "certification"))
            return "TRAINING";
        if (containsAny(lower, "hospital", "medical", "pharmacy", "medicine", "doctor", "clinic", "health"))
            return "MEDICAL";
        if (containsAny(lower, "uber", "ola", "taxi", "cab", "fuel", "petrol", "diesel", "parking", "toll", "metro", "bus", "rapido", "ride charge", "ride id", "captain"))
            return "TRANSPORTATION";
        if (containsAny(lower, "phone", "mobile", "internet", "broadband", "telecom", "airtel", "jio"))
            return "COMMUNICATION";
        if (containsAny(lower, "amazon", "flipkart", "myntra", "online", "shopping", "ecommerce"))
            return "OTHER";

        return "OTHER";
    }

    private boolean containsAny(String text, String... keywords) {
        for (String kw : keywords) {
            if (text.contains(kw)) return true;
        }
        return false;
    }

    private List<InvoiceParseResponse.ParsedItem> extractLineItems(String text) {
        List<InvoiceParseResponse.ParsedItem> items = new ArrayList<>();
        Set<String> seenDescs = new HashSet<>();

        Pattern exclusionPattern = Pattern.compile(
                "(?i)(?:^total$|total\\s*amount|total\\s*payable|grand\\s*total|final\\s*amount|" +
                "sub\\s*total|subtotal|sub-total|^total\\s*₹|" +
                "gst|cgst|sgst|igst|cess|" +
                "discount|coupon|promo|saving|cashback|round\\s*off|" +
                "balance|paid|you\\s*pay|net\\s*amount|inclusive|" +
                "tax\\s*category|place\\s*of\\s*supply|gst\\s*number|vehicle\\s*number|captain\\s*name|customer\\s*name|" +
                "address|india|tamil\\s*nadu|karnataka|maharashtra|andhra|telangana|" +
                "pin\\s*code|state\\s|invoice\\s*date|invoice\\s*no|ride\\s*id|time\\s*of|" +
                "qr\\s*pay|upi|wallet|passengers\\s*n\\.?e\\.?c|n\\.?e\\.?c\\.?|" +
                "duration|distance|kms|mins|minutes|kilometers)");

        Pattern currencyDescPattern = Pattern.compile("^(?:₹|Rs\\.?|INR)\\s*\\d");

        Pattern numericDescPattern = Pattern.compile("^[\\d\\s.,₹/]+$");

        String[] itemPatterns = {
                "(?m)^\\s*(?:\\d+[.)]?\\s+)?(.{4,80})\\s+(?:₹|Rs\\.?|INR)\\s*([\\d,]+\\.\\d{2})\\s*$",

                "(?m)^\\s*(?:\\d+[.)]?\\s+)?(.{5,70})\\s{2,}([\\d,]+\\.\\d{2})\\s*$",

                "(?m)^\\s*\\d+[.)]?\\s+(.{3,80})\\s+(\\d+)\\s+(?:₹|Rs\\.?|INR)?\\s*([\\d,]+\\.\\d{2})\\s*$",

                "(?m)^\\s*(?:\\d+[.)]?\\s+)?(.{3,60})\\s+(\\d+)\\s+[\\d,.]+\\s+(?:₹|Rs\\.?|INR)?\\s*([\\d,]+\\.\\d{0,2})\\s*$"
        };

        for (String pattern : itemPatterns) {
            Matcher m = Pattern.compile(pattern).matcher(text);
            while (m.find() && items.size() < 20) {
                try {
                    String desc = m.group(1).trim();

                    if (exclusionPattern.matcher(desc).find()) continue;
                    if (currencyDescPattern.matcher(desc).find()) continue;
                    if (numericDescPattern.matcher(desc).matches()) continue;
                    if (desc.length() < 3 || seenDescs.contains(desc.toLowerCase())) continue;

                    if (desc.matches("(?i).*(?:description|item\\s*name|particulars|s\\.?\\s*no|qty|quantity|^rate$|^price$|^amount$|hsn|sac|bill\\s*details|payment\\s*summary).*"))
                        continue;

                    if (desc.matches("^\\d{4,}.*")) continue;

                    int qty = 1;
                    String amtStr;
                    if (m.groupCount() >= 3 && m.group(3) != null) {
                        try { qty = Integer.parseInt(m.group(2)); } catch (Exception ignored) {}
                        amtStr = m.group(3).replace(",", "");
                    } else {
                        amtStr = m.group(2).replace(",", "");
                    }

                    BigDecimal amount = new BigDecimal(amtStr);
                    if (amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(new BigDecimal("999999")) < 0) {
                        seenDescs.add(desc.toLowerCase());
                        items.add(InvoiceParseResponse.ParsedItem.builder()
                                .description(desc)
                                .amount(amount)
                                .quantity(qty)
                                .build());
                    }
                } catch (Exception ignored) {}
            }
            if (!items.isEmpty()) break;
        }

        return items;
    }

    private String generateTitle(String vendorName, String category, BigDecimal amount) {
        List<String> parts = new ArrayList<>();
        if (vendorName != null && !vendorName.isEmpty()) {
            parts.add(vendorName);
        }
        if (category != null && !category.equals("OTHER")) {
            parts.add(category.replace("_", " ").substring(0, 1).toUpperCase()
                    + category.replace("_", " ").substring(1).toLowerCase());
        }
        if (!parts.isEmpty()) {
            return String.join(" — ", parts);
        }
        if (amount != null) {
            return "Expense ₹" + amount.toPlainString();
        }
        return "Expense";
    }

    private String extractJson(String text) {
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        return text;
    }

    private String getTextOrNull(JsonNode node, String field) {
        if (node.has(field) && !node.get(field).isNull()) {
            String val = node.get(field).asText();
            return "null".equalsIgnoreCase(val) || val.isBlank() ? null : val;
        }
        return null;
    }

    private BigDecimal getDecimalOrNull(JsonNode node, String field) {
        if (node.has(field) && !node.get(field).isNull()) {
            if (node.get(field).isNumber()) {
                return node.get(field).decimalValue();
            }

            try {
                String val = node.get(field).asText().replaceAll("[^\\d.]", "");
                if (!val.isEmpty()) {
                    return new BigDecimal(val);
                }
            } catch (Exception ignored) {}
        }
        return null;
    }
}
