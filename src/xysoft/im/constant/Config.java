package xysoft.im.constant;


public class Config {

	public static final String HOSTNAME = "localhost";
	public static final String APP_VERSION = "0.1";

	// 用户路径,在static中设置
	public static String USER_HOME = "";
	// debug模式输出全部log信息
	public static final boolean IS_DEBUGMODE = true;
	//来消息声音提示
	public static final boolean IS_PLAYMESSAGESOUND = true;

	private static final String USER_DIRECTORY_LINUX = ".XyTalk";
	private static final String USER_DIRECTORY_MAC = "Library/Application Support/XyTalk";
	private static final String USER_DIRECTORY_WINDOWS = "XyTalk";

	public Config() {
		// TODO Auto-generated constructor stub
	}

	static {
		if (System.getenv("APPDATA") != null && !System.getenv("APPDATA").equals("")) {
			USER_HOME = System.getenv("APPDATA") + System.getProperty("file.separator") + getUserConf();
		} else {
			USER_HOME = System.getProperties().getProperty("user.home") + System.getProperty("file.separator")
					+ getUserConf();
		}
	}

	// 不同的操作系统对应不同的用户路径
	static String getUserConf() {
		if (isLinux()) {
			return Config.USER_DIRECTORY_LINUX;
		} else if (isMac()) {
			return Config.USER_DIRECTORY_MAC;
		} else
			return Config.USER_DIRECTORY_WINDOWS;
	}

	public static boolean isLinux() {
		final String osName = System.getProperty("os.name").toLowerCase();
		return osName.startsWith("linux");
	}

	public static boolean isMac() {
		String lcOSName = System.getProperty("os.name").toLowerCase();
		return lcOSName.indexOf("mac") != -1;
	}

	public static boolean isWindows() {
		final String osName = System.getProperty("os.name").toLowerCase();
		return osName.startsWith("windows");
	}
}
