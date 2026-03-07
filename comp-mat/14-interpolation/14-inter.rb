# Вариант 4

require_relative 'linal'

module Interpolation
  TEST_POINTS = [
    [-5, 4],
    [-2, -2],
    [1, 2],
    [4, -4],
    [7, 7],
    [10, -7],
  ]

  module_function  # makes methods static

  def jordan_gauss(pts)
    mat = pts.map do |x, y|
      pts.each_index.map{|i| x ** i }.push(y)
    end

    LinAl::jordan_gauss(mat)
  end

  def lagrange(pts, x)
    xs, ys = pts.transpose

    pts.each_index.sum do |k|
      # xs_no_k = xs[..k-1] + xs[k+1..]
      xs_no_k = xs.values_at(..k-1, k+1..)

      # num = xs_no_k.map do |xm|
      #   x - xm
      # end.reduce :*

      # den = xs_no_k.map do |xm|
      #   xs[k] - xm
      # end.reduce :*

      num = xs_no_k.reduce do |prod, xm|
        prod *= x - xm
      end

      den = xs_no_k.reduce do |prod, xm|
        prod *= xs[k] - xm
      end

      ys[k] * Rational(num, den)
    end
  end

  module Newton
    module_function  # all below

    # def self.split_diff(pts)
    def split_diff(xs, ys, leap_x = 0)
      ypairs = ys.lazy.each_cons(2)
      xpairs = xs.lazy.each_cons(2 + leap_x).map do |win|
        [win.first, win.last]
      end

      diff = proc { |a, b| b - a }

      xdiffs = xpairs.map(&diff)
      ydiffs = ypairs.map(&diff)

      ydiffs.zip(xdiffs).map { |y, x| Rational(y, x) }

      # pts.each_cons(2).map do |a, b|
      #   xa, ya = a
      #   xb, yb = b
      #
      #   Rational(yb - ya, xb - xa)
      # end
    end

    # def self.split_diff_with_order(xs, ys, order)

    def forward(pts, x)
      # _x0, y0 = pts.first
      xs, ys = pts.transpose

      # split_diffs = pts.reduce([ys]) do |total, this|
      #   total << split_diff(xs, total.last, )

      # Enumerator.produce([ys], 0) do |cols, i|
      #   cols << split_diff(xs, cols.last, i)

      #   raise StopIteration if cols.last.one?

      #   [cols, i+1]
      # end

      split_diffs = [ys]
      (pts.length - 1).times do |i|
        split_diffs << split_diff(xs, split_diffs.last, i)
      end

      fail if not cols.last.one?

      (pts.length - 1).times.sum do |k|
        prod = split_diffs[k].first
        xs.take(k).reduce(prod) do |prod, xm|
          prod *= x - xm
        end
      end
    end
  end

  module Test
    module_function  # all below

    # see also: https://www.desmos.com/calculator/ngbjglouly 
    def jordan_gauss(pts = TEST_POINTS)
      coefs = Interpolation::jordan_gauss(pts)

      pts.map do |x, expected|
        actual = coefs.map.with_index do |k, i|
          k * x**i
        end.sum

        (expected - actual).to_f
      end
    end

    def by_x(pts = TEST_POINTS, &meth)
      xs, expected = pts.transpose

      pts.map do |x, expected|
        actual = meth.(pts, x)

        (expected - actual).to_f
      end
    end
  end
end
