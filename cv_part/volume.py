
import numpy as np
import handtrackmodel as htm

class Volume:
    def __init__(self, cap):
        self.cap = cap

    def handle(self):
        detector = htm.handDetector(detectionCon=0.8, maxHands=1)
        i = 0
        while i < 100:
            success, img = self.cap.read()
            # Find Hand
            img = detector.findHands(img)
            lmList, bbox, flag = detector.findPosition(img, draw=False)
            if flag == False:
                continue
            if len(lmList) != 0:
                # Filter based on size
                area = (bbox[2] - bbox[0]) * (bbox[3] - bbox[1]) // 100
                # print(area)
                if 250 < area < 1000:
                    # Find Distance between index and Thumb
                    length, img, lineInfo = detector.findDistance(4, 8, img)
                    # Convert Volume
                    volPer = np.interp(length, [100, 400], [0, 100])
                    # Reduce Resolution to make it smoother
                    smoothness = 10
                    volPer = smoothness * round(volPer / smoothness)
            print(volPer)
            i += 1