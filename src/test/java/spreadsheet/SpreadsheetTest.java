package spreadsheet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class SpreadsheetTest {

    @Test
    void setsAndViewsTheContentOfACell() {
        Spreadsheet spreadsheet = new Spreadsheet();
        Coordinate a1 = Coordinate.parse("A1");
        spreadsheet.setContent(a1, Content.of("=C1+C2"));
        assertEquals("=C1+C2", spreadsheet.getContent(a1).getText());

        spreadsheet.setContent(a1, Content.of("hello"));
        assertEquals("hello", spreadsheet.getContent(a1).getText());
        assertEquals(1, spreadsheet.getCells().size());
    }

    @Test
    void emptyCellHasNoContentAndIsZero() {
        Spreadsheet spreadsheet = new Spreadsheet();
        assertNull(spreadsheet.getContent(Coordinate.parse("Z9")));
        assertEquals(0, spreadsheet.getNumber(Coordinate.parse("Z9")));
    }
}
