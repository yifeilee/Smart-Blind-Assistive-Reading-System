# 字符串缓冲：目标完成对于读到的字符串进行缓冲
# author：zhang winson
# time：2023.1.16

class strBuffer:
    def __init__(self):
        self.area = []
    # 添加字符串
    def add_string(self, str):
        for c in iter(str):
            self.area.append(c)
    # 输出字符串
    def output_string(self):
        str = ""
        l = len(self.area)
        if l <= 0:
            return "0", False
        else:
            for index in range(l):
                str += self.area[index]
                del self.area[index]
            return str, True
