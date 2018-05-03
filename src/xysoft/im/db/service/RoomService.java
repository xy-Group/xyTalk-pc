package xysoft.im.db.service;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import xysoft.im.db.dao.RoomDao;
import xysoft.im.db.model.Room;


public class RoomService extends BasicService<RoomDao, Room>
{
    public RoomService(SqlSession session)
    {
        dao = new RoomDao(session);
        super.setDao(dao);
    }

    public int insertOrUpdate(Room room)
    {
        if (exist(room.getRoomId()))
        {
            return update(room);
        }else
        {
            return insert(room);
        }
    }

    public Room findRelativeRoomIdByUserId(String userId)
    {
        return dao.findRelativeRoomIdByUserId(userId);
    }

    public Room findByName(String name)
    {
        List list = dao.find("name", name);
        if (list.size() > 0)
        {
            return (Room) list.get(0);
        }
        return null;
    }

    public List<Room> searchByName(String name)
    {
        return dao.searchByName(name);
    }

	public int getMsgNum(String id) {
		return dao.getMsgNum(id);
	}
	
	public int getUnReadNum(String id) {
		return dao.getUnReadNum(id);
	}
}
