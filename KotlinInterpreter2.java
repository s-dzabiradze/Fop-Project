import java.util.HashMap;
import java.util.Map;

public class KotlinInterpreter2 {

    private Map<String, Integer> variables = new HashMap<>();

    public void eval(String code) {
        String[] lines = code.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Handle variable assignment
            if (line.startsWith("val")) {
                handleAssignment(line);
            }

            // Handle print statements
            else if (line.startsWith("println")) {
                handlePrint(line);
            }
        }
    }

    private void handleAssignment(String line) {
        // Remove 'val' and semicolon, then split at '='
        String[] parts = line.replace("val", "").replace(";", "").split("=");
        if (parts.length != 2) {
            System.out.println("Something went wrong.");
            return;
        }

        String varName = parts[0].trim();
        String expression = parts[1].trim();

        // Detect operator and operands
        char operator = ' ';
        if (expression.contains("+")) operator = '+';
        else if (expression.contains("-")) operator = '-';
        else if (expression.contains("*")) operator = '*';
        else if (expression.contains("/")) operator = '/';
        else if (expression.contains("%")) operator = '%';
        else {
            System.out.println("Something went wrong.");
            return;
        }

        String[] operands = expression.split("\\" + operator);
        if (operands.length != 2) {
            System.out.println("Something went wrong.");
            return;
        }

        try {
            int num1 = Integer.parseInt(operands[0].trim());
            int num2 = Integer.parseInt(operands[1].trim());
            int result = 0;

            // Perform the operation
            switch (operator) {
                case '+':
                    result = num1 + num2;
                    break;
                case '-':
                    result = num1 - num2;
                    break;
                case '*':
                    result = num1 * num2;
                    break;
                case '/':
                    if (num2 == 0) {
                        System.out.println("Division by zero is not allowed.");
                        return;
                    }
                    result = num1 / num2;
                    break;
                case '%':
                    if (num2 == 0) {
                        System.out.println("Modulo by zero is not allowed.");
                        return;
                    }
                    result = num1 % num2;
                    break;
            }

            variables.put(varName, result);
        } catch (NumberFormatException e) {
            System.out.println("Something went wrong.");
        }
    }

    private void handlePrint(String line) {
        // Remove semicolon and extract variable name inside parentheses
        String varName = line.substring(line.indexOf('(') + 1, line.indexOf(')')).trim();
        if (!variables.containsKey(varName)) {
            System.out.println("Something went wrong.");
        } else {
            System.out.println(variables.get(varName));
        }
    }

    public static void main(String[] args) {
        KotlinInterpreter2 interpreter = new KotlinInterpreter2();

        String program = """
            val sum = 10 + 20;
            println(sum);
            val difference = 50 - 30;
            println(difference);
            val product = 5 * 6;
            println(product);
            val quotient = 40 / 8;
            println(quotient);
            val remainder = 43 % 5;
            println(remainder);
        """;

        interpreter.eval(program);
    }
}


