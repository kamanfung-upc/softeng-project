package spreadsheet.formula;

import java.util.ArrayList;
import java.util.List;
import spreadsheet.Coordinate;
import spreadsheet.Spreadsheet;
import spreadsheet.SpreadsheetException;

public class Range implements Expression {
    private final Coordinate upperLeft;
    private final Coordinate bottomRight;

    public Range(Coordinate upperLeft, Coordinate bottomRight) {
        this.upperLeft = upperLeft;
        this.bottomRight = bottomRight;
    }

    public List<Coordinate> getCoordinates() {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int col = upperLeft.getColumnIndex(); col <= bottomRight.getColumnIndex(); col++) {
            for (int row = upperLeft.getRow(); row <= bottomRight.getRow(); row++) {
                coordinates.add(new Coordinate(Coordinate.columnName(col), row));
            }
        }
        return coordinates;
    }

    @Override
    public double evaluate(Spreadsheet spreadsheet) {
        throw new SpreadsheetException("A range can only be a function argument: " + upperLeft + ":" + bottomRight);
    }

    @Override
    public List<Double> values(Spreadsheet spreadsheet) {
        List<Double> values = new ArrayList<>();
        for (Coordinate coordinate : getCoordinates()) {
            if (spreadsheet.getContent(coordinate) != null) {
                values.add(spreadsheet.getNumber(coordinate));
            }
        }
        return values;
    }
}
