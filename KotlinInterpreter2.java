import java.util.HashMap;
import java.util.Map;

public class KotlinInterpreter2 {

    private final Map<String, Integer> variables = new HashMap<>(); // Variable storage

    public void eval(String code) {
        String[] lines = code.split("\n"); // Split by statement terminator

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Handle variable assignment
            if (line.contains("=")) {
                handleAssignment(line);
            }
            // Handle print statements
            else if (line.startsWith("println")) {
                handlePrint(line);
            }
        }
    }

    private void handleAssignment(String line) {
        // Remove the 'val' keyword and split by '='
        String[] parts = line.substring(4).split("="); // Remove "val " from the start
        String varName = parts[0].trim();
        String expression = parts[1].trim();

        // Evaluate the expression and store the result in the variable map
        int value = evaluateExpression(expression);
        variables.put(varName, value);
    }

    private int evaluateExpression(String expression) {
        // First, replace variables in the expression with their values
        expression = replaceVariables(expression);

        // Remove extra spaces from the expression
        expression = expression.replaceAll("\\s+", "");

        // Handle parentheses by evaluating inner expressions first
        while (expression.contains("(")) {
            int start = expression.lastIndexOf("(");
            int end = expression.indexOf(")", start);
            String subExpression = expression.substring(start + 1, end);
            int result = evaluateExpression(subExpression);
            expression = expression.substring(0, start) + result + expression.substring(end + 1);
        }

        // Handle multiplication, division, and modulus first
        expression = handleMultiplicationDivisionModulus(expression);

        // Handle addition and subtraction
        return handleAdditionSubtraction(expression);
    }

    private String handleMultiplicationDivisionModulus(String expression) {
        while (expression.contains("*") || expression.contains("/") || expression.contains("%")) {
            int index = findOperatorIndex(expression, "*", "/", "%");
            char operator = expression.charAt(index);

            int left = extractNumber(expression.substring(0, index));
            int right = extractNumber(expression.substring(index + 1));

            int result = 0;
            switch (operator) {
                case '*':
                    result = left * right;
                    break;
                case '/':
                    result = left / right;
                    break;
                case '%':
                    result = left % right;
                    break;
            }

            expression = expression.substring(0, index) + result + expression.substring(index + 1 + Integer.toString(right).length());
        }
        return expression;
    }

    private int handleAdditionSubtraction(String expression) {
        // Handle addition and subtraction (lower precedence)
        while (expression.contains("+") || expression.contains("-")) {
            int index = findOperatorIndex(expression, "+", "-");
            char operator = expression.charAt(index);

            int left = extractNumber(expression.substring(0, index));
            int right = extractNumber(expression.substring(index + 1));

            int result = 0;
            switch (operator) {
                case '+':
                    result = left + right;
                    break;
                case '-':
                    result = left - right;
                    break;
            }

            expression = expression.substring(0, index) + result + expression.substring(index + 1 + Integer.toString(right).length());
        }
        return extractNumber(expression); // Return the final result as integer
    }

    private int findOperatorIndex(String expression, String... operators) {
        int index = -1;
        for (String operator : operators) {
            int operatorIndex = expression.indexOf(operator);
            if (operatorIndex != -1 && (index == -1 || operatorIndex < index)) {
                index = operatorIndex;
            }
        }
        return index;
    }

    private String replaceVariables(String expression) {
        // Replace all variables with their values
        for (Map.Entry<String, Integer> entry : variables.entrySet()) {
            expression = expression.replace(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return expression;
    }

    private int extractNumber(String expression) {
        // If it's a variable, return its value
        if (variables.containsKey(expression)) {
            return variables.get(expression);
        }
        // Otherwise, convert it to an integer
        return Integer.parseInt(expression);
    }

    private void handlePrint(String line) {
        // Extract the expression inside the parentheses after "println"
        String expression = line.substring(line.indexOf('(') + 1, line.indexOf(')')).trim();

        // Evaluate the expression (it could be a variable or an operation)
        int value = evaluateExpression(expression);
        System.out.println(value);
    }

    public static void main(String[] args) {
        KotlinInterpreter2 interpreter = new KotlinInterpreter2();

        // Example program: Calculate and print various operations
        String program = """
                val x = 10
                val y = 20
                val sum = x + y
                val difference = x - y
                val product = x * y
                val quotient = y / x
                val remainder = y % x
                println(sum)
                println(difference)
                println(product)
                println(quotient)
                println(remainder)
        """;

        interpreter.eval(program); // Execute the program
    }
}
