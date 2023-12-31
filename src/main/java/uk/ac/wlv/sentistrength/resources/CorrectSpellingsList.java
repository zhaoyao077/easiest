// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   CorrectSpellingsList.java

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
 * 正确拼写列表
 *
 * @author 13986
 * @date 2023/03/25
 */
public class CorrectSpellingsList {
  /**.
   * 正确的单词字符串数组
   */
  private String[] sgCorrectWord;
  /**.
   * 正确的单词的计数器
   */
  private int igCorrectWordCount;
  /**.
   * 正确的单词的最大个数
   */
  private int igCorrectWordMax;

  /**.
   * 构造函数
   */
  private CorrectSpellingsList() {
    igCorrectWordCount = 0;
    igCorrectWordMax = 0;
  }

  /**.
   * 构造实例
   */
  private static CorrectSpellingsList correctSpellingsList = null;

  /**.
   * 获得实例
   */
  public static CorrectSpellingsList getInstance(){
    if(correctSpellingsList == null){
      correctSpellingsList = new CorrectSpellingsList();
    }
    return correctSpellingsList;
  }

  /**.
   * 初始化EnglishWordList
   *
   * @param sFilename 源文件文件名
   * @param options   分类选项
   * @return boolean  是否初始化成功
   */
  public boolean initialise(String sFilename, ClassificationOptions options) {
    if (igCorrectWordMax > 0) {
      return true;
    }
    if (!options.bgCorrectSpellingsUsingDictionary) {
      return true;
    }
    igCorrectWordMax = FileOps.i_CountLinesInTextFile(sFilename) + 2;
    sgCorrectWord = new String[igCorrectWordMax];
    igCorrectWordCount = 0;
    File f = new File(sFilename);
    if (!f.exists()) {
      System.out.println(
              (new StringBuilder("Could not find the spellings file: "))
                      .append(sFilename)
                      .toString()
      );
      return false;
    }
    try {
      BufferedReader rReader;
      if (options.bgForceUTF8) {
        rReader = new BufferedReader(new InputStreamReader(new FileInputStream(sFilename), "UTF8"));
      } else {
        rReader = new BufferedReader(new FileReader(sFilename));
      }
      String sLine;
      while ((sLine = rReader.readLine()) != null) {
        if (!sLine.equals("")) {
          igCorrectWordCount++;
          sgCorrectWord[igCorrectWordCount] = sLine;
        }
      }
      rReader.close();
      Sort.quickSortStrings(sgCorrectWord, 1, igCorrectWordCount);
    } catch (FileNotFoundException e) {
      System.out.println(
              (new StringBuilder("Could not find the spellings file: "))
                      .append(sFilename)
                      .toString()
      );
      e.printStackTrace();
      return false;
    } catch (IOException e) {
      System.out.println(
              (new StringBuilder("Found spellings file but could not read from it: "))
                      .append(sFilename)
                      .toString()
      );
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**.
   * 判断一个单词是否为正确拼写
   *
   * @param sWord 需要判断的字符串
   * @return boolean 是否正确
   */
  public boolean correctSpelling(String sWord) {
    return Sort.i_FindStringPositionInSortedArray(sWord, sgCorrectWord, 1, igCorrectWordCount) >= 0;
  }
}
