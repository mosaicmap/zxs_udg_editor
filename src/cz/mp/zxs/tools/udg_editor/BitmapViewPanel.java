/*
 * BitmapViewPanel.java
 *
 *  created: 16.7.2017
 *  charset: UTF-8
 */
package cz.mp.zxs.tools.udg_editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.UIManager;


/**
 * Zobrazení {@linkplain SquareBooleanBitmap} v mřížce.
 * 
 * @author Martin Pokorný
 * @see SquareBooleanBitmap
 * @see BitmapPreviewPanel
 */
public class BitmapViewPanel extends JPanel {
//    private static final Logger log = LoggerFactory.getLogger(BitmapViewPanel.class);
    
    private SquareBooleanBitmap bmap;
    
    private static final int DEFAULT_TILE_SIZE = 24;
    /** Velikost jednoho bitu bitmapy v klikacím panelu */
    private int bitTileSize = DEFAULT_TILE_SIZE;
    
    public static final int GRID_LINE_WIDTH = 1;
    
    private static final Color DEFAULT_COLOR_OF_ZERO = UIManager.getColor("Panel.background");
    private static final Color DEFAULT_COLOR_OF_ONE = UIManager.getColor("Panel.foreground");
    
    private Color colorOfZero = DEFAULT_COLOR_OF_ZERO;
    private Color colorOfOne = DEFAULT_COLOR_OF_ONE;

    private static final Color COLOR_OF_GRID = Color.LIGHT_GRAY;
    private static final Color COLOR_OF_GRID_8x8 = Color.DARK_GRAY;
    
    
    private int minComponentSideSize;
    private Dimension minComponentDimension;
    
    // viz DrawMode.LINE
    private int startX = -1;
    private int startY = -1;

    
    /** */
    public BitmapViewPanel() {
        this.bmap = new SquareBooleanBitmap();
        computeMinSize();
    }

    public void setBmap(SquareBooleanBitmap bmap) {
        this.bmap = bmap;
        computeMinSize();
    }

    public void setColorOfOne(Color colorOfOne) {
        this.colorOfOne = colorOfOne;
    }
        
    /**
     * 
     */
    private void computeMinSize() {
        minComponentSideSize = bitTileSize * bmap.getDimensionInPxs()
                + (bmap.getDimensionInPxs() + 1) * GRID_LINE_WIDTH;
        minComponentDimension = 
                new Dimension(minComponentSideSize,minComponentSideSize);
    }
    
    @Override
    public Dimension getMinimumSize() {
        return minComponentDimension;
    }

    @Override
    public Dimension getPreferredSize() {
        return minComponentDimension;
    }

    @Override
    public Dimension getSize() {
        return minComponentDimension;
    }

    /**
     * 
     * @param bitTileSize 
     */
    public void setBitTileSize(int bitTileSize) {
        if (bitTileSize <= 1) {
            throw new IllegalArgumentException("bitTileSize <= 1 !");
        }
        this.bitTileSize = bitTileSize;
        
        computeMinSize();
    }

    /**
     * 
     * @return 
     */
    public int getBitTileSize() {
        return bitTileSize;
    }
    
    // viz DrawMode.LINE
    /**
     * 
     * @param startX
     * @param startY 
     */
    void setStartPoint(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
    }
    
    @Override
    protected void paintComponent(Graphics g)  {
        super.paintComponent(g);
        if (bmap == null) {
            return;
        }
        paintGrid(g);
        paintBitMap(g);
        paintDrawStartPoint(g);
    }     

    /**
     * Vykreslí orientační mřížku pro bitmapu.
     * 
     * @param g 
     * @see #paint(Graphics)
     */
    private void paintGrid(Graphics g)  {
        if (GRID_LINE_WIDTH <= 0) {
            return;
        }
        
        g.setColor(COLOR_OF_GRID);
        
        int dimPxs = bmap.getDimensionInPxs();
        
        // mřížka
        int gridSp = bitTileSize + GRID_LINE_WIDTH;
        for (int i=0; i<=dimPxs; i++)  {
            for (int j=0; j<=dimPxs; j++)  {
                for (int k=0; k<GRID_LINE_WIDTH; k++) {
                    int y = j * gridSp + k;
                    int x = i * gridSp + k;
                    g.drawLine (0, y, x, y);    // |
                    g.drawLine (y, 0, y, x);    // --
                }
            }
        }
        
        // mřížka 8x8 (CHAR_SIZExCHAR_SIZE)
        g.setColor(COLOR_OF_GRID_8x8);
        for (int i=0; i<=dimPxs; i+=SquareBooleanBitmap.CHAR_SIZE)  {
            for (int j=0; j<=dimPxs; j+=SquareBooleanBitmap.CHAR_SIZE)  {
                for (int k=0; k<GRID_LINE_WIDTH; k++) {
                    int y = j * gridSp + k;
                    int x = i * gridSp + k;
                    g.drawLine (0, y, x, y);    // |
                    g.drawLine (y, 0, y, x);    // --
                }                
            }
        }
    }
    
    /**
     * Vykreslí hodnoty bitmapy.
     * 
     * @param g 
     */
    private void paintBitMap(Graphics g)  {
        int dimPxs = bmap.getDimensionInPxs();
        
        int gridSp = bitTileSize + GRID_LINE_WIDTH;
        for (int x=0; x<=dimPxs-1; x++)  {
            for (int y=0; y<=dimPxs-1; y++)  {
                //if(bitmap[x][y]) {
                if (bmap.getBit(x, y)) {
                    g.setColor(colorOfOne);
                }
                else {
                    g.setColor(colorOfZero);
                }
                g.fillRect(x * gridSp + GRID_LINE_WIDTH, 
                        y * gridSp + GRID_LINE_WIDTH, 
                        bitTileSize, 
                        bitTileSize);
            }
        }        
    }
    
    // viz DrawMode.LINE
    /**
     * Vykreslí počáteční bod při kreslení přímky.
     * 
     * @param g 
     */
    private void paintDrawStartPoint(Graphics g)  {
        if (startX != BitmapEditorPanel.POINT_NOT_DEF 
                && startY != BitmapEditorPanel.POINT_NOT_DEF) {
            int gridSp = bitTileSize + GRID_LINE_WIDTH;
            g.setColor(Color.BLUE);
            g.fillRect(startX * gridSp + GRID_LINE_WIDTH, 
                    startY * gridSp + GRID_LINE_WIDTH, 
                    bitTileSize, bitTileSize);
        }
    }
    
    @Override
    public void repaint() {
        super.repaint();
    }
    
}   // BitmapViewPanel.java
