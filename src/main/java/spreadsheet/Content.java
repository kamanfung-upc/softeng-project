package spreadsheet;

public abstract class Content {

    public static Content of(String text) {
        if (text.startsWith("=")) {
            return new FormulaContent(text);
        }

        if (text.matches("-?\\d+(\\.\\d+)?")) {
            return new NumericalContent(Double.parseDouble(text));
        }
        
        return new TextContent(text);
    }

    public abstract String getText();

    public abstract double getNumber(Spreadsheet spreadsheet);
}
