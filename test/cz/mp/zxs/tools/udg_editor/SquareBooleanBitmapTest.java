/*
 * SquareBooleanBitmapTest.java
 *
 *  created: 27.9.2017
 *  charset: UTF-8
 */

package cz.mp.zxs.tools.udg_editor;

import java.util.Arrays;
import org.junit.*;
import static org.junit.Assert.*;


/**
 *
 * @author Martin Pokorný
 */
public class SquareBooleanBitmapTest {

    @Test
    public void testGetAndSetDimension01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap();
        
        assertEquals(SquareBooleanBitmap.DEFAULT_NUM_OF_CHARS, 
                sbBmp.getDimensionInChars());

        sbBmp = new SquareBooleanBitmap(1);
        
        int expected;
        int expectedPx;

        expected = 1;
        assertEquals(expected, sbBmp.getDimensionInChars());
        expectedPx = expected * SquareBooleanBitmap.CHAR_SIZE;
        assertEquals(expectedPx, sbBmp.getDimensionInPxs());        
        
        expected = 1;
        sbBmp.setDimensionInCharsAndClear(expected);
        assertEquals(expected, sbBmp.getDimensionInChars());
        expectedPx = expected * SquareBooleanBitmap.CHAR_SIZE;
        assertEquals(expectedPx, sbBmp.getDimensionInPxs());

        expected = 9;
        sbBmp.setDimensionInCharsAndClear(expected);
        assertEquals(expected, sbBmp.getDimensionInChars());
        expectedPx = expected * SquareBooleanBitmap.CHAR_SIZE;
        assertEquals(expectedPx, sbBmp.getDimensionInPxs());        
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAndSetDimensionErrConstructor01() {
        new SquareBooleanBitmap(20);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testGetAndSetDimensionErrConstructor02() {
        new SquareBooleanBitmap(0);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testGetAndSetDimensionErrSetter01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap();
        sbBmp.setDimensionInCharsAndClear(20);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testGetAndSetDimensionErrSetter02() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap();
        sbBmp.setDimensionInCharsAndClear(0);
    }

    @Test
    public void testCheckXY01() {
        int dim = 1;
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(dim);
        sbBmp.checkXY(0,0);
        sbBmp.checkXY(1,1);
        sbBmp.checkXY(
                dim * SquareBooleanBitmap.CHAR_SIZE-1, 
                dim * SquareBooleanBitmap.CHAR_SIZE-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckXYerr01() {
        int dim = 1;
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(dim);
        sbBmp.checkXY(0,-1);
    }    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckXYerr02() {
        int dim = 1;
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(dim);
        sbBmp.checkXY(-1,0);
    }    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckXYerr03() {
        int dim = 1;
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(dim);
        sbBmp.checkXY(0,dim * SquareBooleanBitmap.CHAR_SIZE);
    }    

    @Test
    public void testClearAllAndIsEmpty01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        assertTrue(sbBmp.isEmpty());

        sbBmp.setBit(0, 0);
        sbBmp.setBit(1, 0);
        sbBmp.setBit(1, 1);
        assertFalse(sbBmp.isEmpty());
        
        sbBmp.clearAll();
        assertTrue(sbBmp.isEmpty());
    }
    
    @Test
    public void testGetBitmapCopy01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        sbBmp.setBit(0, 0);
        sbBmp.setBit(1, 0);
        sbBmp.setBit(1, 1);

        assertArrayEquals(sbBmp.getBitmap(), sbBmp.getBitmapCopy());        
    }    
        
    @Test
    public void testSetGetInvertBit01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        assertTrue(sbBmp.isEmpty());
        
        sbBmp.setBit(0, 0);
        assertFalse(sbBmp.isEmpty());
        sbBmp.clearBit(0, 0);
        assertTrue(sbBmp.isEmpty());        
        sbBmp.invertBit(0, 0);
        assertFalse(sbBmp.isEmpty());
        sbBmp.invertBit(0, 0);
        assertTrue(sbBmp.isEmpty());        
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGetInvertBit02Err() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        sbBmp.setBit(10, 0);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetInvertBit03Err() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        sbBmp.clearBit(0, 10);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetInvertBit04Err() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        sbBmp.invertBit(0, -1);
    }
    
