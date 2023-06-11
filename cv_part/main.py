import fingercounter as fi
import cv2

cap = cv2.VideoCapture(0)
fingercounter = fi.Fingercounter(cap)

