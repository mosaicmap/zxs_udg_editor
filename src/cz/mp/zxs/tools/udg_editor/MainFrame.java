/*
 * MainFrame.java
 *
 *  created: 29.5.2017
 *  charset: UTF-8
 */

package cz.mp.zxs.tools.udg_editor;

import cz.mp.zxs.tools.udg_editor.utils.FileUtils;
import cz.mp.zxs.tools.udg_editor.utils.GuiUtils;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.undo.UndoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZX Spectrum UDG Editor.
 * Hlavní okno.
 * 
 * @author Martin Pokorný
 */
public class MainFrame extends JFrame {
    private static final Logger log = LoggerFactory.getLogger(MainFrame.class);
    static {
        log.debug("start initialisation");
    }
    
    private static MainFrame instance = null;

    private JTabbedPane tabbedPanel = new JTabbedPane();
    
    private BitmapEditorPanel bitmapEditorPanel = null;
    private BitmapEditorPanel bitmap2x2EditPanel = null;
    
    private static final String DEFAULT_DATA_8 = 
            "DATA 24,60,126,219,255,36,90,165";
    private static final String DEFAULT_DATA_16 = 
            "DATA 0,0,0,0,0,0,0,0, 96,124,62,44,124,60,24,60, " +
            "0,0,0,0,0,0,0,0, 126,126,239,223,60,110,118,238";
    private JPanel dataToBitmapPanel = new JPanel(new GridBagLayout());
    private JButton dataPasteAndFillBitmapBtn = new JButton("", Images.getImage(Images.PASTE_AND_FILL));
    private JEditorPane dataToBitmapTextArea = new JEditorPane();
    private JScrollPane dataToBitmapScrollPane = new JScrollPane(dataToBitmapTextArea);
    // (hlavně pro dataToBitmapTextArea)
    private UndoManager textAreaUndoManager = new UndoManager();

    private JButton dataToBitmapBtn = new JButton("", Images.getImage(Images.ARROW_TO_RIGHT));

    private JPanel bitmapToDataPanel = new JPanel(new GridBagLayout());
    private JEditorPane bitmapToDataTextArea = new JEditorPane();
    private JScrollPane bitmapToDataScrollPane = new JScrollPane(bitmapToDataTextArea);
    
    private JButton bitmapToDataTextAreaAndClipboardBtn = new JButton("", Images.getImage(Images.EXPORT_AND_COPY));
    private JButton bitmapToXbmFileBtn = new JButton("", Images.getImage(Images.EXPORT_TO_XBM));
    
    private Font biggerFieldFont = bitmapToDataTextArea.getFont()
            .deriveFont(bitmapToDataTextArea.getFont().getSize2D()+1);

    private JTextField aboutSelectableLabel = new JTextField("");
            
    private Toolkit toolkit;
    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    
    private static final String TITLE = "ZX Spectrum UDG Editor";
    
    
    /** */
    private MainFrame() {
        super(TITLE);
        
        try {            
            initComponents();
            initLayout();
            initEventHandlers();
            initFrame();
            log.debug("init done");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }        
    };

