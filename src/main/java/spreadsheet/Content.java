package spreadsheet;

public abstract class Content {

    public static Content of(String text) {
        if (text.startsWith("=")) {
            return new FormulaContent(text);
        }
        try {
            return new NumericalContent(Double.parseDouble(text));
        } catch (NumberFormatException e) {
            return new TextContent(text);
        }
    }

    /** The content as the user wrote it. */
    public abstract String getText();

    /** The value of the content as a number. */
    public abstract double getNumber(Spreadsheet spreadsheet);
}
