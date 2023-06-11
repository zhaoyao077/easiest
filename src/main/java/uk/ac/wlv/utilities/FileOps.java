package uk.ac.wlv.utilities;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 文件操作
 *
 * @author 13986
 * @date 2023/03/05
 */
public class FileOps {
   /**
    * 备份文件和删除原始
    *
    * @param sFileName   文件名
    * @param iMaxBackups 最大备份
    * @return boolean
    */
   public static boolean backupFileAndDeleteOriginal(String sFileName, int iMaxBackups) {
      int iLastBackup;
      File f;
      for(iLastBackup = iMaxBackups; iLastBackup >= 0; --iLastBackup) {
         f = new File(sFileName + iLastBackup + ".bak");
         if (f.exists()) {
            break;
         }
      }

      if (iLastBackup < 1) {//f.exists()==false || iMaxBackups < 0
         f = new File(sFileName);
         if (f.exists()) {
            f.renameTo(new File(sFileName + "1.bak"));//backup1
            return true;
         } else {
            return false;
         }
      } else {//f.exists()==true
         if (iLastBackup == iMaxBackups) {
            f = new File(sFileName + iLastBackup + ".bak");
            f.delete();
            --iLastBackup;
         }

         for(int i = iLastBackup; i > 0; --i) {
            f = new File(sFileName + i + ".bak");
            f.renameTo(new File(sFileName + (i + 1) + ".bak"));
         }

         f = new File(sFileName);
         f.renameTo(new File(sFileName + "1.bak"));//移动
         return true;
      }
   }

   /**
    * 计算文本文件中的行数
    *
    * @param sFileLocation 文件位置
    * @return int
    */
   public static int i_CountLinesInTextFile(String sFileLocation) {
      int iLines = 0;

      try {
         BufferedReader rReader;
         for(rReader = new BufferedReader(new FileReader(sFileLocation)); rReader.ready(); ++iLines) {
            String sLine = rReader.readLine();
         }

         rReader.close();
         return iLines;
      } catch (FileNotFoundException var5) {
         var5.printStackTrace();
         return -1;
      } catch (IOException var6) {
         var6.printStackTrace();
         return -1;
      }
   }

   /**
    * 得到下一个可用文件名
    *
    * @param sFileNameStart 文件名开头
    * @param sFileNameEnd   文件名结尾
    * @return {@link String}
    */
   public static String getNextAvailableFilename(String sFileNameStart, String sFileNameEnd) {
      for(int i = 0; i <= 1000; ++i) {
         String sFileName = sFileNameStart + i + sFileNameEnd;
         File f = new File(sFileName);
         if (!f.isFile()) {
            return sFileName;
         }
      }

      return "";
   }

   /**
    * 切除文件扩展名
    *
    * @param sFilename 文件名
    * @return {@link String}
    */
   public static String s_ChopFileNameExtension(String sFilename) {
      if (sFilename != null && sFilename != "") {
         int iLastDotPos = sFilename.lastIndexOf(".");
         if (iLastDotPos > 0) {
            sFilename = sFilename.substring(0, iLastDotPos);
         }
      }

      return sFilename;
   }
}
