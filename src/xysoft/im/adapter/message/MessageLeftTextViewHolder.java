package xysoft.im.adapter.message;

import xysoft.im.components.Colors;
import xysoft.im.components.GBC;
import xysoft.im.components.SizeAutoAdjustTextArea;
import xysoft.im.components.VerticalFlowLayout;
import xysoft.im.components.message.MessagePopupMenu;
import xysoft.im.components.message.RCLeftImageMessageBubble;
import xysoft.im.frames.MainFrame;
import xysoft.im.utils.FontUtil;

import javax.swing.*;
import java.awt.*;


public class MessageLeftTextViewHolder extends BaseMessageViewHolder
{
    public JLabel sender = new JLabel();
    //public JLabel avatar = new JLabel();
    //public JLabel size = new JLabel();
    //public RCLeftTextMessageBubble text = new RCLeftTextMessageBubble();

    public SizeAutoAdjustTextArea text;
    public RCLeftImageMessageBubble messageBubble = new RCLeftImageMessageBubble();

    private JPanel timePanel = new JPanel();
    private JPanel messageAvatarPanel = new JPanel();
    private MessagePopupMenu popupMenu = new MessagePopupMenu();

    public MessageLeftTextViewHolder()
    {
        initComponents();
        initView();
    }

    private void initComponents()
    {
        int maxWidth = (int) (MainFrame.getContext().currentWindowWidth * 0.5);
        text = new SizeAutoAdjustTextArea(maxWidth);
        text.setParseUrl(true);

        time.setForeground(Colors.FONT_GRAY);
        time.setFont(FontUtil.getDefaultFont(12));

        sender.setFont(FontUtil.getDefaultFont(12));
        sender.setForeground(Colors.FONT_GRAY);

        messageAvatarPanel.setBackground(Colors.WINDOW_BACKGROUND);
        timePanel.setBackground(Colors.WINDOW_BACKGROUND);
    }

    private void initView()
    {
        setLayout(new BorderLayout());
        timePanel.add(time);

        messageBubble.add(text);

        JPanel senderMessagePanel = new JPanel();
        senderMessagePanel.setBackground(Colors.WINDOW_BACKGROUND);
        senderMessagePanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0,0,true, false));
        senderMessagePanel.add(sender);
        senderMessagePanel.add(messageBubble);

        messageAvatarPanel.setLayout(new GridBagLayout());
        messageAvatarPanel.add(avatar, new GBC(1, 0).setWeight(1, 1).setAnchor(GBC.NORTH).setInsets(4,5,0,0));
        messageAvatarPanel.add(senderMessagePanel, new GBC(2, 0)
                .setWeight(1000, 1)
                .setAnchor(GBC.WEST)
                .setInsets(0,5,5,0));

        add(timePanel, BorderLayout.NORTH);
        add(messageAvatarPanel, BorderLayout.CENTER);
    }
}
