# 手势识别：返回相应的21个手部关键点的信息
# author：zhang winson
# time：2023.2.10

import cv2
import mediapipe as mp
import math

class handDetector():
    def __init__(self, mode=False, maxHands=2, modelC = 1, detectionCon=0.5, trackCon=0.5):
        self.mode = mode
        self.maxHands = maxHands
        self.modelC = modelC
        self.detectionCon = detectionCon
        self.trackCon = trackCon
        # mediapipe提供的手势识别模块
        self.mpHands = mp.solutions.hands
        # 包括识别模式（是否一直检测）、最大手的数量、识别极限值
        self.hands = self.mpHands.Hands(self.mode, self.maxHands,
                                        self.modelC, self.detectionCon, self.trackCon)

    def findHands(self, img, draw=True):
        # 传入RGB图像
        imgRGB = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        # 手势识别
        self.results = self.hands.process(imgRGB)
        return img

    def findPosition(self, img, handNo=0, draw=True):
        self.lmList = []
        if self.results.multi_hand_landmarks:
            # 找到手的位置
            myHand = self.results.multi_hand_landmarks[handNo]
            # 返回坐标
            for id, lm in enumerate(myHand.landmark):
                h, w, c = img.shape
                # 将比例系数转化为实际坐标
                cx, cy = int(lm.x * w), int(lm.y * h)
                self.lmList.append([id, cx, cy])
        return self.lmList

    # 手势数字
    def fingersUp(self):
        fingers = []
        # 大拇指顶部关键点在右侧
        if self.lmList[self.tipIds[0]][1] > self.lmList[self.tipIds[0] - 1][1]:
            fingers.append(1)
        else:
            fingers.append(0)
        # 其余四指在下面
        for id in range(1, 5):
            if self.lmList[self.tipIds[id]][2] < self.lmList[self.tipIds[id] - 2][2]:
                fingers.append(1)
            else:
                fingers.append(0)
        return fingers

    # 两指间距离
    def findDistance(self, p1, p2, img, draw=True):
        x1, y1 = self.lmList[p1][1], self.lmList[p1][2]
        x2, y2 = self.lmList[p2][1], self.lmList[p2][2]
        cx, cy = (x1 + x2) // 2, (y1 + y2) // 2

        length = math.hypot(x2 - x1, y2 - y1)
        return length, img, [x1, y1, x2, y2, cx, cy]

