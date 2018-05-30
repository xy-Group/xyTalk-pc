package xysoft.im.components;

import javax.swing.JTextArea;

import java.awt.Dimension;

/**
 * Creates a simple Wrappable label to display Multi-Line Text.
 *
 * @author Derek DeMoro
 */
public class WrappedLabel extends JTextArea {
    private static final long serialVersionUID = 177528477205607705L;

    /**
     * Create a simple Wrappable label.
     */
    public WrappedLabel() {
        this.setEditable(false);
        this.setWrapStyleWord(true);
        this.setLineWrap(true);
        this.setOpaque(false);
    }

    public Dimension getPreferredSize() {
        final Dimension size = super.getPreferredSize();
        size.width = 0;
        return size;
    }
}