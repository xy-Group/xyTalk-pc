package xysoft.im.components.message;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;


public interface RCMessageBubble
{
    void addMouseListener(MouseListener l);

    void setBackgroundIcon(Icon icon);

    NinePatchImageIcon getBackgroundNormalIcon();

    NinePatchImageIcon getBackgroundActiveIcon();
}
