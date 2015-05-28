from random import randint

n = 100000
e = 1000000

es = set()

print n
for i in xrange(e):
    a, b = randint(0, n - 1), randint(0, n - 1)
    while (a, b) in es or a == b:
        a, b = randint(0, n - 1), randint(0, n - 1)
    print " ".join([a, b])
