package spreadsheet.formula;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import spreadsheet.Content;
import spreadsheet.Coordinate;
import spreadsheet.FormulaContent;
import spreadsheet.Spreadsheet;
import spreadsheet.SpreadsheetException;

class ExpressionTest {
    private final Spreadsheet spreadsheet = new Spreadsheet();

    private static Expression number(double n) {
        return new NumberLiteral(n);
    }

    private static Expression cell(String coordinate) {
        return new CellReference(Coordinate.parse(coordinate));
    }

    private void set(String coordinate, String text) {
        spreadsheet.setContent(Coordinate.parse(coordinate), Content.of(text));
    }

    @Test
    void onePlusTwo() {
        Expression e = new BinaryOperation('+', number(1), number(2));
        assertEquals(3, e.evaluate(spreadsheet));
    }

    @Test
    void threeTimesFivePlusFour() {
        Expression e = new BinaryOperation('+', new BinaryOperation('*', number(3), number(5)), number(4));
        assertEquals(19, e.evaluate(spreadsheet));
    }

    @Test
    void parenthesesChangeTheTree() {
        Expression e = new BinaryOperation('*', number(3), new BinaryOperation('+', number(5), number(4)));
        assertEquals(27, e.evaluate(spreadsheet));
    }

    @Test
    void subtractionAndDivision() {
        Expression e = new BinaryOperation('/', new BinaryOperation('-', number(10), number(4)), number(4));
        assertEquals(1.5, e.evaluate(spreadsheet));
    }

    @Test
    void divisionByZeroIsAnError() {
        Expression e = new BinaryOperation('/', number(1), number(0));
        assertThrows(SpreadsheetException.class, () -> e.evaluate(spreadsheet));
    }

    @Test
    void cellReferencesUseTheValueOfTheCell() {
        set("A1", "4");
        set("B1", "2.5");
        Expression e = new BinaryOperation('*', cell("A1"), cell("B1"));
        assertEquals(10, e.evaluate(spreadsheet));
    }

    @Test
    void formulaCellIsEvaluatedThroughItsRoot() {
        set("C1", "1");
        set("C2", "2");
        set("B1", "4");
        spreadsheet.setContent(Coordinate.parse("A1"),
                new FormulaContent("=C1+C2", new BinaryOperation('+', cell("C1"), cell("C2"))));
        Expression c3 = new BinaryOperation('+', cell("A1"), cell("B1"));
        assertEquals(3, spreadsheet.getNumber(Coordinate.parse("A1")));
        assertEquals(7, c3.evaluate(spreadsheet));
    }

    @Test
    void referenceToTextThatIsNotANumberIsAnError() {
        set("B3", "TOTAL");
        Expression e = new BinaryOperation('+', number(1), cell("B3"));
        assertThrows(SpreadsheetException.class, () -> e.evaluate(spreadsheet));
    }

    @Test
    void functionsWithNumbers() {
        List<Expression> args = List.of(number(1), number(2), number(3));
        assertEquals(6, new FunctionCall(Function.SUMA, args).evaluate(spreadsheet));
        assertEquals(1, new FunctionCall(Function.MIN, args).evaluate(spreadsheet));
        assertEquals(3, new FunctionCall(Function.MAX, args).evaluate(spreadsheet));
        assertEquals(2, new FunctionCall(Function.PROMEDIO, args).evaluate(spreadsheet));
    }

    @Test
    void sumOfARange() {
        set("A1", "1");
        set("B1", "2");
        set("A2", "3");
        set("B2", "4");
        Expression e = new FunctionCall(Function.SUMA,
                List.of(new Range(Coordinate.parse("A1"), Coordinate.parse("B2"))));
        assertEquals(10, e.evaluate(spreadsheet));
    }

    @Test
    void rangeListsItsCellsColumnByColumn() {
        Range range = new Range(Coordinate.parse("A1"), Coordinate.parse("B3"));
        assertEquals("[A1, A2, A3, B1, B2, B3]", range.getCoordinates().toString());
    }

    @Test
    void functionsCanMixNumbersCellsRangesAndFunctions() {
        set("A2", "1");
        set("B2", "5");
        set("C1", "10");
        Expression inner = new FunctionCall(Function.MAX, List.of(cell("A2"), cell("B2")));
        Expression e = new FunctionCall(Function.SUMA,
                List.of(new Range(Coordinate.parse("A2"), Coordinate.parse("B2")), inner, cell("C1"), number(27)));
        assertEquals(1 + 5 + 5 + 10 + 27, e.evaluate(spreadsheet));
    }
}
