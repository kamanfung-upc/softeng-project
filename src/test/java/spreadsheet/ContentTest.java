package spreadsheet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ContentTest {
    private final Spreadsheet spreadsheet = new Spreadsheet();

    @Test
    void classifiesContentFromText() {
        assertInstanceOf(NumericalContent.class, Content.of("4"));
        assertInstanceOf(NumericalContent.class, Content.of("1.5"));
        assertInstanceOf(TextContent.class, Content.of("TOTAL"));
        assertInstanceOf(FormulaContent.class, Content.of("=A1+B1"));
    }

    @Test
    void keepsTheTextAsWritten() {
        assertEquals("4", Content.of("4").getText());
        assertEquals("1.5", Content.of("1.5").getText());
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
    void formulaWithoutExpressionCannotGiveANumber() {
        assertThrows(SpreadsheetException.class, () -> new FormulaContent("=1+2").getNumber(spreadsheet));
    }
}
