
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

rain.call_me();
rain.say_hi()
rain.new().say_hi();

(4 != 4).put();

