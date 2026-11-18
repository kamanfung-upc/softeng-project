package spreadsheet;

import spreadsheet.formula.Expression;

public class FormulaContent extends Content {
    private final String text;
    private Expression root;
    private Double value;
    private String error;

    public FormulaContent(String text) {
        this.text = text;
    }

    public FormulaContent(String text, Expression root) {
        this.text = text;
        this.root = root;
    }

    public Expression getRoot() {
        return root;
    }

    public void setRoot(Expression root) {
        this.root = root;
    }

    /** The value computed from the formula, or null if it has none. */
    public Double getValue() {
        return value;
    }

    /** The error of the formula, or null if it has none. */
    public String getError() {
        return error;
    }

    /** A formula has a value or an error, never both. */
    public void setValue(double value) {
        this.value = value;
        this.error = null;
    }

    public void setError(String error) {
        this.error = error;
        this.value = null;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public double getNumber(Spreadsheet spreadsheet) {
        if (root == null) {
            throw new SpreadsheetException("Formula not evaluated: " + text);
        }
        return root.evaluate(spreadsheet);
    }
}
