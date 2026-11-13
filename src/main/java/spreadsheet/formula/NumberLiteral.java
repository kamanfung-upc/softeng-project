package spreadsheet.formula;

import spreadsheet.Spreadsheet;

public class NumberLiteral implements Expression {
    private final double number;

    public NumberLiteral(double number) {
        this.number = number;
    }

    @Override
    public double evaluate(Spreadsheet spreadsheet) {
        return number;
    }
}
