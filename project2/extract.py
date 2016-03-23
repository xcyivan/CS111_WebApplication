f = open('sitem_cat.dat')
last = ""
for line in f.readlines():
	if line == last:
		print line
	else:
		last = line
