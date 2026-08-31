package org.example;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TableDAO {
    public List<String> getAllTableNames(Connection conn) throws SQLException {
        List<String> result = new ArrayList<>();
        try (ResultSet rs = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                result.add(rs.getString("TABLE_NAME"));
            }
        }
        return result;
    }

    public List<String> getSearchableColumns(Connection conn, String table, List<String> columns) throws SQLException {
        List<String> result = new ArrayList<>();
        try (ResultSet rs = conn.getMetaData().getColumns(null, null, table, null)) {
            while (rs.next()) {
                String col = rs.getString("COLUMN_NAME");
                String full = table + "." + col;

                if (columns.isEmpty()) {
                    result.add(col);
                } else if (columns.stream().anyMatch(c -> c.equalsIgnoreCase(full))) {
                    result.add(col);
                }
            }
        }
        return result;
    }

    public void searchInTable(Connection conn, String table, List<String> searchableCols,
                                     String pattern, String rangeInput) throws SQLException {
        String sql = "SELECT * FROM " + table;
        List<String[]> rows = new ArrayList<>();
        List<Boolean> flags = new ArrayList<>();
        boolean hasAnyMatch = false;

        Object minVal = null, maxVal = null;
        boolean rangeParsed = false;
        boolean isDateRange = false;

        if (rangeInput != null && rangeInput.matches("\\[.+:.+\\]")) {
            try {
                String trimmed = rangeInput.substring(1, rangeInput.length() - 1);
                String[] parts = trimmed.split(":", 2);
                String rawMin = parts[0].trim();
                String rawMax = parts[1].trim();

                try {
                    minVal = Integer.parseInt(rawMin);
                    maxVal = Integer.parseInt(rawMax);
                } catch (NumberFormatException e) {
                    DateTimeFormatter fmt = rawMin.contains("/") ?
                            DateTimeFormatter.ofPattern("dd.MM.yyyy") :
                            DateTimeFormatter.ISO_LOCAL_DATE;
                    minVal = LocalDate.parse(rawMin, fmt);
                    maxVal = LocalDate.parse(rawMax, fmt);
                    isDateRange = true;
                }
                rangeParsed = true;
            } catch (Exception ignored) {}
        }

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();

            while (rs.next()) {
                String[] row = new String[cols];
                boolean patternFound = false;
                boolean rowInRange = !rangeParsed;

                for (int i = 1; i <= cols; i++) {
                    String val = rs.getString(i);
                    String col = meta.getColumnLabel(i);
                    int colType = meta.getColumnType(i);

                    boolean isSearchable = searchableCols.stream().anyMatch(c -> c.equalsIgnoreCase(col));
                    if (val != null && isSearchable && val.toLowerCase().contains(pattern.toLowerCase())) {
                        patternFound = true;
                    }

                    if (rangeParsed && val != null) {
                        try {
                            if (!isDateRange && (colType == Types.INTEGER || colType == Types.BIGINT
                                    || colType == Types.SMALLINT || colType == Types.TINYINT)) {
                                int intVal = rs.getInt(i);
                                if (intVal >= (int) minVal && intVal <= (int) maxVal) {
                                    rowInRange = true;
                                }
                            } else if (isDateRange && (colType == Types.DATE || colType == Types.TIMESTAMP)) {
                                Date dateVal = rs.getDate(i);
                                if (dateVal != null) {
                                    LocalDate localVal = dateVal.toLocalDate();
                                    if (!localVal.isBefore((LocalDate) minVal) && !localVal.isAfter((LocalDate) maxVal)) {
                                        rowInRange = true;
                                    }
                                }
                            }
                        } catch (Exception e) {
                        }
                    }

                    row[i - 1] = val;
                }

                boolean matched = patternFound && rowInRange;
                rows.add(row);
                flags.add(matched);
                hasAnyMatch |= matched;
            }

            if (!hasAnyMatch) return;

            System.out.println("\n=== Tabelle: " + table + " ===");
            TableUtils.printRowHeaders(meta, cols);
            TableUtils.printRows(meta, rows, flags, searchableCols, pattern, hasAnyMatch);
        }
    }

}
