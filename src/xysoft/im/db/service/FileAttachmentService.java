package xysoft.im.db.service;

import xysoft.im.db.dao.FileAttachmentDao;
import xysoft.im.db.model.FileAttachment;
import xysoft.im.db.model.Message;
import org.apache.ibatis.session.SqlSession;

import java.util.List;


public class FileAttachmentService extends BasicService<FileAttachmentDao, FileAttachment>
{
    public FileAttachmentService(SqlSession session)
    {
        dao = new FileAttachmentDao(session);
        super.setDao(dao);
    }

    public int insertOrUpdate(FileAttachment attachment)
    {
        if (exist(attachment.getId()))
        {
            return update(attachment);
        }else
        {
            return insert(attachment);
        }
    }

    public List<FileAttachment> search(String key)
    {
        return dao.search(key);
    }
}
