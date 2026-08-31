package org.example;

import java.sql.*;
import java.util.List;

public class TableUtils {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BOLD = "\u001B[1m";

    public static void printRowHeaders(ResultSetMetaData meta, int colCount) throws SQLException {
        System.out.print(ANSI_BOLD);
        for (int i = 1; i <= colCount; i++) {
            System.out.print(meta.getColumnLabel(i) + "\t");
        }
        System.out.println(ANSI_RESET);
    }

    public static void printRows(ResultSetMetaData meta, List<String[]> rows, List<Boolean> flags,
                                  List<String> searchableCols, String pattern, boolean highlight) throws SQLException {
        for (int r = 0; r < rows.size(); r++) {
            if (!flags.get(r)) continue;

            StringBuilder sb = new StringBuilder();
            String[] row = rows.get(r);

            for (int i = 0; i < row.length; i++) {
                String val = row[i];
                String col = meta.getColumnLabel(i + 1);

                if (val == null) {
                    sb.append("NULL\t");
                } else if (highlight && searchableCols.stream().anyMatch(c -> c.equalsIgnoreCase(col))) {
                    sb.append(highlightMatches(val, pattern)).append("\t");
                } else {
                    sb.append(val).append("\t");
                }
            }
            System.out.println(sb);
        }
    }

    public static String highlightMatches(String text, String pattern) {
        if (text == null || pattern == null || pattern.isEmpty()) return text;

        String lowerText = text.toLowerCase();
        String lowerPattern = pattern.toLowerCase();
        int patternLength = pattern.length();

        StringBuilder sb = new StringBuilder();
        int pos = 0;

        while (true) {
            int idx = lowerText.indexOf(lowerPattern, pos);
            if (idx < 0) {
                sb.append(text.substring(pos));
                break;
            }

            sb.append(text, pos, idx)
                    .append(ANSI_RED)
                    .append(text, idx, idx + patternLength)
                    .append(ANSI_RESET);

            pos = idx + patternLength;
        }

        return sb.toString();
    }
}
