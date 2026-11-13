package spreadsheet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CoordinateTest {

    @Test
    void parsesColumnAndRow() {
        Coordinate c = Coordinate.parse("AB12");
        assertEquals("AB", c.getColumn());
        assertEquals(12, c.getRow());
        assertEquals("AB12", c.toString());
    }

    @Test
    void rejectsInvalidCoordinates() {
        assertThrows(SpreadsheetException.class, () -> Coordinate.parse("1A"));
        assertThrows(SpreadsheetException.class, () -> Coordinate.parse("A0"));
        assertThrows(SpreadsheetException.class, () -> Coordinate.parse("A"));
    }

    @Test
    void convertsColumnNamesToIndexesAndBack() {
        assertEquals(1, Coordinate.parse("A1").getColumnIndex());
        assertEquals(26, Coordinate.parse("Z1").getColumnIndex());
        assertEquals(27, Coordinate.parse("AA1").getColumnIndex());
        assertEquals("AB", Coordinate.columnName(28));
    }

    @Test
    void equalCoordinatesAreTheSameKey() {
        assertEquals(Coordinate.parse("B3"), new Coordinate("B", 3));
        assertEquals(Coordinate.parse("B3").hashCode(), new Coordinate("B", 3).hashCode());
    }
}
