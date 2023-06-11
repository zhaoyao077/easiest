// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   NegatingWordList.java

package uk.ac.wlv.sentistrength.resources;

import java.io.*;


import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.utilities.FileOps;
import uk.ac.wlv.utilities.Sort;

// Referenced classes of package uk.ac.wlv.sentistrength:
//            ClassificationOptions

public class NegatingWordList
{
    /**
     * 否定词数组
     */
    private String sgNegatingWord[];
    /**
     * 否定词的个数
     */
    private int igNegatingWordCount;
    /**
     * 否定词的最大个数
     */
    private int igNegatingWordMax;

    /**
     * 构造函数
     */
    private NegatingWordList()
    {
        igNegatingWordCount = 0;
        igNegatingWordMax = 0;
    }

    /**.
     * 构造实例
     */
    private static NegatingWordList negatingWordList = null;

    /**.
     * 获得实例
     */
    public static NegatingWordList getInstance(){
        if(negatingWordList == null){
            negatingWordList = new NegatingWordList();
        }
        return negatingWordList;
    }

    /**
     * 从NegatingWordList.txt初始化否定词
     * @param sFilename 文件名
     * @param options 分类选项
     * @return 是否初始化成功
     */
    public boolean initialise(String sFilename, ClassificationOptions options)
    {
        if(igNegatingWordMax > 0)
            return true;
        File f = new File(sFilename);
        if(!f.exists())
        {
            System.out.println((new StringBuilder("Could not find the negating words file: ")).append(sFilename).toString());
            return false;
        }
        igNegatingWordMax = FileOps.i_CountLinesInTextFile(sFilename) + 2;
        sgNegatingWord = new String[igNegatingWordMax];
        igNegatingWordCount = 0;
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
                    igNegatingWordCount++;
                    sgNegatingWord[igNegatingWordCount] = sLine;
                }
            rReader.close();
            Sort.quickSortStrings(sgNegatingWord, 1, igNegatingWordCount);
        }
        catch(FileNotFoundException e)
        {
            System.out.println((new StringBuilder("Could not find negating words file: ")).append(sFilename).toString());
            e.printStackTrace();
            return false;
        }
        catch(IOException e)
        {
            System.out.println((new StringBuilder("Found negating words file but could not read from it: ")).append(sFilename).toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断是否是否定词
     * <p>用utilities中的Sort类中的发现</p>
     * @param sWord 词字符串
     * @return 是否是否定词
     */
    public boolean negatingWord(String sWord)
    {
        return Sort.i_FindStringPositionInSortedArray(sWord, sgNegatingWord, 1, igNegatingWordCount) >= 0;
    }
}
