package xysoft.im.db.dao;

import org.apache.ibatis.session.SqlSession;


public class ImageAttachmentDao extends BasicDao
{
    public ImageAttachmentDao(SqlSession session)
    {
        super(session, ImageAttachmentDao.class);
    }
}
