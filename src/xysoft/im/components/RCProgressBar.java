package xysoft.im.components;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;


public class RCProgressBar extends JProgressBar
{
    /**
	 * 进度栏
	 */
	private static final long serialVersionUID = -4724729155099598069L;


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
