import cv2
import handtrackmodel as htm
import scan as sc
import volume as vo
import sound as so
import strBuffer as st
import compile_part.braillecompilation as co
import serial as se
import time

class Fingercounter:
    def __init__(self, cap):
        # 定义窗口大小
        self.wCam = 640
        self.hCam = 480
        # 定义摄像头
        self.cap = cap
        # 手部识别模型
        self.detector = htm.handDetector(detectionCon=0.75)

        self.tipIds = [4, 8, 12, 16, 20]
        # 上一帧记录的手部数字
        self.last_Finger = 0
        # 计数器
        self.count = 0
    # 运行手势识别程序
    def run(self):
        # 扫描模块
        scan = sc.Scan(self.cap)
        # 声音调节模块
        volume = vo.Volume(self.cap)
        # 字符缓冲区
        buffer = st.strBuffer()
        # 编译模块
        compilation = co.Compilation()
        # 音频输出模块
        sound = so.Sound()
        # 数据输出模块
        iodata = se.IOdata()
        #进入识别循环
        while True:
            success, img = self.cap.read()
            img = cv2.resize(img, (self.wCam, self.hCam))
            img = self.detector.findHands(img)
            lmList = self.detector.findPosition(img, draw=False)

            if len(lmList) != 0:
                fingers = []
                if lmList[self.tipIds[0]][1] > lmList[self.tipIds[0] - 1][1]:
                    fingers.append(1)
                else:
                    fingers.append(0)
                for id in range(1, 5):
                    if lmList[self.tipIds[id]][2] < lmList[self.tipIds[id] - 2][2]:
                        fingers.append(1)
                    else:
                        fingers.append(0)
                totalFingers = fingers.count(1)
                # 确保手势稳定
                if totalFingers == self.last_Finger:
                    self.count += 1
                else:
                    self.count = 0
                # 稳定后开始处理
                if self.count == 10:
                    # 识别到一号手势，音频输出模块
                    if totalFingers == 1:

                        ######################
                        # Sound模块处理       #
                        # 接口：read_text字符串#
                        ######################

                        self.count = 0
                    # 识别到二号手势，文档扫描模块
                    elif totalFingers == 2:
                        time.sleep(1)
                        read_text, sign = scan.handle()
                        if sign:
                            buffer.add_string(read_text)
                        else:
                            ################################
                            # print("Can't find book!!!!!")#
                            # 输出错误处理提示音              #
                            ###############################
                         self.count = 0
                    # 识别到三号手势，音量调节模块
                    elif totalFingers == 3:
                        volume.handle()
                        self.count = 0
                    # 识别到四号手势，编译输出模块
                    elif totalFingers == 4:
                        compilation.add(buffer)
                        indexlist = compilation.a2index()
                        iodata.index2bin(indexlist)
                        self.count = 0
                    else:
                        self.count = 0
                # 记录识别数字
                self.last_Finger = totalFingers
            cv2.waitKey(1)