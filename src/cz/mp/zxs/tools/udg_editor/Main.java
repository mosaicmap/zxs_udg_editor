/*
 * Main.java
 *
 *  created: 29.5.2017
 *  charset: UTF-8
 */

package cz.mp.zxs.tools.udg_editor;


/**
 * ZX Spectrum UDG Editor.
 * 
 * @author Martin Pokorný
 */
public class Main {    

    public static final String VERSION_SPEC = 
            Main.class.getPackage().getSpecificationVersion(); // viz build.xml
    public static final String VERSION_IMPL = 
            Main.class.getPackage().getImplementationVersion(); // viz build.xml
    public static final String VERSION;
    static {
        if (VERSION_SPEC == null || VERSION_SPEC.length() == 0) {
            VERSION = "DEVEL";
        }
        else if (VERSION_IMPL == null || VERSION_IMPL.length() == 0) {
            VERSION = VERSION_SPEC;
        }
        else {
            VERSION = VERSION_SPEC + " (" + VERSION_IMPL + ")";
        }
    }
    
    /**
     * 
     */
    private static void printVersion() {
        System.out.println(VERSION); 
    }
    
    /** */
    public static void main(String[] args) {
        boolean optVersion = false;
        
        for(int i = 0; i < args.length; i++) {
            String opt = args[i].trim().toLowerCase();
            if (opt.equals("--version")) {
                optVersion = true;
            }
        }
        if (optVersion) {
            printVersion();
            System.exit(0);
        } 
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame.getInstance().setVisible(true);
            }
        });
    }

}   // Main.java
