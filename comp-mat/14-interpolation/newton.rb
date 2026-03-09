module Interpolation
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
end
