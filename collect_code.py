import os

# --- 配置区 ---

# 1. 将此列表修改为您三个项目所在的根目录
#    例如: ['J:/FireRescue', 'J:/RadarThermogram', 'J:/ManageSystemVue']
#    或者，如果它们都在一个文件夹下，比如 'J:/MyProjects'
#    您可以只写: ['J:/MyProjects']
PROJECT_ROOTS = [
    'J:/FireRescue'  # 示例路径，请修改为您的实际路径
    # 'PATH/TO/RadarThermogram', # 取消注释并修改
    # 'PATH/TO/ManageSystemVue'  # 取消注释并修改
]

# 2. 输出文件名
OUTPUT_FILE = 'combined_project_code.txt'

# 3. 要包含的文件扩展名
INCLUDE_EXTENSIONS = {
    '.java', '.py', '.vue', '.ts', '.js', '.html', '.css', '.scss',
    '.xml', '.properties', '.yml', '.yaml'
}

# 4. 要排除的目录（会跳过这些目录下的所有内容）
EXCLUDE_DIRS = {
    '.idea', '.git', '__pycache__', 'node_modules', 'target',
    'out', 'dist', 'build', 'venv', '.vscode'
}

# 5. 要排除的特定文件
EXCLUDE_FILES = {
    '.gitignore', 'mvnw', 'mvnw.cmd', 'yarn.lock',
    'package-lock.json', 'LICENSE'
}


# --- 脚本区 ---

def get_project_files(root_dir):
    """遍历项目目录，收集符合条件的文件路径"""
    file_paths = []
    for dirpath, dirnames, filenames in os.walk(root_dir, topdown=True):

        # 排除目录
        dirnames[:] = [d for d in dirnames if d not in EXCLUDE_DIRS]

        for filename in filenames:
            if filename in EXCLUDE_FILES:
                continue

            file_ext = os.path.splitext(filename)[1]
            if file_ext in INCLUDE_EXTENSIONS:
                full_path = os.path.join(dirpath, filename)
                file_paths.append(full_path)

    return file_paths


def combine_files(project_paths, output_filename):
    """将所有文件内容合并到一个输出文件中"""
    all_files = []
    for path in project_paths:
        if os.path.isdir(path):
            all_files.extend(get_project_files(path))
        elif os.path.isfile(path) and os.path.splitext(path)[1] in INCLUDE_EXTENSIONS:
            all_files.append(path)

    print(f"总共找到 {len(all_files)} 个源文件。正在合并...")

    try:
        with open(output_filename, 'w', encoding='utf-8') as outfile:
            for filepath in all_files:
                # 写入文件头，标明文件路径
                outfile.write(f"\n--- START OF FILE: {filepath} ---\n\n")

                try:
                    with open(filepath, 'r', encoding='utf-8', errors='ignore') as infile:
                        content = infile.read()
                        outfile.write(content)
                except Exception as e:
                    outfile.write(f"[无法读取文件: {e}]\n")

                outfile.write(f"\n\n--- END OF FILE: {filepath} ---\n")

        print(f"成功！所有代码已合并到 {output_filename}")

    except IOError as e:
        print(f"写入输出文件时出错: {e}")
    except Exception as e:
        print(f"发生未知错误: {e}")


if __name__ == "__main__":
    if not PROJECT_ROOTS or PROJECT_ROOTS[0] == '':
        print("错误：请先在脚本中配置 'PROJECT_ROOTS' 变量！")
    else:
        combine_files(PROJECT_ROOTS, OUTPUT_FILE)