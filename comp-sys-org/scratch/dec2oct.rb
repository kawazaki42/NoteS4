# вариант 5

# XXX
# int = Integer(gets)

def from_dec(str)
  result = 0
  
  # while not str.empty?
  #   lastd = str.chop!
  #   lastd_as_int = lastd.ord - '0'.ord
  #   result *= 10
  #   result += lastd_as_int
  # end

  # result

  str.strip.each_char do
    lastd = it.ord - '0'.ord
    result *= 10
    result += lastd
  end

  result
end

def into_oct(int)
  result = ""

  while int != 0
    # последняя восьмеричная цифра
    int, lastd = int.divmod 8

    # поразрядный сдвиг (восьмеричный)
    # int = Integer(int / 8)

    result.prepend (lastd + '0'.ord).chr
  end

  result
end

p into_oct from_dec gets
