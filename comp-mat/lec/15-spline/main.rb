# вариант 4

TEST1 = [
  [-5, -3, -2, 0, 4, 5, 8],
  [7, 6, 5, 10, -10, -7, 6]
].transpose

TEST2 = [
  [-1.6, 8.12544],
  [-0.7, -3.73737],
  [0.7, 16.99677],
  [0.9, 21.86919],
  [1.6, 29.35296],
  [2.6, 6.88896],
].freeze

def quadratic(pts, z0 = -1)
  as = [nil]
  bs = [z0]
  # cs = [nil]

  xs, ys = pts.transpose

  xs.map!(&:to_f)
  ys.map!(&:to_f)

  # zs = [z0]

  (1...pts.length).each do |k|
    dy = ys[k] - ys[k - 1]
    dx = xs[k] - xs[k - 1]
    as << dy / dx**2 - bs[k - 1] / dx
    bs << 2 * dy / dx - bs[k - 1]
  end

  as.shift
  bs.shift
  ys.shift

  [as, bs, ys]
end

def cubic(pts, z0 = -0.01, w0 = 0.02)
  as = [nil]
  bs = [w0 / 2]
  cs = [z0]

  xs, ys = pts.transpose

  xs.map!(&:to_f)
  ys.map!(&:to_f)

  # zs = [z0]

  (1...pts.length).each do |k|
    dy = ys[k] - ys[k - 1]
    dx = xs[k] - xs[k - 1]

    z = cs[k - 1]
    w = 2 * bs[k - 1]

    as <<   dy/dx**3 -   z/dx**2 - w/(2 * dx)
    bs << 3*dy/dx**2 - 3*z/dx    - w
    cs << 3*dy/dx    - 2*z       - w * dx / 2
  end

  as.shift
  bs.shift
  cs.shift
  ys.shift

  [as, bs, cs, ys]
end

if $PROGRAM_NAME == __FILE__
  pp quadratic TEST2
  puts "---"
  pp cubic TEST2
end
