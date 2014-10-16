put(map (|x| -> x*x, 0..100 by 2))

dict = {
    "blue": "red",
    "addfive": |num| -> num + 5
}

put(
    map(dict["addfive"], [1,2,100])
)

dogs = [3,4,5]

if dogs contains 100 put("hello") else put("bye")

put(none or 5 or none)

dict["red"] = 3443

put(dict contains "red")

l = [1, 2, @hello ]

fart = [1..100]

x = 111

poo = [1,2,3,4,100]

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


fun poop |x, t| -> {
    if t is @please
        put("poop " * x)
    else
        put("say please")
}

poop(4, @please)

fun out |x| -> {
    fun add |y| -> {
        return x + y
    }
    return || -> add
}

put("here" * 10)
put(out(4)()(100 % 4))

for g in 10 {
    for h in 10 {
        put(g, h, g+h)
        break
    }
}

put(45343 is 4)
