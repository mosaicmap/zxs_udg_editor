/*
 * FilePreviewDialog.java
 *
 *  created: 26.7.2017
 *  charset: UTF-8
 */

package cz.mp.zxs.tools.udg_editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


/**
 * Okno pro zobrazení náhledu UDG z dat v souboru.
 * Formát souboru:
 * <p><pre><tt>
 * [kategorie_1]
 * 10,20,30,40,50,60,70,80 -- data 1
 * 10,20,30,40,50,60,70,80 -- ...
 * 10,20,30,40,50,60,70,80 -- data n
 * [kategorie_2]
 * 10,20,30,40,50,60,70,80 -- data ..
 * 10,20,30,40,50,60,70,80 -- data ...
 * ...
 * </tt></pre>
 * 
 * @author Martin Pokorný
 */
public class UdgFilePreviewFrame {
    
    // JDialog | JFrame -- podle toho, zda se nastaví ownerOfDialog
    private Window window;
    // null nebo JFrame
    private JFrame ownerOfDialog;
    
    private static UdgFilePreviewFrame instance = null;
        
    private JLabel cathegorysLabel = new JLabel("Cathegory");
    private JComboBox cathegoriesCombo = new JComboBox();
    
    private JPanel dataPanel = new JPanel(new GridBagLayout());
    private JScrollPane dataScrollPane = new JScrollPane(
            dataPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    private JTextField udgFileField = new JTextField();
    private File udgFile;
    private static final String DEFAULT_UDG_TXT_FILE_NAME = "udg_data_examples.txt";        
            
//    private Toolkit toolkit;
//    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    private static final int SIZE_MIN_WIDTH = 400;
    private static final int SIZE_MIN_HEIGHT = 250;

    private static final int SIZE_DEFAULT_WIDTH = 500;
    private static final int SIZE_DEFAULT_HEIGHT = 450;
    
    private static final String TITLE = "ZX Spectrum UDG Previewer";
    
    
    /** */
    private UdgFilePreviewFrame(JFrame owner) {
        super();
        
        this.ownerOfDialog = owner;
        if (ownerOfDialog != null) {
            window = new JDialog(owner);
            ((JDialog) window).setTitle(TITLE);
        }
        else {
            window = new JFrame();
            ((JFrame) window).setTitle(TITLE);
        }
                
        try {
            initComponents();
            initLayout();
            initEventHandlers();
            initWindow();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);            
        }        

    }

    private void initComponents() {
        udgFileField.setEditable(false);
        
        dataScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        dataScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        dataScrollPane.getHorizontalScrollBar().setUnitIncrement(5);
        dataScrollPane.getVerticalScrollBar().setUnitIncrement(7);
    }

    private void initWindow() {
        ArrayList<Image> icons = new ArrayList<Image>();
        icons.add(Images.getImage(Images.LOGO_16).getImage());
        icons.add(Images.getImage(Images.LOGO_32).getImage());
        icons.add(Images.getImage(Images.LOGO_64).getImage());
        window.setIconImages(icons);

//        toolkit = Toolkit.getDefaultToolkit();
//        clipboard = toolkit.getSystemClipboard();
        
        initWindowSizeAndPosition();
    }
    
    private void initWindowSizeAndPosition() {
        window.setMinimumSize(new Dimension(SIZE_MIN_WIDTH, SIZE_MIN_HEIGHT));
        window.setSize(new Dimension(SIZE_DEFAULT_WIDTH, SIZE_DEFAULT_HEIGHT));
        
        window.setLocationRelativeTo(ownerOfDialog);    // pokud je null, zarovná se na prostředek obrazovky
    }

