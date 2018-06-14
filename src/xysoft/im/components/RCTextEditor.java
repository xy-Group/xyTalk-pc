package xysoft.im.components;

import xysoft.im.components.message.FileEditorThumbnail;
import xysoft.im.frames.ImageViewerFrame;
import xysoft.im.utils.ClipboardUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class RCTextEditor extends JTextPane
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6403884724614197703L;

	@Override
    public void paste()
    {
        Object data = ClipboardUtil.paste();
        if (data instanceof String)
        {
            this.replaceSelection((String) data);
        }
        else if (data instanceof ImageIcon)
        {
            ImageIcon icon = (ImageIcon) data;
            adjustAndInsertIcon(icon);
        }
        else if (data instanceof java.util.List)
        {
            java.util.List<Object> list = (java.util.List<Object>) data;
            for (Object obj : list)
            {
                // 图像
                if (obj instanceof ImageIcon)
                {
                    adjustAndInsertIcon((ImageIcon) obj);
                }
                // 文件
                else if (obj instanceof String)
                {
                    FileEditorThumbnail thumbnail = new FileEditorThumbnail((String) obj);
                    this.insertComponent(thumbnail);
                }
            }
        }
    }

    /**
     * 插入图片到编辑框，并自动调整图片大小
     *
     * @param icon
     */
    private void adjustAndInsertIcon(ImageIcon icon)
    {
        String path = icon.getDescription();
        int iconWidth = icon.getIconWidth();
        int iconHeight = icon.getIconHeight();
        float scale = iconWidth * 1.0F / iconHeight;
        boolean needToScale = false;
        int max = 100;
        if (iconWidth >= iconHeight && iconWidth > max)
        {
            iconWidth = max;
            iconHeight = (int) (iconWidth / scale);
            needToScale = true;
        }
        else if (iconHeight >= iconWidth && iconHeight > max)
        {
            iconHeight = max;
            iconWidth = (int) (iconHeight * scale);
            needToScale = true;
        }

        JLabel label = new JLabel();
        if (needToScale)
        {
            ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));
            scaledIcon.setDescription(icon.getDescription());
            //this.insertIcon(scaledIcon);
            label.setIcon(scaledIcon);
        }
        else
        {
            //this.insertIcon(icon);
            label.setIcon(icon);
        }

        label.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                // 双击预览选中的图片
                if (e.getClickCount() == 2)
                {
                    ImageViewerFrame frame = new ImageViewerFrame(path);
                    frame.setVisible(true);
                }
                super.mouseClicked(e);
            }
        });

        insertComponent(label);

    }
}