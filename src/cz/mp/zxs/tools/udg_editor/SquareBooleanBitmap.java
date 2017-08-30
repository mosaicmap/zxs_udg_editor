/*
 * SquareBooleanBitmap.java
 *
 *  created: 26.6.2017
 *  charset: UTF-8
 */

package cz.mp.zxs.tools.udg_editor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Čtvercová bitmapa znaků 8x8.
 * <p>
 * Obsahuje metody pro základní operace.
 *
 * @author Martin Pokorný
 */
public class SquareBooleanBitmap implements Cloneable {

    /** Osm bytů; celkem 8x8 bitů. */
    public static final int CHAR_SIZE = 8;
    private static final int MAX_VALUE = 255; //(int) Math.pow(2, CHAR_SIZE) - 1;
    private static final int DEFAULT_NUM_OF_CHARS = 1;
    /** 
     * Délka strany ve znacích 8x8. 
     * 1 pro 1x1, 2 pro 2x2, 3 pro 3x3, ... 
     */
    private int dimChars = DEFAULT_NUM_OF_CHARS;
    /** 
     * Počet znaků 8x8, které se editují. 
     * 1 pro 1x1, 4 pro 2x2, 9 pro 3x3, ...
     */
    private int totalNumOfChars = dimChars * dimChars;
    /**
     * Délka strany ve pixelech.
     * Pro velikos 1x1 je 8, pro 2x2 je 16, ...
     */
    private int dimPxs = CHAR_SIZE * dimChars;
    
    /**
     * Pole reprezentující bitmapu.
     * [x][y] = [sloupec][radek]
     */
    private boolean[][] bitmap;
    

    /** */
    public SquareBooleanBitmap() {
        this(DEFAULT_NUM_OF_CHARS);
    }
    
    /**
     * 
     * @param dimChars  Délka strany ve znacích 8x8. 
     *      1 pro 1x1, 2 pro 2x2, 3 pro 3x3, ...
     *      Min 1, Max 10
     * @throws IllegalArgumentException  
     */
    public SquareBooleanBitmap(int dimChars) {
        setDimensionInCharsImpl(dimChars);
        initBitmapArray();
    }

    /**
     * Kopírovací konstruktor.
     * 
     * @param orig 
     */
    public SquareBooleanBitmap(SquareBooleanBitmap orig) {
        this.setDimensionInCharsImpl(orig.dimChars);
        this.bitmap = orig.getBitmapCopyImpl();
    }
    
    /**
     * 
     */
    private void initBitmapArray() {
        bitmap = new boolean[dimPxs][dimPxs];
    }

    /**
     * 
     * @param dimChars  Délka strany ve znacích 8x8. 
     *      1 pro 1x1, 2 pro 2x2, 3 pro 3x3, ...
     *      Min 1, Max 10
     * @throws IllegalArgumentException  
     */    
    public void setDimensionInCharsAndClear(int dimChars) {
        setDimensionInCharsImpl(dimChars);
        initBitmapArray();
    }
    
    /**
     * 
     * @param dimChars  Délka strany ve znacích 8x8. 
     *      1 pro 1x1, 2 pro 2x2, 3 pro 3x3, ...
     *      Min 1, Max 10
     * @throws IllegalArgumentException  
     */
    private void setDimensionInCharsImpl(int dimChars) {
        if (dimChars <= 0 || dimChars > 10) {
            throw new IllegalArgumentException("dimChars! (0 < dimChars >= 10)");
        }
        this.dimChars = dimChars;
        this.dimPxs = CHAR_SIZE * dimChars;
        this.totalNumOfChars = dimChars * dimChars;
    }

    public int getDimensionInChars() {
        return dimChars;
    }

    public int getDimensionInPxs() {
        return dimPxs;
    }
    
    
    
    /**
     * 
     */
    public void clearAll() {
        for (int x=0; x<=dimPxs-1; x++)  {
            for (int y=0; y<=dimPxs-1; y++)  {
                this.bitmap[x][y] = false;
            }
        }        
    }
    
