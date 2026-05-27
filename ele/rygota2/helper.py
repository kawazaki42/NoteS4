def preproc(s):
    print()
    print(''.join(s.split()).replace('−', '-').replace('⋅', '*'))
    print()

preproc("""



𝑗
9
7
.
0
4
6
⋅
(
−
0
.
2
7
7
1
−
𝑗
0
.
9
2
9
3
)
⋅
(
−
0
.
2
7
7
1
−
𝑗
0
.
9
2
9
3
)


""")

# -75.398j*(0.4003+0.4103j)

# 30.936-30.182j-(0-64.997j)

def fmt(c):
    print()
    print(f"complex({round(c.real, 3)}, {round(c.imag, 3)})")
    print()
