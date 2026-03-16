package Zabgu;

public class Bot4 implements IBot {

    String questions[] = {
        "kyky.*!*",
        "bb.*!*",
        "\\d+\\+\\d+",
    };

    String[] answers = { "Kyky Epta!", "Docvidoc", "ya tupoy" };

    @Override
    public String answer(String input) {
        input = input.toLowerCase();

        for (int i = 0; i < questions.length; i++) {
            String regex = questions[i]
            if (input.matches(regex))
                return answers[i];
        }
        return "NEPON";
    }
}
