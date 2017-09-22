/*
 * ExtFileFilter.java
 *
 *  created: 21.9.2017
 *  charset: UTF-8
 */

package cz.mp.zxs.tools.udg_editor;

import cz.mp.zxs.tools.udg_editor.utils.FileUtils;
import java.io.File;
import javax.swing.filechooser.FileFilter;


/**
 * Filtr souborů podle přípon.
 * 
 * @author Martin Pokorný
 */
public class ExtFileFilter extends FileFilter {
    
    //private ArrayList<String> extensions = new ArrayList<String>();
    private String[] extensions;
    /** Základní část popisu filtru, bez přípon. */
    private String baseDescription;    
    /** Výsledný popis. */
    private String description;
        
    /**
     * Vytvoří filtr souborů podle přípon.
     * Výsledná text bude ve formátu {@code baseDescription (seznam přípon)}.
     * 
     * @param baseDescription
     * @param extensions  texty přípon. Bez {@code '.'} nebo {@code '*.'},
     *      prostě jen texty přípon
     */
    public ExtFileFilter(String baseDescription, String... extensions) {
        this.baseDescription = baseDescription;
        if (extensions == null || extensions.length == 0) {
            throw new IllegalArgumentException("extensions is blank");
        }
        this.extensions = extensions;
        setUpDescription();        
    }

    /**
     * 
     * @return 
     */
    private String createExtensionsText() {
        StringBuilder sb = new StringBuilder();
        if (extensions.length > 1) {
            for (int i=0; i<extensions.length-1; i++) {
                sb.append("*.").append(extensions[i]);
                sb.append(", ");
            }
        }
        sb.append("*.").append(extensions[extensions.length-1]);
        return sb.toString();
    }
    
    /**
     * 
     */
    private void setUpDescription() {
        description = baseDescription;
        String extsText = createExtensionsText();
        if (baseDescription.isEmpty()) {
            description = extsText;
        }
        else {
            description = description + " (" + extsText + ")";
        }
    }
    
    /**
     * 
     * @return 
     */
    public String[] getExtensionsList() {
        String[] result = new String[extensions.length];
        System.arraycopy(extensions, 0, result, 0, extensions.length);
        return result;
    }
    
    @Override
    public boolean accept(File file) {
        // podmínka musí být (ve Windows, když se klikne na [Tento počítač] v postranním panelu, tak by to jinak spadlo)
        if (file.getName() == null || file.getName().isEmpty()) {   
            return true;
        }
        boolean hasExt = FileUtils.hasFileExtension(file.getName(), extensions);
        boolean isDir = file.isDirectory();
        return isDir || hasExt;
    }

    @Override
    public String getDescription() {
        return description;
    }
    
}   // ExtFileFilter.java
