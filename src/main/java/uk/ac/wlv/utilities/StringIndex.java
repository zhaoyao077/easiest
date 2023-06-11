package uk.ac.wlv.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 字符串索引
 *
 * @author 13986
 * @date 2023/03/06
 */
public class StringIndex {
   /**
    * 最大文本
    */
   private int igTextMax = 10000000;
   /**
    * 文本
    */
   public String[] sgText;
   /**
    * 文本内容
    */
   public String[] sgTextComment;
   /**
    * 少指针文本
    */
   private int[] igTextLessPtr;
   /**
    * 多指针文本
    */
   private int[] igTextMorePtr;
   /**
    * 文本计数
    */
   private int[] igTextCount;
   /**
    * 文本末尾
    */
   private int igTextLast = -1;
   /**
    * 包括计数
    */
   private boolean bgIncludeCounts = false;

   /**
    * 初始化
    *
    * @param iVocabMaxIfOverrideDefault 覆盖默认最大值
    * @param bIncludeCounts             是否包括计数
    * @param bIncludeComments           是否包括内容
    */
   public void initialise(int iVocabMaxIfOverrideDefault, boolean bIncludeCounts, boolean bIncludeComments) {
      this.bgIncludeCounts = bIncludeCounts;
      if (iVocabMaxIfOverrideDefault > 0) {
         this.igTextMax = iVocabMaxIfOverrideDefault;
      }

      this.sgText = new String[this.igTextMax];
      this.igTextLessPtr = new int[this.igTextMax];
      this.igTextMorePtr = new int[this.igTextMax];
      this.igTextLast = -1;
      int i;
      if (this.bgIncludeCounts) {
         this.igTextCount = new int[this.igTextMax];

         for(i = 0; i < this.igTextMax; ++i) {
            this.igTextCount[i] = 0;
         }
      }

      if (bIncludeComments) {
         this.sgTextComment = new String[this.igTextMax];

         for(i = 0; i < this.igTextMax; ++i) {
            this.igTextCount[i] = 0;
         }
      }

   }

   /**
    * 装载
    *
    * @param sVocabTermPtrsCountFileName 词汇术语指针计数文件名
    * @return boolean
    */
   public boolean load(String sVocabTermPtrsCountFileName) {
      File f = new File(sVocabTermPtrsCountFileName);
      if (!f.exists()) {
         System.out.println("Could not find the vocab file: " + sVocabTermPtrsCountFileName);
         return false;
      } else {
         try {
            BufferedReader rReader = new BufferedReader(new InputStreamReader(new FileInputStream(sVocabTermPtrsCountFileName), "UTF8"));
            String sLine = rReader.readLine();
            String[] sData = sLine.split("\t");
            this.igTextLast = -1;

            while(rReader.ready()) {
               sLine = rReader.readLine();
               if (sLine.length() > 0) {
                  sData = sLine.split("\t");
                  if (sData.length > 2) {
                     if (this.igTextLast == this.igTextMax - 1) {
                        this.increaseArraySizes(this.igTextMax * 2);
                     }

                     this.sgText[++this.igTextLast] = sData[0];
                     this.igTextLessPtr[this.igTextLast] = Integer.parseInt(sData[1]);
                     this.igTextMorePtr[this.igTextLast] = Integer.parseInt(sData[2]);
                     this.igTextCount[this.igTextLast] = Integer.parseInt(sData[3]);
                  }
               }
            }

            rReader.close();
            return true;
         } catch (IOException var7) {
            System.out.println("Could not open file for reading or read from file: " + sVocabTermPtrsCountFileName);
            var7.printStackTrace();
            return false;
         }
      }
   }

   /**
    * 得到最后一个词id
    *
    * @return int
    */
   public int getLastWordID() {
      return this.igTextLast;
   }