    @Test
    public void testSetDataFromText01OneChar() {
        SquareBooleanBitmap sbBmpExpected = new SquareBooleanBitmap(1);
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
         
        // ---
        sbBmpExpected.clearAll();
        sbBmpExpected.setBit(0, 0); // 128
        sbBmpExpected.setBit(1, 0); // 64
        sbBmpExpected.setBit(1, 1); // 64
        
        sbBmp.clearAll();
        sbBmp.setDataFromText("192, 64, 0, 0, 0, 0, 0, 0");
                
        assertArrayEquals(sbBmpExpected.getBitmap(), sbBmp.getBitmap());
        
        // ---
        sbBmpExpected.clearAll();
        sbBmpExpected.setBit(7, 2); // 1
        sbBmpExpected.setBit(6, 2); // 2
        sbBmpExpected.setBit(5, 2); // 4
        sbBmpExpected.setBit(7, 7); // 1
        
        sbBmp.clearAll();
        sbBmp.setDataFromText("0, 0, 7, 0, 0, 0, 0, 1");

        assertArrayEquals(sbBmpExpected.getBitmap(), sbBmp.getBitmap());
        
        // ---
        sbBmpExpected.clearAll();
        sbBmpExpected.setBit(7, 2); // 1
        sbBmpExpected.setBit(6, 2); // 2
        sbBmpExpected.setBit(5, 2); // 4
        sbBmpExpected.setBit(7, 7); // 1
        
        sbBmp.clearAll();
        sbBmp.setDataFromText("DATA 0, 0, 7, 0, 0, 0, 0, 1");
        
        assertArrayEquals(sbBmpExpected.getBitmap(), sbBmp.getBitmap());
        
        // ---
        sbBmpExpected.clearAll();
        sbBmpExpected.setBit(7, 2); // 1
        sbBmpExpected.setBit(6, 2); // 2
        sbBmpExpected.setBit(5, 2); // 4
        sbBmpExpected.setBit(7, 7); // 1
        
        sbBmp.clearAll();
        sbBmp.setDataFromText("DATA 0, 0, 7, 0, 0, 0, 0, 1, 1000, 0");
        
        assertArrayEquals(sbBmpExpected.getBitmap(), sbBmp.getBitmap());     
        
        // --- false
        sbBmpExpected.clearAll();
        sbBmpExpected.setBit(7, 2); // 1
        sbBmpExpected.setBit(6, 2); // 2
        sbBmpExpected.setBit(5, 2); // 4
        sbBmpExpected.setBit(7, 7); // 1
        
        sbBmp.clearAll();
        sbBmp.setDataFromText("0, 0, 7, 0, 0, 0, 0, 1 0");  // (pozn: "1 0" --> 10)

        assertFalse(Arrays.equals(sbBmpExpected.getBitmap(), sbBmp.getBitmap()));
        
        // --- bin
        sbBmpExpected.clearAll();
        sbBmpExpected.setBit(7, 2); // 1
        sbBmpExpected.setBit(6, 2); // 2
        sbBmpExpected.setBit(5, 2); // 4
        sbBmpExpected.setBit(7, 7); // 1
        
        sbBmp.clearAll();
        sbBmp.setDataFromText("DATA 0, 0, BIN 00000111, BIN 00000000, 0, 0, 0, BIN 00000001");
        
        assertArrayEquals(sbBmpExpected.getBitmap(), sbBmp.getBitmap());           
    }  
    
