package spreadsheet.formula;

import spreadsheet.Spreadsheet;
import spreadsheet.SpreadsheetException;

public class BinaryOperation implements Expression {
    private final char operator;
    private final Expression left;
    private final Expression right;

    public BinaryOperation(char operator, Expression left, Expression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public double evaluate(Spreadsheet spreadsheet) {
        double a = left.evaluate(spreadsheet);
        double b = right.evaluate(spreadsheet);
        switch (operator) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) {
                    throw new SpreadsheetException("Division by zero");
                }
                return a / b;
            default: throw new SpreadsheetException("Unknown operator: " + operator);
        }
    }
}
