package com.mxic.oiplus.util;

import java.io.*;
import java.nio.channels.*;

public class FileUtil {

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    if (args.length == 2) {
      File f1 = new File(args[0]);
      File f2 = new File(args[1]);
      System.out.println(Copy(f1, f2));
    }
  }

  public static int Compare(File firstFile,
                            File secondFile) {
    boolean result = true;
    FileInputStream f1 = null;
    FileInputStream f2 = null;
    if ((!firstFile.exists()) || (!secondFile.exists()))
      return -1;
    if (firstFile.length() != secondFile.length())
      return 1;
    try {
      f1 = new FileInputStream(firstFile);
      f2 = new FileInputStream(secondFile);
      byte[] buf1 = new byte[4096];
      byte[] buf2 = new byte[4096];
      while (f1.read(buf1) > 0) {
        f2.read(buf2);
        for (int i=0; i<buf1.length; i++) {
          if (buf1[i] != buf2[i]) {
            result = false;
            break;
          }
        }
        if (result == false)
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
      TDSLogger.println(e);
      return -1;
    } finally {
      try {
        f1.close();
        f2.close();
      } catch (IOException e) {
      }
    }
    if (result)
      return 0;
    else
      return 1;
  }

  public static boolean Copy(String sourceFile,
                             String destinationFile) {
    File srcFile = new File(sourceFile);
    File destFile = new File(destinationFile);
    return Copy(srcFile, destFile);
  }

	public static void Delete(String sourceFile) {
		File srcFile = new File(sourceFile);
		try {
			if (srcFile.exists()) {
				srcFile.delete();
			}
		} catch (Exception e) {
			TDSLogger.println("deleteFile() error: " + e.getMessage());
			TDSLogger.println(e);
		}
	}

  public static boolean Copy(File sourceFile,
                             File destinationFile) {
    boolean result = true;
    FileChannel srcChannel = null;
    FileChannel destChannel = null;
    try {
      srcChannel = new FileInputStream(sourceFile).getChannel();
      destChannel = new FileOutputStream(destinationFile).getChannel();
      destChannel.transferFrom(srcChannel, 0, srcChannel.size());
      result = true;
    } catch (Exception e) {
      TDSLogger.println(e);
      result = false;
    } finally {
      try {
        if (srcChannel != null)
          srcChannel.close();
        if (destChannel != null)
          destChannel.close();
      } catch (Exception e) {
      }
    }
    return result;
  }
  
	public static boolean backup(String sourceFile) {
		boolean result = true;
		try {
			File srcFile = new File(sourceFile);
			File destFile = new File(sourceFile + "." + DateUtil.getNowFormat());
			srcFile.renameTo(destFile);
			result = true;
		} catch (Exception e) {
			TDSLogger.println(e);
			result = false;
		} finally {

		}
		return result;
	}
  
}
