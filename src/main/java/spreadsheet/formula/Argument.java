package spreadsheet.formula;

import java.util.List;
import spreadsheet.Spreadsheet;

public interface Argument {

    /** The values this argument contributes to a function. */
    List<Double> values(Spreadsheet spreadsheet);
}
