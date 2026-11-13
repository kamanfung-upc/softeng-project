package spreadsheet;

public class FormulaContent extends Content {
    private final String text;

    public FormulaContent(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }
}