    /**
     * Kontroluje zda lze číst nebo zapisovat ze zadaných souřadnic.
     * 
     * @param x
     * @param y 
     * @throws  IllegalArgumentException
     */
    private void checkXY(int x, int y) {
        if (x < 0 || x >= dimPxs) {
            throw new IllegalArgumentException("x");
        }
        if (y < 0 || y >= dimPxs) {
            throw new IllegalArgumentException("y");
        }
        return;
    }
    
    /**
     * 
     * @param x
     * @param y 
     */
    public void invertBit(int x, int y) {
        checkXY(x,y);
        this.bitmap[x][y] = ! this.bitmap[x][y];
    }
    
    /**
     * 
     * @param x
     * @param y
     * @return 
     */
    public boolean getBit(int x, int y) {
        checkXY(x,y);
        return this.bitmap[x][y];
    }
       
    /**
     * 
     * @param x
     * @param y 
     */
    public void setBit(int x, int y) {
        checkXY(x,y);
        this.bitmap[x][y] = true;
    }
    
    /**
     * 
     * @param x
     * @param y 
     */
    public void clearBit(int x, int y) {
        checkXY(x,y);
        this.bitmap[x][y] = false;
    }
    
    /**
     * Nakreslí úsečku.
     * <p>
     * Algoritmus: Digital Differential Analyzer (DDA)
     * 
     * @param x1  počáteční x
     * @param y1  počáteční y
     * @param x2  koncové x
     * @param y2  koncové y
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
        checkXY(x1,y1);
        checkXY(x2,y2);
//        System.out.println("x1 = " + x1 + "  y1 = " + y1 + "   x2 = " + x2 + "  y2 = " + y2); 

        this.bitmap[x1][y1] = true;
        this.bitmap[x2][y2] = true;
//        data[x1][y1] = ! data[x1][y1];
//        data[x2][y2] = ! data[x2][y2];
//        printDataToStdOutForDebug(data, SIZE);
        
        int dx = x2-x1;
        int dy = y2-y1;
//        System.out.println("dx = " + dx + "  dy = " + dy);

        double m = 0;
        if (dx == 0) {
            m = 10000;
        }
        else if (dy == 0) {
            m = 0.0001;
        }
        else {
            m = ((double)dy)/((double)dx);
        }
//        System.out.println("m = " + m);
        
        if (m <= 1 && m >= -1) {
            int dxSgn = (int) Math.signum(dx);
//            System.out.println("dxSgn = " + dxSgn);
            int x = x1;
            double y = y1;
            while (x != x2) {
                //int y = (int) Math.round(m * Math.abs(x-x1));
//                System.out.println("x = " + x + " -> y = " + y + "  -> " + (int)Math.round(y));
                this.bitmap[x][(int)Math.round(y)] = true;
//                data[x][(int)Math.round(y)] = ! data[x][(int)Math.round(y)];
                y += m * dxSgn;
                x += dxSgn;
            }
        }
        else {
            // zaměnit roli x a y 
            // (protože např. na 3 px v x by připadalo třeba 8 px v y --> v přímce by byly mezery) 
            m = 1 / m;
            int dySgn = (int) Math.signum(dy);
//            System.out.println("dySgn = " + dySgn);
            int y = y1;
            double x = x1;
            while (y != y2) {
//                System.out.println("y = " + y + " -> x = " + x + "  -> " + (int)Math.round(x));
                this.bitmap[(int)Math.round(x)][y] = true;
//                data[(int)Math.round(x)][y] = ! data[(int)Math.round(x)][y];
                x += m * dySgn;
                y += dySgn;
            }            
        }
    }
    
    /**
     * 
     */
    public void invertAll() {
        for (int x=0; x<=dimPxs-1; x++)  {
            for (int y=0; y<=dimPxs-1; y++)  {
                this.bitmap[x][y] = !this.bitmap[x][y];
            }
        }   
    }

