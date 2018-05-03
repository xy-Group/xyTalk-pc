package xysoft.im.db.service;

import xysoft.im.db.dao.ImageAttachmentDao;
import xysoft.im.db.model.ImageAttachment;
import org.apache.ibatis.session.SqlSession;


public class ImageAttachmentService extends BasicService<ImageAttachmentDao, ImageAttachment>
{
    public ImageAttachmentService(SqlSession session)
    {
        dao = new ImageAttachmentDao(session);
        super.setDao(dao);
    }

    public int insertOrUpdate(ImageAttachment attachment)
    {
        if (exist(attachment.getId()))
        {
            return update(attachment);
        }else
        {
            return insert(attachment);
        }
    }

}
