package xysoft.im.db.dao;

import org.apache.ibatis.session.SqlSession;

public class KeyValueDao extends BasicDao
{
    public KeyValueDao(SqlSession session)
    {
        super(session, KeyValueDao.class);
    }
}
