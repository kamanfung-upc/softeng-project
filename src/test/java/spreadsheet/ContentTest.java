package spreadsheet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

class ContentTest {

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
}
