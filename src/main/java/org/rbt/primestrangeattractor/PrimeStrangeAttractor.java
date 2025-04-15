package org.rbt.primestrangeattractor;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class PrimeStrangeAttractor {
    private static final int CANVAS_SIZE = 1200;
    private static final int CANVAS_ZOOM = 50;
    private static final int PRIMES_PER_LINE = 1000;
    private static final int DIFFS_PER_LINE = 20;
    private static final int MAX_PRIME_COUNT = 1000000;
    private static final String CANVAS_BACK_COLOR = "black";
    private static final String OUTPUT_FILE = "/Users/rbtuc/Desktop/psa-graph.html";
    private static final String PRIME_FILE = "C:\\dev\\projects\\primestrangeattractor\\src\\main\\java\\org\\rbt\\primestrangeattractor\\primes.txt";
    private static final String DIFF_FILE = "C:\\dev\\projects\\primestrangeattractor\\src\\main\\java\\org\\rbt\\primestrangeattractor\\diff.csv";

    public static void main(String[] args) {
        printHtml();
    }

    private static List<Integer> loadPrimes() {
        List<Integer> retval = new ArrayList<>();
        LineNumberReader lnr = null;
        try {
            int indx = 0;
            lnr = new LineNumberReader(new FileReader(PRIME_FILE));
            String line;
            while ((line = lnr.readLine()) != null) {
                indx++;
                if (StringUtils.isNotEmpty(line)) {
                    retval.add(Integer.parseInt(line.trim()));
                }

                if (indx > MAX_PRIME_COUNT) {
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                lnr.close();
            } catch (Exception ex) {
            };
        }

        return retval;
    }

    private static List<Integer> loadDiffs() {
        List<Integer> retval = new ArrayList<>();
        LineNumberReader lnr = null;
        try {
            int indx = 0;
            lnr = new LineNumberReader(new FileReader(DIFF_FILE));
            String line;
            while ((line = lnr.readLine()) != null) {
                indx++;
                if (StringUtils.isNotEmpty(line)) {
                    int pos = line.indexOf(",");
                    retval.add(Integer.parseInt(line.substring(0, pos).trim()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                lnr.close();
            } catch (Exception ex) {
            };
        }

        return retval;
    }

    private static void printHtml() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(OUTPUT_FILE);
            pw.println("<!DOCTYPE html>");
            pw.println("<html>");
            printScriptTag(pw);
            pw.println("<body>");
            pw.println("\t<button onClick='doGraph(true)'>Prev</button><button onClick='doGraph()'>Next</button><br />");
            pw.println("\t<canvas id='sp' width='"
                    + CANVAS_SIZE
                    + "' height='"
                    + CANVAS_SIZE
                    + "' style='border:1px solid darkgray; background-color: "
                    + CANVAS_BACK_COLOR
                    + "; zoom: "
                    + CANVAS_ZOOM + "%'></canvas>");
            pw.println("\t<script type='text/javascript'></script>");
            pw.println("</body></html>");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                pw.close();
            } catch (Exception ex) {
            };
        }
    }

    private static void printScriptTag(PrintWriter pw) throws Exception {
        pw.println("<script type='text/javascript'>");
        pw.println("\tconst diffs = " + buildArray(loadDiffs(), DIFFS_PER_LINE, false) + ";");
        pw.println("\tconst primes = " + buildArray(loadPrimes(), PRIMES_PER_LINE, false) + ";");
        pw.println();

        pw.println("\tconst CANVAS_SIZE = " + CANVAS_SIZE + ";");
        pw.println("\tconst POINT_MODULUS = 0;");
        pw.println("\tconst AXIS_COLOR = 'crimson';");
        pw.println("\tconst AXIS_WIDTH = 1;");
        pw.println("\tconst POINT_SIZE = 2;");
        pw.println("\tconst centerX = CANVAS_SIZE / 2;");
        pw.println("\tconst centerY = CANVAS_SIZE / 2;");
        pw.println("\tconst DEFAULT_ALPHA = 0.08;");
        pw.println("\tconst DEFAULT_POINT_COLOR = 'cornsilk';");
        pw.println("\tconst MAX_PRIME_LIMIT = primes[primes.length - 1] + 100;");
        pw.println("\t let diffindx = 1;");
        pw.println();
        pw.println("\tasync function doGraph(prev) {");
        pw.println("\t\tlet ctx = document.getElementById('sp').getContext('2d');");
        pw.println();
        pw.println("\t\tctx.clearRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);");
        pw.println("\t\tif ((diffindx < diffs.length) && (diffindx > 0)) {;");
        pw.println("\t\t\tif (prev) { diffindx--; };");
        pw.println("\t\t\tdrawAxis(ctx);");
        pw.println("\t\t\tfor (let primeindx = 0; primeindx < primes.length; ++primeindx) {");
        pw.println("\t\t\t\tdrawPoint(ctx, diffs[diffindx], primeindx);");
        pw.println("\t\t\t}");
        pw.println("\t\t\tif (!prev) { diffindx++; };");
        pw.println("\t\t} else { alert('end of diff array');}");
        pw.println("\t}");
        pw.println();

        pw.println("\t// draw graph axis");
        pw.println("\tasync function drawAxis(ctx) {");
        pw.println("\t\tctx.beginPath();");
        pw.println("\t\tctx.globalAlpha = 1.0;");
        pw.println("\t\tctx.strokeStyle = AXIS_COLOR;");
        pw.println("\t\tctx.lineWidth = AXIS_WIDTH;");
        pw.println("\t\tctx.moveTo(0, centerY);");
        pw.println("\t\tctx.lineTo(CANVAS_SIZE, centerY);");
        pw.println("\t\tctx.moveTo(centerX, 0);");
        pw.println("\t\tctx.lineTo(centerX, CANVAS_SIZE);");
        pw.println("\t\tctx.stroke();");
        pw.println("\t};");
        pw.println();

        pw.println("\tfunction drawPoint(ctx, diff, primeindx) {");
        pw.println("\t\tif ((primes[primeindx] - primes[primeindx - 1]) === diff) {");
        pw.println("\t\t\tctx.beginPath();");
        pw.println("\t\t\tlet scale = (primes[primeindx] / primes[primes.length - 1]);");
        pw.println("\t\t\tlet theta =  scale * Math.PI * 2;");
        pw.println("\t\t\tlet offset =  scale * centerX;");
        pw.println("\t\t\tlet x = centerX + offset * Math.cos(theta); ");
        pw.println("\t\t\tlet y = centerY + offset * Math.sin(theta);");
        /*
        pw.println("\t\t\tctx.globalAlpha = DEFAULT_ALPHA;");
        pw.println("\t\t\tctx.fillStyle = DEFAULT_POINT_COLOR");
        pw.println("\t\t\tctx.arc(x, y, POINT_SIZE, 0, Math.PI * 2);");
        pw.println("\t\t\tctx.fill();");
*/
        pw.println("\t\t\tctx.beginPath();");
        pw.println("\t\t\tctx.strokeStyle = DEFAULT_POINT_COLOR;");
        pw.println("\t\t\tctx.globalAlpha = DEFAULT_ALPHA;");
        pw.println("\t\t\tctx.lineWidth = 1;");
        pw.println("\t\t\tctx.moveTo(centerX, centerY);");
        pw.println("\t\t\tctx.lineTo(x, y);");
        pw.println("\t\t\tctx.stroke();");

        pw.println("\t\t};");
        pw.println("\t};");
        pw.println("</script>");
        pw.println();

    }

    private static String buildArray(List<Integer> data, int itemsPerLine, boolean sort) {
        StringBuilder retval = new StringBuilder();
        int cnt = 1;
        String comma = "";

        if (sort) {
            Collections.sort(data);
        }
        retval.append("[\n\t\t");
        for (Integer s : data) {
            if ((cnt % itemsPerLine) == 0) {
                retval.append("\n\t\t");
            }

            retval.append(comma);
            retval.append(s);
            comma = ",";
            cnt++;
        }

        retval.append("\n\t]\n");
        return retval.toString();

    }

}