    @Test
    public void testGetDataLines01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        sbBmp.setDataFromText("168, 255, 60, 0, 1, 2, 127, 24");
        int[] expected = new int[] {168, 255, 60, 0, 1, 2, 127, 24};
        assertArrayEquals(expected, sbBmp.getDataLines());
    }
    
    @Test
    public void testGetDataLines02() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(2);
        sbBmp.setDataFromText("168, 255, 60, 0, 1, 2, 127, 24, "
                + "1,2,4,8,16,32,64,128,"
                + "0, 9, 8, 65, 55, 54, 12, 123,"
                + "15, 15, 16, 16, 24, 24, 25, 26");
        int[] expected = new int[] {168, 255, 60, 0, 1, 2, 127, 24,
                1,2,4,8,16,32,64,128,
                0, 9, 8, 65, 55, 54, 12, 123,
                15, 15, 16, 16, 24, 24, 25, 26};
        assertArrayEquals(expected, sbBmp.getDataLines());
    }    
    
    @Test
    public void testGetDataLines03() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        sbBmp.setDataFromText("168, 255, 60, 0, 1, 2, 127, 24, "
                + "1,2,4,8,16,32,64,128,"
                + "0, 9, 8, 65, 55, 54, 12, 123,"
                + "15, 15, 16, 16, 24, 24, 25, 26");
        int[] expected = new int[] {168, 255, 60, 0, 1, 2, 127, 24};
        assertArrayEquals(expected, sbBmp.getDataLines());
    } 
    
    @Test
    public void testGetDataLinesAsText01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        sbBmp.setDataFromText("168, 255, 60, 0, 1, 2, 127, 24");
        assertEquals("168,255,60,0,1,2,127,24", sbBmp.getDataLinesAsText());    // výsledek getDataLinesAsText je bez mezer ...

        sbBmp.clearAll();
        sbBmp.setDataFromText("168,255,60,0,1,2,127,24");
        assertEquals("168,255,60,0,1,2,127,24", sbBmp.getDataLinesAsText());
    }

    @Test
    public void testgetDataAsXBM01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        String expected;
        sbBmp.setDataFromText("168, 255, 60, 0, 1, 2, 127, 24");
        expected = 
                "#define test_width 8" + SquareBooleanBitmap.EOL + 
                "#define test_height 8" + SquareBooleanBitmap.EOL + 
                "static unsigned char test_bits[] = {" + SquareBooleanBitmap.EOL + 
                "  0x15, 0xff, 0x3c, 0x00, 0x80, 0x40, 0xfe, 0x18};" + SquareBooleanBitmap.EOL;
        assertEquals(expected, sbBmp.getDataAsXBM("test"));
        
        sbBmp = new SquareBooleanBitmap(2);
        sbBmp.setDataFromText("128,0,0,0,0,0,0,0, 255,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,32, 0,0,0,0,0,0,0,64");
        expected = 
                "#define test2_width 16" + SquareBooleanBitmap.EOL + 
                "#define test2_height 16" + SquareBooleanBitmap.EOL + 
                "static unsigned char test2_bits[] = {" + SquareBooleanBitmap.EOL + 
                "  0x01, 0xff, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, " + SquareBooleanBitmap.EOL +
                "  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x02};" + SquareBooleanBitmap.EOL;
        assertEquals(expected, sbBmp.getDataAsXBM("test2"));        
    }
    
    @Test
    public void testInvertAll01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        assertTrue(sbBmp.isEmpty());
        sbBmp.setDataFromText("255,255,255,255, 255,255,255,255");
        assertFalse(sbBmp.isEmpty());
        sbBmp.invertAll();
        assertTrue(sbBmp.isEmpty());
        sbBmp.invertAll();
        assertFalse(sbBmp.isEmpty());
    }
    
    @Test
    public void testSetGetInvertBit05() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        
        sbBmp.setDataFromText("255, 0");
        sbBmp.clearBit(7, 0);
        assertEquals("254,0,0,0,0,0,0,0", sbBmp.getDataLinesAsText());
        sbBmp.setBit(7, 0);
        sbBmp.setBit(7, 1);
        assertEquals("255,1,0,0,0,0,0,0", sbBmp.getDataLinesAsText());
        sbBmp.invertBit(7, 1);
        assertEquals("255,0,0,0,0,0,0,0", sbBmp.getDataLinesAsText());
        sbBmp.invertBit(7, 1);
        assertEquals("255,1,0,0,0,0,0,0", sbBmp.getDataLinesAsText());
    }
    
    @Test
    public void testMirrorHorizontal01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        sbBmp.setDataFromText("137,233,121,56,40,42,42,34");
        sbBmp.mirrorHorizontal();
        assertEquals("34,42,42,40,56,121,233,137", sbBmp.getDataLinesAsText());
    }
           
    @Test
    public void testMirrorVertical01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        sbBmp.setDataFromText("127,128,56,40,0,254,1,192");
        sbBmp.mirrorVertical();
        assertEquals("254,1,28,20,0,127,128,3", sbBmp.getDataLinesAsText());
    }    
    
    @Test
    public void testTurnLeft01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        sbBmp.setDataFromText("127,128,56,40,0,254,1,192");
        sbBmp.turnLeft();
        assertEquals("130,132,132,180,164,180,133,69", sbBmp.getDataLinesAsText());                
    }

    @Test
    public void testTurnRight01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        sbBmp.setDataFromText("127,128,56,40,0,254,1,192");
        sbBmp.turnRight();
        assertEquals("162,161,45,37,45,33,33,65", sbBmp.getDataLinesAsText());                
    }

    @Test
    public void testMoveToLeft01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        sbBmp.setDataFromText("127,128,56,40,0,254,1,192");
        sbBmp.moveToLeft();
        assertEquals("254,0,112,80,0,252,2,128", sbBmp.getDataLinesAsText());                
    }

    @Test
    public void testMoveToRight01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        sbBmp.setDataFromText("127,128,56,40,0,254,1,192");
        sbBmp.moveToRight();
        assertEquals("63,64,28,20,0,127,0,96", sbBmp.getDataLinesAsText());                
    }

    @Test
    public void testMoveToUp01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        sbBmp.setDataFromText("127,128,56,40,0,254,1,192");
        sbBmp.moveToUp();
        assertEquals("128,56,40,0,254,1,192,0", sbBmp.getDataLinesAsText());                
    }

    @Test
    public void testMoveToDown01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);
        sbBmp.setDataFromText("127,128,56,40,0,254,1,192");
        sbBmp.moveToDown();
        assertEquals("0,127,128,56,40,0,254,1", sbBmp.getDataLinesAsText());                
    }
    
    @Test
    public void testDrawLine01() {
        SquareBooleanBitmap sbBmp = new SquareBooleanBitmap(1);

        // 0,1 -> 7,1 = 0,255,0,0,0,0,0,0
        sbBmp.clearAll();
        sbBmp.drawLine(0,1, 7,1);
        assertEquals("0,255,0,0,0,0,0,0", sbBmp.getDataLinesAsText());
        // 7,1 -> 0,1 = 0,255,0,0,0,0,0,0
        sbBmp.clearAll();
        sbBmp.drawLine(7,1, 0,1);
        assertEquals("0,255,0,0,0,0,0,0", sbBmp.getDataLinesAsText());
    
        // 3,0 -> 3,7 = 16,16,16,16,16,16,16,16
        sbBmp.clearAll();
        sbBmp.drawLine(3,0, 3,7);
        assertEquals("16,16,16,16,16,16,16,16", sbBmp.getDataLinesAsText());
        // 3,7 -> 3,0 = 16,16,16,16,16,16,16,16
        sbBmp.clearAll();
        sbBmp.drawLine(3,7, 3,0);
        assertEquals("16,16,16,16,16,16,16,16", sbBmp.getDataLinesAsText());

        // 1,0 -> 6,7 = 64,32,32,16,8,4,4,2
        sbBmp.clearAll();
        sbBmp.drawLine(1,0, 6,7);
        assertEquals("64,32,32,16,8,4,4,2", sbBmp.getDataLinesAsText());
        // 6,7 -> 1,0 = 64,32,32,16,8,4,4,2
        sbBmp.clearAll();
        sbBmp.drawLine(6,7, 1,0);
        assertEquals("64,32,32,16,8,4,4,2", sbBmp.getDataLinesAsText());

        // 4,0 -> 0,7 = 8,16,16,32,32,64,64,128
        sbBmp.clearAll();
        sbBmp.drawLine(4,0, 0,7);
        assertEquals("8,16,16,32,32,64,64,128", sbBmp.getDataLinesAsText());
        // 0,7 -> 4,0 = 8,16,16,32,32,64,64,128
        sbBmp.clearAll();
        sbBmp.drawLine(0,7, 4,0);
        assertEquals("8,16,16,32,32,64,64,128", sbBmp.getDataLinesAsText());

        // 0,2 -> 7,5 = 0,0,192,48,12,3,0,0
        sbBmp.clearAll();
        sbBmp.drawLine(0,2, 7,5);
        assertEquals("0,0,192,48,12,3,0,0", sbBmp.getDataLinesAsText());
        // 7,5 -> 0,2 = 0,0,192,48,12,3,0,0
        sbBmp.clearAll();
        sbBmp.drawLine(7,5, 0,2);
        assertEquals("0,0,192,48,12,3,0,0", sbBmp.getDataLinesAsText());

        // 0,6 -> 7,3 = 0,0,0,3,12,48,192,0
        sbBmp.clearAll();
        sbBmp.drawLine(0,6, 7,3);
        assertEquals("0,0,0,3,12,48,192,0", sbBmp.getDataLinesAsText());
        // 7,3 -> 0,6 = 0,0,0,3,12,48,192,0
        sbBmp.clearAll();
        sbBmp.drawLine(7,3, 0,6);
        assertEquals("0,0,0,3,12,48,192,0", sbBmp.getDataLinesAsText());        
        
        // 1,1 -> 6,6 = 0,64,32,16,8,4,2,0
        sbBmp.clearAll();
        sbBmp.drawLine(1,1, 6,6);
        assertEquals("0,64,32,16,8,4,2,0", sbBmp.getDataLinesAsText());
        
        // 6,1 -> 1,6 = 0,2,4,8,16,32,64,0
        sbBmp.clearAll();
        sbBmp.drawLine(6,1, 1,6);
        assertEquals("0,2,4,8,16,32,64,0", sbBmp.getDataLinesAsText());
    }

    
}   // SquareBooleanBitmapTest.java
