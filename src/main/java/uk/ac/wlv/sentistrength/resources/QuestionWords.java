// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   QuestionWords.java

package uk.ac.wlv.sentistrength.resources;

import java.io.*;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.utilities.FileOps;
import uk.ac.wlv.utilities.Sort;

// Referenced classes of package uk.ac.wlv.sentistrength:
//            ClassificationOptions

public class QuestionWords
{
    /**
     * 疑问单词数组
     */
    private String sgQuestionWord[];
    /**
     * 疑问单词个数
     */
    private int igQuestionWordCount;
    /**
     * 疑问单词最大个数
     */
    private int igQuestionWordMax;
    /**
     * 构造函数
     */
    private QuestionWords()
    {
        igQuestionWordCount = 0;
        igQuestionWordMax = 0;
    }

    /**.
     * 构造实例
     */
    private static QuestionWords questionWords = null;

    /**.
     * 获得实例
     */
    public static QuestionWords getInstance(){
        if(questionWords == null){
            questionWords = new QuestionWords();
        }
        return questionWords;
    }

    /**
     * 根据QuestionWords.txt初始化疑问单词
     * @param sFilename 文件名
     * @param options 分类选项
     * @return 是否初始化成功
     */
    public boolean initialise(String sFilename, ClassificationOptions options)
    {
        if(igQuestionWordMax > 0)
            return true;
        File f = new File(sFilename);
        if(!f.exists())
        {
            System.out.println((new StringBuilder("Could not find the question word file: ")).append(sFilename).toString());
            return false;
        }
        igQuestionWordMax = FileOps.i_CountLinesInTextFile(sFilename) + 2;
        sgQuestionWord = new String[igQuestionWordMax];
        igQuestionWordCount = 0;
        try
        {
            BufferedReader rReader;
            if(options.bgForceUTF8)
                rReader = new BufferedReader(new InputStreamReader(new FileInputStream(sFilename), "UTF8"));
            else
                rReader = new BufferedReader(new FileReader(sFilename));
            String sLine;
            while((sLine = rReader.readLine()) != null) 
                if(sLine != "")
                {
                    igQuestionWordCount++;
                    sgQuestionWord[igQuestionWordCount] = sLine;
                }
            rReader.close();
            Sort.quickSortStrings(sgQuestionWord, 1, igQuestionWordCount);
        }
        catch(FileNotFoundException e)
        {
            System.out.println((new StringBuilder("Could not find the question word file: ")).append(sFilename).toString());
            e.printStackTrace();
            return false;
        }
        catch(IOException e)
        {
            System.out.println((new StringBuilder("Found question word file but could not read from it: ")).append(sFilename).toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断是否是疑问单词
     * <p>用utilities中Sort类的快速查找有序数组中的位置方法找到是否在数组中有该疑问词</p>
     * @param sWord 单词字符串
     * @return 是否是疑问单词
     */
    public boolean questionWord(String sWord)
    {
        return Sort.i_FindStringPositionInSortedArray(sWord, sgQuestionWord, 1, igQuestionWordCount) >= 0;
    }
}
