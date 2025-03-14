import java.util.Stack;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CompilerGPT {

    // Step 1: Lexer (Tokenizer)
    // Breaks the input into tokens (keywords, numbers, operators, parentheses, and functions)
    public static String[] tokenize(String input) {
        input = input.trim(); // Remove leading/trailing spaces
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (Character.isWhitespace(c)) {
                // If we encounter whitespace, finalize the current token
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0); // Reset the current token
                }
            } else if (isOperatorOrParenthesis(c)) {
                // If we encounter an operator or parenthesis, finalize the current token
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0); // Reset the current token
                }
                tokens.add(String.valueOf(c)); // Add the operator/parenthesis as a token
            } else if (Character.isLetter(c)) {
                // If we encounter a letter, it's part of a function name (e.g., sin, cos)
                currentToken.append(c);
            } else {
                // Otherwise, add the character to the current token
                currentToken.append(c);
            }
        }

        // Add the last token if it exists
        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }

        // Convert the list of tokens to an array
        return tokens.toArray(new String[0]);
    }

    // Helper method to check if a character is an operator or parenthesis
    private static boolean isOperatorOrParenthesis(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
    }

    // Step 2: Parser and Evaluator
    // Parses the tokens and evaluates the expression using the Shunting Yard algorithm
    public static double parseAndEvaluate(String[] tokens) {
        // Check if the first token is "print"
        if (!tokens[0].equals("print")) {
            throw new RuntimeException("Expected 'print' statement");
        }

        // Extract the expression part (everything after "print")
        String[] expressionTokens = new String[tokens.length - 1];
        System.arraycopy(tokens, 1, expressionTokens, 0, expressionTokens.length);

        // Evaluate the expression
        return evaluateExpression(expressionTokens);
    }

    // Helper method to evaluate the expression
    private static double evaluateExpression(String[] tokens) {
        Stack<Double> values = new Stack<>(); // Stack to hold numbers (now using Double)
        Stack<String> operators = new Stack<>(); // Stack to hold operators, parentheses, and functions

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];

            if (isNumber(token)) {
                // If the token is a number, push it to the values stack
                double number = Double.parseDouble(token);
                values.push(number);
            } else if (token.equals("(")) {
                // If the token is an opening parenthesis, push it to the operators stack
                operators.push(token);
            } else if (token.equals(")")) {
                // If the token is a closing parenthesis, evaluate everything inside the parentheses
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    double result = applyOperation(operators.pop(), values.pop(), values.pop());
                    values.push(result);
                }
                operators.pop(); // Pop the opening parenthesis

                // Check if there's a function to apply
                if (!operators.isEmpty() && isFunction(operators.peek())) {
                    String function = operators.pop();
                    double result = applyFunction(function, values.pop());
                    values.push(result);
                }
            } else if (isOperator(token)) {
                // If the token is an operator, evaluate higher precedence operations first
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
                    double result = applyOperation(operators.pop(), values.pop(), values.pop());
                    values.push(result);
                }
                operators.push(token); // Push the current operator to the stack
            } else if (isFunction(token)) {
                // If the token is a function, push it to the operators stack
                operators.push(token);
            } else {
                throw new RuntimeException("Unknown token: " + token);
            }
        }

        // Evaluate any remaining operations
        while (!operators.isEmpty()) {
            double result = applyOperation(operators.pop(), values.pop(), values.pop());
            values.push(result);
        }

        // The final result is the only value left on the stack
        return values.pop();
    }

    // Helper method to check if a token is a number (including decimals)
    private static boolean isNumber(String token) {
        return token.matches("\\d+(\\.\\d+)?"); // Matches integers or decimals
    }

    // Helper method to check if a token is an operator
    private static boolean isOperator(String token) {
        return token.matches("[+\\-*/]"); // Matches +, -, *, or /
    }

    // Helper method to check if a token is a function
    private static boolean isFunction(String token) {
        return token.equals("sin") || token.equals("cos");
    }

    // Helper method to determine operator precedence
    private static int precedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return 0;
        }
    }

    // Helper method to apply an operation
    private static double applyOperation(String operator, double b, double a) {
        switch (operator) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if (b == 0) {
                    throw new RuntimeException("Division by zero");
                }
                return a / b;
            default:
                throw new RuntimeException("Unsupported operator: " + operator);
        }
    }

    // Helper method to apply a function
    private static double applyFunction(String function, double value) {
        switch (function) {
            case "sin":
                return Math.sin(value);
            case "cos":
                return Math.cos(value);
            default:
                throw new RuntimeException("Unsupported function: " + function);
        }
    }

    // Step 3: Compiler Driver
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: cg <file.cg>");
            return;
        }

        // Get the file path from the command-line argument
        String filePath = args[0];
        File cgFile = new File(filePath);

        if (!cgFile.exists()) {
            System.out.println("Error: File not found - " + filePath);
            return;
        }

        try (Scanner scanner = new Scanner(cgFile)) {
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();

                // Skip empty lines
                if (input.isEmpty()) {
                    continue;
                }

                try {
                    // Tokenize the input
                    String[] tokens = tokenize(input);

                    // Parse and evaluate the tokens
                    double result = parseAndEvaluate(tokens);

                    // Print the result
                    System.out.println("Result: " + result);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not read the file - " + filePath);
        }
    }
}