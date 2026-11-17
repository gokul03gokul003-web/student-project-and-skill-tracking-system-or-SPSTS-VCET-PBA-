package com.vcet.tracking.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility for formatting and printing tabular console data with borders.
 * Demonstrates Unit 1: Console I/O and Unit 4: StringBuilder manipulation.
 */
public class TableFormatter {

    public static void printTable(String[] headers, List<String[]> rows) {
        if (headers == null || headers.length == 0) return;

        int numCols = headers.length;
        int[] colWidths = new int[numCols];

        for (int i = 0; i < numCols; i++) {
            colWidths[i] = headers[i].length();
        }

        if (rows != null) {
            for (String[] row : rows) {
                for (int i = 0; i < numCols && i < row.length; i++) {
                    if (row[i] != null && row[i].length() > colWidths[i]) {
                        colWidths[i] = row[i].length();
                    }
                }
            }
        }

        // Add padding
        for (int i = 0; i < numCols; i++) {
            colWidths[i] += 2;
        }

        // Print header line
        printDivider(colWidths, '+', '-');
        printRow(headers, colWidths, '|');
        printDivider(colWidths, '+', '=');

        // Print data rows
        if (rows == null || rows.isEmpty()) {
            StringBuilder emptyRow = new StringBuilder("|");
            String msg = " No records available ";
            emptyRow.append(msg);
            int totalW = 0;
            for (int w : colWidths) totalW += w + 1;
            for (int k = msg.length(); k < totalW - 1; k++) emptyRow.append(" ");
            emptyRow.append("|");
            System.out.println(emptyRow.toString());
        } else {
            for (String[] row : rows) {
                printRow(row, colWidths, '|');
            }
        }
        printDivider(colWidths, '+', '-');
    }

    private static void printRow(String[] cells, int[] widths, char border) {
        StringBuilder sb = new StringBuilder();
        sb.append(border);
        for (int i = 0; i < widths.length; i++) {
            String val = (i < cells.length && cells[i] != null) ? cells[i] : "";
            sb.append(" ").append(val);
            for (int space = val.length(); space < widths[i] - 1; space++) {
                sb.append(" ");
            }
            sb.append(border);
        }
        System.out.println(sb.toString());
    }

    private static void printDivider(int[] widths, char corner, char fill) {
        StringBuilder sb = new StringBuilder();
        sb.append(corner);
        for (int w : widths) {
            for (int i = 0; i < w; i++) {
                sb.append(fill);
            }
            sb.append(corner);
        }
        System.out.println(sb.toString());
    }
}
