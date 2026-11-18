package spreadsheet;

import java.math.BigDecimal;

public class NumericalContent extends Content {
    private final double number;

    public NumericalContent(double number) {
        this.number = number;
    }

    @Override
    public String getText() {
        return BigDecimal.valueOf(number).stripTrailingZeros().toPlainString();
    }

    @Override
    public double getNumber(Spreadsheet spreadsheet) {
        return number;
    }
}
