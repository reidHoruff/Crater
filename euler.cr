var assert = |b| -> {
    if b == false {
        "ERRRRRRR".put();
        return false;
    } else {
        return true;
    }
}

var problem = | name, answer, code | -> {
    name.put();
    var output = code();

    if output != answer
        "Error".put()
    else
        "  Pass".put()

}

problem("Number compare test", true, || -> {
    var all_good = true;
    var armith_check = |a,b| -> {
        var ans = [false, true, false, true];
        var ii = 0;
        for val in [a>b, a>=b, a<b, a<=b] {
            all_good = all_good and assert(val == ans[ii]);
            ii += 1;
        }
    }

    armith_check(1,1)
    armith_check(1,1.0)
    armith_check(1.0,1)
    armith_check(1.0,1.0)

    return all_good;
});

problem("Problem 1", 233168, || -> {
    var sum = 0;
    for x in 1..1000 {
     if (x % 3 == 0) or (x % 5 == 0) {
      sum += x;
     }
    }
    return sum;
});

problem("Problem 2", 4613732, || -> {
    var a = 0;
    var b = 1;
    var even_sum = 0;
    while b < 4000000 {
      var c = a + b;
      a = b;
      b = c;
      if c % 2 == 0 { even_sum += c }
    }
    return even_sum;
});

problem("Problem 3", 6857, || -> {
    var n = 600851475143
    var i = 2
    while (i * i) < n {
         while n % i == 0 {
             n = n / i
         }
         i = i + 1
    }
    return n;
});

problem("Problem 4", 906609, || ->  {
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
    return max(options);
});

problem("Problem 5", 232792560, || -> {
    var gcd = |a, b| -> {
        while b != 0 {
            var c = a;
            a = b;
            b = c % b
        }
        return a
    }

    var ans = 1
    for i in 1..21 {
        ans = ans * (i / gcd(i, ans))
    }

    return ans
});

