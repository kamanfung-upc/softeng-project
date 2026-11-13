package spreadsheet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Spreadsheet {
    private final Map<Coordinate, Cell> cells = new HashMap<>();

    public Cell getCell(Coordinate coordinate) {
        return cells.get(coordinate);
    }

    public Collection<Cell> getCells() {
        return cells.values();
    }

    public void setContent(Coordinate coordinate, Content content) {
        cells.computeIfAbsent(coordinate, Cell::new).setContent(content);
    }

    /** The content of a cell, or null if the cell is empty. */
    public Content getContent(Coordinate coordinate) {
        Cell cell = cells.get(coordinate);
        return cell == null ? null : cell.getContent();
    }
}
