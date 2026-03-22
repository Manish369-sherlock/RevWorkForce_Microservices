import os
import re

def process_c_style(text):
    out = []
    state = "NORMAL"
    i = 0
    n = len(text)
    while i < n:
        if state == "NORMAL":
            if text[i:i+2] == "//":
                state = "LINE_COMMENT"
                i += 2
            elif text[i:i+2] == "/*":
                state = "BLOCK_COMMENT"
                i += 2
            elif text[i] == '"':
                out.append(text[i])
                state = "STRING"
                i += 1
            elif text[i] == "'":
                out.append(text[i])
                state = "CHAR"
                i += 1
            elif text[i] == '`':
                out.append(text[i])
                state = "TEMPLATE"
                i += 1
            else:
                out.append(text[i])
                i += 1
        elif state == "STRING":
            if text[i] == '\\':
                out.append(text[i])
                i += 1
                if i < n:
                    out.append(text[i])
                    i += 1
            elif text[i] == '"':
                out.append(text[i])
                state = "NORMAL"
                i += 1
            else:
                out.append(text[i])
                i += 1
        elif state == "CHAR":
            if text[i] == '\\':
                out.append(text[i])
                i += 1
                if i < n:
                    out.append(text[i])
                    i += 1
            elif text[i] == "'":
                out.append(text[i])
                state = "NORMAL"
                i += 1
            else:
                out.append(text[i])
                i += 1
        elif state == "TEMPLATE":
            if text[i] == '\\':
                out.append(text[i])
                i += 1
                if i < n:
                    out.append(text[i])
                    i += 1
            elif text[i] == '`':
                out.append(text[i])
                state = "NORMAL"
                i += 1
            else:
                out.append(text[i])
                i += 1
        elif state == "LINE_COMMENT":
            if text[i] == '\n':
                out.append(text[i])
                state = "NORMAL"
            i += 1
        elif state == "BLOCK_COMMENT":
            if text[i:i+2] == "*/":
                state = "NORMAL"
                i += 2
            else:
                if text[i] == '\n':
                    out.append(text[i])
                i += 1

    return "".join(out)

def process_html_xml(text):
    return re.sub(r'<!--.*?-->', '', text, flags=re.DOTALL)

def process_hash_style(text, is_properties=False):
    out = []
    lines = text.split('\n')
    for line in lines:
        stripped = line.lstrip()
        if stripped.startswith('#'):
            continue
        if is_properties and stripped.startswith('!'):
            continue
        out.append(line)
    return "\n".join(out)

def remove_comments_in_file(filepath):
    ext = os.path.splitext(filepath)[1].lower()
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            original_text = f.read()
    except Exception as e:
        return

    new_text = original_text

    if ext in ['.java', '.ts', '.js', '.css', '.scss', '.jsonc']:
        new_text = process_c_style(original_text)
    elif ext in ['.html', '.xml']:
        new_text = process_html_xml(original_text)
    elif ext in ['.properties']:
        new_text = process_hash_style(original_text, is_properties=True)
    elif ext in ['.yaml', '.yml', '.py']:
        new_text = process_hash_style(original_text, is_properties=False)
    else:
        return

    if new_text != original_text:
        try:
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(new_text)
            print(f"Removed comments from: {filepath}")
        except Exception as e:
            print(f"Failed to write to: {filepath}")

def main():
    exclude_dirs = {'.git', '.vscode', 'node_modules', 'target', 'build', 'dist', '.idea', '.angular', 'out'}
    base_dir = r"c:\Users\gopum\OneDrive\Desktop\WorkForce-microservices\Microservices - HRMS"

    count = 0
    for root, dirs, files in os.walk(base_dir):
        dirs[:] = [d for d in dirs if d not in exclude_dirs]
        for file in files:
            remove_comments_in_file(os.path.join(root, file))
            count += 1

    print(f"Finished checking files.")

if __name__ == "__main__":
    main()
