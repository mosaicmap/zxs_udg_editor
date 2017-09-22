/*
 * BitmapPreviewPanel.java
 *
 *  created: 18.7.2017
 *  charset: UTF-8
 */

package cz.mp.zxs.tools.udg_editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.UIManager;


/**
 * Zobrazení malého náhledu {@linkplain SquareBooleanBitmap} v GUI.
 * 
 * @author Martin Pokorný
 * @see SquareBooleanBitmap
 */
public class BitmapPreviewPanel extends JPanel {

    private SquareBooleanBitmap bmap;
        
    private static final int DEFAULT_TILE_SIZE = 2;
    private int bitTileSize = DEFAULT_TILE_SIZE;
    
    private static final int DEFAULT_MARGIN_SIZE = 16;
    private int marginSize = DEFAULT_MARGIN_SIZE;
    
    private int minComponentSideSize;
    private Dimension minComponentDimension;

    private Color colorOfBackground = UIManager.getColor("Panel.background");
    private Color colorOfOne = UIManager.getColor("Panel.foreground");

    
    /** 
     */
    public BitmapPreviewPanel() {
        this.bmap = new SquareBooleanBitmap();
        computeMinSize();
    }

    /**
     * 
     * @param marginSize 
     */
    public BitmapPreviewPanel(int marginSize) {
        this.bmap = new SquareBooleanBitmap();
        this.marginSize = marginSize;
        computeMinSize();
    }

    /**
     * 
     * @param bmap 
     */
    public void setBmap(SquareBooleanBitmap bmap) {
        this.bmap = bmap;
        computeMinSize();
    }

    /**
     * 
     * @param background
     * @param one 
     */
    public void setColors(Color background, Color one) {
        this.colorOfBackground = background;
        this.colorOfOne = one;
    }
    
    
    private void computeMinSize() {
        minComponentSideSize = bitTileSize * bmap.getDimensionInPxs()
                + 2 * marginSize;
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
    
    @Override
    public void repaint() {
        super.repaint();
    }
    
    @Override
    protected void paintComponent (Graphics g)  {
        //super.paintComponent(g);
        paintBackground(g);
        paintBitMap(g);        
    }

    private void paintBackground(Graphics g)  {
        g.setColor(colorOfBackground);
        g.fillRect(0, 0, getWidth()-1, getHeight()-1);      
    }
      
    private void paintBitMap(Graphics g) {
        if (bmap == null) {
            return;
        }
        int dimPxs = bmap.getDimensionInPxs();
        
        for (int x=0; x<=dimPxs-1; x++) {
            for (int y=0; y<=dimPxs-1; y++) {
                if(this.bmap.getBit(x, y)) {
                    g.setColor(colorOfOne);
                }
                else {
                    g.setColor(colorOfBackground);
                }
                g.fillRect(marginSize + x * bitTileSize,
                        marginSize + y * bitTileSize,
                        bitTileSize, 
                        bitTileSize);
            }
        }
    }

}   // BitmapPreviewPanel.java
