package xysoft.im.utils;


import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xysoft.im.constant.Config;
import xysoft.im.constant.Res;



/*
 *  文件io处理
 */
public class FilesIO {
	static Logger log = LoggerFactory.getLogger(FilesIO.class);
	public static void bufferedWrite(String filename, String str) {
		File f = new File(filename);
		OutputStreamWriter writer = null;
		BufferedWriter bw = null;
		try {
			OutputStream os = new FileOutputStream(f);
			writer = new OutputStreamWriter(os, "UTF-8");
			bw = new BufferedWriter(writer);

			bw.write(str);

			bw.flush();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Long GetfilelastModified(String filename) {
		File file = new File(filename);
		return file.lastModified();
	}

	public static String ReadLocalfile(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String str = "";
			String r = br.readLine();
			while (r != null) {
				str += r;
				r = br.readLine();
			}
			return str;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";

	}

	/*
	 * 复制文件
	 */
	public static void CopyFile(File sourceFile, File targetFile) throws IOException {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} finally {
			// 关闭流
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}

	/*
	 *  复制目录
	 */
	public static void CopyDirectiory(String sourceDir, String targetDir) throws IOException {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
				CopyFile(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + File.separator + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + File.separator + file[i].getName();
				CopyDirectiory(dir1, dir2);
			}
		}
	}
	
	/*
	 * 另存
	 */
	public static void saveAsFile(String filePathFull, String fileName) {
		try {
			File f=new File(filePathFull);
			if(f.exists()){
				if (filePathFull == null || filePathFull.trim().length() == 0)
					return;

				FileDialog fd = new FileDialog(new Frame(), "文件另存为",
						FileDialog.SAVE);
				fd.setFile(fileName);
				//fd.setIconImage(SparkManager.getMainWindow().getIconImage());
				fd.toFront();

				fd.setVisible(true);

				if (fd.getFile() != null) {
					copyFile(new File(filePathFull), new File(fd.getDirectory()),fd.getFile());
				} else {
					fd.setAlwaysOnTop(false);
				}
			}else{
				JOptionPane.showMessageDialog(null, Res.FILE_NOT_FOUND, "提示", 1);
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}
	
	@SuppressWarnings("resource")
	public static long copyFile(File srcFile, File destDir, String newFileName) {  
		long copySizes = 0;  
		if (!srcFile.exists()) {  
			log.error("源文件不存在");  
			copySizes = -1;  
		} else if (!destDir.exists()) {  
			log.error("目标目录不存在");  
			copySizes = -1;  
		} else if (newFileName == null) {  
			log.error("文件名为null");  
			copySizes = -1;  
		} else {  
			try {  
				FileChannel fcin = new FileInputStream(srcFile).getChannel();  
				FileChannel fcout = new FileOutputStream(new File(destDir,  
						newFileName)).getChannel();  
				long size = fcin.size();  
				fcin.transferTo(0, fcin.size(), fcout);  
				fcin.close();  
				fcout.close();  
				copySizes = size;  
			} catch (FileNotFoundException e) {  
				log.error(e.getMessage()); 
			} catch (IOException e) {  
				log.error(e.getMessage());
			}  
		}  
		return copySizes;  
	}
	/**
	 * 检查数据库文件是否完整
	 */
	public static  void checkDBFile(File userDbFile){
		//获取数据库连接之前检查数据库文件是否存在，不存在则直接copy到appdata目录
		if(!userDbFile.exists()){
			try {
				
				File dir=	userDbFile.getParentFile();
				if(!dir.exists()){
					dir.mkdirs();
				}

				File dbFile=new File(Config.USER_HOME + File.separator + userDbFile.getName());
				if(dbFile.exists()){
					FilesIO.CopyFile(dbFile, userDbFile);
				}

			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * 锁住当前登录用户，防止再次登录
	 * 
	 */
	public static void lockFile(String userName){
		String lockFile = Config.USER_HOME +"/user/"+userName+"@im/Lock.File";
		File F=new File(lockFile);
		if(F.exists()){
			try {
				F.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(F, "rws");
			FileChannel fc = raf.getChannel();
			fc.tryLock();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 判断文件是否被锁住，如果被占用说明当前用户已登录
	 * @param userName  用户名
	 * @return   返回是否登录
	 */
	public static boolean hasLogin(String userName){
		String lockFile=Config.USER_HOME + "/user/"+userName+"@im/Lock.File";

		File F=new File(lockFile);

		if(!F.exists()){
			F.getParentFile().mkdirs();
			try {
				F.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(F.renameTo(F)){    
			return false;
		}else{        
			return true;
		}
	}
	
	public static boolean deleteDir(File dir) {

		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		} 

		return dir.delete();
	}  
}
