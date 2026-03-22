import os
import re

def clean_spacing(filepath):
    ext = os.path.splitext(filepath)[1].lower()
    if ext not in ['.java', '.ts', '.js', '.css', '.scss', '.jsonc', '.html', '.xml', '.properties', '.yaml', '.yml', '.py']:
        return

    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            text = f.read()
    except:
        return

    original_text = text

    # Remove trailing whitespace from all lines including lines that only have spaces
    text = re.sub(r'[ \t]+$', '', text, flags=re.MULTILINE)

    # Replace 3 or more consecutive newlines with 2 newlines (i.e. one blank line)
    text = re.sub(r'\n{3,}', '\n\n', text)

    # Remove blank lines right after an opening brace '{'
    text = re.sub(r'\{\n\n', '{\n', text)

    # Remove blank lines right before a closing brace '}'
    text = re.sub(r'\n\n(\s*)\}', r'\n\1}', text)

    # Remove leading blank lines at the beginning of the file
    text = text.lstrip('\n')

    if text != original_text:
        try:
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(text)
            print(f"Cleaned spacing: {filepath}")
        except:
            pass

def main():
    exclude_dirs = {'.git', '.vscode', 'node_modules', 'target', 'build', 'dist', '.idea', '.angular', 'out'}
    base_dir = r"c:\Users\gopum\OneDrive\Desktop\WorkForce-microservices\Microservices - HRMS"

    count = 0
    for root, dirs, files in os.walk(base_dir):
        dirs[:] = [d for d in dirs if d not in exclude_dirs]
        for file in files:
            clean_spacing(os.path.join(root, file))
            count += 1

    print("Spacing cleanup complete.")

if __name__ == "__main__":
    main()
