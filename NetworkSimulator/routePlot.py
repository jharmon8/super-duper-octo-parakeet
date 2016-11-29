import math
import numpy
import random
import matplotlib.pyplot as plt
import csv

granularity = 100.0
devices = []

data = []

with open('output_routing.txt', 'r') as f:
	reader = csv.reader(f, delimiter='\t')
	for time, router in reader:
		data.append((time, router))

time = [float(x[0])/granularity for x in data]

timeAxis = range(int(math.ceil(time[-1])))

for point in data:
	if point[1] not in devices:
		devices.append(point[1])

toPlot = []
for p in devices:
	toPlot += [[0] * len(timeAxis)]

print devices
print timeAxis

for point in data:
	sec = int(float(point[0]) / granularity)
	i = devices.index(point[1])
	toPlot[i][sec] += 1

for index in range(0, len(devices)):
	print toPlot[index]
	plt.plot(timeAxis, toPlot[index])

plt.show()







