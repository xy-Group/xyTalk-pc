package xysoft.im.entity;

import xysoft.im.db.model.FileAttachment;

/**
 * 文件型消息item
 *
 */

public class FileAttachmentItem
{
    private String id;
    private String title;
    private String link;
    private String description;

    public FileAttachmentItem(){

    }

    public FileAttachmentItem(String link){

        this.link = link;
    }

    public FileAttachmentItem(FileAttachment fa){
        this.id = fa.getId();
        this.title = fa.getTitle();
        this.link = fa.getLink();
        this.description = fa.getDescription();
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
