def preproc(s):
    print()
    print(''.join(s.split()).replace('−', '-').replace('⋅', '*'))
    print()

preproc("""

3
0
.
9
3
6
−
𝑗
3
0
.
1
8
2
−
(
0
−
𝑗
6
4
.
9
9
7
)

""")

# -75.398j*(0.4003+0.4103j)

# 30.936-30.182j-(0-64.997j)

def fmt(c):
    print()
    print(f"complex({round(c.real, 3)}, {round(c.imag, 3)})")
    print()
