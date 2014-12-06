class rain {
    var name = || -> this.say_hi()
    var color = "red"

    static fun call_me || -> {
        var other = ["a", "b", "c"];
        other = other.map(|x| -> x + " " + static.color);
        other.put()
    }

    fun say_hi || ->  {
        if this is not static {
            "saying hi from static".put()
        } else {
            "saying hi from object".put()
        }
    }
}

rain.say_hi().put()
rain.new().say_hi().put();

(4 != 4).put();
(2.0 + 1.5).put();

(0..100 contains 1).put();


(1..100 contains 100).put();
var foo = [1,2,3,4,3,6];
foo.sort();

var dict = {"a": 123, true: "cat"}
for key in dict key.put()

dict[true].put()
var x = 3
    while x > 0 {
        x.put()
        x = x - 1
        if x == 1 break
    }

var d =  {"a": "cat", "b": "dog"}
for key in d {
    key.put()
    d[key].put()
}

fun count |x| -> {
    if x > 0 {
        x.put()
        count(x - 1)
        var count = "count";
    }
}

var lst = [1,2,3,4,5]
lst.map(|x| -> x*x).put();

fun genFun || -> {
	ret || -> none
}

for x in 0...1 {
    for y in 0...1 {
        for z in 0...1 {
            var sum = x+y+x
            if sum % 2 == 0 {
                sum.put()
            }
        }
    }
}


