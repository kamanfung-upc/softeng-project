package spreadsheet.formula;

import java.util.List;
import spreadsheet.Spreadsheet;

public interface Expression extends Argument {

    double evaluate(Spreadsheet spreadsheet);

    @Override
    default List<Double> values(Spreadsheet spreadsheet) {
        return List.of(evaluate(spreadsheet));
    }
}
