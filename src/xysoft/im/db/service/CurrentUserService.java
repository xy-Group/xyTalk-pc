package xysoft.im.db.service;

import xysoft.im.db.dao.CurrentUserDao;
import xysoft.im.db.model.CurrentUser;
import xysoft.im.utils.DbUtils;
import org.apache.ibatis.session.SqlSession;
import org.sqlite.core.DB;

import java.util.ArrayList;
import java.util.List;


public class CurrentUserService extends BasicService<CurrentUserDao, CurrentUser>
{
    public CurrentUserService(SqlSession session)
    {
        dao = new CurrentUserDao(session);
        super.setDao(dao);
    }

    public int insertOrUpdate(CurrentUser currentUser)
    {
        if (exist(currentUser.getUserId()))
        {
            return update(currentUser);
        }else
        {
            return insert(currentUser);
        }
    }

}
