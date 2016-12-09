import math
import numpy as np
import random
import matplotlib as mlb
mlb.use('TkAgg')
import matplotlib.pyplot as plt
import csv

# time, window, threshold
print("graphing started")

# binary search an aray for the first index less than x
# or .95 len(arr) if the first index is beyond that
def bSearch(arr, x):
#	prev_start = 0;
	start = 0;
	end = len(arr)
	# while(arr[start] < x):
	# 	prev_start = start
	# 	start = start + int((end - start) / 2)
	# 	if(start > .95 * len(arr)):
	# 		return start

	while start < end - 1:
		if(arr[int((start + end) / 2)] < x):
			start = start + int((end - start) / 2)
		else:
			end = end - int((end - start) / 2)

	return start

#	return prev_start

# do a sliding average from x-delta to x+delta
def getSlidingAverage(arrx, arry, x, delta, minx, maxx):
	minx = max(x - delta, minx)
	maxx = min(x + delta, maxx)
	lastx = minx
	lasty = 0
	total = 0
	width = maxx - minx;

	# # get val at minx
	# y_start = 0
	# for data in zipped:
	# 	if(data[0] > minx):
	# 		break;
	# 	y_start = data[1]

	# get val at maxx
	# y_end = 0
	# for data in zipped:
	# 	if(data[0] > maxx):
	# 		break;
	# 	y_end = data[1]

	# start half way through if we can	begin = 0
	begin = 0
#	if(arrx[int(len(arrx)/2)] < minx):
#		begin = int(len(arrx)/2)
	begin = bSearch(arrx, minx)

	zipped = zip(arrx[begin:], arry[begin:])

	#compute total
	for data in zipped:
		if (data[0] > minx) and (data[0] < maxx):
			if data[0] < lastx:
				print("unsorted input array!")
			total += data[1] * (data[0] - lastx)
			lastx = data[0]
			lasty = data[1]
		if(data[0] > maxx):
			break;

	total += lasty * (maxx - lastx)

	return total / width;


#print(getSlidingAverage([0,1,2,3,4,5,5.5,6,7,8], [1,1,1,1,1,4,5,2,1,1], 5.5, 1, 0, 8))

print("loading data")

wind_data = []
wind_names = []

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

buff_names = []
buff_data = []

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

link_names = []
link_data = []

with open('output_link.txt', 'r') as f:
	reader = csv.reader(f, delimiter='\t')
	for time, name, transmitting, junk in reader:
		if name not in link_names:
			link_data.append((name, ([], [])))
			link_names.append(name)
		for entry in link_data:
			if entry[0] == name:
				entry[1][0].append(float(time))
				entry[1][1].append(int(transmitting))

loss_names = []
loss_data = []

with open('output_loss.txt', 'r') as f:
	reader = csv.reader(f, delimiter='\t')
	for time, name, loss, junk in reader:
		if name not in loss_names:
			loss_data.append((name, ([], [])))
			loss_names.append(name)
		for entry in loss_data:
			if entry[0] == name:
				entry[1][0].append(int(float(time)))
				entry[1][1].append(int(loss))

flow_names = []
flow_data = []
delay_data = []
prev_x = 0

with open('output_flow.txt', 'r') as f:
	reader = csv.reader(f, delimiter='\t')
	for time, name, amt in reader:
		if name not in flow_names:
			delay_data.append((name, ([], [])))
			flow_data.append((name, ([], [])))
			flow_names.append(name)
		for entry in flow_data:
			if entry[0] == name:
				entry[1][0].append(int(float(time)))
				entry[1][1].append(int(amt))
		for entry in delay_data:
			if entry[0] == name:
				entry[1][0].append(int(float(time)))
				entry[1][1].append(int(float(time)) - prev_x)
		prev_x = int(float(time))

for entry in delay_data:
	entry[1][1][0] = 0

#print(buff_data)

plt.switch_backend('TkAgg')
#print(plt.style.available)
plt.style.use('ggplot')


plt.subplot(611) # ----------------------------------------------
print("graphing 1")

handles = []
for entry in wind_data:
	c, = plt.plot(entry[1][0], entry[1][1], label=entry[0] + " CWND")
	handles.append(c)

plt.legend(handles=handles)

xmax = 0
ymax = 0
for data in wind_data:
	xmax = max(xmax, max(data[1][0]))
	ymax = max(ymax, max(data[1][1]))

plt.axis([0, xmax * 1.05, 0, ymax * 1.05])
plt.ylabel('Window Size')

