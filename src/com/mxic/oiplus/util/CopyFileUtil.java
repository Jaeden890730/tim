package com.mxic.oiplus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * 复制文件或文件夾
 * 
 * zww
 */
public class CopyFileUtil {

	private static String MESSAGE = "";

	/**
	 * 复制單個文件
	 * 
	 * @param srcFileName
	 *            待复制的文件名
	 * @param descFileName
	 *            目標文件名
	 * @param overlay
	 *            如果目標文件存在，是否覆蓋
	 * @return 如果复制成功返回true，否則返回false
	 */
	public static boolean copyFile(String srcFileName, String destFileName, boolean overlay) {
		File srcFile = new File(srcFileName);

		// 判斷源文件是否存在
		if (!srcFile.exists()) {
			MESSAGE = "源文件：" + srcFileName + "不存在！";
			return false;
		} else if (!srcFile.isFile()) {
			MESSAGE = "复制文件失敗，源文件：" + srcFileName + "不是一個文件！";
			return false;
		}

		// 判斷目標文件是否存在
		File destFile = new File(destFileName);
		if (destFile.exists()) {
			// 如果目標文件存在並允許覆蓋
			if (overlay) {
				// 刪除已經存在的目標文件，無論目標文件是目錄還是單個文件
				new File(destFileName).delete();
			}
		} else {
			// 如果目標文件所在目錄不存在，則創建目錄
			if (!destFile.getParentFile().exists()) {
				// 目標文件所在目錄不存在
				if (!destFile.getParentFile().mkdirs()) {
					// 复制文件失敗：創建目標文件所在目錄失敗
					return false;
				}
			}
		}

		// 复制文件
		int byteread = 0; // 讀取的字節數
		InputStream in = null;
		OutputStream out = null;

		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];

			while ((byteread = in.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 复制整個目錄的內容
	 * 
	 * @param srcDirName
	 *            待复制目錄的目錄名
	 * @param destDirName
	 *            目標目錄名
	 * @param overlay
	 *            如果目標目錄存在，是否覆蓋
	 * @return 如果复制成功返回true，否則返回false
	 */
	public static boolean copyDirectory(String srcDirName, String destDirName, boolean overlay) {
		// 判斷源目錄是否存在
		File srcDir = new File(srcDirName);
		if (!srcDir.exists()) {
			MESSAGE = "复制目錄失敗：源目錄" + srcDirName + "不存在！";

			return false;
		} else if (!srcDir.isDirectory()) {
			MESSAGE = "复制目錄失敗：" + srcDirName + "不是目錄！";

			return false;
		}

		// 如果目標目錄名不是以文件分隔符結尾，則加上文件分隔符
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		File destDir = new File(destDirName);
		// 如果目標文件夾存在
		if (destDir.exists()) {
			// 如果允許覆蓋則刪除已存在的目標目錄
			if (overlay) {
				new File(destDirName).delete();
			} else {
				MESSAGE = "复制目錄失敗：目的目錄" + destDirName + "已存在！";

				return false;
			}
		} else {
			// 創建目的目錄
			System.out.println("目的目錄不存在，准備創建。。。");
			if (!destDir.mkdirs()) {
				System.out.println("复制目錄失敗：創建目的目錄失敗！");
				return false;
			}
		}

		boolean flag = true;
		File[] files = srcDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 复制文件
			if (files[i].isFile()) {
				flag = CopyFileUtil.copyFile(files[i].getAbsolutePath(), destDirName + files[i].getName(), overlay);
				if (!flag)
					break;
			} else if (files[i].isDirectory()) {
				flag = CopyFileUtil.copyDirectory(files[i].getAbsolutePath(), destDirName + files[i].getName(), overlay);
				if (!flag)
					break;
			}
		}
		if (!flag) {
			MESSAGE = "复制目錄" + srcDirName + "至" + destDirName + "失敗！";
			return false;
		} else {
			return true;
		}
	}
	public static void delDirectory(String filepath) throws IOException{   
		File f = new File(filepath);     
		if(f.exists() && f.isDirectory()){
			if(f.listFiles().length==0){
				f.delete();   
			}else{   
				File delFile[]=f.listFiles();   
				int i =f.listFiles().length;   
				for(int j=0;j<i;j++){   
					if(delFile[j].isDirectory()){   
						delDirectory(delFile[j].getAbsolutePath());   
		            }   
					delFile[j].delete();   
				}   
			}   
		}       
	} 

	public static void createDirectory(String filepath) throws IOException {
		File createDir = new File(filepath);
		if (createDir.exists())
			delDirectory(filepath);
		if (!createDir.exists())	// check 目錄
			createDir.mkdirs();
	}
	public static void main(String[] args) {
		String srcDirName = "C:/test/test0/test1";
		String destDirName = "c:/ttt";
		CopyFileUtil.copyDirectory(srcDirName, destDirName, true);
	}
}
