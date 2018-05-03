package xysoft.im.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConfig.JournalMode;
import org.sqlite.SQLiteConfig.SynchronousMode;
import org.sqlite.SQLiteConfig.TempStore;

import xysoft.im.constant.Config;




/*
 *  Sqlite数据库处理
 *  
 *  author: Tim on 04/23/2018.
 *  by Xysoft Group
 */
public class SqliteHelper {
	static Logger log = LoggerFactory.getLogger(SqliteHelper.class);
	static String dir = Config.USER_HOME;

	public static Connection getConnection() {
		// 获取数据库连接之前检查数据库文件是否存在，不存在则直接copy到appdata目录
		File targetDb = new File(dir + "/xytalk.db"); // 数据库文件在缓存目录是否存在
		if (!targetDb.exists()) {
			File dbFile = new File(System.getProperty("user.dir")
					+ "/xytalk.db");
			try {
				FilesIO.CopyFile(dbFile, targetDb);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Connection connection = null;
		SQLiteConfig config = new SQLiteConfig();
		config.setJournalMode(JournalMode.MEMORY);
		config.setTempStore(TempStore.MEMORY);
		config.setSynchronous(SynchronousMode.OFF);
		config.enforceForeignKeys(false);
		//config.enableCountChanges(false);

		String filePath = null;
		if (Config.isWindows()) {
			filePath = targetDb.getAbsolutePath().replace("\\", "/");
			filePath = "//" + filePath.substring(0, 1).toLowerCase()
					+ filePath.substring(1, filePath.length());
		} else {
			filePath = targetDb.getAbsolutePath();
		}
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + filePath,
					config.toProperties());
		} catch (ClassNotFoundException e) {
			log.error("getConnection:" + e);
		} catch (SQLException e) {

			log.error("getConnection:" + e);
		}
		return connection;
	}

	public static boolean isTableExist(String tableName) {
		Connection c = null;
		Statement stat = null;
		try {
			c = getConnection();
			stat = c.createStatement();
			ResultSet rs = stat
					.executeQuery("SELECT COUNT(*) as TabCount FROM sqlite_master where type='table' and name='"
							+ tableName + "'");
			while (rs.next()) {
				if (rs.getInt(1) == 0) {
					return false;
				} else {
					return true;
				}
			}

		} catch (SQLException e) {
			log.error("isTableExist:" + e);
		} finally {
			try {
				stat.close();
				c.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error("isTableExist:" + e);
			}
		}
		return false;

	}

	// 注意：取第一个字段的值放入列表
	public static List<String> GetList(String sql) {
		List<String> list = new ArrayList<String>();
		Connection c = null;
		Statement stat = null;
		try {
			c = getConnection();
			stat = c.createStatement();
			ResultSet rs = stat.executeQuery(sql); // 查询数据
			while (rs.next()) { // 将查询到的数据打印出来
				list.add(rs.getString(1));
			}
		} catch (SQLException e) {
			log.error("getList:" + e);
		} finally {
			try {
				stat.close();
				c.close();
			} catch (SQLException e) {
				log.error("getList:" + e);
			}
		}
		return list;
	}

	public static List<String> GetList(String sql, String fieldName) {
		List<String> list = new ArrayList<String>();
		Connection c = null;
		Statement stat = null;
		try {
			c = getConnection();
			stat = c.createStatement();
			ResultSet rs = stat.executeQuery(sql); // 查询数据
			while (rs.next()) { // 将查询到的数据打印出来
				list.add(rs.getString(fieldName));
			}
		} catch (SQLException e) {
			log.error("getList:" + e);
		} finally {
			try {
				stat.close();
				c.close();
			} catch (SQLException e) {
				log.error("getList:" + e);
			}
		}
		return list;
	}

	public static List<Integer> GetIntList(String sql) {
		List<Integer> list = new ArrayList<Integer>();
		Connection c = null;
		Statement stat = null;
		try {
			c = getConnection();
			stat = c.createStatement();
			ResultSet rs = stat.executeQuery(sql); // 查询数据
			while (rs.next()) { // 将查询到的数据打印出来
				list.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			log.error("getList:" + e);
		} finally {
			try {
				stat.close();
				c.close();
			} catch (SQLException e) {
				log.error("getList:" + e);
			}
		}
		return list;
	}

	public static List<Object> GetObjectList(String sql) {
		List<Object> list = new ArrayList<Object>();
		Connection c = null;
		Statement stat = null;
		try {
			c = getConnection();
			stat = c.createStatement();
			ResultSet rs = stat.executeQuery(sql); // 查询数据
			while (rs.next()) { // 将查询到的数据打印出来
				list.add(rs.getObject(1));
			}
		} catch (SQLException e) {
			log.error("getList:" + e);
		} finally {
			try {
				stat.close();
				c.close();
			} catch (SQLException e) {
				log.error("getList:" + e);
			}
		}
		return list;
	}

	// 注意：第一个字段为key，第二个字段为value
	public static HashMap<String, String> GetMap(String sql) {
		HashMap<String, String> hash = new HashMap<String, String>();
		Connection c = null;
		Statement stat = null;
		try {
			c = getConnection();
			stat = c.createStatement();
			ResultSet rs = stat.executeQuery(sql); // 查询数据
			while (rs.next()) { // 将查询到的数据打印出来

				hash.put(rs.getString(1), rs.getString(2));
			}
		} catch (SQLException e) {
			log.error("GetMap:" + e);
		} finally {
			try {
				stat.close();
				c.close();
			} catch (SQLException e) {
				log.error("GetMap:" + e);
			}
		}
		return hash;
	}

	public static HashMap<String, String> GetMap(String sql, String key,
			String value) {
		HashMap<String, String> hash = new HashMap<String, String>();
		Connection c = null;
		Statement stat = null;
		try {
			c = getConnection();
			stat = c.createStatement();
			ResultSet rs = stat.executeQuery(sql); // 查询数据
			while (rs.next()) { // 将查询到的数据打印出来
				hash.put(rs.getString(key), rs.getString(value));
			}
		} catch (SQLException e) {
			log.error("GetMap:" + e);
		} finally {
			try {
				stat.close();
				c.close();
			} catch (SQLException e) {
				log.error("GetMap:" + e);
			}
		}
		return hash;
	}

	public static ResultSet GetResultSet(String sql) {
		Connection c = null;
		Statement stat = null;
		try {
			c = getConnection();
			stat = c.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			return rs;

		} catch (SQLException e) {
			log.error("GetResultSet:" + e);
		} finally {
			try {
				stat.close();
				c.close();
			} catch (SQLException e) {
				log.error("GetResultSet:" + e);
			}
		}
		return null;

	}

	public static int ExecuteNonQuery(String sql) {
		try {
			Statement stat = getConnection().createStatement();
			return stat.executeUpdate(sql);

		} catch (SQLException e) {
			log.error("ExecuteNonQuery:" + e);
		}
		return 0;
	}

	public static void DropTable(String tableName) {
		try {
			Statement stat = getConnection().createStatement();
			stat.executeUpdate("DROP TABLE " + tableName);

		} catch (SQLException e) {
			log.error("DropTable:" + e);
		}
	}

}
