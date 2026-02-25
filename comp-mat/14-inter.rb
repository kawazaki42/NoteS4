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

class Float
  def almost_zero?()
    self.abs < 1e-100
  end
end

class Array
  def zeros? = all?(&:almost_zero?)
end

# def zeros?(row) = row.all?(&:almost_zero?)

def jordan_gauss(mat)
  nrows = mat.length
  ncols = mat[0].length
  
  diaglen = min(nrows, ncols)

  diaglen.times do |k|
    if mat[k][k].almost_zero?
      non_zero_beginning = (k+1...nrows).find do |i|
        not mat[i][k].almost_zero?
      end

      if non_zero_beginning
        mat[k], mat[i] = mat[i], mat[k]
      end
    end

    leading_item = mat[k][k]

    (k+1...nrows).each do |i|
      (k+1...ncols).each do |j|
        mat[i][j] *= mat[k][k]
        mat[i][j] -= mat[i][k] * mat[k][j]
        mat[i][j] /= mat[k][k]
      end
    end

    mat.each do |row|
      row[k] = 0
    end

    leading_row = mat[k]

    (k...ncols).each do |j|
      leading_row[j] /= leading_item
    end

    mat.delete_if &:zeros?

    fail if mat.any? do |row|
      not row[-1].almost_zero? and row[...-1].zeros?
    end
  end

  result = [nil] * ncols

  # result = [mat[ncols-1][ncols]]
  # result.push result[-1]

  # x[ncols-1] = mat[ncols-1][ncols]

  (0...n).reverse.each do |k|
    mat[k][ncols] - (k+1...ncols).sum do |j|
      mat[k][j] * result[j]
    end
  end

  result
end
