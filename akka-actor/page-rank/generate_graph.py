from random import randint

n = 1000
e = 10000

es = set()

print n
for i in range(n):
    b = randint(0, n - 1)
    while (b == i): b = randint(0, n - 1)
    es.add((i, b))
    print " ".join(map(str, [i, b]))
for i in xrange(e):
    a, b = randint(0, n - 1), randint(0, n - 1)
    while (a, b) in es or a == b:
        a, b = randint(0, n - 1), randint(0, n - 1)
    es.add((a, b))
    print " ".join(map(str, [a, b]))
