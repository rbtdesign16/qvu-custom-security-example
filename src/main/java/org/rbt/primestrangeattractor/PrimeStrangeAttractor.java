package org.rbt.primestrangeattractor;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
            pw.println("<!DOCTYPE html>");
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

        pw.println("\tconst CANVAS_SIZE = " + CANVAS_SIZE + ";");
        pw.println("\tconst POINT_MODULUS = 500;");
        pw.println("\tconst PLOT_LINE_COLOR = '#01F9C6';");
        pw.println("\tconst SCALE_LINE_COLOR = 'crimson';");
        pw.println("\tconst AXIS_COLOR = 'crimson';");
        pw.println("\tconst AXIS_WIDTH = 40;");
        pw.println("\tconst POINT_COLOR = 'magenta';");
        pw.println("\tconst POINT_SIZE = 30;");
        pw.println("\tconst gridScale = 500000;");
        pw.println("\tconst DEFAULT_ALPHA = 0.025;");
        pw.println("\tconst SCALING_FACTOR  = Math.PI / " + SCALING_DENOMINATOR + ";");
        pw.println("\tconst centerX = CANVAS_SIZE / 2;");
        pw.println("\tconst centerY = CANVAS_SIZE / 2;");
        pw.println();
        pw.println("\tasync function doGraph() {");
        pw.println("\t\tlet c=document.getElementById('sp');");
        pw.println("\t\tlet ctx=c.getContext('2d');");
        pw.println();
        pw.println("\t\t// loop over gap list and prime list to plat display");
        pw.println("\t\t// on circle in polar coordinates");
        pw.println("\t\tfor (let primeindx = 0; primeindx < primes.length; ++primeindx) {");
        pw.println("\t\t\tdrawAttractor(ctx, primeindx);");
        pw.println("\t\t}");
        pw.println("\t\t\tdrawAxis(ctx);");
        pw.println("\t\t\tdrawScale(ctx);");
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

        pw.println("\t// draw graph scale");
        pw.println("\tasync function drawScale(ctx) {");
        pw.println("\t\tctx.beginPath();");
        pw.println("\t\tctx.globalAlpha = 1.0;");
        pw.println("\t\tctx.lineWidth = AXIS_WIDTH;");
        pw.println("\t\tctx.strokeStyle = SCALE_LINE_COLOR;");
        pw.println("\t\tctx.arc(centerX, centerY, (5000000 * SCALING_FACTOR), 0, 2 * Math.PI);");
        pw.println("\t\tctx.arc(centerX, centerY, (10000000 * SCALING_FACTOR), 0, 2 * Math.PI);");
        pw.println("\t\tctx.arc(centerX, centerY, (15000000 * SCALING_FACTOR), 0, 2 * Math.PI);");
        pw.println("\t\tctx.lineWidth = AXIS_WIDTH;");
        pw.println("\t\tctx.stroke();");
        pw.println("\t};");
        pw.println();


        pw.println("\t// draw scaled prime on circle in polar coordinates");
        pw.println("\tasync function drawAttractor(ctx, primeindx) {");
        pw.println("\t\tctx.beginPath();");
        pw.println("\t\tctx.globalAlpha = DEFAULT_ALPHA;");
        pw.println("\t\tctx.strokeStyle = PLOT_LINE_COLOR;");
        pw.println("\t\tctx.lineWidth = 1;");
        pw.println("\t\tlet theta = primes[primeindx] * SCALING_FACTOR;");
        pw.println("\t\tlet x = centerX + theta * Math.cos(theta); ");
        pw.println("\t\tlet y = centerY + theta * Math.sin(theta);");
        pw.println("\t\tctx.moveTo(centerX, centerY);");
        pw.println("\t\tctx.lineTo(x, y);");
        pw.println("\t\tctx.stroke();");
        pw.println("\t\tif ((primeindx % POINT_MODULUS) === 0) {");
        pw.println("\t\t\tctx.globalAlpha = 1.0;");
        pw.println("\t\tctx.fillStyle = POINT_COLOR;");
        pw.println("\t\t\tctx.fillRect(x - 10, y - 10, 20, 20);");
        pw.println("\t\t};");
        pw.println("\t};");
        pw.println();
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
