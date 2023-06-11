# 编译模块：目标是完成将缓冲区中的字符串编译为盲文索引
# author：zhang winson
# time：2023.3.10

import mytext
import cv_part.strBuffer as st

class Compilation:
    def __init__(self):
        self.buffer = st.strBuffer()
    # 指定字符缓冲区
    def add(self, buffer):
        self.buffer = buffer
    # 字符串到索引的映射转换
    def a2index(self, len):
        str = self.buffer.output_string()
        indexstr = mytext.alphabet2byte(str, len(str))
        indexlist = list(indexstr)
        res = []
        for i in indexlist:
            index = ord(i)
            if index >= 66:
                index -= 66
            res.append(index)
        return res

