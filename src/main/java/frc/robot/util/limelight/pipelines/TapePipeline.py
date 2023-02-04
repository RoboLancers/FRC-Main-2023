import cv2 as cv
import math
import numpy as np


def tallestFilter(contours):
  return sorted(contours, key=lambda contour: cv.boundingRect(contour)[3])[-2:]


def closest(l, K):
  return list[min(range(len(l)), key=lambda i: abs(l[i] - K))]


def filterMostCentralContours(contours):
  return closest(
    contours,
    max(contours, key=lambda contour: cv.boundingRect(contour)[0]) / 2)[-2:]

def filterContours(contours, order=0):
  if len(contours) == 0:
    return np.array([])
  # 0 = tallest, 1 = most central
  if order == 0:
    filteredContours = tallestFilter(contours)
  elif order == 1:
    filteredContours = filterMostCentralContours(contours)

  return filteredContours


def runPipeline(image, llrobot):
 # convert the input image to the HSV color space
  img_hsv = cv.cvtColor(image, cv.COLOR_BGR2HSV)
    # convert the hsv to a binary image by removing any pixels
    # that do not fall within the following HSV Min/Max values
  img_threshold = cv.inRange(img_hsv, (60, 70, 70), (85, 255, 255))

    # find contours in the new binary image
  contours, _ = cv.findContours(img_threshold,
  cv.RETR_EXTERNAL, cv.CHAIN_APPROX_SIMPLE)

  # draw contours
  cv.drawContours(image, contours, -1, (255, 0, 0), 3)


  filteredContours = np.array([[]])
  llpython = []

  if len(contours) > 0:
    filteredContours = filterContours(contours, 0)

    if len(filteredContours) < 2:
      print('filtered contours has no element')
      return None, None, None

    # print(contours[0])
    # print(filteredContours)

    
    x1, y1, w1, h1 = cv.boundingRect(filteredContours[0])
    x2, y2, w2, h2 = cv.boundingRect(filteredContours[1])

    # Draw the bounding boxes
    image = cv.rectangle(image, (x1, y1), (x1 + w1, y1 + h1), (0, 255, 0), 2)
    image = cv.rectangle(image, (x2, y2), (x2 + w2, y2 + h2), (0, 255, 0), 2)

    # Draw the bounding box of the two bounding boxes
    
    image = cv.rectangle(
      image,
      (min(x1, x2), min(y1, y2)),
      (max(x1 + w1, x2 + w2), max(y1 + h1, y2 + h2)),
      (0, 0, 255),
      2,
    )
    

    W = 1 #llrobot[1]

    angle = 0 #llrobot[0]

    R = (h1 / h2)

    A = (math.tan(angle)**2) - 1
    B = -0.5 * W * math.tan(angle) * (R - 1)
    # C = 0

    Z = ((-B) / A)

    X = Z * math.tan(angle)

    llpython = [Z, X, h1, h2, W, R, A, B]

    print(h1, h2,R)

  return filteredContours, image, llpython
