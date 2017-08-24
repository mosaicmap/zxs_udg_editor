/*
 * BitmapEditorPanel.java
 *
 *  created: 16.7.2017
 *  charset: UTF-8
 */
package cz.mp.zxs.tools.udg_editor;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * Editor {@linkplain SquareBooleanBitmap}.
 * 
 * @author Martin Pokorný
 * @see BitmapViewPanel
 * @see SquareBooleanBitmap
 */
public class BitmapEditorPanel extends JPanel {
    
    private SquareBooleanBitmap bmap;

    private BitmapViewPanel bmapView;
        
    // TODO ? BitmapEditPanel -- nějaké lepší undo,redo
    private NaiveUndoBuffer undoBuffer = new NaiveUndoBuffer();
    // prozkoumat:
    // - java
    //    javax.swing.undo.UndoManager, 
    //    java.swing.event.UndoableEditEvent, 
    //    java.swing.event.UndoableEditListener
    //        undoableEditHappened(UndoableEditEvent e)
    //    fireUndoableEditUpdate
    // - design pattern
    //    Command Pattern
    //    Memento pattern

    // -----
    
    private static final Color PREV_LIGHT = new Color(238,238,238);
    private static final Color PREV_DARK = new Color(51,51,51);

    private JPanel prevsPanel = new JPanel(new GridBagLayout());
    /** Zaregistrované náhledy. */
    private ArrayList<BitmapPreviewPanel> previewers = 
            new ArrayList<BitmapPreviewPanel>();

    private BitmapPreviewPanel prevOnLightPanel = new BitmapPreviewPanel();
    private BitmapPreviewPanel prevOnDarkPanel = new BitmapPreviewPanel();

    private JPanel bmHandlingBtnsPanel = new JPanel(new GridBagLayout());
    private JToggleButton bmDrawPointBtn = new JToggleButton("", true);
    private JToggleButton bmDrawLineBtn = new JToggleButton("", false);
    private JButton bmCleanBtn = new JButton("");
    private JButton bmUndoBtn = new JButton("");
    private JButton bmRedoBtn = new JButton("");
    private JButton bmInvertBtn = new JButton("");
    private JButton bmTurnRightBtn = new JButton("");
    private JButton bmTurnLeftBtn = new JButton("");
    private JButton bmMirrorVerticalBtn = new JButton("");
    private JButton bmMirrorHorizontalBtn = new JButton("");
    private JButton bmMoveRightBtn = new JButton("");
    private JButton bmMoveLeftBtn = new JButton("");
    private JButton bmMoveUpBtn = new JButton("");
    private JButton bmMoveDownBtn = new JButton("");
    
    // -----
    
    /**
     *
     */
    public enum DrawMode {
        POINT,
        LINE,
        // RECTANGLE
        // CIRCLE
        ;
    }

    private DrawMode drawMode = DrawMode.POINT;
    
    static int POINT_NOT_DEF = -1;
    private int startX = POINT_NOT_DEF;
    private int startY = POINT_NOT_DEF;

