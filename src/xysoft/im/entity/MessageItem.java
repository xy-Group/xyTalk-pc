package xysoft.im.entity;

import xysoft.im.app.Launcher;
import xysoft.im.db.model.FileAttachment;
import xysoft.im.db.model.ImageAttachment;
import xysoft.im.db.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 单条消息的实体类，支持文本消息、图片、小视频、音频、文件，其中小视频、音频同样使用
 * @author Administrator
 *
 */
public class MessageItem implements Comparable<MessageItem>
{
    public static final int SYSTEM_MESSAGE = 0;
    public static final int LEFT_TEXT = 1;
    public static final int LEFT_IMAGE = 2;
    public static final int LEFT_ATTACHMENT = 3;
    public static final int LEFT_AUDIO = 4;
    public static final int LEFT_VIDEO = 5;

    public static final int RIGHT_TEXT = -1;
    public static final int RIGHT_IMAGE = -2;
    public static final int RIGHT_ATTACHMENT = -3;
    public static final int RIGHT_AUDIO = -4;
    public static final int RIGHT_VIDEO = -5;


    private String id;
    private String roomId;
    private String messageContent;
    private boolean groupable;
    private long timestamp;
    private String senderUsername;
    private String senderId;
    private long updatedAt;
    private boolean needToResend;
    private int progress;
    private boolean deleted;
    private int messageType;

    private FileAttachmentItem fileAttachment;
    private FileAttachmentItem audioAttachment;
    private FileAttachmentItem videoAttachment;
    private ImageAttachmentItem imageAttachment;

    public MessageItem()
    {
    }

    public MessageItem(Message message, String currentUserId)
    {
        this();
        this.setId(message.getId());
        this.setMessageContent(message.getMessageContent());
        this.setGroupable(message.isGroupable());
        this.setRoomId(message.getRoomId());
        this.setSenderId(message.getSenderId());
        this.setSenderUsername(message.getSenderUsername());
        this.setTimestamp(message.getTimestamp());
        this.setUpdatedAt(message.getUpdatedAt());
        this.setNeedToResend(message.isNeedToResend());
        this.setProgress(message.getProgress());
        this.setDeleted(message.isDeleted());

        boolean isFileAttachment = false;
        boolean isImageAttachment = false;
        boolean isAudioAttachment = false;
        boolean isVideoAttachment = false;

        if (message.getFileAttachmentId() != null)
        {
            isFileAttachment = true;

            FileAttachment fa = Launcher.fileAttachmentService.findById(message.getFileAttachmentId());
            this.fileAttachment = new FileAttachmentItem(fa);
        }
        if (message.getImageAttachmentId() != null)
        {
            isImageAttachment = true;

            ImageAttachment ia = Launcher.imageAttachmentService.findById(message.getImageAttachmentId());
            this.imageAttachment = new ImageAttachmentItem(ia);
        }

        if (message.getAudioAttachmentId() != null)
        {
        	isAudioAttachment = true;

            FileAttachment ia = Launcher.fileAttachmentService.findById(message.getAudioAttachmentId());
            this.audioAttachment = new FileAttachmentItem(ia);
        }

        if (message.getVideoAttachmentId() != null)
        {
        	isVideoAttachment = true;

            FileAttachment ia = Launcher.fileAttachmentService.findById(message.getVideoAttachmentId());
            this.videoAttachment = new FileAttachmentItem(ia);
        }

        if (message.isSystemMessage())
        {
            this.setMessageType(SYSTEM_MESSAGE);
        }
        else
        {
            // 自己发的消息
            if (message.getSenderId().equals(currentUserId))
            {
                // 文件附件
                if (isFileAttachment)
                {
                    this.setMessageType(RIGHT_ATTACHMENT);
                }
                // 图片消息
                else if (isImageAttachment)
                {
                    this.setMessageType(RIGHT_IMAGE);
                }
                else if (isAudioAttachment)
                {
                    this.setMessageType(RIGHT_AUDIO);
                }
                else if (isVideoAttachment)
                {
                    this.setMessageType(RIGHT_VIDEO);
                }
                // 普通文本消息
                else
                {
                    this.setMessageType(RIGHT_TEXT);
                }
            }
            else //别人发的消息
            {
                // 文件附件
                if (isFileAttachment)
                {
                    this.setMessageType(LEFT_ATTACHMENT);
                }
                // 图片消息
                else if (isImageAttachment)
                {
                    this.setMessageType(LEFT_IMAGE);
                }
                else if (isAudioAttachment)
                {
                    this.setMessageType(LEFT_AUDIO);
                }
                else if (isVideoAttachment)
                {
                    this.setMessageType(LEFT_VIDEO);
                }
                // 普通文本消息
                else
                {
                    this.setMessageType(LEFT_TEXT);
                }
            }
        }
    }


	@Override
    public int compareTo( MessageItem o)
    {
    	//时间排序
        return (int) (this.getTimestamp() - o.getTimestamp());

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getRoomId()
    {
        return roomId;
    }

    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }

    public String getMessageContent()
    {
        return messageContent;
    }

    public void setMessageContent(String messageContent)
    {
        this.messageContent = messageContent;
    }

    public boolean isGroupable()
    {
        return groupable;
    }

    public void setGroupable(boolean groupable)
    {
        this.groupable = groupable;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getSenderUsername()
    {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername)
    {
        this.senderUsername = senderUsername;
    }

    public String getSenderId()
    {
        return senderId;
    }

    public void setSenderId(String senderId)
    {
        this.senderId = senderId;
    }

    public long getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public boolean isNeedToResend()
    {
        return needToResend;
    }

    public void setNeedToResend(boolean needToResend)
    {
        this.needToResend = needToResend;
    }

    public int getProgress()
    {
        return progress;
    }

    public void setProgress(int progress)
    {
        this.progress = progress;
    }

    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted(boolean deleted)
    {
        this.deleted = deleted;
    }

    public int getMessageType()
    {
        return messageType;
    }

    public void setMessageType(int messageType)
    {
        this.messageType = messageType;
    }

    public FileAttachmentItem getFileAttachment()
    {
        return fileAttachment;
    }

    public void setFileAttachment(FileAttachmentItem fileAttachment)
    {
        this.fileAttachment = fileAttachment;
    }

    public ImageAttachmentItem getImageAttachment()
    {
        return imageAttachment;
    }

    public void setImageAttachment(ImageAttachmentItem imageAttachment)
    {
        this.imageAttachment = imageAttachment;
    }
    
    public FileAttachmentItem getAudioAttachment() {
		return audioAttachment;
	}

	public void setAudioAttachment(FileAttachmentItem audioAttachment) {
		this.audioAttachment = audioAttachment;
	}

	public FileAttachmentItem getVideoAttachment() {
		return videoAttachment;
	}

	public void setVideoAttachment(FileAttachmentItem videoAttachment) {
		this.videoAttachment = videoAttachment;
	}

}

