public class Main {

    public static void main(String[] args) {
        {
            // работает если используем только функционал в IBot
            IBot b = new Bot2();
            String actual = b.answer("KYKY");
            assert actual.equals("Kyky Epta");
        }

        {
            IBot b = new Bot2();
            String actual = b.answer("BB");
            assert actual.equals("Docvidoc");
        }

        {
            IBot b = new Bot2();
            String actual = b.answer("2 + 2");
            assert actual.equals("5");
        }
    }
}
