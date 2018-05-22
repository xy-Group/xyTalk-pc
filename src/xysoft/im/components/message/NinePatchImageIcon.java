package xysoft.im.components.message;


import javax.imageio.ImageIO;
import javax.swing.*;
import freeseawind.ninepatch.swing.SwingNinePatch;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class NinePatchImageIcon extends ImageIcon {
	
	private static final long serialVersionUID = -5004430700627593660L;
	
	private SwingNinePatch  mNinePatch;
	
	public NinePatchImageIcon(URL urlRes) {
		BufferedImage img = null;
		try {
		    img = ImageIO.read(urlRes);
		    mNinePatch = new SwingNinePatch(img);
		} catch (IOException e) {
		}
		
	}
	
	@Override
	public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
		
		int iCompWidth = c.getWidth();
		int iCompHeigth = c.getHeight();
		mNinePatch.drawNinePatch((Graphics2D) g, 0, 0,iCompWidth, iCompHeigth);
	}

}
