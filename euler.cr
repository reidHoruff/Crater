
var check = |output, answer| -> {
    if output != answer
        "Error".put()
    else
        "Pass".put()
}

var probs = []

var problem_1 = || -> {
    "Problem 1".put()
    var sum = 0;
    for x in 1..1000 {
     if (x % 3 == 0) or (x % 5 == 0) {
      sum += x;
     }
    }
    check( sum, 233168 )
}

probs += [problem_1]

var problem_2 = || -> {
    "Problem 2".put()
    var a = 0;
    var b = 1;
    var even_sum = 0;
    while b < 4000000 {
      var c = a + b;
      a = b;
      b = c;
      if c % 2 == 0 { even_sum += c }
    }
    check( even_sum, 4613732 )
}

probs += [problem_2]

var problem_3 = || -> {
    "Problem 3".put();
    var n = 600851475143
    var i = 2
    while (i * i) < n {
         while n % i == 0 {
             n = n / i
         }
         i = i + 1
    }
    check( n, 6857 )
}

probs += [problem_3]

var problem_4 = || ->  {

    "Problem 4".put();
    var max = |pals| -> {
        var m = pals[0]
        for i in 1..pals.length {
            if pals[i] > m {
                m = pals[i];
            }
        }
        return m
    }

    var options = []
    for i in 100..1000 {
        for j in 100..1000 {
            if ((i*j).to_s()) == ((i*j).to_s()[end...0]) {
                options = options + [i*j];
            }
        }
    }
    check( max(options), 906609 )
}

probs += [problem_4]

for p in probs {
    p()
}

