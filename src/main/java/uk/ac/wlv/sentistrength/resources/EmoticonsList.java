// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   EmoticonsList.java

package uk.ac.wlv.sentistrength.resources;

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
 * 表情符号列表
 *
 * @author 13986
 * @date 2023/03/25
 */
public class EmoticonsList {
  /**.
   * 颜文字字符串列表
   */
  private String[] sgEmoticon;
  /**.
   * 颜文字情绪强度数组
   */
  private int[] igEmoticonStrength;
  /**.
   * 颜文字计数器
   */
  private int igEmoticonCount;
  /**.
   * 颜文字最大个数
   */
  private int igEmoticonMax;
  /**.
   * 颜文字最大个数
   */

  private EmoticonsList() {
    igEmoticonCount = 0;
    igEmoticonMax = 0;
  }

  private static EmoticonsList emoticonsList = null;

  public static EmoticonsList getInstance(){
    if(emoticonsList == null){
      emoticonsList = new EmoticonsList();
    }
    return emoticonsList;
  }

  /**.
   * 得到颜文字的情绪强度
   *
   * @param emoticon 颜文字字符串
   * @return int     颜文字的情绪强度
   */
  public int getEmoticon(String emoticon) {
    int iEmoticon = Sort.i_FindStringPositionInSortedArray(
            emoticon,
            sgEmoticon,
            1,
            igEmoticonCount
    );
    if (iEmoticon >= 0) {
      return igEmoticonStrength[iEmoticon];
    } else {
      return 999;
    }
  }

  /**.
   * 从EmoticonLookupTable.txt中初始化颜文字列表
   *
   * @param sSourceFile 源文件
   * @param options     分类选项
   * @return boolean    是否初始化成功
   */
  public boolean initialise(String sSourceFile, ClassificationOptions options) {
    if (igEmoticonCount > 0) {
      return true;
    }
    File f = new File(sSourceFile);
    if (!f.exists()) {
      System.out.println(
              (new StringBuilder("Could not find file: "))
                      .append(sSourceFile)
                      .toString()
      );
      return false;
    }
    try {
      igEmoticonMax = FileOps.i_CountLinesInTextFile(sSourceFile) + 2;
      igEmoticonCount = 0;
      String[] sEmoticonTemp = new String[igEmoticonMax];
      sgEmoticon = sEmoticonTemp;
      int[] iEmoticonStrengthTemp = new int[igEmoticonMax];
      igEmoticonStrength = iEmoticonStrengthTemp;
      BufferedReader rReader;
      if (options.bgForceUTF8) {
        rReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(sSourceFile),
                        "UTF8")
        );
      } else {
        rReader = new BufferedReader(new FileReader(sSourceFile));
      }
      String sLine;
      while ((sLine = rReader.readLine()) != null) {
        if (!sLine.equals("")) {
          String[] sData = sLine.split("\t");
          if (sData.length > 1) {
            igEmoticonCount++;
            sgEmoticon[igEmoticonCount] = sData[0];
            try {
              igEmoticonStrength[igEmoticonCount] = Integer.parseInt(sData[1].trim());
            } catch (NumberFormatException e) {
              System.out.println("Failed to identify integer weight for emoticon! "
                      + "Ignoring emoticon");
              System.out.println(
                      (new StringBuilder("Line: "))
                              .append(sLine)
                              .toString()
              );
              igEmoticonCount--;
            }
          }
        }
      }
      rReader.close();
    } catch (FileNotFoundException e) {
      System.out.println(
              (new StringBuilder("Could not find emoticon file: "))
                      .append(sSourceFile)
                      .toString()
      );
      e.printStackTrace();
      return false;
    } catch (IOException e) {
      System.out.println(
              (new StringBuilder("Found emoticon file but could not read from it: "))
                      .append(sSourceFile)
                      .toString()
      );
      e.printStackTrace();
      return false;
    }
    if (igEmoticonCount > 1) {
      Sort.quickSortStringsWithInt(sgEmoticon, igEmoticonStrength, 1, igEmoticonCount);
    }
    return true;
  }
}
