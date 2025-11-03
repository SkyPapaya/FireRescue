import os

# --- 配置 ---

# 1. 将此脚本放在包含这三个项目（ManageSystemVue, FireRescueManageSystem, RadarThermogram）的根目录中
# 2. 在下面设置你的项目根目录的路径
#    如果脚本就放在项目根目录，可以保持 '.' 不变
PROJECT_ROOT_DIR = '.'

# 3. 输出文件名
OUTPUT_FILE = 'project_code.txt'

# 4. 需要忽略的目录 (匹配目录名)
#    这些目录通常包含依赖、编译结果或IDE配置，分析时不需要
IGNORE_DIRS = {
    'node_modules', '.idea', '.git', 'dist', 'out', 'target',
    'build', '__pycache__', '.vscode', '.DS_Store'
}

# 5. 需要忽略的文件 (匹配完整文件名)
IGNORE_FILES = {
    'yarn.lock', 'package-lock.json', 'map.png', 'img.png',
    'img_1.png', 'img_2.png', '.gitignore'
}

# 6. 需要包含的文件扩展名
#    (只打包这些类型的文件)
INCLUDE_EXTENSIONS = {
    # Python
    '.py',
    # Java
    '.java',
    # 前端
    '.js', '.ts', '.vue', '.css', '.scss', '.html',
    # 配置
    '.xml', '.properties', '.yml', '.yaml', '.json',
    # 文档
    '.md'
}

# 7. 即使扩展名不在上面，也需要强制包含的特定文件名
#    (例如 pom.xml 没有扩展名，但很重要)
FORCE_INCLUDE_FILES = {
    'pom.xml', 'vite.config.ts', 'tsconfig.json', 'README.md',
    '.env.development', '.env.production'
}


# --- 脚本 ---

def should_ignore_dir(dir_name):
    """检查是否应该忽略此目录"""
    return dir_name in IGNORE_DIRS


def should_include_file(file_name):
    """检查是否应该包含此文件"""
    if file_name in IGNORE_FILES:
        return False

    if file_name in FORCE_INCLUDE_FILES:
        return True

    _, ext = os.path.splitext(file_name)
    return ext.lower() in INCLUDE_EXTENSIONS


def main():
    # 确保旧文件被删除
    if os.path.exists(OUTPUT_FILE):
        os.remove(OUTPUT_FILE)

    print(f"开始扫描项目: {os.path.abspath(PROJECT_ROOT_DIR)}")
    print(f"将输出到: {OUTPUT_FILE}\n")

    file_count = 0

    # os.walk 会遍历所有子目录
    for root, dirs, files in os.walk(PROJECT_ROOT_DIR, topdown=True):

        # 1. 告诉 os.walk 不要进入被忽略的目录
        #    (我们在这里修改 dirs 列表是 os.walk 的标准用法)
        dirs[:] = [d for d in dirs if not should_ignore_dir(d)]

        for file in files:
            if should_include_file(file):
                file_path = os.path.join(root, file)
                relative_path = os.path.relpath(file_path, PROJECT_ROOT_DIR)

                try:
                    with open(file_path, 'r', encoding='utf-8') as f:
                        content = f.read()

                    # 写入到输出文件
                    with open(OUTPUT_FILE, 'a', encoding='utf-8') as out_f:
                        out_f.write(f"\n\n{'=' * 20} START FILE: {relative_path} {'=' * 20}\n\n")
                        out_f.write(content)
                        out_f.write(f"\n\n{'=' * 20} END FILE: {relative_path} {'=' * 20}\n\n")

                    print(f"  [+] 已添加: {relative_path}")
                    file_count += 1

                except UnicodeDecodeError:
                    print(f"  [!] 忽略 (无法解码): {relative_path}")
                except Exception as e:
                    print(f"  [!] 错误 (文件 {relative_path}): {e}")

    print(f"\n--- 完成 ---")
    print(f"总共 {file_count} 个文件已合并到 {OUTPUT_FILE}")


if __name__ == '__main__':
    main()