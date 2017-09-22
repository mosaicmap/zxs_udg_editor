/*
 * FileUtils.java
 *
 *  created: 21.9.2017
 *  charset: UTF-8
 */

package cz.mp.zxs.tools.udg_editor.utils;

import java.io.File;


/**
 *
 * @author Martin Pokorný
 */
public class FileUtils {

    /** */
    private FileUtils() {
    }
    
        
    /**
     * Získá jméno souboru bez cesty a bez přípony.
     * <p>
     * např:
     * <pre>
     * {@literal b/a.txt --> a}
     * </pre>
     * 
     * @return 
     * @throws IllegalArgumentException
     */
    public static String getFileBaseName(String path) {
        if (path == null) {
            throw new IllegalArgumentException("path = null");
        }    
        
        return FileUtils.getFilePathWithoutExt(new File(path).getName());
    }
    
    /**
     * Získá jméno souboru bez přípony, ale s původní cestou.
     * <p>
     * např:
     * <pre>
     * {@literal a/a.txt --> a/a}
     * {@literal a.txt   --> a}
     * </pre>
     * 
     * @param path
     * @return 
     * @throws IllegalArgumentException
     */
    public static String getFilePathWithoutExt(String path) {
        if (path == null) {
            throw new IllegalArgumentException("path = null");
        }
        
        if (!path.contains(".")) {
            return path;
        }
        if (path.indexOf(".") == 0 && 
                path.indexOf(".") == path.lastIndexOf(".")) {
           return path;
        }
        return path.substring(0, path.lastIndexOf("."));        
    }
    
    /**
     * Získá příponu zadaného souboru.
     * 
     * @param fileName
     * @return přípona souboru (bez znaku {@literal .})
     * @throws IllegalArgumentException
     */
    public static String getFileExtension(final String fileName) {
        return getFileExtension(new File(fileName));
    }
    
    /**
     * Získá příponu zadaného souboru.
     * 
     * @param file
     * @return  přípona souboru (bez znaku {@literal .})
     * @throws IllegalArgumentException
     */    
    public static String getFileExtension(final File file) {
        if (file == null) {
            throw new IllegalArgumentException("file=null");
        }
        
        String fileName = file.getName().trim();
        if (!fileName.contains(".")) {
            return "";
        }
        if (fileName.lastIndexOf(".") == 0) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);        
    }
    
    /**
     * Testuje, zda má zadaný soubor jednu ze zadaných přípon.
     * 
     * @param fileName
     * @param exts
     * @return 
     * @throws IllegalArgumentException
     */
    public static boolean hasFileExtension(final String fileName, 
            String... exts) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("file is blank");
        }
        if (exts == null || exts.length == 0) {
            throw new IllegalArgumentException("exts is blank");
        }

        String fileExt = getFileExtension(fileName);
        for (String ext : exts) {
            if (fileExt.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }    
    
}   // FileUtils.java
