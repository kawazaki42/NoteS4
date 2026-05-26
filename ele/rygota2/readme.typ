// #show quote.where(block: true): set block(
//   stroke: (left: 2pt + gray, rest: none),
//   inset: (left: 1em, y: 0.5em),
// )

#show quote.where(block: true): block.with(stroke: (left:2pt + gray, rest: none), inset: (y: 0.5em))

#columns(2)[
  #image("task.excalidraw.svg", height: 50%)

  #colbreak()

  // #quote(block: true)[
  //   Вариант 5
  // ]
  
  == Вариант 5

  #block[
    === сопротивления
    - $R_1 = 16 "Ом"$
    - $R_2 = 20 "Ом"$
    // - $R_3$ -- ?
  ]

  // #line(end: (100%, 0%))


  #block[
    === индуктивности
    - $L_1 = 300 "мГн" = 0.3 "Гн"$
    // - $L_2$ -- ?
    // - $L_3$ -- ?
    - $L_4 = 80 "мГн" = 0.08 "Гн"$
    - $L_5 = 600 "мГн" = 0.6 "Гн"$
    - $L_6 = 750 "мГн" = 0.75 "Гн"$
  ]

  // - $C_1 = 300 "мкФ"$
  // - $C_2$ -- ?
  // - $C_3$ -- ?
  === ёмкость конденсатора
  - $C_4 = 82 "мкФ" = 82 dot 10^(-6) "Ф"$
  // - $C_5 = 600 "мкФ"$
  // - $C_6 = 750 "мкФ"$

  // #line()


  // #let e_m = (137, none, 150, none, -90, none)

  // #block[
  //   #for i in range(6) [
  //     #let val = e_m.at(i)
  //     #let end = if val == none [ -- ?] else [$= #val$]
  //     - $E_(m #{ i + 1 })$ #end
  //   ]
  // ]

  #block[
    === амплитуды ЭДС
    - $E_(m 1) = 52.81 "В"$
    // - $E_(m 2)$ -- ?
    - $E_(m 3) = 42.43 "В"$
    // - $E_(m 4)$ -- ?
    - $E_(m 5) = 91.92 "В"$
    // - $E_(m 6)$ -- ?
  ]

  #block[
    === фазы
    - $psi_(E 1) = 137 degree$
    // - $psi_(E 2)$ -- ?
    - $psi_(E 3) = 150 degree$
    // - $psi_(E 4)$ -- ?
    - $psi_(E 5) = -90 degree$
    // - $psi_(E 6)$ -- ?
  ]

  === частота
  $f = 20 "Гц"$
]

#pagebreak()

= 1.1 Комплексы ЭДС

#let uE = $ underline(E) $
#let rE(idx) = $ "Re"(uE_#idx) $
#let iE(idx) = $ "Im"(uE_#idx) $
#let ee = $upright(e)$

$ uE = rE("") + j iE("") = E ee^(j psi_e) $

среднеквадратическое значение ЭДС (СКЗ):

$ E = E_m / sqrt(2) $

активная (действительная) составляющая ЭДС:

$ rE("") = E cos psi_e $

реактивная (мнимая) составляющая ЭДС:

$ iE("") = E sin psi_e $

#set line(end: (100%, 0%))

== Вычисления

// #line()

#let rnd(value) = calc.round(value, digits: 3)


расчет $uE_1$:

#let i = 1
#let psii = 137deg
#let em = 52.81

#let ei = em / calc.sqrt(2)
#let rei = ei * calc.cos(psii)
#let iei = ei * calc.sin(psii)

$ E_1 = E_(m 1)/sqrt(2) = 52.81/sqrt(2) = rnd(ei) $

$ #rE(1) = E_1 cos psi_(e 1) = rnd(ei) cos 137 degree = rnd(rei) $

$ #iE(1) = E_1 sin psi_(e 1) = rnd(ei) sin 137 degree = rnd(iei) $

#let ue1 = $rnd(rei) + j rnd(iei)$

комплекс $uE_#i$: $ uE_#i = ue1 = rnd(ei) ee^(j #psii.deg() degree) $

//

#let i = 3
#let em = 42.43
#let psii = 150deg

#let ei = em / calc.sqrt(2)
#let rei = ei * calc.cos(psii)
#let iei = ei * calc.sin(psii)

расчет $uE_#i$:

$ E_#i = E_(m #i)/sqrt(2) = em/sqrt(2) = rnd(ei) $

$ #rE(i) = E_#i cos psi_(e #i) = rnd(ei) cos #psii.deg() degree = rnd(rei) $

$ #iE(i) = E_#i sin psi_(e #i) = rnd(ei) sin #psii.deg() degree = rnd(iei) $

