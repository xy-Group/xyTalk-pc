package xysoft.im.panels;

import xysoft.im.adapter.RoomItemViewHolder;
import xysoft.im.adapter.RoomItemsAdapter;
import xysoft.im.app.Launcher;
import xysoft.im.components.*;
import xysoft.im.db.model.Room;
import xysoft.im.db.service.RoomService;
import xysoft.im.entity.RoomItem;
import xysoft.im.utils.DebugUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class RoomsPanel extends ParentAvailablePanel
{
    /**
	 * 联系人、群组、公共对象列表
	 */
	private static final long serialVersionUID = -5088783889048106813L;

	private static RoomsPanel context;
    private RCListView roomsView;
    private List<RoomItem> roomItemList = new ArrayList<>();
    private RoomService roomService = Launcher.roomService;
    private static long updateTimestampLast = 0;

    public RoomsPanel(JPanel parent)
    {
        super(parent);
        context = this;

        initComponents();
        initView();
        initData();
        roomsView.setAdapter(new RoomItemsAdapter(roomItemList));
    }

    private void initComponents()
    {
        roomsView = new RCListView();
    }

    private void initView()
    {
        setLayout(new GridBagLayout());
        roomsView.setContentPanelBackground(Colors.DARK);
        add(roomsView, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1, 1));
        //add(scrollPane, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1, 1));
    }

    private void initData()
    {
        roomItemList.clear();

        // TODO: 从数据库中加载房间列表

        List<Room> rooms = roomService.findAll();
        for (Room room : rooms)
        {
            RoomItem item = new RoomItem();
            item.setRoomId(room.getRoomId());
            item.setTimestamp(room.getLastChatAt());
            item.setTitle(room.getName());
            item.setType(room.getType());
            item.setLastMessage(room.getLastMessage());
            item.setUnreadCount(room.getUnreadCount());

            roomItemList.add(item);
        }
    }

    /**
     * 重绘整个列表
     */
    public void notifyDataSetChanged(boolean keepSize)
    {
    	//十秒钟之内的更新，不刷新
    	long updateTimestamp = System.currentTimeMillis();
    	if (updateTimestamp - updateTimestampLast > 10000){
    		initData();
            roomsView.notifyDataSetChanged(keepSize);
            DebugUtil.debug("roomsView重绘整个列表");          
            updateTimestampLast = System.currentTimeMillis();
    	}

    }

    /**
     * 更新房间列表
     * 当这条消息所在的房间在当前房间列表中排在第一位时，此时房间列表项目顺序不变，无需重新排列
     * 因此无需更新整个房间列表，只需更新第一个项目即可
     *
     * @param msgRoomId
     */
    public void updateRoomsList(String msgRoomId)
    {
        String roomId = (String) ((RoomItemViewHolder) (roomsView.getItem(0))).getTag();
        if (roomId.equals(msgRoomId))//当第一项时更新
        {
            Room room = roomService.findById(roomId);
            for (RoomItem roomItem : roomItemList)
            {
                if (roomItem.getRoomId().equals(roomId))
                {
                    roomItem.setUnreadCount(room.getUnreadCount());
                    roomItem.setTimestamp(room.getLastChatAt());
                    roomItem.setLastMessage(room.getLastMessage());
                    break;
                }
            }

            roomsView.notifyItemChanged(0);
        }
        else //其他项时更新全部？
        {
            notifyDataSetChanged(false);
        }
    }

    /**
     * 更新指定位置的房间项目
     * @param roomId
     */
    public void updateRoomItem(String roomId)
    {
        if (roomId == null || roomId.isEmpty())
        {
            notifyDataSetChanged(true);
            return;
        }

        for (int i = 0; i < roomItemList.size(); i++)
        {
            RoomItem item = roomItemList.get(i);
            if (item.getRoomId().equals(roomId))
            {
                Room room = roomService.findById(item.getRoomId());
                if (room != null)
                {
                    item.setLastMessage(room.getLastMessage());
                    item.setTimestamp(room.getLastChatAt());
                    item.setUnreadCount(room.getUnreadCount());
                    roomsView.notifyItemChanged(i);
                }
                break;
            }
        }
    }
    
    public void updateRoomItemAddUnread(String roomId,String msg)
    {
        if (roomId == null || roomId.isEmpty())
        {
            notifyDataSetChanged(true);
            return;
        }

        for (int i = 0; i < roomItemList.size(); i++)
        {
            RoomItem item = roomItemList.get(i);
            if (item.getRoomId().equals(roomId))
            {
                Room room = roomService.findById(item.getRoomId());
                if (room != null)
                {
                    item.setLastMessage(msg);
                    item.setUnreadCount(room.getUnreadCount()+1);
                    roomsView.notifyItemChanged(i);
                }
                break;
            }
        }
    }

    /**
     * 激活指定的房间项目
     * @param position
     */
    public void activeItem(int position)
    {
        RoomItemViewHolder holder = (RoomItemViewHolder) roomsView.getItem(position);
        setItemBackground(holder, Colors.ITEM_SELECTED);
    }

    /**
     * 设置每个房间项目的背影色
     * @param holder
     * @param color
     */
    private void setItemBackground(RoomItemViewHolder holder, Color color)
    {
        holder.setBackground(color);
        holder.nameBrief.setBackground(color);
        holder.timeUnread.setBackground(color);
    }

    public static RoomsPanel getContext()
    {
        return context;
    }
}
