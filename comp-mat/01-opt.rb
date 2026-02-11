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

def step(prevx, d)
  prevx += d
  d *= 2

  [prevx, d]
end

def pit?(left, center, right)
  center < left and
  center < right
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
def localize(f)
  x0 = 0  # arbitrary
  d = 1   # arbitrary, >0

  f_left = f.(x0 - d)
  f_center = f.(x0)
  f_right = f.(x0 + d)

  fail 'f must be unimodal!' if
    # "hump"
    f_center > f_left and
    f_center > f_right

  # if (f_center < f_left and
      # f_center < f_right) then

  if pit? f_left, f_center, f_right
    return f_left, f_right
  end

  # f is decreasing
  if (f_right..f_left).cover? f_center
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

    return seek(x0, d, f)
  elsif (f_left..f_right).cover? f_center
    return seek(x0, -d, f)
  end
end

def avg(a, b) = (a + b)/2

def quarter_divide(a, b)
  xs = [a, b]
  xs.insert 1, avg(xs[0], xs[-1])
  xs.insert 1, arg(xs[0], xs[1])
  xs.insert -2, avg(xs[-2], xs[-1])

  xs
end

def half_divide(a, b)
  xs = quarter_divide(a, b)
