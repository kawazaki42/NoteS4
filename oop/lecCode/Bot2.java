package Zabgu;

public class Bot2 implements IBot {

    HashMap<String, String> qna;

    // List<int> не работает. надо List<Integer>

    Bot2() {
        qna = new HashMap();
        qna.put("kyky", "Kyky Epta");
        qna.put("bb", "Docvidoc");
        qna.put("2+2", "5");
    }

    @Override
    public String answer(String input) {
        // return "?";
        input = input.toLowerCase();
        String answer = qna.get(input);

        if (answer == null) return "NEPON";

        return answer;
    }
}
