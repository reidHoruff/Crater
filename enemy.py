def find_palindrome():
    # We will go backwards to make sure that we return the result ASAP
    all_palindromes = (i * j
                       for i in reversed(range(100, 1000))
                       for j in reversed(range(100, 1000))
                       if str(i * j) == str(i * j)[::-1]
                       )

    return max(all_palindromes)

find_palindrome()
