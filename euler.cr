
var sum = 0;
for x in 1..1000 {
 if (x % 3 == 0) or (x % 5 == 0) {
  sum += x;
 }
}

"Problem 1".put()
sum.put();

"Problem 2".put();

var a = 0;
var b = 1;
var even_sum = 0;

while b < 4000000 {
  var c = a + b;
  a = b;
  b = c;
  if c % 2 == 0 { even_sum += c }
}
even_sum.put()

"Problem 3".put();

var n = 600851475143
var i = 2
while (i * i) < n {
     while n % i == 0 {
         n = n / i
     }
     i = i + 1
}
n.put();

var substr = "foo.bar.baz"[10...0 by 0.5];
substr.put();