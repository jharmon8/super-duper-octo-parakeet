import math
import numpy
import random
import matplotlib.pyplot as plt

time, event = numpy.genfromtxt("output.txt", unpack = True)

packet = []
acknowledgement = []
packetCounter = 0
acknowledgementCounter = 0
index = 0


while index < len(time):
	if event[index] == 0:
		packetCounter += 1
	else:
		acknowledgementCounter += 1

	index += 1

	packet.append(packetCounter)
	acknowledgement.append(acknowledgementCounter)


plt.plot(time, packet)
plt.plot(time, acknowledgement)
plt.show()







