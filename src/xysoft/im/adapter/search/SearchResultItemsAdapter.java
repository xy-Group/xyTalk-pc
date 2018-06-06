package xysoft.im.adapter.search;

import xysoft.im.adapter.BaseAdapter;
import xysoft.im.app.Launcher;
import xysoft.im.cache.UserCache;
import xysoft.im.components.Colors;
import xysoft.im.db.model.CurrentUser;
import xysoft.im.db.model.FileAttachment;
import xysoft.im.db.model.Message;
import xysoft.im.db.model.Room;
import xysoft.im.db.service.CurrentUserService;
import xysoft.im.db.service.FileAttachmentService;
import xysoft.im.db.service.MessageService;
import xysoft.im.db.service.RoomService;
import xysoft.im.entity.SearchResultItem;
import xysoft.im.panels.ChatPanel;
import xysoft.im.panels.ListPanel;
import xysoft.im.panels.SearchPanel;
import xysoft.im.service.ChatService;
import xysoft.im.helper.AttachmentIconHelper;
import xysoft.im.listener.AbstractMouseListener;
import xysoft.im.tasks.DownloadTask;
import xysoft.im.tasks.HttpResponseListener;
import xysoft.im.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * 搜索结果适配器
 */
public class SearchResultItemsAdapter extends BaseAdapter<SearchResultItemViewHolder>
{
    private List<SearchResultItem> searchResultItems;
    private String keyWord;
    private RoomService roomService = Launcher.roomService;
    private SearchMessageOrFileListener searchMessageOrFileListener;

    public static final int VIEW_TYPE_CONTACTS_ROOM = 0;
    public static final int VIEW_TYPE_MESSAGE = 1;
    public static final int VIEW_TYPE_FILE = 2;
    private MessageService messageService = Launcher.messageService;
    private AttachmentIconHelper attachmentIconHelper = new AttachmentIconHelper();
    private FileCache fileCache = new FileCache();
    private FileAttachmentService fileAttachmentService = Launcher.fileAttachmentService;


    private List<String> downloadingFiles = new ArrayList<>(); // 正在下载的文件

    //private List<SearchResultFileItemViewHolder> fileItemViewHolders = new ArrayList<>();
    private Map<String, SearchResultFileItemViewHolder> fileItemViewHolders = new HashMap<>();


    public SearchResultItemsAdapter(List<SearchResultItem> searchResultItems)
    {
        this.searchResultItems = searchResultItems;
    }

    @Override
    public int getCount()
    {
        return searchResultItems.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        // return super.getItemViewType(position);
        String type = searchResultItems.get(position).getType();
        if (type.equals("s") || type.equals("m") || type.equals("q") || type.equals("searchMessage") || type.equals("searchFile"))
        {
            return VIEW_TYPE_CONTACTS_ROOM;
        }
        else if (type.equals("message"))
        {
            return VIEW_TYPE_MESSAGE;
        }
        else if (type.equals("file"))
        {
            return VIEW_TYPE_FILE;
        }
        else
        {
            throw new RuntimeException("ViewType 不正确");
        }
    }


    @Override
    public SearchResultItemViewHolder onCreateViewHolder(int viewType)
    {
        switch (viewType)
        {
            case VIEW_TYPE_CONTACTS_ROOM:
            {
                return new SearchResultUserItemViewHolder();
            }
            case VIEW_TYPE_MESSAGE:
            {
                return new SearchResultMessageItemViewHolder();
            }
            case VIEW_TYPE_FILE:
            {
                return new SearchResultFileItemViewHolder();
            }
            default:
            {
                return null;
            }
        }
    }

    @Override
    public void onBindViewHolder(SearchResultItemViewHolder viewHolder, int position)
    {
        SearchResultItem item = searchResultItems.get(position);

        if (viewHolder instanceof SearchResultUserItemViewHolder)
        {
            processContactsOrRoomsResult(viewHolder, item);
        }
        else if (viewHolder instanceof SearchResultMessageItemViewHolder)
        {
            processMessageResult(viewHolder, item);
        }
        else if (viewHolder instanceof SearchResultFileItemViewHolder)
        {
            processFileResult(viewHolder, item);
        }

//        if (!viewHolders.contains(viewHolder))
//        {
//            viewHolders.add(viewHolder);
//        }

        //viewHolder.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //SearchResultItem item = searchResultItems.get(position);

    }

