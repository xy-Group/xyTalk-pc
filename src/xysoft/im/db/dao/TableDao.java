package xysoft.im.db.dao;

import org.apache.ibatis.session.SqlSession;


public class TableDao
{
    private SqlSession session;

    public TableDao(SqlSession session)
    {
        this.session = session;
    }

    public void createCurrentUserTable()
    {
        session.update("createCurrentUserTable");
    }

    public boolean exist(String name)
    {
        return ((Integer) session.selectOne("tableExist", name)) > 0;
    }

    public void createRoomTable()
    {
        session.update("createRoomTable");
    }

    public void createMessageTable()
    {
        session.update("createMessageTable");
    }

    public void createFileAttachmentTable()
    {
        session.update("createFileAttachmentTable");
    }

    public void createImageAttachmentTable()
    {
        session.update("createImageAttachmentTable");
    }

    public void createContactsUserTable()
    {
        session.update("createContactsUserTable");
    }
}