#let ue3 = $rnd(rei) + j rnd(iei)$

комплекс $uE_#i$: $ uE_#i = ue3 = rnd(ei) ee^(j #psii.deg() degree) $

//

#let i = 5
#let em = 91.92
#let psii = -90deg

#let ei = em / calc.sqrt(2)
#let rei = ei * calc.cos(psii)
#let iei = ei * calc.sin(psii)

расчет $uE_#i$:

$ E_#i = E_(m #i)/sqrt(2) = em/sqrt(2) = rnd(ei) $

$ #rE(i) = E_#i cos psi_(e #i) = rnd(ei) cos (#psii.deg()degree) = rnd(rei) $

$ #iE(i) = E_#i sin psi_(e #i) = rnd(ei) sin (#psii.deg()degree) = rnd(iei) $

#let ue5 = $rnd(rei) - j rnd(#calc.abs(iei))$

комплекс $uE_#i$: $ uE_#i = ue5 = rnd(ei) ee^(-j #calc.abs(psii.deg())degree) $

#pagebreak()

= 1.2 Комплексы сопротивлений ветвей

$ underline(Z) = R + j X = R + j(X_L - X_C) = Z ee^(j phi) $

- $R$ -- активное сопротивление ветви
- $X = X_L - X_C$ -- реактивное сопротивление
- $X_L = omega L$ -- индуктивное сопротивление
- $X_C = 1 / (omega C)$ -- емкостное сопротивление
- $omega = 2 pi f$ -- угловая частота
- $Z = sqrt(R^2 + X^2)$ -- полное сопротивление (импеданс, модуль комплекса)
- $phi = psi_u - psi_i$ -- разность фаз (аргумент комплекса)

== Вычисления

#let f = 20
#let omegaI = 2 * calc.pi * f

$ omega = 2 pi f = 2 dot #rnd(calc.pi) dot #f = rnd(omegaI) "рад"/с space (c^(-1)) $


#let phase(real, imag) = calc.atan2(real, imag)

#let i = 1

=== Ветвь #i

#let r1 = 16
#let l1 = 0.3
#let l4 = 0.08
#let c4 = 82e-6
#let c4str = $82 dot 10^(-6)$
#let xl1 = omegaI * l1
#let xl4 = omegaI * l4
#let xc4 = 1 / (omegaI * c4)
#let x1 = xl1 + xl4 - xc4
#let z1 = calc.sqrt(r1 * r1 + x1 * x1)
#let phii = phase(r1, x1).deg()

Пассивные элементы:
- $R_1 = 16 "Ом"$
- $L_1 = 0.3 "Гн"$
- $L_4 = 0.08 "Гн"$
- $C_4 = 82 dot 10^(-6) Ф$

Сопротивления:
- $R_1 = 16 "Ом"$
- $X_(L 1) = omega L_1 = rnd(omegaI) dot l1 = rnd(xl1) "Ом"$
- $X_(L 4) = omega L_4 = rnd(omegaI) dot l4 = rnd(xl4) "Ом"$
- $X_(C 4) = 1 / (omega C_4) = 1 / (rnd(omegaI) dot c4str) = rnd(xc4) "Ом"$

#let uz1 = $16 - j #rnd(calc.abs(x1))$

$
  underline(Z)_1 =
  R_1 + j(X_(L 1) + X_(L 4) - X_(C 4)) =
  // 16 + j(rnd(xl1) + rnd(xl4) - rnd(xc4)) =
  #uz1 = rnd(z1) ee^(-j rnd(#calc.abs(phii))degree)
  "Ом"
$


#let i = 2

=== Ветвь #i

#let r2 = 20
#let l5 = 0.6

#let xl5 = omegaI * l5
#let x2 = xl5
#let z2 = calc.sqrt(r2 * r2 + x2 * x2)
#let phii = phase(r2, x2).deg()

Пассивные элементы:
- $R_2 = r2 "Ом"$
- $L_5 = l5 "Гн"$

Сопротивления:
- $R_2 = r2 "Ом"$
- $X_(L 5) = omega L_5 = rnd(omegaI) dot l5 = rnd(xl5) "Ом"$

#let uz2 = $r2 + j #rnd(x2)$

$
  underline(Z)_#i =
  R_2 + j X_(L 5) = #uz2 = rnd(z2) ee^(j rnd(phii)degree)
  // 16 + j(rnd(xl1) + rnd(xl4) - rnd(xc4)) =
  "Ом"
$


#let i = 3

=== Ветвь #i

#let l6 = 0.75

#let xl6 = omegaI * l6
#let ri = 0
#let xi = xl6
#let zi = calc.sqrt(ri * ri + xi * xi)
#let phii = phase(ri, xi).deg()

