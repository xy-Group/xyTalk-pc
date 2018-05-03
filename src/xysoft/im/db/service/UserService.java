package xysoft.im.db.service;

import org.apache.ibatis.session.SqlSession;
import xysoft.im.db.dao.UserDao;
import xysoft.im.db.model.User;

public class UserService extends BasicService<UserDao, User>{

	public UserService(SqlSession session) {
		dao = new UserDao(session);
        super.setDao(dao);
	}

	public String getName(String fromUsername) {
		// TODO Auto-generated method stub
		return "中文";
	}

}
