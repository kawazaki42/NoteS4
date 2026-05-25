#show quote.where(block: true): set block(
  stroke: (left: 2pt + gray),
  inset: (left: 1em, y: 0.5em),
)

#quote(block: true)[
  вариант 5
]

#columns(2)[
  #image("task.excalidraw.svg", height: 50%)

  #colbreak()

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

= комплекс ЭДС

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


комплекс $uE_#i$: $ uE_#i = rnd(rei) + j rnd(iei) = rnd(ei) ee^(j #psii.deg() degree) $

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

комплекс $uE_#i$: $ uE_#i = rnd(rei) + j rnd(iei) = rnd(ei) ee^(j #psii.deg() degree) $

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

комплекс $uE_#i$: $ uE_#i = rnd(rei) + j rnd(iei) = rnd(ei) ee^(-j #calc.abs(psii.deg())degree) $

#pagebreak()

= Комплекс сопротивлений ветвей

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

$
  underline(Z)_1 =
  R_1 + j(X_(L 1) + X_(L 4) - X_(C 4)) =
  // 16 + j(rnd(xl1) + rnd(xl4) - rnd(xc4)) =
  16 - j #rnd(calc.abs(x1)) = rnd(z1) ee ^(-j rnd(#calc.abs(phii))degree)
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

$
  underline(Z)_#i =
  R_2 + j X_(L 5) =
  // 16 + j(rnd(xl1) + rnd(xl4) - rnd(xc4)) =
  r2 + j #rnd(x2) = rnd(z2) ee ^(j rnd(phii)degree)
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

$
  underline(Z)_#i =
  R_2 + j X_(L 5) =
  // 16 + j(rnd(xl1) + rnd(xl4) - rnd(xc4)) =
  ri + j #rnd(xi) = rnd(zi) ee ^(j rnd(phii)degree)
  "Ом"
$
