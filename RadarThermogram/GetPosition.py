from PIL import Image
import keyboard
import time


# 定义一个函数，判断一个像素点是否是蓝色的
def is_blue(pixel, tolerance=80):
    blue_rgb = (0, 0, 255)
    return all(abs(component - blue_rgb[i]) <= tolerance for i, component in enumerate(pixel))


# 定义一个函数，获取图片中所有蓝色像素点的坐标
def get_blue_pixel_coordinates(image):
    blue_pixel_coordinates = []

    # 获取图片的大小
    width, height = image.size

    # 将图片转换为RGB模式（如果不是的话）
    image = image.convert('RGB')

    # 将图片转换为像素点数据
    pixels = image.load()

    # 搜索蓝色像素点，接受一定的误差
    for x in range(width):
        for y in range(height):
            if is_blue(pixels[x, y]):
                blue_pixel_coordinates.append((x, y))

    print("coordinates before:{%s}" % len(blue_pixel_coordinates))

    return blue_pixel_coordinates


# 定义一个函数，检测blue_pixel_coordinates中点之间的距离是否小于某个阈值，如果是则合并
def merge_close_points(coordinates, threshold=10):
    merged_coordinates = []
    for coord in coordinates:
        x, y = coord
        merged = False
        for merged_coord in merged_coordinates:
            merged_x, merged_y = merged_coord
            if abs(x - merged_x) <= threshold and abs(y - merged_y) <= threshold:
                merged = True
                break
        if not merged:
            merged_coordinates.append(coord)
    print("coordinates after:{%s}" % len(blue_pixel_coordinates))

    return merged_coordinates


# 定义一个函数，新建一个图窗，调用一次
def visualize_coordinates(image, coordinates):
    # 输出蓝色像素点的坐标
    if blue_pixel_coordinates:
        print("蓝色像素点的坐标：")
        for coord in blue_pixel_coordinates:
            print(coord)
    else:
        print("图片中不存在蓝色像素点。")

    import matplotlib.pyplot as plt
    plt.ion()  # 开启交互模式
    fig, ax = plt.subplots()
    ax.imshow(image)

    for coord in coordinates:
        x, y = coord
        ax.plot(x, y, 'rx', markersize=10)

    plt.draw()
    plt.pause(0.5)

    return plt


# 定义一个函数，接收一个栈和一个坐标列表，将新出现的坐标点压入栈中，并且如果栈内所有坐标点中有与新坐标点距离较近则视为同一坐标点
def push_coordinates(stack, coordinates):
    original_len = len(stack)
    tolerance = 10
    for coord in coordinates:
        x, y = coord
        merged = False
        for i, stack_coord in enumerate(stack):
            stack_x, stack_y = stack_coord
            if abs(x - stack_x) <= tolerance and abs(y - stack_y) <= tolerance:
                stack[i] = coord
                merged = True
                break
        if not merged:
            stack.append(coord)

    print("---" * 10)
    # 打印栈的长度
    print(f"stack len from {original_len} to {len(stack)}")
    # 打印栈顶元素
    print(f"stack top element: {stack[-1]}")
    print("---" * 10)

    return stack


if __name__ == '__main__':
    # 新建一个栈用于存放所有出现过的坐标点，且栈顶元素为最新的坐标点
    stack = []
    image_index = 1
    time_interval = 3

    # 每5秒运行一次循环
    while True:
        # 打开图片
        image_path = f'./img/test_map_{image_index}.png'
        image_index += 1
        image = Image.open(image_path)
        # 获取所有蓝色像素点的坐标
        blue_pixel_coordinates = get_blue_pixel_coordinates(image)
        # 合并距离较近的点
        blue_pixel_coordinates = merge_close_points(blue_pixel_coordinates)
        # 可视化坐标点
        plt = visualize_coordinates(image, blue_pixel_coordinates)
        # 将新出现的坐标点压入栈中
        stack = push_coordinates(stack, blue_pixel_coordinates)
        # 获取栈顶元素
        top_element = stack[-1]
        print(f"===top element: {top_element}===")

        # 等待10秒
        time.sleep(time_interval)
        # 清除图窗
        plt.close()

    # # 打开第二张图片
    # image_path_2 = 'RadarThermogram/img/test_map_2.png'  
    # image_2 = Image.open(image_path_2)
    # # 获取所有蓝色像素点的坐标
    # blue_pixel_coordinates_2 = get_blue_pixel_coordinates(image_2)
    # # 合并距离较近的点
    # blue_pixel_coordinates_2 = merge_close_points(blue_pixel_coordinates_2)
    # # 可视化坐标点
    # visualize_coordinates(image_2, blue_pixel_coordinates_2)
    # # 将新出现的坐标点压入栈中
    # stack = push_coordinates(stack, blue_pixel_coordinates_2)

    # 等待按下space键结束程序
    print("Press space to exit.")
    keyboard.wait('space')
