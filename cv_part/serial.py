# 数据输出模块：目标将转换后的字符索引转化为二进制序列进行输出
# author：zhang winson
# time：2023.1.12

import RPi.GPIO as GPIO
import time

class IOdata:
    def __init__(self, numlist):
        self.binbuffer = []
    # 配置树莓派引脚
    def sent_bit(self):
        GPIO.setmode(GPIO.BOARD)
        Pinlist = [7, 11, 13, 15, 12, 16]
        GPIO.setup(Pinlist, GPIO.OUT)
        for i in range(len(Pinlist)):
            GPIO.output(Pinlist[i], self.binbuffer[i])
        time.sleep(3)
    # 转换为二进制序列
    def index2bin(self, indexlist):
        for i in range(len(indexlist)):
            if(indexlist[i] == 1):
                self.binbuffer = [0, 0, 0, 0, 0, 1]
                self.sent_bit()
            elif(indexlist[i] == 2):
                self.binbuffer = [0, 0, 0, 0, 1, 0]
                self.sent_bit()
            elif (indexlist[i] == 3):
                self.binbuffer = [0, 0, 0, 1, 0, 0]
                self.sent_bit()
            elif (indexlist[i] == 4):
                self.binbuffer = [0, 0, 0, 1, 0, 1]
                self.sent_bit()
            elif (indexlist[i] == 5):
                self.binbuffer = [0, 0, 0, 1, 1, 0]
                self.sent_bit()
            elif (indexlist[i] == 6):
                self.binbuffer = [0, 0, 0, 1, 1, 1]
                self.sent_bit()
            elif (indexlist[i] == 7):
                self.binbuffer = [0, 0, 1, 0, 0, 0]
                self.sent_bit()
            elif (indexlist[i] == 8):
                self.binbuffer = [0, 0, 1, 0, 0, 1]
                self.sent_bit()
            elif (indexlist[i] == 9):
                self.binbuffer = [0, 0, 1, 0, 1, 0]
                self.sent_bit()
            elif (indexlist[i] == 10):
                self.binbuffer = [0, 0, 1, 0, 1, 1]
                self.sent_bit()
            elif (indexlist[i] == 11):
                self.binbuffer = [0, 0, 1, 1, 0, 0]
                self.sent_bit()
            elif (indexlist[i] == 12):
                self.binbuffer = [0, 0, 1, 1, 0, 1]
                self.sent_bit()
            elif (indexlist[i] == 13):
                self.binbuffer = [0, 0, 1, 1, 1, 0]
                self.sent_bit()
            elif (indexlist[i] == 14):
                self.binbuffer = [0, 0, 1, 1, 1, 1]
                self.sent_bit()
            elif (indexlist[i] == 15):
                self.binbuffer = [0, 1, 0, 0, 0, 0]
                self.sent_bit()
            elif (indexlist[i] == 16):
                self.binbuffer = [0, 1, 0, 0, 0, 1]
                self.sent_bit()
            elif (indexlist[i] == 17):
                self.binbuffer = [0, 1, 0, 0, 1, 0]
                self.sent_bit()
            elif (indexlist[i] == 18):
                self.binbuffer = [0, 1, 0, 0, 1, 1]
                self.sent_bit()
            elif (indexlist[i] == 19):
                self.binbuffer = [0, 1, 0, 1, 0, 0]
                self.sent_bit()
            elif (indexlist[i] == 20):
                self.binbuffer = [0, 1, 0, 1, 0, 1]
                self.sent_bit()
            elif (indexlist[i] == 21):
                self.binbuffer = [0, 1, 0, 1, 1, 0]
                self.sent_bit()
            elif (indexlist[i] == 22):
                self.binbuffer = [0, 1, 0, 1, 1, 1]
                self.sent_bit()
            elif (indexlist[i] == 23):
                self.binbuffer = [0, 1, 1, 0, 0, 0]
                self.sent_bit()
            elif (indexlist[i] == 24):
                self.binbuffer = [0, 1, 1, 0, 0, 1]
                self.sent_bit()
            else:
                self.binbuffer = [0, 1, 1, 0, 1, 0]
                self.sent_bit()

