
package org.rbt.primestrangeattractor;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author rbtuc
 */
public class PrimeStrangeAttractor {
    private static final int CANVAS_SIZE = 16000;
    private static final int CANVAS_ZOOM = 4;
    private static final int GAPS_PER_LINE = 50;
    private static final int PRIMES_PER_LINE = 2000;
    private static final String CANVAS_BACK_COLOR = "black";
    private static final String OUTPUT_FILE = "/Users/rbtuc/Desktop/spiral.html";
    private static final String PRIME_FILE = "/Users/rbtuc/Desktop/primes.txt";
    private static final  String GAP_FILE = "/Users/rbtuc/Desktop/diff.csv";

    
    public static void main(String[] args) {
        toSpiral();
    }

    private static List<Integer> loadPrimes() {
        List<Integer> retval = new ArrayList<>();
        LineNumberReader lnr = null;
        try {
            lnr = new LineNumberReader(new FileReader(PRIME_FILE));
            String line;
            while ((line = lnr.readLine()) != null) {
                if (StringUtils.isNotEmpty(line)) {
                    retval.add(Integer.parseInt(line.trim()));
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
    
    private static List<Integer> loadGaps() {
        List<Integer> retval = new ArrayList<>();
        LineNumberReader lnr = null;
        try {
            lnr = new LineNumberReader(new FileReader(GAP_FILE));
            String line;
            while ((line = lnr.readLine()) != null) {
                int pos = line.indexOf(",");
                retval.add(Integer.parseInt(line.substring(0, pos).trim()));
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

    
    private static void toSpiral() {
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
    
            pw.println("\tconst gaps = " + buildArray(loadGaps(), GAPS_PER_LINE, true) + ";");
            pw.println("\tconst primes = " + buildArray(loadPrimes(), PRIMES_PER_LINE, false) + ";");
            pw.println();
            
            pw.println("\tconst PLOT_LINE_COLOR = 'LightGoldenrodYellow';");
            pw.println("\tconst BACKGROUND_COLOR = 'black';");
            pw.println("\tconst gridScale = 500000;");
            pw.println("\tconst AXIS_COLOR = 'crimson';");
            pw.println("\tconst SPIRAL_COLOR = 'jade';");
            pw.println("\tconst ANGLE_LINE_COLOR = 'forestgreen';");
            pw.println("\tconst CONCENTRIC_RING_COLOR = 'CornflowerBlue';");
            pw.println("\tconst DEFAULT_ALPHA = 0.01;");
            pw.println("\tconst DEFAULT_LINE_WIDTH = 25;");
            pw.println("\tconst SCALING_FACTOR  = Math.PI / 6000;");
            pw.println("\tconst centerX = " + (CANVAS_SIZE / 2) + ";");
            pw.println("\tconst centerY = " + (CANVAS_SIZE / 2) + ";");
            pw.println("\tconst GRAPH_LINE_VALUES = [1000000, 5000000, 10000000, 15000000];");
            pw.println();
            
            pw.println("\tasync function doGraph() {");
            pw.println("\t\tlet c=document.getElementById('sp');");
            pw.println("\t\tlet cxt=c.getContext('2d');");
            pw.println("\t\tcxt.strokeStyle = PLOT_LINE_COLOR;");
            pw.println("\t\tcxt.globalAlpha = DEFAULT_ALPHA ;");
            pw.println("\t\tcxt.lineWidth = 1;");
            pw.println();
            pw.println("\t\t// loop over gap list and prime list to plat display");
            pw.println("\t\t// on circle in polar coordinates - 2 PI cover entire range");
            pw.println("\t\tfor (let gapindx = 0; gapindx < gaps.length; ++gapindx) {");
            pw.println("\t\t\tcxt.beginPath();");
            pw.println("\t\t\tcxt.moveTo(centerX, centerY);");
            pw.println("\t\t\tfor (let primeindx = 0; primeindx < primes.length; ++primeindx) {");
            pw.println("\t\t\t\tdrawAttractor(cxt, gaps[gapindx], primeindx)");
            pw.println("\t\t\t}");
            pw.println("\t\t\tcxt.stroke();");
            pw.println("\t\t}");
            
            pw.println("\t\tcxt.stroke();");
            pw.println("\t\t// concentric lines");
            pw.println("\t\tcxt.beginPath();");
            pw.println("\t\tcxt.globalAlpha = 1.0;");
            pw.println("\t\tcxt.lineWidth = DEFAULT_LINE_WIDTH;");
            pw.println("\t\tcxt.strokeStyle = CONCENTRIC_RING_COLOR;");
            pw.println("\t\tfor (let i = 0; i < GRAPH_LINE_VALUES.length; ++i) {");
            pw.println("\t\t\tcxt.arc(centerX, centerY, SCALING_FACTOR * GRAPH_LINE_VALUES[i], 0, 2 * Math.PI);");
            pw.println("\t\t}");
            pw.println("\t\tcxt.stroke();");

            pw.println();
            pw.println("// angle lines");
            pw.println("\t\tcxt.beginPath();");
            pw.println("\t\tcxt.strokeStyle = ANGLE_LINE_COLOR;");
            pw.println("\t\tfor (let i = 1; i < 12; ++i) {");
            pw.println("\t\t\tcxt.moveTo(centerX, centerY);");
            pw.println("\t\t\tlet x = centerX +  (" + CANVAS_SIZE / 2 + ") * Math.cos(i * (Math.PI/6));");
            pw.println("\t\t\tlet y = centerY + (" + CANVAS_SIZE / 2 + ")  * Math.sin(i * (Math.PI/6));");
            pw.println("\t\t\tcxt.lineTo(x, y);");
            pw.println("\t\t}");
            pw.println("\t\tcxt.stroke();");

            pw.println();
            pw.println("// diagonal lines");
            pw.println("\t\tcxt.beginPath();");
            pw.println("\t\tcxt.strokeStyle = AXIS_COLOR;");
            pw.println("\t\tcxt.moveTo(0, centerY);");
            pw.println("\t\tcxt.lineTo(2 * centerX, centerY);");
            pw.println("\t\tcxt.moveTo(centerX, 0);");
            pw.println("\t\tcxt.lineTo(centerX, 2 * centerY);");
            pw.println("\t\tcxt.stroke();");
            pw.println();
            pw.println("// axis lines");
            pw.println("\t\tcxt.beginPath();");
            pw.println("\t\tcxt.lineWidth = DEFAULT_LINE_WIDTH;");
            pw.println("\t\tcxt.moveTo(0, 0);");
            pw.println("\t\tcxt.lineTo(" + CANVAS_SIZE + "," + CANVAS_SIZE + ");");
            pw.println("\t\tcxt.moveTo(0," + CANVAS_SIZE + ");");
            pw.println("\t\tcxt.lineTo(" + CANVAS_SIZE + ",0)");
            pw.println("\t\tcxt.stroke();");
         //   printSpiralFunction(pw);

            pw.println("\t}");
 
            
            pw.println("\t// draw scaled prime magnited for specified prime gap");
            pw.println("\t// on circle in polar coordinates - 2 PI cover entire range");
            pw.println("\tasync function drawAttractor(cxt, gap, primeindx) {");
            pw.println("\t\tlet diff = primes[primeindx] - primes[primeindx - 1];");
            pw.println("\t\tif (diff == gap) {");
            pw.println("\t\t\tlet theta = primes[primeindx] * SCALING_FACTOR;");
            pw.println("\t\t\tlet x = centerX + theta * Math.cos(theta); ");
            pw.println("\t\t\tlet y = centerY + theta * Math.sin(theta);");
            pw.println("\t\t\tcxt.lineTo(x, y);");
            pw.println("\t\t\tcxt.moveTo(centerX, centerY);");
            pw.println("\t\t};");
            pw.println("\t};");
            pw.println();
 
            pw.println("\t</script>");
            pw.println();
    }
    
    private static void printSpiralFunction(PrintWriter pw) {
        pw.println("\tasync function drawSpiral(ctx) {");;
        pw.println("\t\tcxt.beginPath();");
        pw.println("\t\tcxt.lineWidth = 30;");
        pw.println("\t\tcxt.strokeStyle = SPIRAL_COLOR;");
        pw.println("\t\t\tlet theta = 0;");
        pw.println("\t\tcxt.moveTo(centerX, 0);");
        pw.println("\t\tlet increment = (primes[primes.length - 1] * SCALING_FACTOR);");
        pw.println("\t\t\tcxt.moveTo(centerX, 0);");
        pw.println("\t\tfor (let i = 0; i < 360; ++i) {");
        pw.println("\t\t\ttheta += increment;");
        pw.println("\t\t\tlet x = centerX + theta * Math.cos(theta); ");
        pw.println("\t\t\tlet y = centerY + theta * Math.sin(theta);");
        pw.println("\t\t\tcxt.lineTo(x, y);");
        pw.println("\t\t}");
        pw.println("\t\tcxt.stroke();");
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
