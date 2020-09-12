# The script reads a file path from a user for a .cap file, parses the file,
# outputs the information in table format, displays the summary and TCP connections.

# Author: Damir Omelic
# SID: 040918352
# Course: CST8108 012
# Assignment: Wireshark Log Dissector
# Professor: Risvan Coskun
# Date: April 21, 2019

import os
from scapy.all import *
from prettytable import PrettyTable

# enter path to .cap file
user_input = raw_input("Enter file path: ")

# opens the .cap file
f = open(user_input, 'r+')

# initialize the columns
source = []
destination = []
sourceIp = []
destinationIp = []
layer4 = []
layer7 = []
sourcePort = []
destinationPort = []
ttl = []
info = []

# reads the cap files
packets = rdpcap(user_input)

# initialize the flags
flags = ' ' 

# enters console command and reads from file
os.system('tshark -r ' + str(user_input) + '> tshark.txt')

# reads the textual file
with open('tshark.txt', 'r') as myfile:
  data = myfile.read()

# splits each new line to list
tshark = data.split('\n')

# opens a textual file
d = open('log.txt', 'w')

# remove generated file (optional)
#os.remove("tshark.txt")

# formats the table
t = PrettyTable([
  '#',
  'L2 Src',
  'L2 Dest',
  'L3 Src',
  'L3 Dest',
  'L4P',
  'L7P',
  'SPort',
  'DPort',
  'TTL',
  'Info'
  ])

# initialize the sentinel value
i = 0

# start of loop
for packet in packets:
  # appends layer 2 source
  source.append(str(packet.src))

  # appends layer 2 destination
  destination.append(str(packet.dst))

  try:
    # appends layer 3 source
    sourceIp.append(str(packet[IP].src))
  except:
    sourceIp.append('')

  try:
    # appends layer 3 destination
    destinationIp.append(str(packet[IP].dst))
  except:
    destinationIp.append('')

  # appends layer 4 protocol
  try:
    # defaults padding fields
    if str(packet.getlayer(2).name) == 'Padding':
      layer4.append('')
    elif 'IPv6 Extension Header - Hop-by-Hop Options Header' in str(packet.getlayer(2).name):
      layer4.append('ICMPv6')
    elif 'IP Option Router Alert' in str(packet.getlayer(2).name):
      layer4.append('IGMP')
    else:
      layer4.append(str(packet.getlayer(2).name))
  except:
    layer4.append('')

  # checks for common protocols layer 7 protocols and appends
  if 'SSDP' in tshark[i]:
    layer7.append('SSDP')
  elif 'HTTP' in tshark[i]:
    if 'TCP' in tshark[i]:
      layer7.append('')
    else:
      layer7.append('HTTP')
  elif 'DNS' in tshark[i]:
    layer7.append('DNS')
  elif 'BROWSER' in tshark[i]:
    layer7.append('SMB')
  elif 'NBNS' in tshark[i]:
    layer7.append('NBNS')
  elif 'MDNS' in tshark[i]:
    layer7.append('MDNS')
  elif 'LLMNR' in tshark[i]:
    layer7.append('LLMNR')
  elif 'ICMP' in tshark[i]:
    layer7.append('ICMP')
  elif 'NBT' in tshark[i]:
    layer7.append('NBT')
  elif 'IRC' in tshark[i]:
    layer7.append('IRC')
  elif 'ASAP' in tshark[i]:
    layer7.append('ASAP')
  elif 'TLSv1' in tshark[i]:
    layer7.append('SSL')

  else:
    layer7.append('')

  # appends source port
  try:
    sourcePort.append(str(packet.sport))
  except:
    sourcePort.append('')

  # appends destination port
  try:
    destinationPort.append(str(packet.dport))
  except:
    destinationPort.append('')

  # appends TTL
  try:
    ttl.append(str(packet.ttl))
  except:
    ttl.append('')

  # reads flags
  try:
    flags = str(packet[TCP].flags)
  except:
    pass

  hasInfo = False

  try:
    # characteristic ARP value
    if packet.type == 2054:
      info.append('ARP')
      hasInfo = True
    else:
     if 'TCP' in tshark[i]:
        # characteristic TCP connection values
        if flags == '2':
          info.append('[SYN]')
          hasInfo = True
        if flags == '18':
          info.append('[SYN-ACK]')
          hasInfo = True
        if flags == '16':
          info.append('[ACK]')
          hasInfo = True
        if flags == '24':
          info.append('[PSH-ACK]')
          hasInfo = True
        if flags == '25':
          info.append('[FIN-PSH-ACK]')
          hasInfo = True
        if flags == '17':
          info.append('[FIN-ACK]')
          hasInfo = True
  except:
    pass

  if hasInfo == False:
    info.append('')

  # adds row to table
  t.add_row([
    i + 1,
    source[i],
    destination[i],
    sourceIp[i],
    destinationIp[i],
    layer4[i],
    layer7[i],
    sourcePort[i],
    destinationPort[i],
    ttl[i],
    info[i]
    ])

  # increments sentinel value
  i += 1
