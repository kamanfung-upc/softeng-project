package spreadsheet.formula;

import java.util.List;
import spreadsheet.Spreadsheet;

public interface Expression {

    double evaluate(Spreadsheet spreadsheet);

    /** The values this expression contributes as a function argument. */
    default List<Double> values(Spreadsheet spreadsheet) {
        return List.of(evaluate(spreadsheet));
    }
}
