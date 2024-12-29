
      /*

                // 1-st Algorithm: Sum of First N Numbers

                    var n = 10;
                    var sum = n * (n + 1) / 2;
                    println(sum);

                    // 2-nd Algorithm: Factorial of N
                    n = 5;
                    var factorial = 1;
                    for (var i = 1; i <= n; i++) {
                        factorial *= i;
                    }
                    println(factorial);

                    // 3-rd Algorithm: GCD of Two Numbers
                    var a = 48;
                    var b = 18;
                    while (b != 0) {
                        var temp = b;
                        b = a % b;
                        a = temp;
                    }
                    println(a);

                    // 4-th Algorithm: Reverse a Number
                    var number = 1234;
                    var reversed = 0;
                    while (number != 0) {
                        var digit = number % 10;
                        reversed = reversed * 10 + digit;
                        number /= 10;
                    }
                    println(reversed);

                    // 5-th Algorithm: Check if a Number is Prime
                    n = 13;
                    var isPrime = true;
                    if (n <= 1) isPrime = false;
                    for (var i = 2; i * i <= n; i++) {
                        if (n % i == 0) {
                            isPrime = false;
                            break;
                        }
                    }
                    println(isPrime);

                    // 6-th Algorithm: Check if a Number is Palindrome
                    number = 121;
                    reversed = 0;
                    var original = number;
                    while (number != 0) {
                        var digit = number % 10;
                        reversed = reversed * 10 + digit;
                        number /= 10;
                    }
                    println(reversed == original);

                    // 7-th Algorithm: Find the Largest Digit in a Number
                    number = 3947;
                    var largest = 0;
                    while (number != 0) {
                        var digit = number % 10;
                        if (digit > largest) largest = digit;
                        number /= 10;
                    }
                    println(largest);

                    // 8-th Algorithm: Sum of Digits
                    number = 1234;
                    var sumOfDigits = 0;
                    while (number != 0) {
                        sumOfDigits += number % 10;
                        number /= 10;
                    }
                    println(sumOfDigits);

                    // 9-th Algorithm: Multiplication Table
                    n = 5;
                    for (var i = 1; i <= 10; i++) {
                        println(n * i);
                    }

                    // 10-th Algorithm: Nth Fibonacci Number
                    n = 10;
                    var a1 = 0;
                    var b1 = 1;
                    var fib = 0;
                    for (var i = 2; i <= n; i++) {
                        fib = a1 + b1;
                        a1 = b1;
                        b1 = fib;
                    }
                    println(n <= 1 ? n : fib);
                }

                public static void println(Object x) {
                    System.out.println(x);


                }

 */