    // -----
    /**
     * 
     */
    public BitmapEditorPanel() {
        super();
                                
        initComponents();
        initLayout();
        initEventHandlers();
    }

            
    /** */
    private void initComponents() {
        bmap = new SquareBooleanBitmap();

        bmapView = new BitmapViewPanel();
        bmapView.setBmap(bmap);
        
        prevOnLightPanel.setColors(PREV_LIGHT, PREV_DARK);
        prevOnLightPanel.setBmap(bmap);
        previewers.add(prevOnLightPanel);

        prevOnDarkPanel.setColors(PREV_DARK, PREV_LIGHT);
        prevOnDarkPanel.setBmap(bmap);
        previewers.add(prevOnDarkPanel);

        
        setUpBmBtn(bmCleanBtn, "Clear", Images.getImage(Images.BTN_CLEAR));

        setUpBmBtn(bmDrawPointBtn, "Draw point", Images.getImage(Images.BTN_DRAW_POINT));
        setUpBmBtn(bmDrawLineBtn, "Draw line", Images.getImage(Images.BTN_DRAW_LINE));

        setUpBmBtn(bmUndoBtn, "Undo", Images.getImage(Images.BTN_UNDO));
        setUpBmBtn(bmRedoBtn, "Redo", Images.getImage(Images.BTN_REDO));

        setUpBmBtn(bmInvertBtn, "Invert", Images.getImage(Images.BTN_INVERT));
        setUpBmBtn(bmTurnRightBtn, "Turn right", Images.getImage(Images.BTN_TO_RIGHT));
        setUpBmBtn(bmTurnLeftBtn, "Turn left", Images.getImage(Images.BTN_TO_LEFT));
        setUpBmBtn(bmMirrorVerticalBtn, "Mirror vertical", Images.getImage(Images.BTN_MIRROR_VERTICAL));
        setUpBmBtn(bmMirrorHorizontalBtn, "Mirror horizontal", Images.getImage(Images.BTN_MIRROR_HORIZONTAL));
        setUpBmBtn(bmMoveLeftBtn, "Move left", Images.getImage(Images.BTN_MOVE_LEFT));
        setUpBmBtn(bmMoveRightBtn, "Move right", Images.getImage(Images.BTN_MOVE_RIGHT));
        setUpBmBtn(bmMoveUpBtn, "Move up", Images.getImage(Images.BTN_MOVE_UP));
        setUpBmBtn(bmMoveDownBtn, "Move down", Images.getImage(Images.BTN_MOVE_DOWN));
        
        ButtonGroup drawBg = new ButtonGroup();
        drawBg.add(bmDrawPointBtn);
        drawBg.add(bmDrawLineBtn);
        
        
        prevsPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        
        setUndoRedoBtnsEnabled();
    }

    /** */
    private void initLayout() {        
        this.setLayout(new GridBagLayout());

        Insets ins5555 = new Insets(5, 5, 5, 5);
        Insets ins1111 = new Insets(1, 1, 1, 1);
        Insets ins11x1 = new Insets(1, 1, 10, 1);
                
        int row = 0;

        int rbm=0;
        bmHandlingBtnsPanel.add(bmDrawPointBtn, new GridBagConstraints(0,rbm,1,1,0.0,0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, ins11x1, 0,0));                
        bmHandlingBtnsPanel.add(bmDrawLineBtn, new GridBagConstraints(1,rbm,1,1,0.0,0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, ins11x1, 0,0));
        rbm++;
        bmHandlingBtnsPanel.add(bmUndoBtn, new GridBagConstraints(0,rbm,1,1,0.0,0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, ins11x1, 0,0));                
        bmHandlingBtnsPanel.add(bmRedoBtn, new GridBagConstraints(1,rbm,1,1,0.0,0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, ins11x1, 0,0));
        rbm++;
        bmHandlingBtnsPanel.add(bmCleanBtn, new GridBagConstraints(0,rbm,1,1,0.0,0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, ins1111, 0,0));                
        bmHandlingBtnsPanel.add(bmInvertBtn, new GridBagConstraints(1,rbm,1,1,0.0,0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, ins1111, 0,0));                
        rbm++;
        bmHandlingBtnsPanel.add(bmMirrorHorizontalBtn, new GridBagConstraints(0,rbm,1,1,0.0,0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, ins1111, 0,0));                
        bmHandlingBtnsPanel.add(bmMirrorVerticalBtn, new GridBagConstraints(1,rbm,1,1,0.0,0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, ins1111, 0,0));                
        rbm++;
        bmHandlingBtnsPanel.add(bmTurnLeftBtn, new GridBagConstraints(0,rbm,1,1,0.0,0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, ins1111, 0,0));                
        bmHandlingBtnsPanel.add(bmTurnRightBtn, new GridBagConstraints(1,rbm,1,1,0.0,0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, ins1111, 0,0));                
        rbm++;
        bmHandlingBtnsPanel.add(bmMoveUpBtn, new GridBagConstraints(0,rbm,2,1,1.0,0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, ins1111, 0,0));                
        rbm++;
        bmHandlingBtnsPanel.add(bmMoveLeftBtn, new GridBagConstraints(0,rbm,1,1,0.0,0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, ins1111, 0,0));                
        bmHandlingBtnsPanel.add(bmMoveRightBtn, new GridBagConstraints(1,rbm,1,1,0.0,0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, ins1111, 0,0));                
        rbm++;
        bmHandlingBtnsPanel.add(bmMoveDownBtn, new GridBagConstraints(0,rbm,2,1,1.0,0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, ins1111, 0,0));                
        
        this.add(bmHandlingBtnsPanel, new GridBagConstraints(0,row,1,5,0.0,1.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.VERTICAL, ins5555, 0,0));        
        
        this.add(bmapView, new GridBagConstraints(1,row,5,5,0.0,1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, ins5555, 0,0));
        
        prevsPanel.add(prevOnLightPanel, new GridBagConstraints(0,1,1,1,0.0,0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, ins5555, 0,0));                
        prevsPanel.add(prevOnDarkPanel, new GridBagConstraints(0,2,1,1,0.0,0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, ins5555, 0,0));                
       
        this.add(prevsPanel, new GridBagConstraints(7,row,1,2,1.0,1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, ins5555, 0,0));                        
    }

//    private static Insets DEFAULT_BTN_MARGIN = new JButton().getMargin();
//    private static Insets BM_BTN_MARGIN = new Insets(
//            DEFAULT_BTN_MARGIN.top, 
//            DEFAULT_BTN_MARGIN.top, 
//            DEFAULT_BTN_MARGIN.bottom, 
//            DEFAULT_BTN_MARGIN.bottom);
    /**
     * 
     * @param btn
     * @param tooltip
     * @param icon 
     */
    private static void setUpBmBtn(AbstractButton btn, String tooltip, Icon icon) {
        btn.setIcon(icon);
        btn.setToolTipText(tooltip);
//        btn.setMargin(BM_BTN_MARGIN);
        btn.setIconTextGap(0);
    }
    
