package xysoft.im.db.dao;

import org.apache.ibatis.session.SqlSession;

public class UserDao extends BasicDao{

	public UserDao(SqlSession session) {
		super(session, UserDao.class);
	}

}