    /**
     * 
     */
    public void turnRight() {
        boolean[][] result = new boolean[dimPxs][dimPxs];
        for (int x=0; x<=dimPxs-1; x++)  {
            for (int y=0; y<=dimPxs-1; y++)  {
                result[x][y] = this.bitmap[y][dimPxs-x-1];
            }
        }
        this.bitmap = result;
    }
    
    /**
     * 
     */
    public void turnLeft() {
        boolean[][] result = new boolean[dimPxs][dimPxs];
        for (int x=0; x<=dimPxs-1; x++)  {
            for (int y=0; y<=dimPxs-1; y++)  {
                result[x][y] = this.bitmap[dimPxs-y-1][x];
            }
        }
        this.bitmap = result;
    }

    /**
     * 
     */
    public void mirrorHorizontal() {
        boolean[][] result = new boolean[dimPxs][dimPxs];
        for (int x=0; x<=dimPxs-1; x++)  {
            for (int y=0; y<=dimPxs-1; y++)  {
                result[x][y] = this.bitmap[x][dimPxs-y-1];
            }
        }
        this.bitmap = result;
    }
            
    /**
     * 
     */
    public void mirrorVertical() {
        boolean[][] result = new boolean[dimPxs][dimPxs];
        for (int x=0; x<=dimPxs-1; x++)  {
            for (int y=0; y<=dimPxs-1; y++)  {
                result[x][y] = this.bitmap[dimPxs-x-1][y];
            }
        }
        this.bitmap = result;
    }

    /**
     *
     */
    public void moveToLeft() {
        boolean[][] result = new boolean[dimPxs][dimPxs];
        for (int x=0; x<=dimPxs-2; x++)  {
            for (int y=0; y<=dimPxs-1; y++)  {
                result[x][y] = this.bitmap[x+1][y];
            }
        }
        this.bitmap = result;
    }

    /**
     *
     */
    public void moveToRight() {
        boolean[][] result = new boolean[dimPxs][dimPxs];
        for (int x=1; x<=dimPxs-1; x++)  {
            for (int y=0; y<=dimPxs-1; y++)  {
                result[x][y] = this.bitmap[x-1][y];
            }
        }
        this.bitmap = result;
    }
            
    /**
     *
     */
    public void moveToDown() {
        boolean[][] result = new boolean[dimPxs][dimPxs];
        for (int x=0; x<=dimPxs-1; x++)  {
            for (int y=1; y<=dimPxs-1; y++)  {
                result[x][y] = this.bitmap[x][y-1];
            }
        }
        this.bitmap = result;
    }
    
    /**
     *
     */
    public void moveToUp() {
        boolean[][] result = new boolean[dimPxs][dimPxs];
        for (int x=0; x<=dimPxs-1; x++)  {
            for (int y=0; y<=dimPxs-2; y++)  {
                result[x][y] = this.bitmap[x][y+1];
            }
        }
        this.bitmap = result;
    }    

    /**
     * Získá data bitmapy jako číselné hodnoty jednotlivých řádků znaku.
     * <p>
     * hodnoty jsou v pořadí znaků:
     * <p>
     * totalNumOfChars = 1:  (číslo v tabulce = číslo znaku)
     * <table>
     * <tr> <td>0</td> </tr>
     * </table>
     * totalNumOfChars = 4:
     * <table>
     * <tr> <td>0</td><td>1</td> </tr>
     * <tr> <td>2</td><td>3</td> </tr>
     * </table>
     * totalNumOfChars = 9:
     * <table>
     * <tr> <td>0</td><td>1</td><td>2</td> </tr>
     * <tr> <td>3</td><td>4</td><td>5</td> </tr>
     * <tr> <td>6</td><td>7</td><td>8</td> </tr>
     * </table>
     * ...
     * <p>
     * Hodnoty v každém znaku jsou brány po řádcích od shora.
     * 
     * @return
     */
    public int[] getDataLines() {
        int[] result = new int[totalNumOfChars * CHAR_SIZE];
        int resultRow=0;
        for (int charNum=0; charNum<totalNumOfChars; charNum++) {
            int xBase = dimChars - 1 - (charNum % dimChars);
            xBase *= CHAR_SIZE;
            int yBase = charNum / dimChars; 
            yBase *= CHAR_SIZE; 
            //System.out.println("charNum = " + charNum + "    xBase = " + xBase + "  yBase = " + yBase);
            
            for (int row=yBase; row<yBase+CHAR_SIZE; row++)  {
                for (int x=xBase, val=1; x<xBase+CHAR_SIZE; x++, val*=2)  {
                    result[resultRow] += this.bitmap[dimPxs-1-x][row] ? val : 0;
                }
                resultRow++;
            }
        }
        
        return result;
    }
    