    /** */
    private void initEventHandlers() {

        // -- stisk ESC zruší startX a startY
        Action cancelStartPointAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                startX = startY = POINT_NOT_DEF;
                repaintBmap();
            }
        };
        KeyStroke escape = KeyStroke.getKeyStroke("ESCAPE");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        this.getActionMap().put("ESCAPE", cancelStartPointAction);
        // --

        // TODO kreslení bodů tažením

        bmapView.addMouseListener (new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
            public void mousePressed(MouseEvent e) {
                if (! SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }
                
                int ts = bmapView.getBitTileSize() 
                        + BitmapViewPanel.GRID_LINE_WIDTH;
                        
                int x = (e.getX()-1) / ts;
                int y = (e.getY()-1) / ts;
                
                if (drawMode == DrawMode.POINT) {
                    undoBuffer.addUndoableStep();
                    bmap.invertBit(x, y);
                    startX = startY = POINT_NOT_DEF;
                }
                else if (drawMode == DrawMode.LINE) {
                    if (startX == POINT_NOT_DEF && startY == POINT_NOT_DEF) {
                        startX = x;
                        startY = y;
                    }
                    else {
                        undoBuffer.addUndoableStep();
                        bmap.drawLine(startX, startY, x, y);
                        startX = startY = POINT_NOT_DEF;
                    }
                }
                else {
                    System.err.println("drawMode = " + drawMode); 
                    throw new IllegalStateException("unsupported drawMode");
                }
                
                // (pozn. ideální by bylo překreslit jen změnu, ale to nejde, aspoň ne jednoduše)
                repaintBmap();
                fireBmapChangeEvent();
            }
        
        } );
        
        // -----
        bmCleanBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });        
        bmUndoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });
        bmRedoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        });
        
        bmDrawPointBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawMode = DrawMode.POINT;
            }
        });
        bmDrawLineBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawMode = DrawMode.LINE;
            }
        });        
        
        bmInvertBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                invert();
            }
        });
        bmTurnRightBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turnRight();
            }
        });
        bmTurnLeftBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turnLeft();
            }
        });
        bmMirrorHorizontalBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mirrorHorizontal();
            }
        });
        bmMirrorVerticalBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mirrorVertical();
            }
        });
        bmMoveLeftBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveToLeft();
            }
        });
        bmMoveRightBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveToRight();
            }
        });
        bmMoveUpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveToUp();
            }
        });
        bmMoveDownBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveToDown();
            }
        });        
    }
    
    /**
     * 
     * @param bitTileSize 
     * @throws IllegalArgumentException
     */
    public void setBitTileSize(int bitTileSize) {
        bmapView.setBitTileSize(bitTileSize);
        
        repaintBmap();
        //fireBmapChangeEvent();
    }
    
    /**
     * 
     * @param dimChars  Délka strany ve znacích 8x8. 
     *      1 pro 1x1, 2 pro 2x2, 3 pro 3x3, ...
     *      Min 1, Max 10
     * @throws IllegalArgumentException  
     */
    public void setDimensionInChars(int dimChars) {
        setDimensionInCharsImpl(dimChars);
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
        
        bmap.setDimensionInCharsAndClear(dimChars);        
        for (BitmapPreviewPanel previewer : previewers) {            
            previewer.setBmap(bmap);            
        }
        
        repaintBmap();
        fireBmapChangeEvent();
    }

    /**
     *
     * @return
     */
    public int getDimensionInChars() {
        return bmap.getDimensionInChars();
    }

    // -----
    
    private void clear() {
        undoBuffer.addUndoableStep();
        bmap.clearAll();
        repaintBmap();
        fireBmapChangeEvent();
    }

    private void invert() {
        undoBuffer.addUndoableStep();
        bmap.invertAll();
        repaintBmap();
        fireBmapChangeEvent();
    }

    private void turnRight() {
        undoBuffer.addUndoableStep();
        bmap.turnRight();
        repaintBmap();
        fireBmapChangeEvent();
    }
    
    private void turnLeft() {
        undoBuffer.addUndoableStep();
        bmap.turnLeft();
        repaintBmap();
        fireBmapChangeEvent();
    }

    private void mirrorHorizontal() {
        undoBuffer.addUndoableStep();
        bmap.mirrorHorizontal();
        repaintBmap();
        fireBmapChangeEvent();
    }
    
    private void mirrorVertical() {
        undoBuffer.addUndoableStep();
        bmap.mirrorVertical();
        repaintBmap();
        fireBmapChangeEvent();
    }

    private void moveToLeft() {
        undoBuffer.addUndoableStep();
        bmap.moveToLeft();
        repaintBmap();
        fireBmapChangeEvent();
    }

    private void moveToRight() {
        undoBuffer.addUndoableStep();
        bmap.moveToRight();
        repaintBmap();
        fireBmapChangeEvent();
    }
            
    private void moveToDown() {
        undoBuffer.addUndoableStep();
        bmap.moveToDown();
        repaintBmap();
        fireBmapChangeEvent();
    }
    
    private void moveToUp() {
        undoBuffer.addUndoableStep();
        bmap.moveToUp();
        repaintBmap();
        fireBmapChangeEvent();
    }

    // -----
    
    /**
     * Získá data bitmapy jako desítová čísla v texové podobě všech řádků znaku.
     * <p>
     * hodnoty jsou v pořadí znaků:
     * <tt><pre>
     * totalNumOfChars = 0:  (číslo v 'matici' číslo znaku)
     *  0
     * totalNumOfChars = 1:
     *  0 1
     *  2 3
     * totalNumOfChars = 1:
     *  0 1 2
     *  3 4 5
     *  6 7 8
     * ...
     * </pre></tt>
     * Hodnoty v každém znaku jsou brány po řádcích od shora.
     * 
     * @return 
     */
    public String getDataAsText() {
        return bmap.getDataLinesAsText();
    }
    
    /**
     * 
     * @param data data desítkově oddělená čárkou; 
     *      např. {@code DATA 1,2,4,0,55,90,128,254} 
     *      nebo {@code 1,2,4,0,55,90,128,254}
     *      nebo {@code 8010 DATA 1,2,4,0,55,90,128,254}
     *      nebo {@code DATA 1,2,4,0,55,90,128,254, 24,60,126,219,255,36,90,165}
     *      ...
     * @return  {@code false}, pokud jsou data zadána úplně špatně;
     *      jinak {@code true}
     */
    public boolean setDataFromText(String data) {
        SquareBooleanBitmap origBmap = new SquareBooleanBitmap(bmap);
        boolean result = bmap.setDataFromText(data);
        if (result) {
            undoBuffer.addUndoableStep(origBmap);
            repaintBmap();
            fireBmapChangeEvent();
        }
        return result;
    }    

    // viz repaintBmap()
    @Override
    public void repaint() {
        super.repaint();

        if(bmapView != null) {
            repaintBmap();
        }
    }
    
    /**
     * 
     */
    private void repaintBmap() {
        bmapView.setStartPoint(startX, startY);
        bmapView.repaint();
        
        repaintPreviewers();
    }
            
    /**
     * 
     */
    private void repaintPreviewers() {
        if (previewers == null) {
            return;
        }
        for (BitmapPreviewPanel previewer : previewers) {            
            previewer.repaint();
        }
    }

    /**
     * 
     * @param newBmap 
     */
    private void setNewBmapAndRepaint(SquareBooleanBitmap newBmap) {
        this.bmap = newBmap;
        bmapView.setBmap(bmap);
        for (BitmapPreviewPanel previewer : previewers) {
            previewer.setBmap(bmap);
        }
        repaintBmap();
        fireBmapChangeEvent();
    }
    
    // ----- ChangeEvent...

    private EventListenerList bmapChangeListeners = new EventListenerList();
    
    public void addBmapChangeListener(ChangeListener listener) {
         bmapChangeListeners.add(ChangeListener.class, listener);
    }
    public void removeBmapChangeListener(ChangeListener listener) {
         bmapChangeListeners.remove(ChangeListener.class, listener);
    }
    
    private ChangeEvent bmpaChangeEvent = new ChangeEvent(this);
    private void fireBmapChangeEvent() { 
        fireBmapChangeEvent(bmpaChangeEvent);
    }
    private void fireBmapChangeEvent(ChangeEvent changeEvent) {
         Object[] listeners = bmapChangeListeners.getListenerList();
         for (int i = listeners.length-2; i>=0; i-=2) {
              if (listeners[i]==ChangeListener.class) {
                    ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
              }            
         }
    }
    
    // ----- undo / redo

    /**
     * 
     */
    private void setUndoRedoBtnsEnabled() {
        bmUndoBtn.setEnabled(canUndo());
        bmRedoBtn.setEnabled(canRedo());
    }
    
    public void undo() {
        undoBuffer.undo();
    }
    
    public boolean canUndo() {
        return undoBuffer.canUndo();
    }

    public void redo() {
        undoBuffer.redo();
    }

    public boolean canRedo() {
        return undoBuffer.canRedo();
    }
    
    // (pro miniaturní bitmapy a malý počet max. kroků, by nemělo zatěžovat paměť)
    // -------------------------------------------------------------------------
    /**
     * 
     */
    private class NaiveUndoBuffer {
        private static final int MAX_UNDO_STEPS = 32;
        private LinkedList<SquareBooleanBitmap> undoSteps = 
                new LinkedList<SquareBooleanBitmap>();
        private LinkedList<SquareBooleanBitmap> redoSteps = 
                new LinkedList<SquareBooleanBitmap>();
        
        public void addUndoableStep() {
            addUndoableStep(new SquareBooleanBitmap(bmap));
            
            redoSteps.clear();
            
            setUndoRedoBtnsEnabled();
        }

        public void addUndoableStep(SquareBooleanBitmap sbm) {
            addToFirstInUndoSteps(sbm);
            
            redoSteps.clear();
            
            setUndoRedoBtnsEnabled();
        }
                
        private void addToFirstInUndoSteps(SquareBooleanBitmap bm) {
            undoSteps.addFirst(bm);
            if (undoSteps.size() > MAX_UNDO_STEPS) {
                undoSteps.removeLast();
            }            
        }

        private void addToFirstInRedos(SquareBooleanBitmap bm) {
            redoSteps.addFirst(bm);
            if (redoSteps.size() > MAX_UNDO_STEPS) {
                redoSteps.removeLast();
            }            
        }

        public void undo() {
            if (undoSteps.size() > 0) {
                addToFirstInRedos(new SquareBooleanBitmap(bmap));
                        
                SquareBooleanBitmap prevBmap = undoSteps.removeFirst();
                
                setNewBmapAndRepaint(prevBmap);
                
                setUndoRedoBtnsEnabled();
            }
        }

        public boolean canUndo() {
            return undoSteps.size() > 0;
        }

        public void redo() {
            if (redoSteps.size() > 0) {
                addToFirstInUndoSteps(new SquareBooleanBitmap(bmap));

                SquareBooleanBitmap nextBmap = redoSteps.removeFirst();
                
                setNewBmapAndRepaint(nextBmap);
                
                setUndoRedoBtnsEnabled();
            }
        }

        public boolean canRedo() {
            return redoSteps.size() > 0;
        }

        public void reset() {
            undoSteps.clear();
            redoSteps.clear();
        }
    }   // NaiveUndoBuffer
    
}
