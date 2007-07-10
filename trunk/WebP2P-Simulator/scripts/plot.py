#!/usr/bin/env python

import sys

INICIO="webp2p_sim.proxy.Browser.generateRequest"
FIM="webp2p_sim.proxy.Browser.hereIsContent"

f = open(sys.argv[1])

def extractCommand(line):
	ini = line.find("*") + 2
	fim = line.find("==>") - 1
	return line[ini:fim]

def extractRequestId(line):
	tks = line.split()
	i = tks.index("Request:")
	return int(tks[i + 1])

def extractTime(line):
	tks = line.split()
	i = tks.index("SYSTEMTIME:")
	return int(tks[i + 1][:-1])

m = dict()

for l in f:
	t = extractTime(l)
	cmd = extractCommand(l)
	if (cmd == INICIO):
		m[extractRequestId(l)] = [t, None]
	if (cmd == FIM):
		m[extractRequestId(l)][1] = t

for i in m.itervalues():
	if (not(i[0] is None) and not(i[1] is None)): print i[0], i[1] - i[0]
