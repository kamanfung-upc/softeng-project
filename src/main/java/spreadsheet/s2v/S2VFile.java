package spreadsheet.s2v;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import spreadsheet.Cell;
import spreadsheet.Content;
import spreadsheet.Coordinate;
import spreadsheet.FormulaContent;
import spreadsheet.Spreadsheet;
import spreadsheet.SpreadsheetException;

public class S2VFile {
    private final Path path;

    public S2VFile(String path) {
        this.path = Path.of(path);
    }

    public Spreadsheet load() {
        List<String> lines;

        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            throw new SpreadsheetException("Cannot read file: " + path);
        }

        Spreadsheet spreadsheet = new Spreadsheet();

        for (int row = 1; row <= lines.size(); row++) {
            String[] fields = lines.get(row - 1).split(";", -1);

            for (int col = 1; col <= fields.length; col++) {
                String field = fields[col - 1];

                if (field.isEmpty()) {
                    continue;
                }

                if (field.startsWith("=")) {
                    field = field.replace(',', ';');
                }

                spreadsheet.setContent(new Coordinate(Coordinate.columnName(col), row), Content.of(field));
            }
        }
        return spreadsheet;
    }

    public void save(Spreadsheet spreadsheet) {
        int lastRow = 0;

        for (Cell cell : spreadsheet.getCells()) {
            if (cell.getContent() != null) {
                lastRow = Math.max(lastRow, cell.getCoordinate().getRow());
            }
        }

        StringBuilder text = new StringBuilder();

        for (int row = 1; row <= lastRow; row++) {
            text.append(line(spreadsheet, row)).append('\n');
        }

        try {
            Files.writeString(path, text);
        } catch (IOException e) {
            throw new SpreadsheetException("Cannot write file: " + path);
        }
    }

    private String line(Spreadsheet spreadsheet, int row) {
        int lastCol = 0;
        for (Cell cell : spreadsheet.getCells()) {
            if (cell.getContent() != null && cell.getCoordinate().getRow() == row) {
                lastCol = Math.max(lastCol, cell.getCoordinate().getColumnIndex());
            }
        }

        List<String> fields = new ArrayList<>();
        for (int col = 1; col <= lastCol; col++) {
            Content content = spreadsheet.getContent(new Coordinate(Coordinate.columnName(col), row));
            String text = content == null ? "" : content.getText();
            if (content instanceof FormulaContent) {
                text = text.replace(';', ',');
            }
            fields.add(text);
        }
        
        return String.join(";", fields);
    }
}