    private void initEventHandlers() {
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                window.dispose();
            }
        });        
                
        cathegoriesCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() != ItemEvent.SELECTED) {
                    return;
                }
                updateSelectedPreviewInEdt();
            }
        });
    }

    /**
     * 
     */
    private void updateSelectedPreviewInEdt() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateSelectedPreview();
            }
        });                        
    }
    
    /**
     * 
     */
    private void updateSelectedPreview() {
        Data selectedData = ((DataItemsComboBoxModel) 
                cathegoriesCombo.getModel()).getSelectedData();                        
        //System.out.println(selectedData.getKey() + "  " + selectedData.getValues()); 
        
        dataPanel.removeAll();
        dataPanel.revalidate();
        dataPanel.repaint();

        Insets ins5555 = new Insets(3, 5, 3, 5);
        Insets ins5055 = new Insets(3, 0, 3, 5);

        int r = 0;
        for (String textData : selectedData.getValues()) {
            int commas = textData.replaceAll("[^,]*", "").length();
            int dimInChars = commas > 8 ? 2 : 1;        // jen jednoduše
//            System.out.println("dimInChars = " + dimInChars);
            
            SquareBooleanBitmap bmap = new SquareBooleanBitmap(dimInChars);
//            System.out.println("data = " + textData);
            bmap.setDataFromText(textData);
            int margin = 2;
            BitmapPreviewPanel bmPreview = new BitmapPreviewPanel(margin);
            bmPreview.setBmap(bmap);

            JTextField dataFiled = new JTextField(textData);
            dataFiled.setEditable(false);
            
            dataPanel.add(bmPreview, new GridBagConstraints(0,r,1,1,0.0,0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, ins5555, 0,0));
            dataPanel.add(dataFiled, new GridBagConstraints(1,r,1,1,1.0,0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, ins5055, 0,0));
            r++;
        }
        dataPanel.revalidate();
        dataPanel.repaint();
        
//        System.out.println("-----");
    }


    private void initLayout() {
        window.setLayout(new GridBagLayout());
        
        Insets ins5555 = new Insets(5, 5, 5, 5);
        Insets ins5550 = new Insets(5, 5, 5, 0);
                
        int r=0;
        window.add(udgFileField, new GridBagConstraints(0,r,5,1,1.0,0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, ins5555, 0,0));
        r++;
        
        window.add(cathegorysLabel, new GridBagConstraints(0,r,1,1,0.0,0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, ins5550, 0,0));
        window.add(cathegoriesCombo, new GridBagConstraints(1,r,5,1,1.0,0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, ins5555, 0,0));
        r++;
        
        window.add(dataScrollPane, new GridBagConstraints(0,r,5,5,1.0,1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, ins5555, 0,0));
        r+=5;
    }
    
    /**
     * 
     * @param file
     * @throws IOException 
     */
    private void setUdgFile(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("file");
        }
        if (!file.exists() || file.isDirectory()) {
            //throw new IllegalArgumentException("file neexistuje");
            udgFileField.setText("ERROR:  " + file.getName() + "  not found!");
            cathegoriesCombo.setModel(new DefaultComboBoxModel());
            cathegoriesCombo.repaint();
            return;
        }

        udgFile = file;
        
        udgFileField.setText(udgFile.getAbsolutePath());
        fillCathegoriesCombo();
    }

    /**
     * .
     * Volat po nastavení souboru, po {@linkplain #setUdgFile(java.io.File) }.
     * 
     * @throws IOException  pokud se čtení souboru nepovede
     */
    private void fillCathegoriesCombo() throws IOException {
        if (udgFile == null) {
            throw new IllegalStateException("udgFile = null");
        }
        
        ArrayList<Data> udgData = getDataFromFile(udgFile);
        
        DataItemsComboBoxModel model = new DataItemsComboBoxModel(udgData);
        
        cathegoriesCombo.setModel(model);      
        cathegoriesCombo.repaint();
        
        updateSelectedPreviewInEdt();
    }
    
    /**
     * 
     * @param owner  pokud je zadáno okno {@code JFrame}, pak se použije 
     *      {@code JDialog}. Pokud je {@code null}, pak se použije {@code JFrame}.
     * @return 
     * @see showWindow()
     */
    public static UdgFilePreviewFrame getInstance(JFrame owner) {
        if (instance == null) {
            instance = new UdgFilePreviewFrame(owner);
        }
        return instance;
    }

    /**
     * Zobrazí tento dialog nebo okno.
     * 
     * @see #getInstance(javax.swing.JFrame) 
     */
    public void showWindow() {
        
        window.setVisible(true);
    }

    
    private static final int MAX_CATHEGORY_NAME_LEN = 64;
    private static final String FILE_CHARSET_NAME = "UTF-8";
    /**
     * 
     * @param file
     * @return  seznam dat
     * @throws FileNotFoundException
     * @throws IOException 
     */
    static ArrayList<Data> getDataFromFile(File file) 
            throws FileNotFoundException, IOException {
        if (file == null) {
            throw new IllegalArgumentException("file = null");
        }
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException("file not found");
        }
        ArrayList<Data> data = new ArrayList<Data>();
        
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), FILE_CHARSET_NAME));
        
        String category = "";
        ArrayList<String> categoryData = new ArrayList<String>();
        String line;
        String tLine = "";
        while ((line = br.readLine()) != null) {
            tLine = line.trim();
            if (tLine.isEmpty() || tLine.startsWith("#")) {
                continue;
            }
            // [kategorie]
            if (tLine.startsWith("[") && tLine.endsWith("]")) {
                if (! category.isEmpty()) {   
                    // omezení na délku textu v kombu
                    if (category.length() > MAX_CATHEGORY_NAME_LEN) {
                        category = category.substring(0, MAX_CATHEGORY_NAME_LEN-1);
                    }
                    data.add(new Data(category, categoryData));
                }
                category = tLine.substring(1, tLine.length()-1);
//                System.out.println("  category = " + category);
                categoryData = new ArrayList<String>();
                continue;
            }
            categoryData.add(tLine);
        }
        
        // poslední kategorie
        if (! categoryData.isEmpty()) {   
            // omezení na délku textu v kombu
            if (category.length() > MAX_CATHEGORY_NAME_LEN) {
                category = category.substring(0, MAX_CATHEGORY_NAME_LEN-1);
            }
            data.add(new Data(category, categoryData));
        }

        return data;
    }

    
    /** */
    public static void main(String[] args) 
            throws IOException {

        final File udgFile;
        if (args.length >= 1) {
            udgFile = new File(args[0]);
        }
        else {
            udgFile = new File(DEFAULT_UDG_TXT_FILE_NAME);
        }
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UdgFilePreviewFrame prevFrame = 
                        UdgFilePreviewFrame.getInstance(null);
                try {
                    prevFrame.setUdgFile(udgFile);
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
                prevFrame.showWindow();
            }
        });
        
    }   // main

}   // UdgFilePreviewFrame   

