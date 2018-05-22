package xysoft.im.utils;

import xysoft.im.app.Launcher;

public class DebugUtil {

	public DebugUtil() {
		// TODO Auto-generated constructor stub
	}

	public static void debug(String str){
		if (Launcher.IS_DEBUGMODE)
			System.out.println(str);
	}
}
