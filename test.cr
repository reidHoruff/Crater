put(map (|x| -> x*x, 0..100 by 2))

var dict = {
    "blue": "red",
    "addfive": |num| -> num + 5
}

put(
    map(dict["addfive"], [1,2,100])
)

var dogs = [3,4,5]

if dogs contains 100
    put("hello")
else
    put("bye")

put(none or 5 or none)

dict["red"] = 3443

put(dict contains "red")

var l = [1, 2, @hello ]

var fart = [1..100]

var x = 111

var poo = [1,2,3,4,100]

for x in len(poo) {
    put(poo[x])
}

while true {
    if len(map(|a| -> a, [1,2,3])) > 0 {
        for k in {"a": 2, "d": 4} {
            put(k)
        }
    } else {
        put({})
    }
    break;
}

put(len(0..10 by 2))


fun poop22 |x, t| -> {
    if t is @please
        put("poop " * x)
    else
        put("say please")
}

poop22(4, @please)

fun out |x| -> {
    fun add |y| -> {
        return x + y
    }
    return || -> (add, add)
}

put("here" * 10)
put(out(4)()[0](100 % 4))

var rng = 1..100
put(rng)

put((1,2) + (2,3))
put([1,2,3] + [4,5,6][5])
put((|x| -> @fuckyou)(5))

fun haha || -> {
    put(1 + 1 + none)
}

fun poop |x| -> {
    haha()
}

var poop2 = poop

x = 4
x = 2
put(x)

for x in 5 {
}

l = []

map(|x| -> l.append(x+1), 0..10)
put(l)

put([1,2,3])

fun again |x, y| -> for g in x y()

var r = 1..100 by 33 + 3

put(4.to_s().length())

4.each(|x| -> put(x))

put([1,2,3].append(3,3,4, r.expand()))

fun recur |x| -> {
    var yyy = x
    put(yyy)
    if x > 0
        recur(x-1)
    put(yyy)
}

recur(4)

put("start")

if true and (1 < 5) {
    var lol = 4
    put(lol)
    if true {
        lol = 5;
        put(lol)
    }
    put(lol)
}
