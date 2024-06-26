import matplotlib.pyplot as plt
import cv2
import numpy as np
import pandas as pd
import seaborn as sns
import SearchRoute as SearchRoute

# 计算安全值权重
def safety_factor(temperature=1, humidity=2, smoke=3):
    return temperature * 0.1 + humidity * 0.1 + smoke * 0.8

# 根据传感器读数更新安全系数
def renew_safety_factor(temperature, humidity, smoke):
    return safety_factor(temperature, humidity, smoke)

# 将图像转换为二值矩阵
def convert_to_binary(img):
    gray_img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    _, binary_img = cv2.threshold(gray_img, 230, 255, cv2.THRESH_BINARY)
    inverted_binary_img = cv2.bitwise_not(binary_img)
    binary_matrix = (inverted_binary_img / 255).astype(np.uint8)
    return binary_matrix

# 读取图像
image_path = './img/map_new.png'
image = cv2.imread(image_path)

# 转换为只含有0和1的矩阵
building = convert_to_binary(image)

# 获取图像的尺寸
rows, cols = building.shape

# 确认起始位置在图像范围内
start = (min(400, rows - 1), min(400, cols - 1))  # 起始位置

# 主函数
# 初始化无人机当前位置
positionX, positionY = min(240, rows - 1), min(400, cols - 1)

# 设置初始传感器参数
smoke = 1
humidity = 1
temperature = 2

# 初始化安全系数数据
safety_factor_data = safety_factor(smoke, humidity, temperature)

# 执行搜索以获取路径
path = SearchRoute.get_path(building, start)

# 确保路径中的所有点在图像范围内
path = [(max(0, min(p[0], rows - 1)), max(0, min(p[1], cols - 1))) for p in path]

# 动态更新建筑矩阵中的安全系数
for pos in path:
    # 模拟传感器数据更新
    smoke = np.random.randint(1, 4)
    humidity = np.random.randint(1, 3)
    temperature = np.random.randint(1, 3)

    # 根据更新的传感器数据更新安全系数
    safety_factor_data = renew_safety_factor(temperature, humidity, smoke)

    # 更新当前路径点的建筑矩阵
    building[pos[0]][pos[1]] = safety_factor_data

# 将二值矩阵转换为DataFrame
df = pd.DataFrame(building)

# 设置颜色映射
cmap = "RdYlGn_r"  # 选择热力图颜色映射

# 绘制热力图
plt.figure(figsize=(12, 12))  # 设置图像大小
sns.heatmap(df, cmap=cmap, square=True, cbar=True)  # 绘制热力图

plt.gca().invert_yaxis()  # 反转y轴，使(0,0)在左上角
plt.show()  # 显示图像
