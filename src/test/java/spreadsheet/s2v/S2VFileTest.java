package spreadsheet.s2v;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import spreadsheet.Content;
import spreadsheet.Coordinate;
import spreadsheet.Spreadsheet;
import spreadsheet.SpreadsheetException;

class S2VFileTest {
    @TempDir
    Path dir;

    private static void set(Spreadsheet spreadsheet, String coordinate, String text) {
        spreadsheet.setContent(Coordinate.parse(coordinate), Content.of(text));
    }

    private static String text(Spreadsheet spreadsheet, String coordinate) {
        return spreadsheet.getContent(Coordinate.parse(coordinate)).getText();
    }

    @Test
    void savesTheExampleOfTheSpecification() throws IOException {
        Spreadsheet spreadsheet = new Spreadsheet();
        set(spreadsheet, "A1", "=C1+C2");
        set(spreadsheet, "B1", "4");
        set(spreadsheet, "C1", "1");
        set(spreadsheet, "C2", "2");
        set(spreadsheet, "B3", "TOTAL");
        set(spreadsheet, "C3", "=A1+B1");

        Path file = dir.resolve("example.s2v");
        new S2VFile(file.toString()).save(spreadsheet);

        assertEquals("=C1+C2;4;1\n;;2\n;TOTAL;=A1+B1\n", Files.readString(file));
    }

    @Test
    void loadsTheExampleOfTheSpecification() throws IOException {
        Path file = dir.resolve("example.s2v");
        Files.writeString(file, "=C1+C2;4;1\n;;2\n;TOTAL;=A1+B1\n");

        Spreadsheet spreadsheet = new S2VFile(file.toString()).load();

        assertEquals(6, spreadsheet.getCells().size());
        assertEquals("=C1+C2", text(spreadsheet, "A1"));
        assertEquals("4", text(spreadsheet, "B1"));
        assertEquals("2", text(spreadsheet, "C2"));
        assertEquals("TOTAL", text(spreadsheet, "B3"));
        assertEquals("=A1+B1", text(spreadsheet, "C3"));
        assertNull(spreadsheet.getContent(Coordinate.parse("A2")));
    }

    @Test
    void functionArgumentsUseCommaInTheFile() throws IOException {
        Spreadsheet spreadsheet = new Spreadsheet();
        set(spreadsheet, "A1", "=SUMA(A2:B5;PROMEDIO(B6:D8);C1;27)");
        Path file = dir.resolve("functions.s2v");
        S2VFile s2v = new S2VFile(file.toString());

        s2v.save(spreadsheet);
        assertEquals("=SUMA(A2:B5,PROMEDIO(B6:D8),C1,27)\n", Files.readString(file));

        assertEquals("=SUMA(A2:B5;PROMEDIO(B6:D8);C1;27)", text(s2v.load(), "A1"));
    }

    @Test
    void emptyCellsLeaveEmptyFieldsUpToTheLastContent() throws IOException {
        Spreadsheet spreadsheet = new Spreadsheet();
        set(spreadsheet, "D2", "x");
        Path file = dir.resolve("sparse.s2v");

        new S2VFile(file.toString()).save(spreadsheet);

        assertEquals("\n;;;x\n", Files.readString(file));
    }

    @Test
    void missingFileIsAnError() {
        assertThrows(SpreadsheetException.class, () -> new S2VFile(dir.resolve("missing.s2v").toString()).load());
    }
}
