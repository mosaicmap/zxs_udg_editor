/*
 * FileUtilsTest.java
 *
 *  created: 10.10.2017
 *  charset: UTF-8
 */

package cz.mp.zxs.tools.udg_editor.utils;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 *
 * @author Martin Pokorný
 */
public class FileUtilsTest {

    // -----
    
    @Test
    public void testGetFileBaseName01() {
        String result = FileUtils.getFileBaseName("test.txt");
        String expected = "test";
        assertEquals(expected, result);
    }  
    
    @Test
    public void testGetFileBaseName03() {
        String result = FileUtils.getFileBaseName("");
        String expected = "";
        assertEquals(expected, result);
    } 
    
    @Test
    public void testGetFileBaseName05() {
        String result = FileUtils.getFileBaseName("test.");
        String expected = "test";
        assertEquals(expected, result);
    } 
    
    @Test
    public void testGetFileBaseName07() {
        String result = FileUtils.getFileBaseName("/opt/test.txt");
        String expected = "test";
        assertEquals(expected, result);
    }  
    
    @Test
    public void testGetFileBaseName08() {
        String result = FileUtils.getFileBaseName("/opt/test.text.txt");
        String expected = "test.text";
        assertEquals(expected, result);
    }    
        
    // -----
    
    @Test
    public void testGetFileExtension01() {
        String result = FileUtils.getFileExtension("test.txt");
        String expected = "txt";
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetFileExtension02() {
        String result = FileUtils.getFileExtension(".txt");  // pokud začíná tečkou, je to považováno za soubor (na linuxu jako skrytý soubor)
        String expected = "";
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetFileExtension03() {
        String result = FileUtils.getFileExtension("");
        String expected = "";
        assertEquals(expected, result);
    }    

    @Test
    public void testGetFileExtension04() {
        String result = FileUtils.getFileExtension(".");
        String expected = "";
        assertEquals(expected, result);
    }    
    
    @Test
    public void testGetFileExtension05() {
        String result = FileUtils.getFileExtension("test.");
        String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testGetFileExtension06() {
        String result = FileUtils.getFileExtension("test...txt");
        String expected = "txt";
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetFileExtension07() {
        String result = FileUtils.getFileExtension("/opt/test.txt");
        String expected = "txt";
        assertEquals(expected, result);
    }    
    
    @Test
    public void testGetFileExtension08() {
        String result = FileUtils.getFileExtension("/opt/test.text.txt");
        String expected = "txt";
        assertEquals(expected, result);
    }    
    
    // -----    

    @Test
    public void testGetFilePathWithoutExt01() {
        String result = FileUtils.getFilePathWithoutExt("test.txt");
        String expected = "test";
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetFilePathWithoutExt06() {
        String result = FileUtils.getFilePathWithoutExt("/a/test.txt");
        String expected = "/a/test";
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetFilePathWithoutExt08() {
        String result = FileUtils.getFilePathWithoutExt("/opt/test.text.txt");
        String expected = "/opt/test.text";
        assertEquals(expected, result);
    }    
    
    // -----

    @Test
    public void testHasFileExtension01() {
        boolean result = FileUtils.hasFileExtension("test.txt", "TXT");
        assertTrue(result);
    }
    
    @Test
    public void testHasFileExtension02() {
        boolean result = FileUtils.hasFileExtension("test.txt", "html", "xml");
        assertFalse(result);
    }    
                
    // -----
    
}   // FileUtilsTest.java
