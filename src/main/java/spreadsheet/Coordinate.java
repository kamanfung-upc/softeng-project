package spreadsheet;

import java.util.Objects;

public class Coordinate {
    private final String column;
    private final int row;

    public Coordinate(String column, int row) {
        this.column = column;
        this.row = row;
    }

    public static Coordinate parse(String text) {
        int i = 0;
        while (i < text.length() && Character.isLetter(text.charAt(i))) {
            i++;
        }

        if (i == 0 || i == text.length()) {
            throw new SpreadsheetException("Invalid coordinate: " + text);
        }

        try {
            int row = Integer.parseInt(text.substring(i));
            
            if (row < 1) {
                throw new SpreadsheetException("Invalid coordinate: " + text);
            }

            return new Coordinate(text.substring(0, i).toUpperCase(), row);
        } catch (NumberFormatException e) {
            throw new SpreadsheetException("Invalid coordinate: " + text);
        }
    }

    public String getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getColumnIndex() {
        int index = 0;
        for (char c : column.toCharArray()) {
            index = index * 26 + (c - 'A' + 1);
        }

        return index;
    }

    public static String columnName(int index) {
        StringBuilder name = new StringBuilder();

        while (index > 0) {
            index--;
            name.insert(0, (char) ('A' + index % 26));
            index /= 26;
        }

        return name.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coordinate)) {
            return false;
        }

        Coordinate other = (Coordinate) o;
        
        return column.equals(other.column) && row == other.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }

    @Override
    public String toString() {
        return column + row;
    }
}
