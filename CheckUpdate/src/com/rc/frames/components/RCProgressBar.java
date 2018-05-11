package com.rc.frames.components;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;


public class RCProgressBar extends JProgressBar
{
    public RCProgressBar()
    {
        setForeground(Colors.PROGRESS_BAR_START);

        setBorder(new LineBorder(Colors.PROGRESS_BAR_END));
    }

    @Override
    protected void paintBorder(Graphics g)
    {
    }


    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(getWidth(), 6);
    }
}
