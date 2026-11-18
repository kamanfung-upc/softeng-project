package spreadsheet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ContentTest {
    private final Spreadsheet spreadsheet = new Spreadsheet();

    @Test
    void classifiesContentFromText() {
        assertInstanceOf(NumericalContent.class, Content.of("4"));
        assertInstanceOf(NumericalContent.class, Content.of("1.5"));
        assertInstanceOf(TextContent.class, Content.of("TOTAL"));
        assertInstanceOf(TextContent.class, Content.of("4d"));
        assertInstanceOf(TextContent.class, Content.of("Infinity"));
        assertInstanceOf(FormulaContent.class, Content.of("=A1+B1"));
    }

    @Test
    void keepsTheTextAsWritten() {
        assertEquals("4", Content.of("4").getText());
        assertEquals("1.5", Content.of("1.5").getText());
        assertEquals("-0.25", Content.of("-0.25").getText());
        assertEquals("100000000000000000000", Content.of("100000000000000000000").getText());
        assertEquals("TOTAL", Content.of("TOTAL").getText());
        assertEquals("=A1+B1", Content.of("=A1+B1").getText());
    }

    @Test
    void textGivesANumberOnlyWhenItIsOne() {
        assertEquals(0, new TextContent("").getNumber(spreadsheet));
        assertEquals(1.2, new TextContent("1.2").getNumber(spreadsheet));
        assertThrows(SpreadsheetException.class, () -> new TextContent("TOTAL").getNumber(spreadsheet));
    }

    @Test
    void formulaHasAValueOrAnErrorNeverBoth() {
        FormulaContent formula = new FormulaContent("=1/0");
        formula.setValue(3);
        assertEquals(3, formula.getValue());
        formula.setError("Division by zero");
        assertEquals("Division by zero", formula.getError());
        assertNull(formula.getValue());
        formula.setValue(1);
        assertNull(formula.getError());
    }

    @Test
    void formulaWithoutExpressionCannotGiveANumber() {
        assertThrows(SpreadsheetException.class, () -> new FormulaContent("=1+2").getNumber(spreadsheet));
    }
}
