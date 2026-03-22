import os

TARGETS = {
    "01-eureka-server.md": "service-discovery",
    "02-config-server.md": "config-server",
    "03-api-gateway.md": "api-gateway",
    "04-user-service.md": "user-service",
    "05-employee-management-service.md": "employee-management-service",
    "06-leave-service.md": "leave-service",
    "07-performance-service.md": "performance-service",
    "08-reporting-service.md": "reporting-service",
    "09-notification-service.md": "notification-service"
}

BASE_DIR = r"c:\Users\gopum\OneDrive\Desktop\WorkForce-microservices\Microservices - HRMS"
EXP_DIR = os.path.join(BASE_DIR, "explanation")

def get_tree(startpath):
    tree = []
    tree.append(f"📦 {os.path.basename(startpath)}")
    for root, dirs, files in os.walk(startpath):
        dirs[:] = [d for d in dirs if d not in ['.idea', '.vscode', 'target', '.mvn', 'node_modules', '.git']]
        level = root.replace(startpath, '').count(os.sep)
        indent = ' ' * 4 * (level)
        if level > 0:
            tree.append(f"{indent}📂 {os.path.basename(root)}")
        subindent = ' ' * 4 * (level + 1)
        for f in files:
            tree.append(f"{subindent}📜 {f}")
    return "\n".join(tree)

def extract_file_content(filepath):
    try:
        with open(filepath, 'r', encoding='utf-8', errors='ignore') as f:
            return f.read()
    except Exception as e:
        return f"Error reading {filepath}: {e}"

for md_file, ms_dir in TARGETS.items():
    md_path = os.path.join(EXP_DIR, md_file)
    ms_path = os.path.join(BASE_DIR, ms_dir)
    
    if not os.path.exists(md_path) or not os.path.exists(ms_path):
        print(f"Skipping {md_file} as paths don't exist")
        continue
    
    # Read existing
    with open(md_path, 'r', encoding='utf-8') as f:
        existing = f.read()
    
    # Stop before Deep Dive if previously appended
    if "## 🛑 Deep Dive Component Codes & Project Structure" in existing:
        existing = existing.split("## 🛑 Deep Dive Component Codes & Project Structure")[0]
        
    print(f"Processing {md_file} ({ms_dir})...")
    
    out = []
    out.append("\n\n## 🛑 Deep Dive Component Codes & Project Structure")
    out.append("This section contains the full, exhaustive breakdown of the microservice's source code, project structure, and dependencies. It operates as the fundamental source of truth replacing isolated snippets with the actual working code.\n")
    
    out.append("### 🌳 Complete Project Tree")
    out.append("```text")
    out.append(get_tree(ms_path))
    out.append("```\n")
    
    pom_path = os.path.join(ms_path, "pom.xml")
    if os.path.exists(pom_path):
        out.append("### 📦 Dependencies (`pom.xml`)")
        out.append("```xml")
        out.append(extract_file_content(pom_path))
        out.append("```\n")
        
    res_dir = os.path.join(ms_path, "src", "main", "resources")
    if os.path.exists(res_dir):
        out.append("### ⚙️ Configurations (`src/main/resources`)")
        for f in os.listdir(res_dir):
            if f.endswith('.properties') or f.endswith('.yml') or f.endswith('.yaml'):
                f_path = os.path.join(res_dir, f)
                out.append(f"**`{f}`**")
                out.append(f"```properties")
                out.append(extract_file_content(f_path))
                out.append("```\n")
                
    # Gather Java Code Files
    out.append("### ☕ Source Code Files")
    java_dir = os.path.join(ms_path, "src", "main", "java")
    if os.path.exists(java_dir):
        for root, dirs, files in os.walk(java_dir):
            for f in files:
                if f.endswith('.java'):
                    f_path = os.path.join(root, f)
                    rel_path = os.path.relpath(f_path, ms_path)
                    content = extract_file_content(f_path)
                    out.append(f"#### **`{rel_path.replace(os.sep, '/')}`**")
                    out.append("```java")
                    out.append(content)
                    out.append("```\n")
                    
    # Write back
    final_content = existing.strip() + "\n" + "\n".join(out)
    with open(md_path, 'w', encoding='utf-8') as f:
        f.write(final_content)
        
    print(f"Successfully documented complete codebase for {md_file}.")