Пассивные элементы:
- $L_6 = l6 "Гн"$

Сопротивления:
- $X_(L 6) = omega L_6 = rnd(omegaI) dot l6 = rnd(xl6) "Ом"$

#let uz3 = $ri + j #rnd(xi)$

$
  underline(Z)_#i =
  R_2 + j X_(L 5) =
  // 16 + j(rnd(xl1) + rnd(xl4) - rnd(xc4)) =
  #uz3 = rnd(zi) ee^(j rnd(phii)degree)
  "Ом"
$

#pagebreak()

= 2 Система уравнений для мгновенных значений

для активного сопротивления: $ u = R i $

для индуктивности: $ u = L (dif i) / (dif t) $

для ёмкости: $ u = 1/C integral i dif t $

3 ветви, 2 узла

2 уравнения по 2 закону Кирхгофа

1 уравение по 1 закону

3 уравнения с 3 неизвестными

узел $a$: $ i_1 + i_2 + i_3 = 0 $

#let eq2 = $
  1/C integral i_1 dif t +
  L_4 (dif i_1) / (dif t) +
  L_1 (dif i_1) / (dif t) +
  R_1 i_1
  - R_2 i_2
  - L_5 i_2
  = e_1 + e_5
$

левый контур: ветви 1 и 2, по часовой стрелке: #eq2


#let eq3 = $
  L_5 (dif i_2) / (dif t)
  + R_2 i_2
  - L_6 (dif i_3) / (dif t)
  = - e_5 - e_3
$

правый контур: ветви 2 и 3, по часовой стрелке: #eq3

система:
$
  cases(
    i_1 + i_2 + i_3 = 0,
    eq2,
    eq3,
  )
$

#pagebreak()

= 3 Комплексная система уравнений

#let uI = $ underline(I) $
#let uZ = $ underline(Z) $
#let uE = $ underline(E) $

для активного сопротивления: $ underline(U) = R underline(I) $

для индуктивности:
$ underline(U) = j omega L underline(I) = j X_L underline(I) $

для ёмкости:
$ underline(U) = 1/(j omega C) underline(I) = -j X_C underline(I) $

=== по законам Кирхгофа:

#let eq1 = $ uI_1 + uI_2 + uI_3 = 0 $
#let eq2 = $
  underline(Z)_1 uI_1
  - underline(Z)_2 uI_2
  = underline(E)_1 + underline(E)_5
$
#let eq3 = $
  uZ_2 uI_2 - uZ_3 uI_3 = -uE_5 - uE_3
$

узел $a$: #eq1

_левый_ контур: #eq2

_правый_ контур: #eq3


// #let ul(inner) = underline(inner)
// #show "Z": set underline()
// Z

система: $ cases(eq1, eq2, eq3) $

в матричной форме:

$ mat(augment: #(-1),
  1, 1, 1, 0;
  uZ_1, -uZ_2, 0, uE_1 + uE_5;
  0, uZ_2, - uZ_3, -uE_5 - uE_3
) $

$ mat(
  1, 1, 1;
  uZ_1, -uZ_2, 0;
  0, uZ_2, - uZ_3;
) vec(uI_1, uI_2, uI_3) = vec(0, uE_1 + uE_5, -uE_5 - uE_3) $

// #let z1 = $16 - j #rnd(calc.abs(x1))$

$ mat(
  1, 1, 1;
  uz1, -(uz2), 0;
  0, uz2, - (uz3);
) vec(uI_1, uI_2, uI_3) = vec(0, ue1 + ue5, -(ue5) - (ue3)) $

$
mat(
  1, 1, 1;
  uz1, -20 -j 75.398, 0;
  0, uz2, - j 94.248;
) vec(uI_1, uI_2, uI_3) =
// vec(0, -27.31 - j 39.597, +j 64.997 + 25.983 -j 15.001)
vec(
  0,
  -27.31 - j 39.597,
  +j 49.996 + 25.983,
)
// 49,996
$

// #math.equation()

// #eval()

#let uis = "  -0.2771 - 0.9293i
   0.4003 + 0.4103i
  -0.1232 + 0.5190i
".replace("i", "j").trim().split("\n").map(it => { eval(it, mode: "math") })

Решение:

