package spreadsheet;

import spreadsheet.formula.Expression;

public class FormulaContent extends Content {
    private final String text;
    private Expression root;

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
