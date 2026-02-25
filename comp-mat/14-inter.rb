# Вариант 4

xs = [-5, -2, 1, 4, 7, 10]
ys = [4, -2, 2, -4, 7, -7]

fail if xs.length != ys.length

# amount of points
n = xs.length

def lagrange(x)
  (0...n).sum do |k|
    xs_no_k = xs[..k-1] + xs[k+1..]

    num = xs_no_k.map do |xm|
      x - xm
    end.reduce :*

    den = xs_no_k.map do |xm|
      xs[k] - xm
    end.reduce :*

    ys[k] * Rational(num, den)
  end
end

class Numeric
  def almost_zero?()
    self.abs < 1e-10
  end
end

class Array
  def zeros? = all?(&:almost_zero?)
end

def jordan_gauss(mat)
  nrows = mat.length
  ncols = mat[0].length

  mat.map! do |row|
    row.map! &:to_f
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

    (k+1...nrows).each do |i|
      (k+1...ncols).each do |j|
        mat[i][j] *= leading_item
        mat[i][j] -= mat[i][k] * mat[k][j]
        mat[i][j] /= leading_item
      end
    end

    # step 3
    # zero out the column below current element

    mat.drop(k+1).each do |row|
      row[k] = 0
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

  extcols = ncols
  basic_cols = extcols - 1

  result = [nil] * basic_cols

  (0...basic_cols).reverse_each do |k|
    result[k] = mat[k][-1] - (k+1...basic_cols).sum do |j|
      mat[k][j] * result[j]
    end
  end

  result
end

TEST_DATA = [
  [5, 2, 9, 2, 44],
  [8, 8, 2, 6, 54],
  [7, 2, 8, 9, 71],
  [3, 8, 8, 3, 55],
]
