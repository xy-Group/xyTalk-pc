package xysoft.im.command;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import xysoft.im.adapter.RoomItemViewHolder;
import xysoft.im.app.Launcher;
import xysoft.im.components.Colors;
import xysoft.im.db.model.Room;
import xysoft.im.db.service.RoomService;
import xysoft.im.entity.RoomItem;
import xysoft.im.listener.AbstractMouseListener;
import xysoft.im.panels.ChatPanel;

public class RoomItemCommand {

    private RoomService roomService = Launcher.roomService;
	public RoomItemCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public static AbstractMouseListener commad(List<RoomItemViewHolder> viewHolders,
			 RoomItemViewHolder selectedViewHolder,
										RoomItemViewHolder viewHolder, 
										RoomItem item) {
		return new AbstractMouseListener()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {

                    if (selectedViewHolder != viewHolder)
                    {
                        // 进入房间
                        enterRoom(item.getRoomId());

                        for (RoomItemViewHolder holder : viewHolders)
                        {
                            if (holder != viewHolder)
                            {
                                setBackground(holder, Colors.DARK);
                            }
                        }

                        //setBackground(viewHolder, Colors.ITEM_SELECTED);
                        //selectedViewHolder = viewHolder;
                    }
                }
            }


            @Override
            public void mouseEntered(MouseEvent e)
            {
                if (selectedViewHolder != viewHolder)
                {
                    setBackground(viewHolder, Colors.ITEM_SELECTED_DARK);
                }
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                if (selectedViewHolder != viewHolder)
                {
                    setBackground(viewHolder, Colors.DARK);
                }
            }
        };
	}


	
	
    private static void enterRoom(String roomId)
    {
        // 加载房间消息
        ChatPanel.getContext().enterRoom(roomId);

        //TitlePanel.getContext().hideRoomMembersPanel();
        /*RoomMembersPanel.getContext().setRoomId(roomId);
        if (RoomMembersPanel.getContext().isVisible())
        {
            RoomMembersPanel.getContext().updateUI();
        }*/
    }
    
    private static void setBackground(RoomItemViewHolder holder, Color color)
    {
        holder.setBackground(color);
        holder.nameBrief.setBackground(color);
        holder.timeUnread.setBackground(color);
    }

}
