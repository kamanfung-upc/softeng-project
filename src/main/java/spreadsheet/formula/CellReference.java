package spreadsheet.formula;

import spreadsheet.Coordinate;
import spreadsheet.Spreadsheet;

public class CellReference implements Expression {
    private final Coordinate coordinate;

    public CellReference(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public double evaluate(Spreadsheet spreadsheet) {
        return spreadsheet.getNumber(coordinate);
    }
}
