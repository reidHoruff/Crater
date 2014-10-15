
put( map (|x| -> x*x, 0..100 by 2))

dict = {
    "blue": "red",
    "addfive": |num| -> num+5
}

put(map(dict["addfive"], [1,2,100]))

dogs = [3,4,5]

if dogs contains 100 put("hello") else put("bye")

put(none or 5 or none)

dict["red"] = 3443

put(dict contains "red")

l = [1,2,@hello]


fart = [1..100]

x = 111
poo = [1,2,3,4,100]
for x in len(poo) {put(poo[x])}

for k in {"a": 2, "d": 4} put(k)

put(len(0..10 by 2))


put(x)

