import cv2
import numpy as np
import utlis
import ocr

class Scan:
    def __init__(self, cap):
        self.cap = cap

    def handle(self):
        heightImg = 500
        widthImg = 300
        success, img = self.cap.read()
        img = cv2.resize(img, (widthImg, heightImg))  # RESIZE IMAGE
        imgGray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)  # CONVERT IMAGE TO GRAY SCALE
        imgBlur = cv2.GaussianBlur(imgGray, (5, 5), 1)  # ADD GAUSSIAN BLUR
        imgThreshold = cv2.Canny(imgBlur, 100, 100)  # APPLY CANNY BLUR
        kernel = np.ones((5, 5))
        imgDial = cv2.dilate(imgThreshold, kernel, iterations=2)  # APPLY DILATION
        imgThreshold = cv2.erode(imgDial, kernel, iterations=1)  # APPLY EROSION

        # 返回边界轮廓的所有点的集合，是一个num，x，y的三维数组
        contours, hierarchy = cv2.findContours(imgThreshold, cv2.RETR_EXTERNAL,
                                               cv2.CHAIN_APPROX_SIMPLE)  # FIND ALL CONTOURS
        # FIND THE BIGGEST COUNTOUR
        biggest = utlis.biggestContour(contours)  # FIND THE BIGGEST CONTOUR
        if len(biggest) != 0:
            biggest = utlis.reorder(biggest)
            pts1 = np.float32(biggest)  # PREPARE POINTS FOR WARP
            pts2 = np.float32([[0, 0], [widthImg, 0], [0, heightImg], [widthImg, heightImg]])  # PREPARE POINTS FOR WARP
            matrix = cv2.getPerspectiveTransform(pts1, pts2)
            imgWarpColored = cv2.warpPerspective(img, matrix, (widthImg, heightImg))

            # REMOVE 20 PIXELS FORM EACH SIDE
            imgWarpColored = imgWarpColored[20:imgWarpColored.shape[0] - 20, 20:imgWarpColored.shape[1] - 20]
            imgWarpColored = cv2.resize(imgWarpColored, (widthImg, heightImg))

            read_text =ocr.OCR_demo(imgWarpColored)

            return read_text, True
        else:
            return "Can't find the picture", False

