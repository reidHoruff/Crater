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
            ret 4
        } else {
            "saying hi from object".put()
            ret 3..4 by 1..1
        }
    }
}

rain.say_hi().put()
rain.new().say_hi().put();

(4 != 4).put();
(2.0 + 1.5).put();

(0..100 contains 1).put();

var sum = 0;
1000.each(|x| -> sum += x)
sum.put()
