
class rain {
    var name = none
    var names = []

    fun set_name |name| -> {
        this.name = name
        ret this
    }

    fun hi |x| -> {
        var y = x
        y.put()
        if x > 0
            hi(x-1)
        y.put()
    }

    fun add_inst || -> static.names.append(this)
}

fun hi |x| -> {
    var y = x
    if x > 0
        hi(x-1)
}

hi(2)
