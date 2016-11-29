import math
import numpy
import random
import matplotlib.pyplot as plt
import csv

# time, flowID, windowsize

devices = []
data = []
plotStuff = []

plotIndex = 0

with open('output_window.txt', 'r') as f:
	reader = csv.reader(f, delimiter='\t')
	for time, flowID, windowsize in reader:
		data.append([time, flowID, windowsize])

for point in data:
	if point[1] not in devices:
		devices.append(point[1])

for thing in devices:
	plotStuff.append([[], []])

for element in data:
	flowIndex = devices.index(element[1])
	plotStuff[flowIndex][0].append(int(float(element[0])))
	plotStuff[flowIndex][1].append(int(element[2]))

while plotIndex < len(plotStuff):
	plt.plot(plotStuff[plotIndex][0], plotStuff[plotIndex][1])
	plotIndex += 1

plt.show()
