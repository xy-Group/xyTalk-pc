package xysoft.im.utils;

import java.awt.Color;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import xysoft.im.components.Colors;
import xysoft.im.listener.AbstractMouseListener;

public class SwingAnimation {
 
	/*
	 * 鼠标字体浮动动态效果
	 */
	public static AbstractMouseListener foreAnimationMouse(final JComponent obj) {
		return new AbstractMouseListener()
        {

    		Color colorOriginal = null;
            @Override
            public void mouseEntered(MouseEvent e)
            {
            	colorOriginal=obj.getForeground();
            	obj.setForeground(Colors.FONT_GRAY);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
            	obj.setForeground(colorOriginal);
                super.mouseExited(e);
            }
        };
	}
	
	/*
	 * 鼠标字体（背景）浮动动态效果
	 */
	public static AbstractMouseListener backAnimationMouse(final JComponent obj) {
		return new AbstractMouseListener()
        {
    		Color colorOriginal = null;
            @Override
            public void mouseEntered(MouseEvent e)
            {
            	colorOriginal=obj.getBackground();
            	obj.setBackground(Colors.SHADOW);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
            	obj.setBackground(colorOriginal);
                super.mouseExited(e);
            }
        };
	}
	
	public static AbstractMouseListener backLightAnimationMouse(final JComponent obj) {
		return new AbstractMouseListener()
        {
    		Color colorOriginal = null;
            @Override
            public void mouseEntered(MouseEvent e)
            {
            	colorOriginal=obj.getBackground();
            	obj.setBackground(Colors.FONT_WHITE);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
            	obj.setBackground(colorOriginal);
                super.mouseExited(e);
            }
        };
	}
}
