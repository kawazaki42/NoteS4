# Вариант 4

# NOTE: `self` in some places refers to class currently being defined.
#       Thus, `self.some_method` means that `some_method` is 'static'.

module Interpolation
  TEST_POINTS = [
    [-5, 4],
    [-2, -2],
    [1, 2],
    [4, -4],
    [7, 7],
    [10, -7],
  ]

  # auxilary module to solve linear equation systems
  module LinAl
    TEST_EQUS = [
      [5, 2, 9, 2, 44],
      [8, 8, 2, 6, 54],
      [7, 2, 8, 9, 71],
      [3, 8, 8, 3, 55],
    ]

    refine Numeric do
      def almost_zero?()
        self.abs < 1e-10
      end
    end

    refine Array do
      def zeros? = all?(&:almost_zero?)
    end

    # needed to use `refine` above
    using self

    # solve an equation system
    def self.jordan_gauss(mat)
      nrows = mat.length
      ncols = mat[0].length

      mat.map! do |row|
        row.map! &:to_r
      end
  
      diaglen = [nrows, ncols].min

      diaglen.times do |k|
        # pp mat
        # p

        # step 1

        if mat[k][k].almost_zero?
          non_zero_beginning = (k+1...nrows).find do |i|
            not mat[i][k].almost_zero?
          end

          if non_zero_beginning
            mat[k], mat[i] = mat[i], mat[k]
          end
        end

        # step 2

        leading_item = mat[k][k]

        # (k+1...nrows).each do |i|
        (0..k-1).chain(k+1...nrows).each do |i|
          (k+1...ncols).each do |j|
            mat[i][j] *= leading_item
            mat[i][j] -= mat[i][k] * mat[k][j]
            mat[i][j] /= leading_item
          end
        end

        # step 3

        # # zero out the column below current element
        # mat.drop(k+1).each do |row|

        # # zero out the column except current element
        mat.each_with_index do |row, i|
          row[k] = 0 if i != k
        end

        # step 4

        leading_row = mat[k]

        (k...ncols).each do |j|
          leading_row[j] /= leading_item
        end

        # step 5 (not thoroughly tested)

        mat.delete_if &:zeros?

        mat.each do |row|
          if not row[-1].almost_zero? and row[...-1].zeros?
            pp mat
            fail 'no solutions possible'
          end
        end
      end

      # extcols = ncols
      # basic_cols = extcols - 1

      # result = [nil] * basic_cols

      # (0...basic_cols).reverse_each do |k|
      #   result[k] = mat[k][-1] - (k+1...basic_cols).sum do |j|
      #     mat[k][j] * result[j]
      #   end
      # end

      # result

      mat.map &:last
    end
  end

  def self.jordan_gauss(pts)
    mat = pts.map do |x, y|
      pts.each_index.map{|i| x ** i }.push(y)
    end

    LinAl::jordan_gauss(mat)
  end

  def self.lagrange(pts, x)
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
    # def self.split_diff(pts)
    def self.split_diff(xs, ys, leap_x = 0)
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

      (pts.length - 1).times.sum do |i|
        prod = split_diffs[i].first
        xs.take(k).reduce(prod) do |prod, xm|
          prod *= x - xm
        end
      end
    end
  end

  module Test
    # see also: https://www.desmos.com/calculator/ngbjglouly 
    def self.jordan_gauss(pts = TEST_POINTS)
      coefs = Interpolation::jordan_gauss(pts)

      pts.map do |x, y|
        actual = coefs.map.with_index do |k, i|
          k * x**i
        end.sum

        expected = y

        (expected - actual).to_f
      end
    end
  end
end
