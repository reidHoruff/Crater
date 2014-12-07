"hello there".put()

class my_class {
  fun sayHi || -> {
    "hi".put()
  }
}

class my_class2 {
  fun sayHi || -> {
    "hi 2".put()
  }
}


fun call |x| -> {
  x.sayHi();
}

var times = 0

while true {
  call(my_class.new());
  call(my_class2.new());
  times += 1

  if times > 10 {
    break;
  }
}





