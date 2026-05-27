#set page(footer: align(center)[Чита 2026], background: rect(width: 80%, height: 95%), paper: "a4")

#set text(lang: "ru")

#show raw: set text(fill: red)

// #show page: rect(width: 100%, height: 100%)[page()]

#align(center)[
МИНИСТЕРСТВО ОБРАЗОВАНИЯ И НАУКИ РОССИЙСКОЙ ФЕДЕРАЦИИ

Федеральное государственное бюджетное образовательное учреждение \
высшего профессионального образования

"Забайкальский государственный университет" \
(ФГБОУ ВО "ЗабГУ")

Факультет Компьютерных Наук и Технологий

Кафедра "Информатика, вычислительная техника и прикладная математика"
]

#align(horizon + center)[
РАСЧЁТНО-ГРАФИЧЕСКАЯ РАБОТА №`TODO` \
По дисциплине: "Электротехника" \
"`TODO`" \
Вариант `TODO`
]

#set align(bottom)

// #set 

#let field(size) = for i in range(size) [\_]

// #grid(columns: 2, align: top + center, column-gutter: 12pt, [Выполнил:])[
//   Ст. гр. ИВТ-24


//   `TODO` \
//   #field(10) #field(7)
// ]

// __ подпись __ дата

#grid(columns: 2, align: top, column-gutter: 12pt, [Выполнил:], )[
  Ст. гр. ИВТ-24

  #block(inset: (left: 12pt))[
    `TODO` \
    #field(10) #field(7)
  ]
]

#grid(columns: 2, align: top, column-gutter: 12pt, [Проверил:], )[
  Доцент кафедры энергетики, Кандидат тех. Наук

  #block(inset: (left: 12pt))[
    Дейс Д. А. \
    #field(10) #field(7)
  ]
]

// Проверил: Доцент кафедры энергетики, Кандидат тех. Наук \ Дейс Д. А.

// __ подпись __ дата