# end of loop

# closes the input file
f.close()

# writes the table to console
print(t)

# writes the table to output file
d.write(str(t))

# gets the unique occurences in each column
sourceSet = set(source)
destinationSet = set(destination)
sourceIpSet = set(sourceIp)
destinationIpSet = set(destinationIp)
sourcePortSet = set(sourcePort)
destinationPortSet = set(destinationPort)
layer4set = set(layer4)
layer7set = set(layer7)
infoset = set(info)

# manipulates the string to remove formatting and punctuation
sourceArr = str(sourceSet).replace('set([', '').replace('])','').split(',')
destinationArr = str(destinationSet).replace('set([', '').replace('])','').split(',')
sourceIpArr = str(sourceIpSet).replace('set([', '').replace('])','').split(',')
destinationIpArr = str(destinationIpSet).replace('set([', '').replace('])','').split(',')
sourcePortArr = str(sourcePortSet).replace('set([', '').replace('])','').split(',')
destinationPortArr = str(destinationPortSet).replace('set([', '').replace('])','').split(',')
l4arr = str(layer4set).replace('set([', '').replace('])','').split(',')
l7arr = str(layer7set).replace('set([ ', '').replace('])','').split(',')
infoarr = str(infoset).replace('set([ ', '').replace('])','').split(',')

# deletes first blank index
del l7arr[0]
del infoarr[0]
#del sourceArr[0]
#del destinationArr[0]
#del sourceIpArr[0]
#del destinationIpArr[0]
#del sourcePortArr[0]
#del destinationPortArr[0]
#del l4arr[0]

# counter for each unique value
sourceCount = [0] * len(sourceArr)
destinationCount = [0] * len(destinationArr)
sourceIpCount = [0] * len(sourceIpArr)
destinationIpCount = [0] * len(destinationIpArr)
sourcePortCount = [0] * len(sourcePortArr)
destinationPortCount = [0] * len(destinationPortArr)
l4count = [0] * len(l4arr)
l7count = [0] * len(l7arr)
infocount = [0] * len(infoarr)

# loops through columns, checks each value and adds to the counter list
# also removes extraneous formatting from the comparison list
for x in range(0, len(source)):
  for y in range(0, len(sourceArr)):
    sourceArr[y] = str(sourceArr[y]).replace(' ','').replace('\'','')
    if str(sourceArr[y]) in str(source[x]):
      sourceCount[y] += 1
  for y in range(0, len(destinationArr)):
    destinationArr[y] = str(destinationArr[y]).replace(' ','').replace('\'','')
    if str(destinationArr[y]) in str(destination[x]):
      destinationCount[y] += 1
  for y in range(0, len(sourceIpArr)):
    sourceIpArr[y] = str(sourceIpArr[y]).replace(' ','').replace('\'','')
    if str(sourceIpArr[y]) in str(sourceIp[x]):
      sourceIpCount[y] += 1
  for y in range(0, len(destinationIpArr)):
    destinationIpArr[y] = str(destinationIpArr[y]).replace(' ','').replace('\'','')
    if str(destinationIpArr[y]) in str(destinationIp[x]):
      destinationIpCount[y] += 1
  for y in range(0, len(sourcePortArr)):
    sourcePortArr[y] = str(sourcePortArr[y]).replace(' ','').replace('\'','')
    if str(sourcePortArr[y]) in str(sourcePort[x]):
      sourcePortCount[y] += 1
  for y in range(0, len(destinationPortArr)):
    destinationPortArr[y] = str(destinationPortArr[y]).replace(' ','').replace('\'','')
    if str(destinationPortArr[y]) in str(destinationPort[x]):
      destinationPortCount[y] += 1
  for y in range(0, len(l4arr)):
    l4arr[y] = str(l4arr[y]).replace(' ','').replace('\'','')
    if str(l4arr[y]) in str(layer4[x]):
      l4count[y] += 1
  for y in range(0, len(l7arr)):
    l7arr[y] = str(l7arr[y]).replace(' ','').replace('\'','')
    if str(l7arr[y]) in str(layer7[x]):
      l7count[y] += 1
  for y in range(0, len(infoarr)):
    infoarr[y] = str(infoarr[y]).replace(' ','').replace('\'','')
    if (str(infoarr[y])) in str(info[x]):
      infocount[y] += 1

