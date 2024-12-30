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
            } else if (line.startsWith("println")) {
                handlePrint(line);
            } else if (line.startsWith("if")) {
                handleIfElse(code.substring(code.indexOf(line)));
                break;
            }
        }
    }
    private void handleAssignment(String line) {
        // Remove 'val' and any extra spaces
        String[] parts = line.replace("val", "").split("=");
        if (parts.length != 2) {
            System.out.println("Invalid assignment statement");
            return;
        }
        String varName = parts[0].trim();
        String expression = parts[1].trim();

        int result = evaluateExpression(expression);

        variables.put(varName, result);
    }
    private int evaluateExpression(String expression) {
        expression = expression.trim();
        
        char operator = ' ';
        if (expression.contains("+")) operator = '+';
        else if (expression.contains("-")) operator = '-';
        else if (expression.contains("*")) operator = '*';
        else if (expression.contains("/")) operator = '/';
        else if (expression.contains("%")) operator = '%';

        // If there's no operator, it's just a value 
        if (operator == ' ') {
            return getValue(expression);
        }
        // Split the operands based on the operator
        String[] operands = expression.split("\\" + operator);
        if (operands.length != 2) {
            System.out.println("Invalid arithmetic expression.");
            return 0; // Return default value
        }
        int num1 = getValue(operands[0].trim());
        int num2 = getValue(operands[1].trim());

        // Perform the arithmetic operation
        switch (operator) {
            case '+': return num1 + num2;
            case '-': return num1 - num2;
            case '*': return num1 * num2;
            case '/':
                if (num2 == 0) {
                    System.out.println("Division by zero is not allowed.");
                    return 0; // Return default value
                }
                return num1 / num2;
            case '%':
                if (num2 == 0) {
                    System.out.println("Modulo by zero is not allowed.");
                    return 0; // Return default value
                }
                return num1 % num2;
        }

        return 0; 
    }
    private int getValue(String operand) {
        if (variables.containsKey(operand)) {
            return variables.get(operand);
        }
        try {
            return Integer.parseInt(operand);
        } catch (NumberFormatException e) {
            System.out.println("Invalid operand: " + operand);
            return 0; 
        }
    }
    private void handlePrint(String line) {
        String varName = line.substring(line.indexOf('(') + 1, line.indexOf(')')).trim();
        if (!variables.containsKey(varName)) {
            System.out.println("Undefined variable");
        } else {
            System.out.println(variables.get(varName));
        }
    }
    private void handleIfElse(String code) {
        try {
            int conditionStart = code.indexOf('(') + 1;
            int conditionEnd = code.indexOf(')');
            if (conditionStart == 0 || conditionEnd == -1) {
                throw new IllegalArgumentException("Malformed or missing condition in if statement");
            }
            String condition = code.substring(conditionStart, conditionEnd).trim();

            boolean conditionResult = evaluateCondition(condition);

            String ifBlock = extractBlock(code, code.indexOf('{'));
            String elseBlock = "";

            if (code.contains("else")) {
                int elseIndex = code.indexOf("else");
                elseBlock = extractBlock(code, code.indexOf('{', elseIndex));
            }

            if (conditionResult) {
                eval(ifBlock);
            } else if (!elseBlock.isEmpty()) {
                eval(elseBlock);
            }
        } catch (Exception e) {
            System.out.println("Error in if-else handling: " + e.getMessage());
        }
    }

    private String extractBlock(String code, int startIndex) {
        if (startIndex == -1) {
            throw new IllegalArgumentException("Missing opening brace for block");
        }

        int openBraces = 0;
        int closeIndex = -1;
        for (int i = startIndex; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c == '{') openBraces++;
            if (c == '}') openBraces--;

            if (openBraces == 0) {
                closeIndex = i;
                break;
            }
        }

        if (closeIndex == -1) {
            throw new IllegalArgumentException("Block not properly closed");
        }
        return code.substring(startIndex + 1, closeIndex).trim();
    }
    private boolean evaluateCondition(String condition) {
        condition = condition.trim();

        if (condition.equals("true")) return true;
        if (condition.equals("false")) return false;

        if (condition.contains("==")) {
            String[] parts = condition.split("==");
            return getValue(parts[0].trim()) == getValue(parts[1].trim());
        } else if (condition.contains("!=")) {
            String[] parts = condition.split("!=");
            return getValue(parts[0].trim()) != getValue(parts[1].trim());
        } else if (condition.contains("<")) {
            String[] parts = condition.split("<");
            return getValue(parts[0].trim()) < getValue(parts[1].trim());
        } else if (condition.contains(">")) {
            String[] parts = condition.split(">");
            return getValue(parts[0].trim()) > getValue(parts[1].trim());
        } else if (condition.contains("<=")) {
            String[] parts = condition.split("<=");
            return getValue(parts[0].trim()) <= getValue(parts[1].trim());
        } else if (condition.contains(">=")) {
            String[] parts = condition.split(">=");
            return getValue(parts[0].trim()) >= getValue(parts[1].trim());
        }
        throw new IllegalArgumentException("Something Went Wrong");
    }
    public static void main(String[] args) {
        KotlinInterpreter2 interpreter = new KotlinInterpreter2();
        String program = """
            val sum = 10 + 20
            val num = 5
            if (sum == 30) {
                println(sum)
            } else {
                println(num)
            }
        """;
        interpreter.eval(program);
    }
}


