package io.github.ivtipm;

// Test-driven development

public class Bot implements IBot {

    @Override
    public String answer(String user_input) {
        // case folding
        user_input = user_input.toLowerCase();

        if (user_input.matches("kyky.*!*")) return "Kyky Epta";
        if (user_input.equals("bb")) return "Docvidoc";
        if (user_input.equals("2 + 2")) return "5";

        return "NEPON";
    }
}
