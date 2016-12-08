import math
import numpy as np
import random
import matplotlib as mlb
mlb.use('TkAgg')
import matplotlib.pyplot as plt
import csv

# time, window, threshold

timeList = []
windowList = []
threshList = []

wind_data = []
wind_names = []

buff_time = []
#buff_occupancy = []
buff_names = []
buff_data = []

with open('output_window.txt', 'r') as f:
	reader = csv.reader(f, delimiter='\t')
	for time, addr, windowSize, thresh in reader:
		if addr not in wind_names:
			wind_data.append((addr, ([], [], [])))
			wind_names.append(addr)
		for entry in wind_data:
			if entry[0] == addr:
				entry[1][0].append(int(float(time)))
				entry[1][1].append(int(windowSize))
				entry[1][2].append(int(thresh))

with open('output_buffer.txt', 'r') as f:
	reader = csv.reader(f, delimiter='\t')
	for time, name, occupancy in reader:
		if name not in buff_names:
			buff_data.append((name, ([], [])))
			buff_names.append(name)
		for entry in buff_data:
			if entry[0] == name:
				entry[1][0].append(int(float(time)))
				entry[1][1].append(int(occupancy))

#print(buff_data)

plt.switch_backend('TkAgg')
#print(plt.style.available)
plt.style.use('ggplot')

plt.subplot(211)
handles = []
for entry in wind_data:
	c, = plt.plot(entry[1][0], entry[1][1], label=entry[0] + " CWND")
	handles.append(c)
#	d, = plt.plot(entry[1][0], entry[1][2], label=entry[0] + " THRESH")
#	handles.append(d)

plt.legend(handles=handles)

#a, = plt.plot(timeList, windowList, label='CWND')
#b, = plt.plot(timeList, threshList, label="Sat")
#plt.legend(handles=[a,b])

xmax = 0
ymax = 0
for data in wind_data:
	xmax = max(xmax, max(data[1][0]))
	ymax = max(ymax, max(data[1][1]))

plt.axis([0, xmax * 1.05, 0, ymax * 1.05])
plt.ylabel('Window Size')

plt.subplot(212)
handles = []
for entry in buff_data:
	c, = plt.plot(entry[1][0], entry[1][1], label=entry[0])
	handles.append(c)

plt.legend(handles=handles)

#plt.subplot(313)
#plt.plot(buff_time, buff_occupancy)

xmax = 0
ymax = 0
for data in buff_data:
	xmax = max(xmax, max(data[1][0]))
	ymax = max(ymax, max(data[1][1]))

plt.axis([0, xmax * 1.05, 0, ymax * 1.05])
plt.xlabel('Time (ms)')
plt.ylabel('Buffer Occupancy (Bytes)')

mng = plt.get_current_fig_manager()
#mng.window.state('zoomed')
mng.resize(*mng.window.maxsize())

plt.show()