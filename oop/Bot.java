package io.github.ivtipm;

// Test-driven development

public class Bot implements IBot {
    @Override
    String answer(String user_input) {
        // case folding
        user_input = user_input.toLowerCase();

        if (user_input.matches("kyky.*!*")) return "Kyky Epta";
        if (user_input.equals("bb")) return "Docvidoc";
        if (user_input.equals("2 + 2")) return "5";

        return "NEPON";
    }
}

public class Main {
    public static void main(String[] args) {
        {
            IBot b = new Bot();
            String actual = b.answer("KYKY");
            assert actual.equals("Kyky Epta");
        }

        {
            IBot b = new Bot();
            String actual = b.answer("BB");
            assert actual.equals("Docvidoc");
        }

        {
            IBot b = new Bot();
            String actual = b.answer("2 + 2");
            assert actual.equals("5");
        }
    }
}
