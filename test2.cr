
class rain {
    var name = none
    var names = []

    fun set_name |name| -> {
        this.name = name
        static.names.append(name)
    }

    fun print_name || -> {
        this.name.put()
    }
}

rain.new().set_name("reid")
rain.new().set_name("bob")
rain.new().set_name("alex")

rain.names.each(|name|->name.put())
for n in rain.new().static.names n.put()

