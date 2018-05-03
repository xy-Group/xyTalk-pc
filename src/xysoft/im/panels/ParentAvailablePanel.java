package xysoft.im.panels;

import javax.swing.*;


public class ParentAvailablePanel extends JPanel
{
    /**
	 * 控件可和父容器通讯
	 */
	private static final long serialVersionUID = 2576301643948392457L;
	private JPanel parent;

    public ParentAvailablePanel(JPanel parent)
    {
        this.parent = parent;
    }

    public JPanel getParentPanel()
    {
        return parent;
    }
}