plt.subplot(612) # ----------------------------------------------
print("graphing 2")

new_buff_data = []

ymax = 0
for data in buff_data:
	ymax = max(ymax, max(data[1][1]))

delta = xmax/250;

for entry in buff_data:
	new_entry = (entry[0], ([], []))
	for val in range(0, xmax, int(xmax/250)):
#		print(val)
		new_entry[1][0].append(val)
		new_entry[1][1].append(getSlidingAverage(entry[1][0], entry[1][1], val, delta, 0, xmax))
	new_buff_data.append(new_entry)

handles = []
for entry in new_buff_data:
	c, = plt.plot(entry[1][0], entry[1][1], label=entry[0])
	handles.append(c)

plt.legend(handles=handles)

plt.axis([0, xmax * 1.05, 0, ymax * 1.05])
plt.ylabel('Buffer Occupancy (Bytes)')

plt.subplot(613) # ----------------------------------------------
print("graphing 3")

new_link_data = []

for entry in link_data:
	new_entry = (entry[0], ([], []))
#	for val in range(0, int(xmax), 40):
	for val in range(0, int(xmax), int(xmax/250)):
		new_entry[1][0].append(val)
		new_entry[1][1].append(getSlidingAverage(entry[1][0], entry[1][1], val, delta, 0, xmax))
	new_link_data.append(new_entry)

ymax = 0
for data in new_link_data:
	ymax = max(ymax, max(data[1][1]))

handles = []
for entry in new_link_data:
	c, = plt.plot(entry[1][0], entry[1][1], label=entry[0])
	handles.append(c)

plt.legend(handles=handles)

plt.axis([0, xmax * 1.05, 0, ymax * 1.05])
plt.ylabel('Link Rate (fraction)')


plt.subplot(614) # ----------------------------------------------
print("graphing 4")

new_loss_data = loss_data

# for entry in loss_data:
# 	new_entry = (entry[0], ([], []))
# 	for val in range(0, int(xmax), 40):
# 		new_entry[1][0].append(val)
# 		new_entry[1][1].append(getSlidingAverage(entry[1][0], entry[1][1], val, delta, 0, xmax))
# 	new_loss_data.append(new_entry)

ymax = 0
for data in new_loss_data:
	ymax = max(ymax, max(data[1][1]))

handles = []
for entry in new_loss_data:
	c, = plt.plot(entry[1][0], entry[1][1], label=entry[0])
	handles.append(c)

plt.legend(handles=handles)

plt.axis([0, xmax * 1.05, 0, ymax * 1.05])
plt.ylabel('Losses')

plt.subplot(615) # ---------------------------------------------- flow rate
print("graphing 5")

new_flow_data = []

for entry in flow_data:
	new_entry = (entry[0], ([], []))
	for val in range(0, int(xmax), int(xmax/250)):
		new_entry[1][0].append(val)
		new_entry[1][1].append(getSlidingAverage(entry[1][0], entry[1][1], val, delta, 0, xmax))
	new_flow_data.append(new_entry)

ymax = 0
for data in new_flow_data:
	ymax = max(ymax, max(data[1][1]))

handles = []
for entry in new_flow_data:
	c, = plt.plot(entry[1][0], entry[1][1], label=entry[0])
	handles.append(c)

plt.legend(handles=handles)

plt.axis([0, xmax * 1.05, 0, ymax * 1.05])
plt.ylabel('Flow ACKs')


plt.subplot(616) # ---------------------------------------------- packet delay
print("graphing 6")

new_delay_data = []

for entry in delay_data:
	new_entry = (entry[0], ([], []))
	for val in range(0, int(xmax), int(xmax/250)):
		new_entry[1][0].append(val)
		new_entry[1][1].append(getSlidingAverage(entry[1][0], entry[1][1], val, delta, 0, xmax))
	new_delay_data.append(new_entry)

ymax = 0
for data in new_delay_data:
	ymax = max(ymax, max(data[1][1]))

handles = []
for entry in new_delay_data:
	c, = plt.plot(entry[1][0], entry[1][1], label=entry[0])
	handles.append(c)

plt.legend(handles=handles)

plt.axis([0, xmax * 1.05, 0, ymax * 1.05])
plt.xlabel('Time (ms)')
plt.ylabel('Delay (ms)')

# ----------------------------------------------------------------

mng = plt.get_current_fig_manager()
#mng.window.state('zoomed')
mng.resize(*mng.window.maxsize())

plt.show()

print("graphing finished")
