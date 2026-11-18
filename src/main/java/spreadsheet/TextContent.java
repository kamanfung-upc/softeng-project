package spreadsheet;

public class TextContent extends Content {
    private final String text;

    public TextContent(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public double getNumber(Spreadsheet spreadsheet) {
        if (text.isEmpty()) {
            return 0;
        }
        
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new SpreadsheetException("Text is not a number: " + text);
        }
    }
}
