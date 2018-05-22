package xysoft.im.db.model;


public class Message extends BasicModel
{
    private String id;
    private String roomId;
    private String messageContent;
    private boolean groupable;
    private long timestamp;
    private String senderUsername;
    private String senderId;
    private long updatedAt;
    private boolean needToResend;
    private int progress; // 文件上传进度
    private boolean deleted;
    private boolean systemMessage; //是否是系统消息

    private String fileAttachmentId;
    private String imageAttachmentId;
    private String audioAttachmentId;
    private String videoAttachmentId;
    private String x1AttachmentId;
    private String x2AttachmentId;
    private String x3AttachmentId;

    
	public Message()
    {
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

    public boolean isSystemMessage()
    {
        return systemMessage;
    }

    public void setSystemMessage(boolean systemMessage)
    {
        this.systemMessage = systemMessage;
    }

    public String getFileAttachmentId()
    {
        return fileAttachmentId;
    }

    public void setFileAttachmentId(String fileAttachmentId)
    {
        this.fileAttachmentId = fileAttachmentId;
    }

    public String getImageAttachmentId()
    {
        return imageAttachmentId;
    }

    public void setImageAttachmentId(String imageAttachmentId)
    {
        this.imageAttachmentId = imageAttachmentId;
    }
    
    public String getAudioAttachmentId() {
		return audioAttachmentId;
	}

	public void setAudioAttachmentId(String audioAttachmentId) {
		this.audioAttachmentId = audioAttachmentId;
	}

	public String getVideoAttachmentId() {
		return videoAttachmentId;
	}

	public void setVideoAttachmentId(String videoAttachmentId) {
		this.videoAttachmentId = videoAttachmentId;
	}

	public String getX1AttachmentId() {
		return x1AttachmentId;
	}

	public void setX1AttachmentId(String x1AttachmentId) {
		this.x1AttachmentId = x1AttachmentId;
	}

	public String getX2AttachmentId() {
		return x2AttachmentId;
	}

	public void setX2AttachmentId(String x2AttachmentId) {
		this.x2AttachmentId = x2AttachmentId;
	}

	public String getX3AttachmentId() {
		return x3AttachmentId;
	}

	public void setX3AttachmentId(String x3AttachmentId) {
		this.x3AttachmentId = x3AttachmentId;
	}

}

