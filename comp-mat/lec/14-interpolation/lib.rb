# Вариант 4

require_relative 'linal'
require_relative 'newton'

module Interpolation
  TEST_POINTS = [
    [-5, 4],
    [-2, -2],
    [1, 2],
    [4, -4],
    [7, 7],
    [10, -7],
  ].freeze

  module_function  # makes methods static

  def jordan_gauss(pts)
    mat = pts.map do |x, y|
      pts.each_index.map { |i| x**i }.push(y)
    end

    LinAl::jordan_gauss(mat)
  end

  def lagrange(pts, x)
    xs, ys = pts.transpose

    pts.each_index.sum do |k|
      # xs_no_k = xs[..k-1] + xs[k+1..]
      xs_no_k = xs.values_at(..k - 1, k + 1..)

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
      # xs, expected = pts.transpose

      pts.map do |x, expected|
        actual = meth.(pts, x)

        (expected - actual).to_f
      end
    end
  end
end
