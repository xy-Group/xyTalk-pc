package xysoft.im.db.service;

import xysoft.im.db.dao.ContactsUserDao;
import xysoft.im.db.model.ContactsUser;
import xysoft.im.db.model.CurrentUser;
import org.apache.ibatis.session.SqlSession;
import org.omg.PortableInterceptor.USER_EXCEPTION;

import java.util.List;


public class ContactsUserService extends BasicService<ContactsUserDao, ContactsUser>
{
    public ContactsUserService(SqlSession session)
    {
        dao = new ContactsUserDao(session);
        super.setDao(dao);
    }

    public int insertOrUpdate(ContactsUser contactsUser)
    {
        if (exist(contactsUser.getUserId()))
        {
            return update(contactsUser);
        }else
        {
            return insert(contactsUser);
        }
    }

    public int deleteByUsername(String name)
    {
        return dao.deleteByUsername(name);
    }

    public ContactsUser findByUsername(String username)
    {
        List list = dao.find("username", username);
        if (list != null && list.size() > 0)
        {
            return (ContactsUser) list.get(0);
        }

        return null;
    }

    public List<ContactsUser> searchByUsernameOrName(String username, String name)
    {
        return dao.searchByUsernameOrName(username, name);
    }
}
