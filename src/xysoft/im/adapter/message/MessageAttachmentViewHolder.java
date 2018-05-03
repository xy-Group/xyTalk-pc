package xysoft.im.adapter.message;

import xysoft.im.components.Colors;
import xysoft.im.components.GradientProgressBarUI;
import xysoft.im.components.RCProgressBar;
import xysoft.im.components.SizeAutoAdjustTextArea;
import xysoft.im.components.message.AttachmentPanel;
import xysoft.im.components.message.RCAttachmentMessageBubble;
import xysoft.im.frames.MainFrame;
import xysoft.im.utils.FontUtil;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MessageAttachmentViewHolder extends BaseMessageViewHolder
{
    public SizeAutoAdjustTextArea attachmentTitle;
    public RCProgressBar progressBar = new RCProgressBar(); // 进度条
    public JPanel timePanel = new JPanel(); // 时间面板
    public JPanel messageAvatarPanel = new JPanel(); // 消息 + 头像组合面板
    public AttachmentPanel attachmentPanel = new AttachmentPanel(); // 附件面板
    public JLabel attachmentIcon = new JLabel(); // 附件类型icon
    public JLabel sizeLabel = new JLabel();
    public RCAttachmentMessageBubble messageBubble;

    public MessageAttachmentViewHolder()
    {
        initComponents();
        setListeners();
    }

    private void setListeners()
    {
        MouseAdapter listener = new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                messageBubble.setActiveStatus(true);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                messageBubble.setActiveStatus(false);
                super.mouseExited(e);
            }
        };

        attachmentPanel.addMouseListener(listener);
        attachmentTitle.addMouseListener(listener);

    }

    private void initComponents()
    {
        int maxWidth = (int) (MainFrame.getContext().currentWindowWidth * 0.427);
        attachmentTitle = new SizeAutoAdjustTextArea(maxWidth);

        timePanel.setBackground(Colors.WINDOW_BACKGROUND);
        messageAvatarPanel.setBackground(Colors.WINDOW_BACKGROUND);

        time.setForeground(Colors.FONT_GRAY);
        time.setFont(FontUtil.getDefaultFont(12));

        attachmentPanel.setOpaque(false);

        progressBar.setMaximum(100);
        progressBar.setMinimum(0);
        progressBar.setValue(100);
        progressBar.setUI(new GradientProgressBarUI());
        progressBar.setVisible(false);

        sizeLabel.setFont(FontUtil.getDefaultFont(12));
        sizeLabel.setForeground(Colors.FONT_GRAY);
    }
}
