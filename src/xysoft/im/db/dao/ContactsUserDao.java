package xysoft.im.db.dao;

import xysoft.im.db.model.ContactsUser;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ContactsUserDao extends BasicDao
{
    public ContactsUserDao(SqlSession session)
    {
        super(session, ContactsUserDao.class);
    }

    public int deleteByUsername(String username)
    {
        return session.delete("deleteByUsername", username);
    }

    public List<ContactsUser> searchByUsernameOrName(String username, String name)
    {
        Map map = new HashMap();
        map.put("usernameCondition", "'%" + username + "%'");
        map.put("nameCondition", "'%" + name + "%'");
        return session.selectList("searchByUsernameOrName", map);
    }

	public List<ContactsUser> search100user() {

		return session.selectList("search100user");
	}

	public List<ContactsUser> findSize(int size) {

		return session.selectList("findSize",size);
	}

	public List<ContactsUser> findStartWith(String sp) {

		if (sp.equals("0-9")){
			return session.selectList("findStartWithNum");
		}
		if (sp.equals("abc")){
			return session.selectList("findStartWithABC");
		}
		if (sp.equals("def")){
			return session.selectList("findStartWithDEF");
		}
		if (sp.equals("ghi")){
			return session.selectList("findStartWithGHI");
		}
		if (sp.equals("jkl")){
			return session.selectList("findStartWithJKL");
		}
		if (sp.equals("mno")){
			return session.selectList("findStartWithMNO");
		}
		if (sp.equals("pq")){
			return session.selectList("findStartWithPQ");
		}
		if (sp.equals("rs")){
			return session.selectList("findStartWithRS");
		}
		if (sp.equals("tu")){
			return session.selectList("findStartWithTU");
		}
		if (sp.equals("vw")){
			return session.selectList("findStartWithVW");
		}
		if (sp.equals("xyz")){
			return session.selectList("findStartWithXYZ");
		}
		return session.selectList("search100user");
	}
}
