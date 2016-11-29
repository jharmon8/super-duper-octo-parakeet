import math
import numpy
import random
import matplotlib.pyplot as plt
import csv

# time, window, threshold

timeList = []
windowList = []
threshList = []

with open('output_window.txt', 'r') as f:
	reader = csv.reader(f, delimiter='\t')
	for time, windowSize, thresh in reader:
		timeList.append(int(float(time)))
		windowList.append(int(windowSize))
		threshList.append(int(thresh))

plt.plot(timeList, windowList)
plt.plot(timeList, threshList)
plt.show()

