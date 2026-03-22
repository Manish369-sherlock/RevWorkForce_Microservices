package com.revworkforce.apigateway.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.*;

public class HeaderMutableRequestWrapper extends HttpServletRequestWrapper {
    private final Map<String, String> customHeaders = new HashMap<>();

    public HeaderMutableRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public void addHeader(String name, String value) {
        customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String customValue = customHeaders.get(name);
        if (customValue != null) {
            return customValue;
        }
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> names = new LinkedHashSet<>(customHeaders.keySet());
        Enumeration<String> originalNames = super.getHeaderNames();
        while (originalNames.hasMoreElements()) {
            names.add(originalNames.nextElement());
        }
        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        String customValue = customHeaders.get(name);
        if (customValue != null) {
            return Collections.enumeration(List.of(customValue));
        }
        return super.getHeaders(name);
    }
}

