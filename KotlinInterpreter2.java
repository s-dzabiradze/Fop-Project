import java.util.HashMap;
import java.util.Map;

public class KotlinInterpreter2 {

    private Map<String, Integer> variables = new HashMap<>();

    public void eval(String code) {
        String[] lines = code.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("val") || line.contains("=")) {
                handleAssignment(line);
            } else if (line.startsWith("println")) {
                handlePrint(line);
            } else if (line.startsWith("while")) {
                handleWhileLoop(code.substring(code.indexOf(line)));
                break;
            } else if (line.startsWith("if")) {
                handleIfElse(code.substring(code.indexOf(line)));
                break;
            }
        }
    }

    private void handleAssignment(String line) {
        boolean isDeclaration = line.startsWith("val");
        String[] parts = isDeclaration ? line.replace("val", "").split("=") : line.split("=");

        if (parts.length != 2) {
            System.out.println("Invalid assignment statement");
            return;
        }

        String varName = parts[0].trim();
        String expression = parts[1].trim();

        int result = evaluateExpression(expression);

        if (isDeclaration) {
            if (variables.containsKey(varName)) {
                System.out.println("Variable already defined: " + varName);
            } else {
                variables.put(varName, result);
            }
        } else {
            if (!variables.containsKey(varName)) {
                System.out.println("Undefined variable: " + varName);
            } else {
                variables.put(varName, result);
            }
        }
    }

    private int evaluateExpression(String expression) {
        expression = expression.trim();

        char operator = ' ';
        if (expression.contains("+")) operator = '+';
        else if (expression.contains("-")) operator = '-';
        else if (expression.contains("*")) operator = '*';
        else if (expression.contains("/")) operator = '/';
        else if (expression.contains("%")) operator = '%';

        if (operator == ' ') {
            return getValue(expression);
        }

        String[] operands = expression.split("\\" + operator);
        if (operands.length != 2) {
            System.out.println("Invalid arithmetic expression.");
            return 0;
        }
        int num1 = getValue(operands[0].trim());
        int num2 = getValue(operands[1].trim());

        switch (operator) {
            case '+': return num1 + num2;
            case '-': return num1 - num2;
            case '*': return num1 * num2;
            case '/':
                if (num2 == 0) {
                    System.out.println("Division by zero is not allowed.");
                    return 0;
                }
                return num1 / num2;
            case '%':
                if (num2 == 0) {
                    System.out.println("Modulo by zero is not allowed.");
                    return 0;
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

    private void handleWhileLoop(String code) {
        try {
            int conditionStart = code.indexOf('(') + 1;
            int conditionEnd = code.indexOf(')');
            if (conditionStart == 0 || conditionEnd == -1) {
                throw new IllegalArgumentException("Malformed or missing condition in while statement");
            }
            String condition = code.substring(conditionStart, conditionEnd).trim();

            String loopBody = extractBlock(code, code.indexOf('{'));

            while (evaluateCondition(condition)) {
                eval(loopBody);
            }
        } catch (Exception e) {
            System.out.println("Error in while loop handling: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        KotlinInterpreter2 interpreter = new KotlinInterpreter2();
        String algorithm1 = """
        val n = 10
        val sum = 0
        while(n > 0) {
        sum = sum + n
        n = n - 1
        println(sum)
        }
        """;
        String algorithm2 = """
                val n = 5
                val factorial = 1
                while(n > 1) {
                factorial = factorial * n
                n = n - 1
                println(factorial)
                }
                """;
        String algorithm3 = """
                val a = 48
                val b = 18
                val temp = 0
                while (b > 0) {
                temp = b
                b = a % b
                a = temp
                println(a)
                }
                """;
        String algorithm4 = """
                val number = 1234
                val reverse = 0
                val reminder = 0
                val multiplied = 0
                while (number > 0) {
                reminder = number % 10
                multiplied = reverse * 10
                reverse = multiplied + reminder
                number = number / 10
                println(reverse)
                }
                """;
        String algorithm5 = """
                val n = 13
                val isPrime = 1
                val i = 2
                val iSquare = 4
                while (iSquare <= n) {
                if (n % i == 0) {
                val isPrime = 0
                break
                }
                i = i + 1
                iSquare = i * i
                println(isPrime)
                }
                """;
        String algorithm6 = """
                val number = 121
                val original = number
                val reverse = 0
                val reminder = 0
                val multiplication = 0
                while (number > 0) {
                multiplication = reverse * 10
                remainder = number % 10
                reverse = multiplication + remainder
                number = number / 10
                println(original == reverse)
                }
                """;
        String algorithm7 = """
                val number = 3947
                val largest = 0
                val digit = 0
                while (number > 0) {
                digit = number % 10
                if (digit > largest) {
                largest = digit
                }
                number = number / 10
                }
                println(largest)
                """;
        String algorithm8 = """
                val number = 1234
                val sum = 0
                val reminder = 0
                while (number > 0) {
                reminder = number % 10
                sum = sum + reminder
                number = number / 10
                println(sum)
                }
                """;
        String algorithm9 = """
                val n = 5
                val i = 1
                val mult = 0
                while (i < 11) {
                mult = n * i
                println(mult)
                i = i + 1
                }
                """;
        String algorithm10 = """
                val n = 10
                val a = 1
                val b = 1
                val count = 2
                val temp = 0
                val tempn = 0
                while (count < tempn) {
                temp = b
                b = a + b
                println(b)
                a = temp
                count = count + 1
                tempn = n + 1
                }
                """;
        interpreter.eval(algorithm1);
    }
}
