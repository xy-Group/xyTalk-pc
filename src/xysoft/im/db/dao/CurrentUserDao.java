package xysoft.im.db.dao;

import xysoft.im.db.model.CurrentUser;
import xysoft.im.db.service.CurrentUserService;
import org.apache.ibatis.session.SqlSession;

import java.util.List;


public  class CurrentUserDao extends BasicDao
{
    public CurrentUserDao(SqlSession session)
    {
        super(session, CurrentUserDao.class);
    }
}