    private void initFrame() {
        log.debug("");
        
        ArrayList<Image> icons = new ArrayList<Image>();
        icons.add(Images.getImage(Images.LOGO_16).getImage());
        icons.add(Images.getImage(Images.LOGO_32).getImage());
        icons.add(Images.getImage(Images.LOGO_64).getImage());
        this.setIconImages(icons);

        toolkit = Toolkit.getDefaultToolkit();
        clipboard = toolkit.getSystemClipboard();
        
        initFrameSizeAndPosition();
        
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);        
    }

    private void initFrameSizeAndPosition() {
        log.debug("");
        this.pack();
        this.setMinimumSize(new Dimension(this.getWidth(), this.getHeight()));
        this.setSize(new Dimension(this.getWidth(), this.getHeight()));
        
        this.setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        log.debug("");
        
        initBitmapPanel();
        init2x2BitmapPanel();
        
        initTabbedPanel();
                
        // -----
        dataPasteAndFillBitmapBtn.setToolTipText("Paste DATA from clipboard & fill bitmap");
        dataToBitmapTextArea.setText(DEFAULT_DATA_8);
        dataToBitmapTextArea.setFont(biggerFieldFont);
        dataToBitmapScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        bitmapToDataTextAreaAndClipboardBtn.setToolTipText("Export bitmap to DATA & copy DATA to clipboard");
        bitmapToDataTextArea.setEditable(false);
        bitmapToDataTextArea.setFont(biggerFieldFont);
        bitmapToDataScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        bitmapToXbmFileBtn.setToolTipText("Export bitmap to XBM image file.");
                
        initAbout();
                
        dataToBitmapTextArea.getDocument().addUndoableEditListener(textAreaUndoManager);
    }

    private void initAbout() {
        log.debug("");
        // (c) \u00A9 2017 Martin Pokorn\u00FD
        aboutSelectableLabel.setText(" \u00A9 2017 Martin Pokorn\u00FD"
                + "   MartinPokorny.czech@gmail.com"
                + "   Version:" + Version.VERSION_SPEC + " ");
        aboutSelectableLabel.setOpaque(false);
        aboutSelectableLabel.setEditable(false);        
        aboutSelectableLabel.setBackground(null);
//        aboutSelectableLabel.setBorder(null);
        aboutSelectableLabel.setBorder(BorderFactory.createEmptyBorder());
        aboutSelectableLabel.setMargin(new Insets(10, 3, 3, 3));
    }
    
    private void initBitmapPanel() {
        log.debug("");
        bitmapEditorPanel = new BitmapEditorPanel();
        
        bitmapEditorPanel.setDimensionInChars(1);
        bitmapEditorPanel.setBitTileSize(24);

        bitmapEditorPanel.addBmapChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                bitmapToDataTextArea.setText("");
            }
        });
    }
        
    private void init2x2BitmapPanel() {
        log.debug("");
        bitmap2x2EditPanel = new BitmapEditorPanel();

        bitmap2x2EditPanel.setDimensionInChars(2);
        bitmap2x2EditPanel.setBitTileSize(18);
        
        bitmap2x2EditPanel.addBmapChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                bitmapToDataTextArea.setText("");
            }
        });        
    }

    private void initTabbedPanel() {
        log.debug("");
        tabbedPanel.addTab("   1   ", bitmapEditorPanel);
        tabbedPanel.addTab(" 2 \u00D7 2 ", bitmap2x2EditPanel);
    }
    
    /**
     * 
     * @return 
     */
    private BitmapEditorPanel getSelectedBitmapEditorPanel() {
        Component selected = tabbedPanel.getSelectedComponent();
        if (! (selected instanceof BitmapEditorPanel)) {
            return bitmapEditorPanel;
        }
        return (BitmapEditorPanel) selected;        
    }

    private void initLayout() {
        log.debug("");
        this.setLayout (new GridBagLayout());

        Insets ins5555 = new Insets(5, 5, 5, 5);
        Insets ins0000 = new Insets(0, 0, 0, 0);
                
        int r=0;
        Container c = getContentPane();
        
        dataToBitmapPanel.add(GuiUtils.createMinWidthFoobar(150,250), new GridBagConstraints(1,0,1,1,0.0,0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, ins0000, 0,0));
        dataToBitmapPanel.add(dataToBitmapScrollPane, new GridBagConstraints(1,1,1,2,1.0,1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, ins5555, 0,0));
        dataToBitmapPanel.add(dataPasteAndFillBitmapBtn, new GridBagConstraints(2,1,1,1,0.0,0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, ins5555, 0,0));
        dataToBitmapPanel.add(dataToBitmapBtn, new GridBagConstraints(2,2,1,1,0.0,0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, ins5555, 0,0));
        c.add(dataToBitmapPanel, new GridBagConstraints(0,r,10,2,1.0,0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, ins0000, 0,0));
        
        r+=2;

        c.add(tabbedPanel, new GridBagConstraints(0,r,6,5,1.0,1.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, ins5555, 0,0));
        r+=5;

        bitmapToDataPanel.add(GuiUtils.createMinWidthFoobar(150,250), new GridBagConstraints(2,0,1,1,0.0,0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, ins5555, 0,0));
        bitmapToDataPanel.add(bitmapToDataTextAreaAndClipboardBtn, new GridBagConstraints(1,1,1,1,0.0,0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, ins5555, 0,0));
        bitmapToDataPanel.add(bitmapToXbmFileBtn, new GridBagConstraints(1,2,1,1,0.0,0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, ins5555, 0,0));
        bitmapToDataPanel.add(bitmapToDataScrollPane, new GridBagConstraints(2,1,1,2,1.0,0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, ins5555, 0,0));
        // "vata" kvůli dataToBitmapScrollPane
        bitmapToDataPanel.add(new JLabel(), new GridBagConstraints(2,2,1,1,0.0,0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, ins5555, 0,25));
        c.add(bitmapToDataPanel, new GridBagConstraints(0,r,10,1,1.0,0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, ins0000, 0,0));
        r++;
        
        c.add(new JSeparator(JSeparator.HORIZONTAL), new GridBagConstraints(0,r,10,1,1.0,0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
                new Insets(15, 0, 0, 0), 0,0));
        r++;

        c.add(aboutSelectableLabel, new GridBagConstraints(0,r,10,1,1.0,0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, ins5555, 0,0));
    }

    private void initEventHandlers() {
        log.debug("");
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                log.info("close frame!");
                System.exit(0);
            }
        });
        
        dataToBitmapTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
                    if (textAreaUndoManager.canUndo()) {
                        //log.trace("textArea undo");
                        textAreaUndoManager.undo();
                    }
                }
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Y) {
                    if (textAreaUndoManager.canRedo()) {
                        //log.trace("textArea redo");
                        textAreaUndoManager.redo();
                    }
                }
            }
        });
        
        tabbedPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                BitmapEditorPanel selEdit = getSelectedBitmapEditorPanel();                
                if (selEdit == bitmapEditorPanel) {
                    log.debug("(tabbedPanel) -> bitmapEditorPanel");
                    if (dataToBitmapTextArea.getText().equals(DEFAULT_DATA_16)) {
                        dataToBitmapTextArea.setText(DEFAULT_DATA_8);
                        textAreaUndoManager.discardAllEdits();
                    }                    
                }
                else if (selEdit == bitmap2x2EditPanel) {
                    log.debug("(tabbedPanel) -> bitmap2x2EditPanel");
                    if (dataToBitmapTextArea.getText().equals(DEFAULT_DATA_8)) {
                        dataToBitmapTextArea.setText(DEFAULT_DATA_16);
                        textAreaUndoManager.discardAllEdits();
                    }                
                }
                bitmapToDataTextArea.setText("");
            }
        });

        // --- tlačítka data --> bitmap  a  bitmap --> data | soubor
        
        dataToBitmapBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("(dataToBitmapBtn click)");
                try {
                    dataToBitmap(dataToBitmapTextArea.getText());
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
        });        
           
        dataPasteAndFillBitmapBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("(dataPasteAndFillBitmapBtn click)");
                try {
                    dataPasteToTextAreaAndBitmap();
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(MainFrame.this,
                            ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }   
        });
       
        bitmapToDataTextAreaAndClipboardBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("(bitmapToDataTextAreaAndClipboardBtn click)");
                try {
                    bitmapToDataTextAreaAndClipboard();
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(MainFrame.this,
                            ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        bitmapToXbmFileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("(bitmapToXbmFileBtn click)");
                try {
                    bitmapToXbmFileWithFileDialog();
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(MainFrame.this,
                            ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    /**
     * Nastaní bitmapu z textových dat.
     * Pokud mají data špatný formát, zobrazí chybovou hlášku.
     * 
     * @return {@code true}, pokud se data do bitmapy podaří nastavit
     */
    private boolean dataToBitmap(String textData) {
        log.debug("");
        boolean ok = getSelectedBitmapEditorPanel().setDataFromText(
                textData);
        if (!ok) {
            log.info("Wrong data format. Data could not be analyzed.");
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Wrong data format. Data could not be analyzed.",
                    "Data error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return ok;
    }
    
    /**
     * 
     * @return 
     * @see #dataToBitmap(java.lang.String) 
     */
    private void dataPasteToTextAreaAndBitmap() {
        try {
            log.debug("(dataPasteAndFillBitmapBtn)");
            // -- clipboard -> data
            Transferable clipData = clipboard.getContents(MainFrame.this);
            String clipString = (String) clipData.getTransferData(
                    DataFlavor.stringFlavor);

            dataToBitmapTextArea.setText(clipString);

            // -- data -> bitmap
            dataToBitmap(clipString);

        } catch (UnsupportedFlavorException ex) {
            // nic !
            log.debug(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }         
    }
    
    /**
     * 
     * @return 
     */
    private void bitmapToDataTextAreaAndClipboard() {
        log.debug("");
        // -- bitmap -> data
        String data = getSelectedBitmapEditorPanel().getDataAsText();
        bitmapToDataTextArea.setText("DATA " + data);
        
        // -- opravdu vypsat na stdout
        System.out.println(data);

        // -- data -> clipboard
        clipboard.setContents(
                new StringSelection(bitmapToDataTextArea.getText()),
                null);
    }
            
    /** Dialog pro zadání cílového souboru pro uložení XBM. 
     * @see #createSaveXbmFileChooser() */
    private JFileChooser saveXbmFileChooser;
            
    /**
     * Vytvoří a inicializuje dialog pro výběr souboru pro uložení XBM souboru.
     * 
     * @return 
     */
    private static JFileChooser createSaveXbmFileChooser() {
        log.debug("");
        JFileChooser fileChooser = new JFileChooser();
        
        ExtFileFilter xbmExtFileFilter = new ExtFileFilter("XBM image (X11)", "xbm");
        fileChooser.addChoosableFileFilter(xbmExtFileFilter);
        //fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(xbmExtFileFilter);
        fileChooser.setCurrentDirectory(new File("."));

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);        
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        //fileChooser.setApproveButtonText("Save");
        fileChooser.setDialogTitle("Export bitmap to XBM image");
        
        log.debug("fileChooser initialized");
        return fileChooser;
    }
    
    /**
     * 
     * @param file
     * @param data
     * @throws IOException 
     * @see SquareBooleanBitmap#getDataAsXBM(java.lang.String) 
     */
    private void saveXbmFile(File file, String data) throws IOException {
        log.debug("");
        if (file == null) {
            throw new IllegalArgumentException("file = null");
        }
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("'data' is blank");
        }
        
        try(
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file), "UTF-8"));  // (doporučuje se vždy explicitně uvést kódování)
        ) {
            bw.write(data);
            bw.flush();
            log.info(file.getName() + " saved");
        }        
    }
    
    /**
     * Exportuje bitmapu do XBM souboru zadaného dialogem pro výběr souborů.
     * 
     * @see #createSaveXbmFileChooser() 
     * @see #saveXbmFile(java.io.File, java.lang.String) 
     */
    private void bitmapToXbmFileWithFileDialog() {
        log.debug("");
        if (saveXbmFileChooser == null) {
            saveXbmFileChooser = createSaveXbmFileChooser();
        }
        saveXbmFileChooser.setSelectedFile(new File("name.xbm"));

        if (saveXbmFileChooser.showSaveDialog(MainFrame.this) ==
                JFileChooser.APPROVE_OPTION) {

            // doplnit příponu .xbm, pokud ji soubor nemá
            File selectedFile = saveXbmFileChooser.getSelectedFile();
            String ext = FileUtils.getFileExtension(selectedFile);
            if (! ext.toLowerCase().equals("xbm")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".xbm");
            }

            // soubor již existuje, přepsat?
            if (selectedFile.exists() && !selectedFile.isDirectory()) {
                log.debug(selectedFile.getName() + " exists. Overwrite?");
                int res = JOptionPane.showConfirmDialog(MainFrame.this,
                        "File " + selectedFile.getName() + " already exists. Overwrite?", 
                        "Question", JOptionPane.YES_NO_OPTION);
                if (res != JOptionPane.YES_OPTION) {
                    log.info("canceled");
                    // (správně by se mělo vrátit na výběr souboru)
                    return;
                }
            }
            log.info("XBM: " + selectedFile.getAbsolutePath());
            
            String nameForXbm = FileUtils.getFileBaseName(selectedFile.getName());
            String xbmData = getSelectedBitmapEditorPanel().getDataAsXBM(nameForXbm);
            try {
                saveXbmFile(selectedFile, xbmData);
                
                JOptionPane.showMessageDialog(MainFrame.this,
                        selectedFile.getName() + " saved.", "Info", 
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                log.warn(ex.getMessage(), ex);
                JOptionPane.showMessageDialog(MainFrame.this,
                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            log.info("canceled");
        }
    }
    
    /**
     * 
     * @return 
     */
    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }
    
    @Override
    public void setVisible(boolean b) {
        log.info("" + b);
        super.setVisible(b);
    }

}   // MainFrame.java
