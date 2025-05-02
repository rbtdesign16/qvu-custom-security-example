package org.rbt.primestrangeattractor;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class PrimeStrangeAttractor {
    private static final int CANVAS_SIZE = 20000;
    private static final int CANVAS_ZOOM = 3;
    private static final int PRIMES_PER_LINE = 500;
    private static final int MAX_PRIME_COUNT = 1000000;
    private static final String CANVAS_BACK_COLOR = "black";
    private static final String OUTPUT_FILE = "/Users/rbtuc/Desktop/psa-graph.html";
    private static final String PRIME_FILE = "C:\\dev\\projects\\primestrangeattractor\\src\\main\\java\\org\\rbt\\primestrangeattractor\\primes.txt";

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
                    + "' style='background-color: "
                    + CANVAS_BACK_COLOR
                    + "; zoom: "
                    + CANVAS_ZOOM + "%'></canvas>");
            pw.println("\t<script type='text/javascript'>doGraph();</script>");
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
        pw.println("\tconst AXIS_COLOR = 'crimson';");
        pw.println("\tconst AXIS_WIDTH = 18;");
        pw.println("\tconst POINT_SIZE = 6;");
        pw.println("\tconst centerX = CANVAS_SIZE / 2;");
        pw.println("\tconst centerY = CANVAS_SIZE / 2;");
        pw.println("\tconst DEFAULT_ALPHA = 0.8;");
        pw.println("\tconst DEFAULT_POINT_COLOR = 'white';");
        pw.println("\tconst MAX_PRIME_LIMIT = primes[primes.length - 1] + 100;");
        pw.println();
        pw.println("\tasync function doGraph() {");
        pw.println("\t\tlet ctx = document.getElementById('sp').getContext('2d');");
        pw.println("\t\t\tdrawAxis(ctx);");
        pw.println("\t\t\tfor (let primeindx = 1; primeindx < primes.length; ++primeindx) {");
      //  pw.println("\t\t\t\tdrawPoint(ctx, primeindx);");
        pw.println("\t\t\tdrawAttractor(ctx, primeindx);");
        pw.println("\t\t\t}");
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

        pw.println("\tfunction drawPoint(ctx, primeindx) {");
        pw.println("\t\tctx.beginPath();");
        pw.println("\t\tlet scale = (primes[primeindx] / primes[primes.length - 1]);");
        pw.println("\t\tlet theta =  scale * Math.PI * 2;");
        pw.println("\t\tlet offset =  scale * centerX;");
        pw.println("\t\tlet x = centerX + offset * Math.cos(theta); ");
        pw.println("\t\tlet y = centerY + offset * Math.sin(theta);");
        pw.println("\t\tctx.globalAlpha = DEFAULT_ALPHA;");
        pw.println("\t\tlet diff = primes[primeindx] - primes[primeindx - 1];");
        pw.println("\t\tif (Math.log(diff) / Math.log(2) === 6) {");
        pw.println("\t\t\tctx.fillStyle = 'gold';");
        pw.println("\t\t\tctx.arc(x, y, 10, 0, Math.PI * 2);");
        pw.println("\t\t\tctx.fill();");
        pw.println("\t\t} else {");
    //    pw.println("\t\t\tctx.fillStyle = DEFAULT_POINT_COLOR");
    //    pw.println("\t\t\tctx.lineWidth = 2");
   //     pw.println("\t\t\tctx.arc(x, y, POINT_SIZE, 0, Math.PI * 2);");
    //    pw.println("\t\t\tctx.fill();");
        pw.println("\t\t};");
        pw.println("\t};");
        
        pw.println("\tasync function drawAttractor(ctx, primeindx) {");
        pw.println("\t\tctx.beginPath();");
        pw.println("\t\tctx.strokeStyle = 'goldenrod';");
        pw.println("\t\tctx.globalAlpha = 0.02;");
        pw.println("\t\tctx.lineWidth = 1;");
        pw.println("\t\tlet scale = (10000.0 * Math.PI) / 16000000.0;");
        pw.println("\t\tlet theta =  primes[primeindx] * scale;");
        pw.println("\t\tlet newX = centerX + theta * Math.cos(theta); ");
        pw.println("\t\tlet newY = centerY + theta * Math.sin(theta);");
        pw.println("\t\tctx.moveTo(centerX, centerY);");
        pw.println("\t\tctx.lineTo(newX, newY);");
        pw.println("\t\tctx.stroke();");
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
