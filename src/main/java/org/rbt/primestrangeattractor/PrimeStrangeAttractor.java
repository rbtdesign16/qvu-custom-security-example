package org.rbt.primestrangeattractor;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class PrimeStrangeAttractor {
    private static final int CANVAS_SIZE = 23000;
    private static final int CANVAS_ZOOM = 3;
    private static final int PRIMES_PER_LINE = 1000;
    private static final int MAX_PRIME_COUNT = 1000000;
    private static final Integer SCALING_DENOMINATOR = 4000;
    private static final String CANVAS_BACK_COLOR = "black";
    private static final String OUTPUT_FILE = "/Users/rbtuc/Desktop/psa-graph.html";
    private static final String PRIME_FILE = "/Users/rbtuc/Desktop/primes.txt";

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

    private static void printHtml() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(OUTPUT_FILE);
            pw.println("<html>");
            printScriptTag(pw);
            pw.println("<body>");
            pw.println("\t<canvas id='sp' width='"
                    + CANVAS_SIZE
                    + "' height='"
                    + CANVAS_SIZE
                    + "' style='border:1px solid darkgray; background-color: "
                    + CANVAS_BACK_COLOR
                    + "; zoom: "
                    + CANVAS_ZOOM + "%'></canvas>");
            pw.println("\t<script type='text/javascript'>setTimeout(1000, doGraph())</script>");
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
        pw.println("\tconst primes = " + buildArray(loadPrimes(), PRIMES_PER_LINE, false) + ";");
        pw.println();

        pw.println("\tconst PLOT_LINE_COLOR = 'gold';");
        pw.println("\tconst BACKGROUND_COLOR = 'black';");
        pw.println("\tconst gridScale = 500000;");
        pw.println("\tconst DEFAULT_ALPHA = 0.05;");
        pw.println("\tconst SCALING_FACTOR  = Math.PI / " + SCALING_DENOMINATOR + ";");
        pw.println("\tconst centerX = " + (CANVAS_SIZE / 2) + ";");
        pw.println("\tconst centerY = " + (CANVAS_SIZE / 2) + ";");
        pw.println();
        pw.println("\tasync function doGraph() {");
        pw.println("\t\tlet c=document.getElementById('sp');");
        pw.println("\t\tlet ctx=c.getContext('2d');");
        pw.println("\t\tctx.strokeStyle = PLOT_LINE_COLOR;");
        pw.println("\t\tctx.globalAlpha = DEFAULT_ALPHA ;");
        pw.println("\t\tctx.lineWidth = 1;");
        pw.println();
        pw.println("\t\t// loop over gap list and prime list to plat display");
        pw.println("\t\t// on circle in polar coordinates");
        pw.println("\t\tfor (let primeindx = 0; primeindx < primes.length; ++primeindx) {");
        pw.println("\t\t\tctx.beginPath();");
        pw.println("\t\t\tctx.globalAlpha = getGlobalAlpha(primes[primeindx]);");
        pw.println("\t\t\tdrawAttractor(ctx, primeindx)");
        pw.println("\t\t\tctx.stroke();");
        pw.println("\t\t}");
        pw.println("\t\t// plotSelectedPrimes(ctx);");
        pw.println("\t}");
        pw.println();

        pw.println("\t\t// handle alpha so center is more transparent");
        pw.println("\tfunction getGlobalAlpha(val) {");
        pw.println("\t\tif (val <= 250000) {");
        pw.println("\t\t\treturn DEFAULT_ALPHA / 15000;");
        pw.println("\t\t} else if (val <= 500000) {");
        pw.println("\t\t\treturn DEFAULT_ALPHA / 10000;");
        pw.println("\t\t} else if (val <= 1000000) {");
        pw.println("\t\t\treturn DEFAULT_ALPHA / 5000;");
        pw.println("\t\t} else if (val <= 5000000) {");
        pw.println("\t\t\treturn DEFAULT_ALPHA / 1000;");
        pw.println("\t\t} else {");
        pw.println("\t\t\treturn DEFAULT_ALPHA;");
        pw.println("\t\t}");
        pw.println("\t}");
        pw.println();

        pw.println("\t// draw scaled prime on circle in polar coordinates");
        pw.println("\tasync function drawAttractor(ctx, primeindx) {");
        pw.println("\t\tlet theta = primes[primeindx] * SCALING_FACTOR;");
        pw.println("\t\tlet x = centerX + theta * Math.cos(theta); ");
        pw.println("\t\tlet y = centerY + theta * Math.sin(theta);");
        pw.println("\t\tctx.moveTo(centerX, centerY);");
        pw.println("\t\tctx.lineTo(x, y);");
        pw.println("\t};");
        pw.println();

        pw.println("\tfunction plotSelectedPrimes(ctx) {");
        pw.println("\t\tctx.globalAlpha = 1.0;");
        pw.println("\t\tctx.beginPath();");
        pw.println("\t\tctx.lineWidth = 5;");
        pw.println("\t\tctx.strokeStyle = 'red';");
        pw.println("\t\tfor (let indx = 0; indx < BOLD_INDEX.length; ++indx) {");
        pw.println("\t\t\tctx.strokeStyle = 'blue';");
        pw.println("\t\t\tlet theta = DIFF_2_PRIMES[indx][0];");
        pw.println("\t\t\tlet x = theta * Math.cos(theta);");
        pw.println("\t\t\tlet y = theta * Math.sin(theta);");
        pw.println("\t\t\tctx.arc(centerX + x, centerY + y,  10, 0, 2 * Math.PI);");
        pw.println("\t\t\tctx.stroke();");
        pw.println("\t\t}");
        pw.println("\t}");

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
