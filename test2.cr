
class rain {
    var name = none
    var names = []

    fun set_name |name| -> {
        this.name = name
        ret this
    }

    fun add_inst || -> static.names.append(this)
}

var a = rain.new()
a.set_name("reid").add_inst()
var b = rain.new()
b.set_name("bob").add_inst()
var c = rain.new()
c.set_name("alex").add_inst()

a.set_name("foo")

for n in rain.names {
    n.name.put()
}