    /**
     * Získá data bitmapy jako desítová čísla v texové podobě všech řádků znaku.
     * <p>
     * hodnoty jsou v pořadí znaků:
     * <p>
     * totalNumOfChars = 1:  (číslo v tabulce = číslo znaku)
     * <table>
     * <tr> <td>0</td> </tr>
     * </table>
     * totalNumOfChars = 4:
     * <table>
     * <tr> <td>0</td><td>1</td> </tr>
     * <tr> <td>2</td><td>3</td> </tr>
     * </table>
     * totalNumOfChars = 9:
     * <table>
     * <tr> <td>0</td><td>1</td><td>2</td> </tr>
     * <tr> <td>3</td><td>4</td><td>5</td> </tr>
     * <tr> <td>6</td><td>7</td><td>8</td> </tr>
     * </table>
     * ...
     * <p>
     * Hodnoty v každém znaku jsou brány po řádcích od shora.
     * 
     * @return 
     */
    public String getDataLinesAsText() {
        int[] data = this.getDataLines();

        if (data.length < 1) {
            throw new IllegalStateException("no data");
        }
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<data.length-1; i++) {
            sb.append(data[i]).append(",");
            if ((i+1) % CHAR_SIZE == 0) {
                sb.append(" ");
            }
        }
        sb.append(data[data.length-1]);
        return sb.toString();
    }
    
    /**
     * 
     * @see #parseTextData(java.lang.String) 
     */
    private static Pattern binParsePattern = Pattern.compile("(BIN\\s+(\\d+))");

    /**
     * Analyzuje text a získá hodnoty řádků znaků ve formě čísel.
     *
     * @param data
     * @return {@code null}, pokud jsou data úplně špatně,
     *      nebo pole hodnot ve formě čísel
     * @see #setDataFromText(java.lang.String)
     */
    private int[] parseTextData(String data) {
        if (data == null || data.trim().length() <= 1) {
            //throw new IllegalArgumentException("data is blank");
            return null;
        }
        String dtmp = data;
//        System.out.println("----\ndtmp (0) = " + dtmp);
        if (dtmp.indexOf(",") == -1) {
            return null;
        }
        dtmp = dtmp.replaceAll("(,,)", ",0,");
//        System.out.println("dtmp (1) = " + dtmp);
        dtmp = dtmp.replaceAll("\\n", ",");
//        System.out.println("dtmp (2) = " + dtmp);
        dtmp = dtmp.replaceAll("\\d*\\s*DATA\\s*", "");   // odstranit číslo řádky před DATA, i s DATA
//        System.out.println("dtmp (3) = " + dtmp);
        if (dtmp.replaceAll("\\d", "").length() == data.length()) {       // = není žádné číslice -> určitě chyba
            return null;
        }
        
        // "BIN 00001010" atd nahradit za desítkové hodnoty 
        if (dtmp.contains("BIN")) {
            Matcher matcher = binParsePattern.matcher(dtmp);
            while (matcher.find()) {
                String val = matcher.group(1);          // BIN 00110000
                String valOfBin = matcher.group(2);     // 00110000
                //System.out.println("val = " + val + "  valOfBin = " + valOfBin);
                try {
                    dtmp = dtmp.replaceFirst(val, 
                            String.valueOf(Integer.parseInt(valOfBin, 2)));
                } catch (java.lang.NumberFormatException ex) {                    
                    return null;    // (null = špatně data)
                }
            }
        }
        
        dtmp = dtmp.replaceAll("[^\\d,]", "");
//        System.out.println("dtmp (4) = " + dtmp);

        // protože jinak: DATA "H",8,16,68,68,68,60,4,56  -->  "H",8,16,68,68,68,60,4,56  --> ,8,16,68,68,68,60,4,56 --> 0,8,16,68,68,68,60,4,56
        while (dtmp.startsWith(",")) {
            dtmp = dtmp.substring(1);
        }
//        System.out.println("dtmp (5) = " + dtmp);
        
        String[] values = dtmp.split("[,]");
//        System.out.println("values = " + Arrays.toString(values) + "   length = " + values.length);
        if (values.length == 0) {
            return null;
        }

        int[] result = new int[values.length];
        for (int i=0; i<result.length; i++) {
            if (values[i].isEmpty()) {
                continue;
            }
            try {
                result[i] = Integer.valueOf(values[i]);
            } catch (NumberFormatException nex) {
                return null;
            }
            if(result[i] > MAX_VALUE) {
                result[i] = MAX_VALUE;
            }
        }
//        System.out.println("valuesDec = " + Arrays.toString(result) + "   length = " + result.length);

        return result;
    }

    /**
     * 
     * @param data data desítkově, oddělená čárkou; 
     *      např. {@code DATA 1,2,4,0,55,90,128,254} 
     *      nebo {@code 1,2,4,0,55,90,128,254}
     *      nebo {@code 8010 DATA 1,2,4,0,55,90,128,254}
     *      nebo {@code DATA 1,2,4,0,55,90,128,254, 24,60,126,219,255,36,90,165}
     *      ...
     * @return  {@code false}, pokud jsou data zadána úplně špatně;
     *      jinak {@code true}
     */
    public boolean setDataFromText(String data) {
        if (data == null || data.trim().length() <= 1) {
            return false;
        }

        int[] valuesDec = parseTextData(data);
        if (valuesDec == null) {
            return false;
        }

        int[] eightsome = new int[CHAR_SIZE];   // 8 hodnot pro každý znak (8x8)

        boolean ok = true;
        int charNum = 0;

        clearAll();
        
        for (int i=0; i<valuesDec.length; i++) {
            eightsome[i%CHAR_SIZE] = valuesDec[i];

            if((i+1)%CHAR_SIZE == 0 || i==valuesDec.length-1) {
//                System.out.println("new ");
//                System.out.println(Arrays.toString(eightsome));

                // hodnoty -> na bitmapu
                ok &= fillChar(charNum, eightsome);
                        
                eightsome = new int[CHAR_SIZE];
                charNum++;
                if (charNum == totalNumOfChars) {
                    break;  // nelze pokračovat, nebylo by kam kreslit
                }                 
            }
        }
        
//        printBitmapToStdOutForDebug();
        
        return ok;
    }
    
    /**
     * Z dat vyplní jeden znak 8x8 v bitmapě.
     * <p>
     * postup vyplňování čterců 8x8 px (charNum):
     * <p>
     * totalNumOfChars = 1:  (číslo v tabulce = číslo znaku)
     * <table>
     * <tr> <td>0</td> </tr>
     * </table>
     * totalNumOfChars = 4:
     * <table>
     * <tr> <td>0</td><td>1</td> </tr>
     * <tr> <td>2</td><td>3</td> </tr>
     * </table>
     * totalNumOfChars = 9:
     * <table>
     * <tr> <td>0</td><td>1</td><td>2</td> </tr>
     * <tr> <td>3</td><td>4</td><td>5</td> </tr>
     * <tr> <td>6</td><td>7</td><td>8</td> </tr>
     * </table>
     * ...
     * <p>
     * 
     * @param charNum  číslo znaku od 0 do totalNumOfChars-1
     * @param data  pole délky {@linkplain #CHAR_SIZE} s
     *      hodnotami 0 - {@linkplain #MAX_VALUE}
     * @return
     * @see #setDataFromText(java.lang.String) 
     */
    private boolean fillChar(int charNum, int[] data) {      
        if (data.length != CHAR_SIZE) {
            throw new IllegalArgumentException("data.length != CHAR_SIZE");
        }
        if (charNum < 0 || charNum >= totalNumOfChars) {
            throw new IllegalArgumentException("charNum");
        }
        
        // -- NE, není správně. -- (mám asi jinak indexování/kreslení pole, než jsem myslel ? Někde se něco otáčí ??)
        // pro dimChars = 1:
        //    0:  x=0, y=0;
        // pro dimChars = 2:
        //    0:  x=1 y=0;  1: x=0, y=0;    2: x=1, y=1;  3: x=0, y=1
        // pro dimChars = 3:
        //    0:  x=2 y=0;  1: x=1, y=0;  2: x=0, y=0;    3: x=2, y=1
        //int xBase = size - 1 - (charNum % size);
        
        // -- OK:
        // pro dimChars = 1:
        //    0:  x=0, y=0;
        // pro dimChars = 2:
        //    0:  x=0 y=0;  1: x=1, y=0;    2: x=0, y=1;  3: x=1, y=1
        int xBase = charNum % dimChars;
        xBase *= CHAR_SIZE;
        int yBase = charNum / dimChars; 
        yBase *= CHAR_SIZE;
        
        for (int i=0; i<data.length; i++) {
            int x = CHAR_SIZE-1 + xBase;
            int row = i + yBase;
            int val = data[i];
            while(val > 0) {            // (100100 = 36.. 00 100100)
                this.bitmap[x][row] = (val % 2) == 1;
                val /= 2;
                x--;
            }
            // nuly na začátku. Bez toho zůstává předchozí hodnota; ale kvůli volání crearAll už netřeba
//            while(x >= xBase) {             // 00 před 100100
//                this.bitmap[x][row] = false;
//                x--;
//            }
        }
        
        return true;
    }

    
    /**
     * 
     * @return  kopie bitmapy
     */
    public boolean[][] getBitmapCopy() {
        return getBitmapCopyImpl();
    }

    /**
     * 
     * @return  kopie bitmapy
     */
    private boolean[][] getBitmapCopyImpl() {
        boolean[][] result = new boolean[dimPxs][dimPxs];
        // NE: System.arraycopy(this.bitmap, 0, result, 0, dimPxs); // NE, je pro 1D pole !
        for(int i=0; i<dimPxs; i++) {
            System.arraycopy(this.bitmap[i], 0, result[i], 0, dimPxs);
        }
        return result;
    }
    
    /**
     * 
     * @return 
     */
    boolean[][] getBitmap() {
        return this.bitmap;
    }

    
    /**
     * Pouze pro ladění.
     *
     */
    void printBitmapToStdOutForDebug() {
        System.out.println("");
        for (int row=0; row<dimPxs; row++)  {
            if (row % CHAR_SIZE == 0) {
                System.out.println();
            }
            for (int x=0; x<dimPxs; x++)  {
                if (x > 0 && x % CHAR_SIZE == 0) {
                    System.out.print(" ");
                }
                System.out.print(" " + (this.bitmap[x][row] ? "1" : "-"));
            }
            System.out.println();
        }
    }
    
    // JEN PRO TEST ... --------------------------------------------------------
//    /** */
//    public static void main(String[] args) {
//        SquareBooleanBitmap sbb = new SquareBooleanBitmap();
//    }

}   // SquareBooleanBitmap.java
