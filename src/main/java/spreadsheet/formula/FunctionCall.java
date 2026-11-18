package spreadsheet.formula;

import java.util.ArrayList;
import java.util.List;
import spreadsheet.Spreadsheet;
import spreadsheet.SpreadsheetException;

public class FunctionCall implements Expression {
    private final Function function;
    private final List<Argument> arguments;

    public FunctionCall(Function function, List<Argument> arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    @Override
    public double evaluate(Spreadsheet spreadsheet) {
        List<Double> values = new ArrayList<>();
        for (Argument argument : arguments) {
            values.addAll(argument.values(spreadsheet));
        }
        if (values.isEmpty()) {
            throw new SpreadsheetException(function + " has no values");
        }
        return function.apply(values);
    }
}
