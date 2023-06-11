// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   BoosterWordsList.java

package uk.ac.wlv.sentistrength.resources;

//import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.utilities.FileOps;
import uk.ac.wlv.utilities.Sort;

// Referenced classes of package uk.ac.wlv.sentistrength:
//            ClassificationOptions

/**.
 * 辅助词列表
 *
 * @author 13986
 * @date 2023/03/25
 */
public class BoosterWordsList {
  /**.
   * 助词数组
   */
  private String[] sgBoosterWords;
  /**.
   * 助词强度
   */
  private int[] igBoosterWordStrength;
  /**.
   * 助词个数
   */
  private int igBoosterWordsCount;

  /**.
   * 构造函数
   */
  private BoosterWordsList(){igBoosterWordsCount = 0;}

  /**.
   * 构造实例
   */
  private static BoosterWordsList boosterWordsList = null;

  /**.
   * 获得实例
   */
  public static BoosterWordsList getInstance(){
    if(boosterWordsList == null){
      boosterWordsList = new BoosterWordsList();
    }
    return boosterWordsList;
  }

  /**
   * 从BoosterWordsList.txt中初始化助词列表
   *
   * @param sFilename                        文件名
   * @param options                          分类选项
   * @param iExtraBlankArrayEntriesToInclude 额外的空白数组位置（供新增助词）
   * @return boolean                         是否初始化成功
   */
  public boolean initialise(String sFilename,
                            ClassificationOptions options,
                            int iExtraBlankArrayEntriesToInclude) {
    int iLinesInFile = 0;
    int iWordStrength = 0;
    if (sFilename.equals("")) {
      System.out.println("No booster words file specified");
      return false;
    }
    File f = new File(sFilename);
    if (!f.exists()) {
      System.out.println(
              (new StringBuilder("Could not find booster words file: "))
              .append(sFilename).toString()
      );
      return false;
    }
    iLinesInFile = FileOps.i_CountLinesInTextFile(sFilename);
    if (iLinesInFile < 1) {
      System.out.println("No booster words specified");
      return false;
    }
    sgBoosterWords = new String[iLinesInFile + 1 + iExtraBlankArrayEntriesToInclude];
    igBoosterWordStrength = new int[iLinesInFile + 1 + iExtraBlankArrayEntriesToInclude];
    igBoosterWordsCount = 0;
    try {
      BufferedReader rReader;
      if (options.bgForceUTF8) {
        rReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(sFilename),
                        "UTF8")
        );
      } else {
        rReader = new BufferedReader(new FileReader(sFilename));
      }
      String sLine;
      while ((sLine = rReader.readLine()) != null) {
        if (!sLine.equals("")) {
          int iFirstTabLocation = sLine.indexOf("\t");
          if (iFirstTabLocation >= 0) {
            int iSecondTabLocation = sLine.indexOf("\t", iFirstTabLocation + 1);
            try {
              if (iSecondTabLocation > 0) {
                iWordStrength = Integer.parseInt(
                        sLine.substring(iFirstTabLocation + 1,
                        iSecondTabLocation)
                );
              } else {
                iWordStrength = Integer.parseInt(sLine.substring(iFirstTabLocation + 1).trim());
              }
            } catch (NumberFormatException e) {
              System.out.println("Failed to identify integer weight for booster word! "
                      + "Assuming it is zero");
              System.out.println((new StringBuilder("Line: ")).append(sLine).toString());
              iWordStrength = 0;
            }
            sLine = sLine.substring(0, iFirstTabLocation);
            if (sLine.contains(" ")) {
              sLine = sLine.trim();
            }
            if (!sLine.equals("")) {
              igBoosterWordsCount++;
              sgBoosterWords[igBoosterWordsCount] = sLine;
              igBoosterWordStrength[igBoosterWordsCount] = iWordStrength;
            }
          }
        }
      }
      Sort.quickSortStringsWithInt(sgBoosterWords, igBoosterWordStrength, 1, igBoosterWordsCount);
      rReader.close();
    } catch (FileNotFoundException e) {
      System.out.println(
              (new StringBuilder("Could not find booster words file: "))
                      .append(sFilename)
                      .toString()
      );
      e.printStackTrace();
      return false;
    } catch (IOException e) {
      System.out.println(
              (new StringBuilder("Found booster words file but could not read from it: "))
                      .append(sFilename)
                      .toString()
      );
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**.
   * 增加额外的术语
   *
   * @param sText                           文本
   * @param iWordStrength                   单词情绪强度
   * @param bSortBoosterListAfterAddingTerm 在增加术语后是否排序助词列表
   * @return boolean                        是否增加成功
   */
  public boolean addExtraTerm(String sText,
                              int iWordStrength,
                              boolean bSortBoosterListAfterAddingTerm) {
    try {
      igBoosterWordsCount++;
      sgBoosterWords[igBoosterWordsCount] = sText;
      igBoosterWordStrength[igBoosterWordsCount] = iWordStrength;
      if (bSortBoosterListAfterAddingTerm) {
        Sort.quickSortStringsWithInt(sgBoosterWords, igBoosterWordStrength, 1, igBoosterWordsCount);
      }
    } catch (Exception e) {
      System.out.println(
              (new StringBuilder("Could not add extra booster word: "))
                      .append(sText)
                      .toString()
      );
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**.
   * 使用utilities包中Sort类中的快排方法排序助词列表
   */
  public void sortBoosterWordList() {
    Sort.quickSortStringsWithInt(sgBoosterWords, igBoosterWordStrength, 1, igBoosterWordsCount);
  }

  /**.
   * 使用utilities包中的Sort类中的在有序数组中找到字符串位置得到助词情绪强度
   *
   * @param sWord 单词字符串
   * @return int  助词情绪强度
   */
  public int getBoosterStrength(String sWord) {
    int iWordID = Sort.i_FindStringPositionInSortedArray(
            sWord.toLowerCase(),
            sgBoosterWords,
            1,
            igBoosterWordsCount
    );
    if (iWordID >= 0) {
      return igBoosterWordStrength[iWordID];
    } else {
      return 0;
    }
  }
}
