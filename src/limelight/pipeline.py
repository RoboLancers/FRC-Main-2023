import cv2 as cv
import math 
def tallestFilter(contours):
    return sorted(contours, key=lambda contour: cv.boundingRect(contour)[3])[-2:]


def closest(list, K):
    return list[min(range(len(list)), key=lambda i: abs(list[i] - K))]


def filterMostCentralContours(contours):

    return closest(
        contours, max(contours, key=lambda contour: cv.boundingRect(contour)[0]) / 2
    )[-2:]


def filterContours(contours, order=0):

    if len(contours) == 0:
        return []
    filteredContours = []
    # 0 = tallest, 1 = most central
    if order == 0:
        filteredContours.append(tallestFilter(contours))
    elif order == 1:
        filteredContours.append(filterMostCentralContours(contours))

    return filteredContours


def runPipeline(image, llrobot):

    img_hsv = cv.cvtColor(image, cv.COLOR_BGR2HSV)

    img_threshold = cv.inRange(
        img_hsv, cv.Mat(shape=[60, 60, 70]), cv.Mat(shape=[255, 255, 255])
    )

    contours, _ = cv.findContours(
        img_threshold, cv.RETR_EXTERNAL, cv.CHAIN_APPROX_SIMPLE
    )

    filteredContours = filterContours(contours, llrobot[2])

    if len(filteredContours) == 0:
        return

    x1, y1, w1, h1 = cv.boundingRect(filteredContours[0])
    x2, y2, w2, h2 = cv.boundingRect(filteredContours[1])

    angle = llrobot[0]

    R = (h1 / h2)

    W = llrobot[1]

    A = (math.tan(angle)**2) - 1
    B = -0.5 * W * math.tan(angle) * (R - 1)

    Z = ( -B / A)

    X = Z * math.tan(angle)

    llpython = [Z, X, R, A, B, h1, h2, angle]

   

    return image, llpython