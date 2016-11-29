import math
import numpy
import random
import matplotlib.pyplot as plt
import csv

data = []
devices = []
packList = []
timeList = [0]
routerList = []

i = 0
index = 0

with open('ass.txt', 'r') as f:
	reader = csv.reader(f, delimiter='\t')
	for time, router in reader:
		timeList.append(int(float(time)))
		routerList.append(router)

for point in routerList:
	if point not in devices:
		devices.append(point)
		packList.append([0])

while index < len(timeList) - 1:
	currRout = devices.index(routerList[index])
	i = 0
	while i < len(packList):
		if i == currRout:
			packList[i].append(packList[i][-1] + 1)
		else:
			packList[i].append(packList[i][-1])
		i += 1
	index += 1


for element in packList:
	plt.plot(timeList, element)

plt.show()