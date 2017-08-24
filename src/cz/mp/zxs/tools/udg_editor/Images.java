/*
 * Images.java
 *
 */
package cz.mp.zxs.tools.udg_editor;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 * Třída {@code Images} umožňuje přístup k obrázkům v resources.
 * 
 * @author Martin Pokorný
 */
public class Images {

    private static final String IMG_RESOURCE = "/resources/images/";
    
    /** */
    private Images() {        
    }

    /**
     * Seznam obrázků;
     * klíč je jméno souboru obrázku.
     */
    private static Map<String, ImageIcon> images = 
            new HashMap<String, ImageIcon>();

    public static final String LOGO_16 = "logo_16.png";
    public static final String LOGO_32 = "logo_32.png";
    public static final String LOGO_64 = "logo_64.png";
    
    public static final String OK = "tick_ok.png";
    public static final String CANCEL = "cross_cancel.png";


    public static final String BTN_DRAW_POINT = "draw_point.png";
    public static final String BTN_DRAW_LINE = "draw_line.png";
    
    public static final String BTN_UNDO = "undo.png";
    public static final String BTN_REDO = "redo.png";

    public static final String BTN_CLEAR = "clear.png";
    public static final String BTN_INVERT = "invert.png";

    public static final String BTN_TO_RIGHT = "to_right.png";
    public static final String BTN_TO_LEFT = "to_left.png";
    public static final String BTN_MIRROR_VERTICAL = "mirror_vertical.png";
    public static final String BTN_MIRROR_HORIZONTAL = "mirror_horizontal.png";
    public static final String BTN_MOVE_UP = "move_to_up.png";
    public static final String BTN_MOVE_DOWN = "move_to_down.png";
    public static final String BTN_MOVE_LEFT = "move_to_left.png";
    public static final String BTN_MOVE_RIGHT = "move_to_right.png";
    
    public static final String ARROW_TO_RIGHT = "arrow_to_right.png";
    public static final String COPY = "copy.png";
    public static final String PASTE = "paste.png";

    // 16 x 8
    public static final String EXPORT_AND_COPY = "export_and_copy_1.png";
    // 16 x 8
    public static final String PASTE_AND_FILL = "paste_and_fill_1.png";
    
    
    /**
     * 
     * @param imgName  jméno obrázku např. 
     *      {@linkplain #OK}, {@linkplain #CANCEL}
     * @return 
     *      {@code null}, pokud není zadané jméno definováno, jinak ikonu.
     */
    public static ImageIcon getImage(String imgName) {
        if (!images.containsKey(imgName)) {
            URL imgURL = Images.class.getResource(IMG_RESOURCE + imgName);
            if (imgURL != null) {
                images.put(imgName, new ImageIcon(imgURL));
            }
            else {
                System.err.print("imgName " + imgName + "  NOT FOUND !");
            }
        }
        return images.get(imgName);
    }
}   // Images