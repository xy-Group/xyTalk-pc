package xysoft.im.db.dao;

import xysoft.im.db.model.FileAttachment;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileAttachmentDao extends BasicDao
{
    public FileAttachmentDao(SqlSession session)
    {
        super(session, FileAttachmentDao.class);
    }

    public List<FileAttachment> search(String key)
    {
        Map map = new HashMap();
        map.put("condition", "'%" + key + "%'");
        return session.selectList(FileAttachmentDao.class.getName() + ".search", map);
    }
}