# prints the layer 2 source column summary; also writes to file
print('\nLayer 2 Source Summary\n')
d.write('\nLayer 2 Source Summary\n')

for x in range(0, len(sourceCount)):
  if '' != sourceArr[x]:
    print (str(sourceArr[x]) + ': ' + str(sourceCount[x]))
    d.write(str(sourceArr[x]) + ': ' + str(sourceCount[x]) + '\n')

# prints the layer 2 destination column summary; also writes to file
print('\nLayer 2 Destination Summary\n')
d.write('\nLayer 2 Destination Summary\n')

for x in range(0, len(destinationCount)):
  if '' != destinationArr[x]:
    print(str(destinationArr[x]) + ': ' + str(destinationCount[x]))
    d.write(str(destinationArr[x]) + ': ' + str(destinationCount[x]) + '\n')

# prints the layer 3 source column summary; also writes to file
print('\nLayer 3 Source Summary\n')
d.write('\nLayer 3 Source Summary\n')

for x in range(0, len(sourceIpCount)):
  if '' != sourceIpArr[x]:
    print(str(sourceIpArr[x]) + ': ' + str(sourceIpCount[x]))
    d.write(str(sourceIpArr[x]) + ': ' + str(sourceIpCount[x]) + '\n')

# prints the layer 3 destination column summary; also writes to file
print('\nLayer 3 Destination Summary\n')
d.write('\nLayer 3 Destination Summary\n')

for x in range(0, len(destinationIpCount)):
  if '' != destinationIpArr[x]:
    print(str(destinationIpArr[x]) + ': ' + str(destinationIpCount[x]))
    d.write(str(destinationIpArr[x]) + ': ' + str(destinationIpCount[x]) + '\n')

# prints the layer 4 port column summary; also writes to file
print('\nLayer 4 Summary\n')
d.write('\nLayer 4 Summary\n')

for x in range(0, len(l4count)):
  if '' != l4arr[x]:
    print(str(l4arr[x]) + ': ' + str(l4count[x]))
    d.write(str(l4arr[x]) + ': ' + str(l4count[x]) + '\n')

# prints the layer 7 port column summary; also writes to file
print('\nLayer 7 Summary\n')
d.write('\nLayer 7 Summary\n')

for x in range(0, len(l7count)):
  if '' != l7arr[x]:
    print(str(l7arr[x]) + ': ' + str(l7count[x]))
    d.write(str(l7arr[x]) + ': ' + str(l7count[x]) + '\n')

# prints the source port column summary; also writes to file
print('\nSource Port Summary\n')
d.write('\nSource Port Summary\n')

for x in range(0, len(sourcePortCount)):
  if '' != sourcePortArr[x]:
    print(str(sourcePortArr[x]) + ': ' + str(sourcePortCount[x]))
    d.write(str(sourcePortArr[x]) + ': ' + str(sourcePortCount[x]) + '\n')

# prints the destination port column summary; also writes to file
print('\nDestination Port Summary\n')
d.write('\nDestination Port Summary\n')

for x in range(0, len(destinationPortCount)):
  if '' != destinationPortArr[x]:
    print(str(destinationPortArr[x]) + ': ' + str(destinationPortCount[x]))
    d.write(str(destinationPortArr[x]) + ': ' + str(destinationPortCount[x]) + '\n')

