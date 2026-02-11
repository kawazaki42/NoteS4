# Вариант 4

def f(x, y, z)
  x -= 0.6
  x = x ** 4
  x *= 0.7

  y -= 0.5
  y *= y * 0.2

  z -= 0.1
  z = z ** 4
  z *= 0.7

  Math.log x + y + z + 1
end

EPSILON = 1e-10

# def next_x(prev_x, iteration)
#   = x_prev + 2 ** (iteration) * d

def hump?(left, center, right)
  center > left and
  center > right
end

def pit?(left, center, right)
  center < left and
  center < right
end

def increase?(left, center, right)
  # (left..right).cover? center
  left < center and center < right
end

def decrease?(left, center, right)
  left > center and center > right
end

def step(prevx, d)
  prevx += d
  d *= 2

  [prevx, d]
end

def seek(x0, d, f)
  x1, d = step(x0, d)
  x2, d = step(x1, d)

  until pit? *[x0, x1, x2].map(&f)
    # d *= 2
    x3, d = step(x2, d)
    x0, x1, x2 = x1, x2, x3
  end

  [x0, x2].sort
end

# Svenn method
def localize(f, x0 = 0, d = 1)
  # x0 = 0  # arbitrary
  # d = 1   # arbitrary, >0

  # f_left = f.(x0 - d)
  # f_center = f.(x0)
  # f_right = f.(x0 + d)

  range = [x0-d, x0, x0+d]
  ys = range.map(&f)

  fail 'f must be unimodal!' if hump? *ys
    # # "hump"
    # f_center > f_left and
    # f_center > f_right

  # if (f_center < f_left and
      # f_center < f_right) then

  if pit? *ys
    range.values_at(0, 2)
    # return f_left, f_right
  # end

  elsif decrease? *ys

  # f is decreasing
  # if (f_right..f_left).cover? f_center
    # # k = 1

    # # pow2 === 2 ** (k-1)
    # # pow2 = 1

    # x1, d = step(x0, d)
    # x2, d = step(x1, d)

    # # x1 = x0 + d
    # # x2 = x1 + 2 * d

    # # x1 = next_x x0, 0
    # # x2 = next_x x1, 1
    # # xs = [x0, x1, x2]

    # # xk = next_x x0, 0
    # # fxk = f.call xk

    # until pit? *[x0, x1, x2].map(&f)
    #   # d *= 2
    #   x3, d = step(x2, d)
    #   x0, x1, x2 = x1, x2, x3
    # end

    # return x0, x2

    seek(x0, d, f)
  elsif increase? *ys
    seek(x0, -d, f)
  end
end

def avg(a, b) = (a + b)/2

# def insert_avg(arr, at)
#   a = arr[at-1]
#   b = arr[at]
#   c = avg a, b
#   arr.insert(at, c)
# end

def quarter_divide(a, b)
  ab = avg(a, b)
  aab = avg(a, ab)
  abb = avg(ab, b)

  [a, aab, ab, abb, b]

  # insert_avg xs, 1
  # insert_avg xs, -1
  # insert_avg xs, 1
  # xs.insert 1, avg(xs[0], xs[-1])
  # xs.insert 1, arg(xs[0], xs[1])
  # xs.insert -2, avg(xs[-2], xs[-1])

  # xs
end

def confine_quarters(f, a, b)
  xs = quarter_divide(a, b)

  y3 = xs[1, 3].map(&f)

  indices = case
    when pit?(*y3) then [1, 3]  # mid
    when decrease?(*y3) then [2, 4]  # rightmost
    when increase?(*y3) then [0, 2]  # leftmost
    end

  xs.values_at *indices
end

def confine(f, a, b, prec = EPSILON)
  until b - a <= 2 * prec
    a, b = confine_quarters(f, a, b)
  end

  (a + b)/2
end

# if run as main
if $PROGRAM_NAME == __FILE__
  f = proc do |x|
    arg = (x-2) * x + 11
    Math.log(arg)
  end

  a, b = localize f

  p confine f, a, b
end
