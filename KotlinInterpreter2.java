import java.util.HashMap;
import java.util.Map;

public class KotlinInterpreter {

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

        // Split expression by '+'
        String[] numbers = expression.split("\\+");
        if (numbers.length != 2) {
            System.out.println("Something went wrong.");
            return;
        }

        try {
            // Convert both numbers and add them
            int num1 = Integer.parseInt(numbers[0].trim());
            int num2 = Integer.parseInt(numbers[1].trim());
            int result = num1 + num2;
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
        KotlinInterpreter interpreter = new KotlinInterpreter();

        String program = """
            val sum = 10 + 20;
            println(sum);
        """;

        interpreter.eval(program);
    }
}