# prints the info column summary; also writes to file
print('\nInfo Summary\n')
d.write('\nInfo Summary\n')

for x in range(0, len(infocount)):
  if '' != infoarr[x]:
    print(str(infoarr[x]) + ': ' + str(infocount[x]))
    d.write(str(infoarr[x]) + ': ' + str(infocount[x]) + '\n')

# formats the three-way handshake table
h = PrettyTable([
  '#',
  'L2 Src',
  'L2 Dest',
  'L3 Src',
  'L3 Dest',
  'L4P',
  'L7P',
  'SPort',
  'DPort',
  'TTL',
  'Info'
  ])

# formats the termination table
b = PrettyTable([
  '#',
  'L2 Src',
  'L2 Dest',
  'L3 Src',
  'L3 Dest',
  'L4P',
  'L7P',
  'SPort',
  'DPort',
  'TTL',
  'Info'
  ])

# formats the half-open connections table
o = PrettyTable([
  '#',
  'L2 Src',
  'L2 Dest',
  'L3 Src',
  'L3 Dest',
  'L4P',
  'L7P',
  'SPort',
  'DPort',
  'TTL',
  'Info'
  ])

# counter for the TCP connections
twhCount = 0
termCount = 0
halfOpen = 0
openFlag = True

# loops through the tables for each significant flag occurence; if flag is present,
# inner loops increment from flag index to end, matching for the source and
# destination ports for expected follow-up flags; finally adds information to table
for x in range(0, len(info)):
  openFlag = True
  try:
    # start of three-way handshake checking block
    if '[SYN]' in str(info[x]):
      for y in range(x + 1, len(info)):
        if '[SYN-ACK]' in str(info[y]):
          if sourcePort[y] == destinationPort[x] and destinationPort[y] == sourcePort[x]:
            for z in range(y + 1, len(info)):
              if '[ACK]' in str(info[z]):
                if sourcePort[z] == sourcePort[x] and destinationPort[z] == destinationPort[x]:
                  twhCount += 1
                  h.add_row([x + 1,
                    source[x],
                    destination[x],
                    sourceIp[x],
                    destinationIp[x],
                    layer4[x],
                    layer7[x],
                    sourcePort[x],
                    destinationPort[x],
                    ttl[x],
                    info[x]
                    ])
                  h.add_row([y + 1,
                    source[y],
                    destination[y],
                    sourceIp[y],
                    destinationIp[y],
                    layer4[y],
                    layer7[y],
                    sourcePort[y],
                    destinationPort[y],
                    ttl[y],
                    info[y]
                    ])
                  h.add_row([z + 1,
                    source[z],
                    destination[z],
                    sourceIp[z],
                    destinationIp[z],
                    layer4[z],
                    layer7[z],
                    sourcePort[z],
                    destinationPort[z],
                    ttl[z],
                    info[z]
                    ])
                  break
    # start of termination checking block (part 1)
    if '[FIN-ACK]' in str(info[x]):
      for y in range(x + 1, len(info)):
        if '[ACK]' in str(info[y]):
          if sourcePort[y] == destinationPort[x] and destinationPort[y] == sourcePort[x]:
            for z in range(y + 1, len(info)):
              if '[FIN-ACK]' in str(info[z]):
                if sourcePort[z] == sourcePort[y] and destinationPort[z] == destinationPort[y]:
                  for k in range(z + 1, len(info)):
                    if '[ACK]' in str(info[k]):
                      if sourcePort[k] == destinationPort[z] and destinationPort[k] == sourcePort[z]:
                        termCount += 1
                        openFlag = False
                        b.add_row([x + 1,
                          source[x],
                          destination[x],
                          sourceIp[x],
                          destinationIp[x],
                          layer4[x],
                          layer7[x],
                          sourcePort[x],
                          destinationPort[x],
                          ttl[x],
                          info[x]
                          ])
                        b.add_row([y + 1,
                          source[y],
                          destination[y],
                          sourceIp[y],
                          destinationIp[y],
                          layer4[y],
                          layer7[y],
                          sourcePort[y],
                          destinationPort[y],
                          ttl[y],
                          info[y]
                          ])
                        b.add_row([z + 1,
                          source[z],
                          destination[z],
                          sourceIp[z],
                          destinationIp[z],
                          layer4[z],
                          layer7[z],
                          sourcePort[z],
                          destinationPort[z],
                          ttl[z],
                          info[z]
                          ])
                        b.add_row([k + 1,
                          source[k],
                          destination[k],
                          sourceIp[k],
                          destinationIp[k],
                          layer4[k],
                          layer7[k],
                          sourcePort[k],
                          destinationPort[k],
                          ttl[k],
                          info[k]
                          ])
                        break
        # start of termination checking block (part 2)
        if '[FIN-ACK]' in str(info[y]):
          if sourcePort[y] == destinationPort[x] and destinationPort[y] == sourcePort[x]:
            for z in range(y + 1, len(info)):
              if '[ACK]' in str(info[z]):
                if sourcePort[z] == destinationPort[y] and destinationPort[z] == sourcePort[y]:
                  for k in range(z + 1, len(info)):
                    if '[ACK]' in str(info[k]):
                      if sourcePort[k] == destinationPort[z] and destinationPort[k] == sourcePort[z]:
                        termCount += 1
                        openFlag = False
                        b.add_row([x + 1,
                          source[x],
                          destination[x],
                          sourceIp[x],
                          destinationIp[x],
                          layer4[x],
                          layer7[x],
                          sourcePort[x],
                          destinationPort[x],
                          ttl[x],
                          info[x]
                          ])
                        b.add_row([y + 1,
                          source[y],
                          destination[y],
                          sourceIp[y],
                          destinationIp[y],
                          layer4[y],
                          layer7[y],
                          sourcePort[y],
                          destinationPort[y],
                          ttl[y],
                          info[y]
                          ])
                        b.add_row([z + 1,
                          source[z],
                          destination[z],
                          sourceIp[z],
                          destinationIp[z],
                          layer4[z],
                          layer7[z],
                          sourcePort[z],
                          destinationPort[z],
                          ttl[z],
                          info[z]
                          ])
                        b.add_row([k + 1,
                          source[k],
                          destination[k],
                          sourceIp[k],
                          destinationIp[k],
                          layer4[k],
                          layer7[k],
                          sourcePort[k],
                          destinationPort[k],
                          ttl[k],
                          info[k]
                          ])
                        break
    # start of half-open connections block; boolean condition executes if none
    # of the above blocks executed
    if openFlag == True:
      if '[FIN-ACK]' in str(info[x]):
        for y in range(x + 1, len(info)):
          if '[ACK]' in str(info[y]):
            if sourcePort[y] == destinationPort[x] and destinationPort[y] == sourcePort[x]:
              halfOpen += 1
              o.add_row([x + 1,
                source[x],
                destination[x],
                sourceIp[x],
                destinationIp[x],
                layer4[x],
                layer7[x],
                sourcePort[x],
                destinationPort[x],
                ttl[x],
                info[x]
                ])
              o.add_row([y + 1,
                source[y],
                destination[y],
                sourceIp[y],
                destinationIp[y],
                layer4[y],
                layer7[y],
                sourcePort[y],
                destinationPort[y],
                ttl[y],
                info[y]
                ])
              break
  except:
    pass

# prints the three-way handshakes; also writes to file
print('\nThree-Way Handshakes\n')
d.write('\nThree-Way Handshakes\n')

print('There are: ' + str(twhCount) + ' three-way handshakes\n')
d.write('There are: ' + str(twhCount) + ' three-way handshakes\n')
if (twhCount != 0):
  print(h)
  d.write(str(h))

# prints the terminations; also writes to file
print('\nTermination\n')
d.write('\nTermination\n')

print('There are: ' + str(termCount) + ' terminations\n')
d.write('There are: ' + str(termCount) + ' terminations\n')
if (termCount != 0):
  print(b)
  d.write(str(b))

# prints the half-open connections; also writes to file
print('\nHalf-Open Connections\n')
d.write('\nHalf-Open Connections\n')

print('There are: ' + str(halfOpen) + ' half-open connections\n')
d.write('There are: ' + str(halfOpen) + ' half-open connections\n')
if (halfOpen != 0):
  print(o)
  d.write(str(o))

# closes the output file
d.close()