// #let s = "times"
$
// #block[
   uI_1 = uis.at(#0) \
   uI_2 = uis.at(#1) \
   uI_3 = uis.at(#2) \
// ]
$

#pagebreak()

= 4 Метод двух узлов

#let euab = $underline(U)_(a b)$

$ underline(U)_(a b) = (sum_k plus.minus uE_k / uZ_k) / (sum_n 1/uZ_n) $

// `+` если ЭДС против U_ab

$ underline(U)_(a b) = (uE_1 / uZ_1 + uE_3 / uZ_3) / (1/uZ_1 + 1/uZ_2 + 1/uZ_3) $

$ underline(U)_(a b) = (ue1 / uz1 + ue3 / uz3) / (1/uz1 + 1/uz2 + 1/uz3) $

$ /* underline(U)_(a b) */ = ((-0.6301 - j 0.3495) + (0.1592 +j 0.2757)) / ((0.0059573 + j 0.018353) + (0.0032868 -j 0.012391) -j 0.010610) $

// #let phase = angle(-2.5202479938768034 ).deg()
#let phase = -144.39957337545337

#let uab = $-37.457 - j 26.817$

$ /* underline(U)_(a b) */ = uab = 46.067 ee^(-j rnd(#calc.abs(phase))degree) В $

== по закону Ома (для активной ветви)

$ uI_1 = (uE_1 - euab)/uZ_1 $

$ uI_2 = (-uE_5 - euab)/uZ_2 $

$ uI_3 = (uE_3 - euab)/uZ_3 $

#line()

$ uI_1 = ((ue1) - (uab))/uz1 = -0.8991 + j 0.4977 $

$ uI_2 = (-(ue5) - (uab))/uz2 $

// $ uI_2 = (-(ue5) - (uab))/uz2 $

#pagebreak()

= 5 Баланс мощностей

$ sum underline(S)_"ист" = sum underline(S)_"потр" $

$ underline(S) = underline(U I^*) $

$I^*$ -- сопряженный комплекс тока

$ sum plus.minus uE dot uI^* = sum underline(U) dot uI^* $

$
  sum underline(S)_"потр" =
  sum underline(U) dot uI^* =
  sum underline(I Z) dot uI^* =
  sum I ee^(j psi_I) uZ dot I ee^(-j psi_I) =
  sum I^2 uZ
$

// зависит от совпадения направлений тока и ЭДС

$ sum plus.minus uE dot uI^* = sum I^2 uZ $

$
  underline(S)_"ист" =
  sum plus.minus uE dot uI^* =
  uE_1 dot uI_1^*
  - uE_5 dot uI_2^*
  + uE_3 dot uI_3^*
$

$
  underline(S)_"потр" =
  sum I^2 uZ =
  I_1^2 uZ_1
  + I_2^2 uZ_2
  + I_3^2 uZ_3
$

#line()

// $
//   underline(S)_"ист" =
//   (ue1) dot (ui1)^*
//   - (ue5) dot (ui2)^*
//   + (ue3) dot (ui3)^*
// $

#let complex(real, imag) = {
  let imag = if imag < 0 [$- j #calc.abs(imag)$] else [$+ j #imag$]
  $#real #imag$
}

#let compexp(real, imag) = {
  let abs = calc.sqrt(real * real + imag * imag)
  let phase = calc.atan2(real, imag).deg()
  let phase = rnd(phase)

  let phase = if phase >= 0 [$j phase$] else [$-j #calc.abs(phase)$]

  $rnd(abs) ee^(phase degree)$
}

$
  underline(S)_"ист" = \
  // sum plus.minus uE dot uI^* =
  (ue1) dot (-0.2771 + j 0.9293) - \
  - (ue5) dot (0.4003 -j 0.4103) + \
  + (ue3) dot (-0.1232 -j 0.5190) = \
  = 21.556 + j 5.219
  = compexp(#21.556, #5.219)
$

#line()

$
  underline(S)_"потр" = \
  abs(-0.2771 -j 0.9293)^2 dot (uz1) + \
  + abs(0.4003 +j 0.4103)^2 dot (uz2) + \
  + abs(-0.1232 +j 0.5190)^2 dot (uz3) = \
  = 21.618 + j 5.238
  = compexp(#21.618, #5.238)
$

// #compexp(-1, -1)

/*
```python
i1 = -0.2771 - 0.9293j
i2 = 0.4003 + 0.4103j
i3 = -0.1232 + 0.5190j

z1 = 16 - 49.293j
z2 = 20 + 75.398j
z3 = 0 + 94.248j

e1 = -27.31 + 25.467j
e3 = -25.983 + 15.001j
e5 = -64.997j
```

```python
# S_ист

e1 * i1.conjugate() - e5 * i2.conjugate() + e3 * i3.conjugate()
abs(i1)**2 * z1 + abs(i2)**2 * z2 + abs(i3)**2 * z3
```


```python
actual = [
    i1 + i2 + i3,
    z1*i1 - z2*i2,
    z2*i2 - z3*i3,
]

expected = [
    0, e1 + e5, -e5 - e3
]

zip(lambda a, b: a - b, actual, expacted)
```
*/
