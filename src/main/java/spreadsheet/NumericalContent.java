package spreadsheet;

public class NumericalContent extends Content {
    private final double number;

    public NumericalContent(double number) {
        this.number = number;
    }

    @Override
    public String getText() {
        if (number == Math.rint(number)) {
            return String.valueOf((long) number);
        }
        return String.valueOf(number);
    }
}