/**
 * 
 * @author Martin Pokorný
 */
class Data {
    private String key;
    private ArrayList<String> values;

    /**
     * 
     * @param key
     * @param values 
     */
    public Data(String key, ArrayList<String> values) {
        this.key = key;
        this.values = values;
    }

    public String getKey() {
        return key;
    }
    
    public ArrayList<String> getValues() {
        return values;
    }
    
    @Override
    public String toString() {
        return key.toString();
    }    
}

/**
 * 
 * @author Martin Pokorný
 * @see Data
 */
class DataItemsComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private ArrayList<Data> data;
    private int selectedIdx; // vybraný klíč = kategorie

    public DataItemsComboBoxModel(ArrayList<Data> data) {
        this.data = data;
    }
            
    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public Object getElementAt(int index) {
        if (index < 0 || index >= data.size()) {
            return null;
        }
        return data.get(index);
    }

    @Override
    public void setSelectedItem(Object item) {
        if (item == null) {
            selectedIdx = -1;
            return;
        }        
        if (item instanceof Data) {
            for(int i=0; i<data.size(); i++) {
                if (data.get(i).getKey().equals(((Data)item).getKey())) {
                //if (data.get(i).equals((Data)item)) {
                    selectedIdx = i;
                    break;
                }
            }            
        }
    }

    @Override
    public Object getSelectedItem() {
        if (selectedIdx < 0) {
            return null;
        }        
        return data.get(selectedIdx);
    }
    
    /**
     * 
     * @return {@linkplain Data} nebo {@code null}
     */
    public Data getSelectedData() {
        if (selectedIdx < 0) {
            return null;
        }        
        return data.get(selectedIdx);        
    }
}

// FilePreviewDialog.java
