public class Main {

    public static String fonk(Integer x) {
        return "111";
    }

    public static String buzz(Integer x) {
        return "pivo";
    }

    public static void main(String[] args) {
        // тоже указатель на функцию
        // которая принимает Integer и возвращает String
        Function<Integer, String> f = Main::fonk;
        f = Main::buzz;

        fonk(1);
        // Main::fonk.apply(1);  // ne rabotaet
        f.apply(1); // необходимо использовать метод apply; просто так не вызовется

        new Bot4().answer("KykY");
        new Bot3().answer("/time");

        {
            // работает если используем только функционал в IBot
            IBot b = new Bot4();
            String actual = b.answer("KYKY");
            assert actual.equals("Kyky Epta");
        }

        {
            IBot b = new Bot4();
            String actual = b.answer("BB");
            assert actual.equals("Docvidoc");
        }

        {
            IBot b = new Bot4();
            String actual = b.answer("2 + 2");
            assert actual.equals("5");
        }

        // тип указателя на функцию (c++)
        //
        // using TFunc = double (*)(int, float);
        // double fonk(int x, float y) { return 0.4; }
        // TFunc f1 = fonk;
        // double asdf(int x, float y) { return 0.4; }
        // TFunc f2 = asdf;
        //
        // fonk(1, 2.9);
        // f1(1, 2.9);
    }
}
