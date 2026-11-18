package spreadsheet.formula;

import java.util.List;
import spreadsheet.Spreadsheet;

public interface Argument {

    List<Double> values(Spreadsheet spreadsheet);
}