   /**
    * 保存
    *
    * @param sVocabTermPtrsCountFileName 词汇术语指针计数文件名
    * @return boolean
    */
   public boolean save(String sVocabTermPtrsCountFileName) {
      if (!FileOps.backupFileAndDeleteOriginal(sVocabTermPtrsCountFileName, 10)) {
         System.out.println("Could not backup vocab! Perhaps no index exists yet.");
      }

      try {
         BufferedWriter wWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sVocabTermPtrsCountFileName), "UTF8"));
         wWriter.write("Word\tLessPtr\tMorePtr\tAllTopics");

         for(int i = 0; i <= this.igTextLast; ++i) {
            wWriter.write(this.sgText[i] + "\t" + this.igTextLessPtr[i] + "\t" + this.igTextMorePtr[i] + "\t" + this.igTextCount[i] + "\n");
         }

         wWriter.close();
         return true;
      } catch (IOException var4) {
         System.out.println("Could not open file for writing or write to file: " + sVocabTermPtrsCountFileName);
         var4.printStackTrace();
         return false;
      }
   }

   /**
    * 添加字符串
    *
    * @param sText        文本
    * @param bRecordCount 计数记录
    * @return int
    */
   public int addString(String sText, boolean bRecordCount) {
      boolean iPos = true;
      if (this.igTextLast == this.igTextMax - 1) {
         this.increaseArraySizes(this.igTextMax * 2);
      }

      int iPos1;
      if (bRecordCount) {
         iPos1 = Trie.i_GetTriePositionForStringAndAddCount(sText, this.sgText, this.igTextCount, this.igTextLessPtr, this.igTextMorePtr, 0, this.igTextLast, false, 1);
      } else {
         iPos1 = Trie.i_GetTriePositionForString(sText, this.sgText, this.igTextLessPtr, this.igTextMorePtr, 0, this.igTextLast, false);
      }

      if (iPos1 > this.igTextLast) {
         this.igTextLast = iPos1;
      }

      return iPos1;
   }

   /**
    * 找到字符串
    *
    * @param sText 文本
    * @return int
    */
   public int findString(String sText) {
      return Trie.i_GetTriePositionForString(sText, this.sgText, this.igTextLessPtr, this.igTextMorePtr, 0, this.igTextLast, true);
   }

   /**
    * 计数加一
    *
    * @param iStringPos 字符串位置
    */
   public void add1ToCount(int iStringPos) {
      int var10002 = this.igTextCount[iStringPos]++;
   }

   /**
    * 增加数组大小
    *
    * @param iNewArraySize 新数组大小
    */
   private void increaseArraySizes(int iNewArraySize) {
      if (iNewArraySize > this.igTextMax) {
         String[] sgTextTemp = new String[iNewArraySize];
         int[] iTextLessPtrTemp = new int[iNewArraySize];
         int[] iTextMorePtrTemp = new int[iNewArraySize];
         System.arraycopy(this.sgText, 0, sgTextTemp, 0, this.igTextMax);
         System.arraycopy(this.igTextLessPtr, 0, iTextLessPtrTemp, 0, this.igTextMax);
         System.arraycopy(this.igTextMorePtr, 0, iTextMorePtrTemp, 0, this.igTextMax);
         if (this.bgIncludeCounts) {
            int[] iVocabCountTemp = new int[iNewArraySize];
            System.arraycopy(this.igTextCount, 0, iVocabCountTemp, 0, this.igTextMax);

            for(int i = this.igTextMax; i < iNewArraySize; ++i) {
               this.igTextCount[i] = 0;
            }

            this.igTextCount = iVocabCountTemp;
         }

         this.igTextMax = iNewArraySize;
         this.igTextLessPtr = iTextLessPtrTemp;
         this.igTextMorePtr = iTextMorePtrTemp;
      }
   }

   /**
    * 增加数组大小
    *
    * @param sArray            数组
    * @param iCurrentArraySize 当前数组大小
    * @param iNewArraySize     新数组大小
    * @return {@link String[]}
    */
   private static String[] increaseArraySize(String[] sArray, int iCurrentArraySize, int iNewArraySize) {
      if (iNewArraySize <= iCurrentArraySize) {
         return sArray;
      } else {
         String[] sArrayTemp = new String[iNewArraySize];
         System.arraycopy(sArray, 0, sArrayTemp, 0, iCurrentArraySize);
         return sArrayTemp;
      }
   }

   /**
    * 增加数组大小
    *
    * @param iArray            数组
    * @param iCurrentArraySize 当前数组大小
    * @param iNewArraySize     新数组大小
    * @return {@link int[]}
    */
   private static int[] increaseArraySize(int[] iArray, int iCurrentArraySize, int iNewArraySize) {
      if (iNewArraySize <= iCurrentArraySize) {
         return iArray;
      } else {
         int[] iArrayTemp = new int[iNewArraySize];
         System.arraycopy(iArray, 0, iArrayTemp, 0, iCurrentArraySize);
         return iArrayTemp;
      }
   }

   /**
    * 获取字符串
    *
    * @param iStringPos 字符串位置
    * @return {@link String}
    */
   public String getString(int iStringPos) {
      return this.sgText[iStringPos];
   }

   /**
    * 获取评论
    *
    * @param iStringPos 字符串位置
    * @return {@link String}
    */
   public String getComment(int iStringPos) {
      return this.sgTextComment[iStringPos] == null ? "" : this.sgTextComment[iStringPos];
   }

   /**
    * 添加内容
    *
    * @param iStringPos 字符串位置
    * @param sComment   内容
    */
   public void addComment(int iStringPos, String sComment) {
      this.sgTextComment[iStringPos] = sComment;
   }

   /**
    * 得到计数
    *
    * @param iStringPos 字符串位置
    * @return int
    */
   public int getCount(int iStringPos) {
      return this.igTextCount[iStringPos];
   }

   /**
    * 设置计数为0
    *
    * @param iStringPos 字符串位置
    */
   public void setCountToZero(int iStringPos) {
      this.igTextCount[iStringPos] = 0;
   }

   /**
    * 设置所有计数为零
    */
   public void setAllCountsToZero() {
      for(int i = 0; i <= this.igTextLast; ++i) {
         this.igTextCount[i] = 0;
      }

   }
}
