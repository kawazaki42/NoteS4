package Zabgu;

import java.util.HashMap;
import java.util.function.Function;
import java.util.Random;
import java.time.LocalDateTime;

public class Bot3 implements IBot {

    public HashMap<String, Function<String, String>> commands
        = new HashMap<>();

    public static String hell(String input) {
        // return "Kyky Epta!";
        final String answers[] = {"Kyky", "Darova", "Zakolebal"};

        Random r = new Random();
        int idx = r.nextInt(0, 3);

        answers[idx];
    }

    public static String bb(String input) {
        return "Docvidoc";
    }

    public static String plus(String input) {
        return "5";
    }

    public static String getTime(String input) {
        LocalDateTime t = LocalDateTime.now();
        return t.toString();
    }

    Bot3() {
        commands.put("kyky", Bot3::hell);
        commands.put("bb", Bot3::bb);
        commands.put("2+2", Bot3::plus);
        commands.put("/time", Bot3::getTime);
    }

    @Override
    public String answer(String input) {
        // return "?";
        input = input.toLowerCase();
        Function<String, String> answerGetter = commands.get(input);
        if (answerGetter == null) return "NEPON";

        // вызываем фцию из словаря (apply необходим)
        return answerGetter.apply(input)
    }
}