    /**
     * 处理文件搜索结果
     * @param viewHolder
     * @param item
     */
    private void processFileResult(SearchResultItemViewHolder viewHolder, SearchResultItem item)
    {
        SearchResultFileItemViewHolder holder = (SearchResultFileItemViewHolder) viewHolder;
        //fileItemViewHolders.add(holder);
        fileItemViewHolders.put(item.getId(), holder);

        ImageIcon attachmentTypeIcon = attachmentIconHelper.getImageIcon(item.getName());
        attachmentTypeIcon.setImage(attachmentTypeIcon.getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH));
        holder.avatar.setIcon(attachmentTypeIcon);
        holder.name.setKeyWord(keyWord);

        String filename = item.getName();
        if (item.getName().length() > 20)
        {
            String suffix = filename.substring(filename.lastIndexOf("."));
            filename = item.getName().substring(0, 15) + "..." + suffix;
        }

        holder.name.setText(filename);

        String filePath = fileCache.tryGetFileCache(item.getId(), item.getName());
        if (filePath != null)
        {
            holder.size.setText(fileCache.fileSizeString(filePath));
        }
        else
        {
            holder.size.setText("未下载");
        }

        holder.setToolTipText(item.getName());

        holder.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                setBackground(holder, Colors.ITEM_SELECTED_DARK);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                setBackground(holder, Colors.DARK);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    downloadOrOpenFile(item.getId(), holder);
                }
                super.mouseReleased(e);
            }
        });
    }

    /**
     * 处理消息搜索结果
     *
     * @param viewHolder
     * @param item
     */
    private void processMessageResult(SearchResultItemViewHolder viewHolder, SearchResultItem item)
    {
        SearchResultMessageItemViewHolder holder = (SearchResultMessageItemViewHolder) viewHolder;
        Room room = roomService.findById((String) item.getTag());
        Message message = messageService.findById(item.getId());

        holder.avatar.setIcon(new ImageIcon(getRoomAvatar(room.getType(), room.getName(), null)));
        holder.brief.setKeyWord(keyWord);
        holder.brief.setText(item.getName());
        holder.roomName.setText(room.getName());
        holder.time.setText(TimeUtil.diff(message.getTimestamp()));

        holder.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    enterRoom(room.getRoomId(), message.getTimestamp());
                    clearSearchText();
                }
                super.mouseReleased(e);
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                setBackground(holder, Colors.ITEM_SELECTED_DARK);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                setBackground(holder, Colors.DARK);
            }
        });
    }

    private void processMouseListeners(SearchResultItemViewHolder viewHolder, SearchResultItem item)
    {
        viewHolder.addMouseListener(new AbstractMouseListener()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {

                    if (item.getType().equals("s"))
                    {
                    	String roomId = null;
                    	Room roomExist = roomService.findRelativeRoomIdByUserId(item.getId());
                    	if (roomExist==null ){
                    		//发起新会话
                        	roomId = item.getId()+"@"+Launcher.DOMAIN;
                        	ChatService.createNewRoom(roomId);    
                    	}else{
                        	roomId = roomExist.getRoomId();                    		
                    	}

                        enterRoom(roomId, 0L);    
                        clearSearchText();
                    }
                    else if (item.getType().equals("m") || item.getType().equals("q"))
                    {
                        enterRoom(item.getId(), 0L);
                        clearSearchText();
                    }
                    else if (item.getType().equals("searchMessage"))
                    {
                        if (searchMessageOrFileListener != null)
                        {
                            searchMessageOrFileListener.onSearchMessage();
                        }
                    }
                    else if (item.getType().equals("searchFile"))
                    {
                        if (searchMessageOrFileListener != null)
                        {
                            searchMessageOrFileListener.onSearchFile();
                        }
                    }
                }
            }


            @Override
            public void mouseEntered(MouseEvent e)
            {
                setBackground(viewHolder, Colors.ITEM_SELECTED_DARK);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                setBackground(viewHolder, Colors.DARK);
            }
        });
    }

    private void clearSearchText()
    {
        ListPanel.getContext().showPanel(ListPanel.CHAT);
        SearchPanel.getContext().clearSearchText();
    }

    /**
     * 处理通讯录或群组探索结果
     *
     * @param viewHolder
     * @param item
     */
    private void processContactsOrRoomsResult(SearchResultItemViewHolder viewHolder, SearchResultItem item)
    {
        SearchResultUserItemViewHolder holder = (SearchResultUserItemViewHolder) viewHolder;

        holder.name.setKeyWord(this.keyWord);
        holder.name.setText(item.getName());

        ImageIcon icon = new ImageIcon();
        // 群组头像
        String type = item.getType();

        if (type.equals("m") || type.equals("q") || type.equals("s"))
        {
            icon.setImage(getRoomAvatar(type, item.getName(), null));
        }
        else
        {
            if (type.equals("searchMessage"))
            {
                icon.setImage(IconUtil.getIcon(this, "/image/message.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
            }
            else if (type.equals("searchFile"))
            {
                icon.setImage(IconUtil.getIcon(this, "/image/file_icon.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
            }
            /*else if (type.equals("message"))
            {
                Room room = roomService.findById((String) ((Map) item.getTag()).get("roomId"));
                if (room != null)
                {
                    icon.setImage(getRoomAvatar(room.getType(), room.getName()));
                }
            }*/
        }
        holder.avatar.setIcon(icon);

        processMouseListeners(viewHolder, item);
    }


    /**
     * 根据房间类型获取对应的头像
     *
     * @param type
     * @param name
     * @return
     */
    /*private Image getRoomAvatar(String type, String name)
    {
        if (type.equals("m"))
        {
            return AvatarUtil.createOrLoadGroupAvatar("##", name).getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        }
        else if (type.equals("q"))
        {
            return AvatarUtil.createOrLoadGroupAvatar("#", name).getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        }
        // 私聊头像
        else if (type.equals("s"))
        {
            return AvatarUtil.createOrLoadUserAvatar(name).getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        }

        return null;
    }*/

    /**
     * 根据房间类型获取对应的头像
     *
     * @param type
     * @param name
     * @return
     */
    private Image getRoomAvatar(String type, String name, String[] members)
    {
        if (type.equals("m") || type.equals("q"))
        {
            //return AvatarUtil.createOrLoadGroupAvatar(name, members, type).getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        	return AvatarUtil.staticGroupAvatar().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        }
        // 私聊头像
        else if (type.equals("s"))
        {
            return AvatarUtil.createOrLoadUserAvatar(name).getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        }

        return null;
    }

    /**
     * 设置item的背影色
     * @param holder
     * @param color
     */
    private void setBackground(SearchResultItemViewHolder holder, Color color)
    {
        holder.setBackground(color);
        if (holder instanceof SearchResultUserItemViewHolder)
        {
            ((SearchResultUserItemViewHolder) holder).name.setBackground(color);
        }
        else if (holder instanceof SearchResultMessageItemViewHolder)
        {
            ((SearchResultMessageItemViewHolder) holder).nameBrief.setBackground(color);
        }
        else if (holder instanceof SearchResultFileItemViewHolder)
        {
            ((SearchResultFileItemViewHolder) holder).nameProgressPanel.setBackground(color);
        }
    }

    public void setKeyWord(String keyWord)
    {
        this.keyWord = keyWord;
    }

    private void enterRoom(String roomId, long firstMessageTimestamp)
    {
        ChatPanel.getContext().enterRoom(roomId, firstMessageTimestamp);
    }

    public void setSearchMessageOrFileListener(SearchMessageOrFileListener searchMessageOrFileListener)
    {
        this.searchMessageOrFileListener = searchMessageOrFileListener;
    }

    /**
     * 打开文件，如果文件不存在，则下载
     *
     * @param fileId
     * @param holder
     */
    public void downloadOrOpenFile(String fileId, SearchResultFileItemViewHolder holder)
    {
        FileAttachment fileAttachment = fileAttachmentService.findById(fileId);

        if (fileAttachment == null)
        {
            JOptionPane.showMessageDialog(null, "无效的附件", "附件无效", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String filepath = fileCache.tryGetFileCache(fileAttachment.getId(), fileAttachment.getTitle());
        if (filepath == null)
        {
            // 服务器上的文件
            if (fileAttachment.getLink().startsWith("/file-upload"))
            {
                // 如果当前文件正在下载，则不下载
                if (downloadingFiles.contains(fileId))
                {
                    holder.progressBar.setVisible(true);
                    holder.size.setText("下载中...");
                }
                else
                {
                    downloadFile(fileAttachment);
                }
            }
            // 本地的文件
            else
            {
                openFileWithDefaultApplication(fileAttachment.getLink());
            }
        }
        else
        {
            openFileWithDefaultApplication(filepath);
        }
    }

    /**
     * 下载文件
     *  @param fileAttachment
     */
    private void downloadFile(FileAttachment fileAttachment)
    {
        downloadingFiles.add(fileAttachment.getId());
        //holder.fileId = fileAttachment.getId();

        final DownloadTask task = new DownloadTask(new HttpUtil.ProgressListener()
        {
            @Override
            public void onProgress(int progress)
            {
                SearchResultFileItemViewHolder holder = fileItemViewHolders.get(fileAttachment.getId());
                if (progress >= 0 && progress < 100)
                {

                    if (holder.size.isVisible())
                    {
                        holder.size.setVisible(false);
                    }
                    if (!holder.progressBar.isVisible())
                    {
                        holder.progressBar.setVisible(true);
                    }

                    holder.progressBar.setValue(progress);
                }
                else if (progress >= 100)
                {
                    holder.progressBar.setVisible(false);
                    holder.size.setVisible(true);
                }
            }
        });

        task.setListener(new HttpResponseListener<byte[]>()
        {
            @Override
            public void onSuccess(byte[] data)
            {
                SearchResultFileItemViewHolder holder = fileItemViewHolders.get(fileAttachment.getId());

                String path = fileCache.cacheFile(fileAttachment.getId(), fileAttachment.getTitle(), data);

                if (path == null)
                {
                    holder.size.setVisible(true);
                    holder.size.setText("文件获取失败");
                    holder.progressBar.setVisible(false);
                }
                else
                {
                    holder.size.setVisible(true);
                    System.out.println("文件已缓存在 " + path);
                    holder.size.setText(fileCache.fileSizeString(path));
                    downloadingFiles.remove(fileAttachment.getId());

                    /*for (SearchResultFileItemViewHolder h : fileItemViewHolders)
                    {
                        if (h.fileId.equals(fileAttachment.getId()))
                        {
                            h.progressBar.setVisible(false);
                            h.size.setVisible(true);
                            h.size.setText(fileCache.fileSizeString(fileCache.tryGetFileCache(fileAttachment.getId(), fileAttachment.getTitle())));
                            //break;
                            //fileItemViewHolders.remove(h);
                        }
                    }*/

                    /*SearchResultFileItemViewHolder h = fileItemViewHolders.get(fileAttachment.getId());
                    h.progressBar.setVisible(false);
                    h.size.setVisible(true);
                    h.size.setText(fileCache.fileSizeString(fileCache.tryGetFileCache(fileAttachment.getId(), fileAttachment.getTitle())));*/
                }
            }

            @Override
            public void onFailed()
            {
                SearchResultFileItemViewHolder holder = fileItemViewHolders.get(fileAttachment.getId());
                holder.size.setVisible(true);
                holder.size.setText("文件获取失败");
                holder.progressBar.setVisible(false);
            }
        });

        String url = Launcher.HOSTNAME + fileAttachment.getLink() + "?rc_uid=" + UserCache.CurrentBareJid + "&rc_token=" + UserCache.CurrentUserToken;
        task.execute(url);
    }

    /**
     * 使用默认程序打开文件
     *
     * @param path
     */
    private void openFileWithDefaultApplication(String path)
    {
        try
        {
            Desktop.getDesktop().open(new File(path));
        }
        catch (IOException e1)
        {
            JOptionPane.showMessageDialog(null, "文件打开失败，没有找到关联的应用程序", "打开失败", JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
        }
    }


    public interface SearchMessageOrFileListener
    {
        void onSearchMessage();

        void onSearchFile();
    }
}
