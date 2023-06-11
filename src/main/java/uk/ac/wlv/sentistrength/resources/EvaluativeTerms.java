// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   EvaluativeTerms.java

package uk.ac.wlv.sentistrength.resources;

import java.io.*;


import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.utilities.FileOps;

// Referenced classes of package uk.ac.wlv.sentistrength:
//            ClassificationOptions, IdiomList, SentimentWords

public class EvaluativeTerms
{
    /**
     * 对象评论的最大个数
     */
    private int igObjectEvaluationMax;
    /**
     * 对象字符串数组
     */
    public String sgObject[];
    /**
     * 对象评论数组
     */
    public String sgObjectEvaluation[];
    /**
     * 对象评论情绪强度数组
     */
    public int igObjectEvaluationStrength[];
    /**
     * 对象评论个数
     */
    public int igObjectEvaluationCount;

    /**
     * 构造函数
     */
    private EvaluativeTerms()
    {
        igObjectEvaluationMax = 0;
        igObjectEvaluationCount = 0;
    }

    /**.
     * 构造实例
     */
    private static EvaluativeTerms evaluativeTerms = null;

    /**.
     * 获得实例
     */
    public static EvaluativeTerms getInstance(){
        if(evaluativeTerms == null){
            evaluativeTerms = new EvaluativeTerms();
        }
        return evaluativeTerms;
    }

    /**
     * 初始化评论术语
     * <p>如果以“\t”分割后的字符串长度大于2，并且索引为2的位置并没有以“##”开头那么就初始化到对象评论列表</p>
     * <p>如果以"\t"分割后的字符串的索引为0的位置的字符串中是有空格的那么就初始化到短语列表</p>
     * <p>如果以“\t”分割后的字符串中没有空格那么就初始化到情感单词列表</p>
     * @param sSourceFile 源文件
     * @param options 分类选项
     * @param idiomList 短语列表
     * @param sentimentWords 情感单词列表
     * @return 是否初始化成功
     */
    public boolean initialise(String sSourceFile, ClassificationOptions options, IdiomList idiomList, SentimentWords sentimentWords)
    {
        if(igObjectEvaluationCount > 0)
            return true;
        File f = new File(sSourceFile);
        if(!f.exists())
        {
            System.out.println((new StringBuilder("Could not find additional (object/evaluation) file: ")).append(sSourceFile).toString());
            return false;
        }
        int iStrength = 0;
        boolean bIdiomsAdded = false;
        boolean bSentimentWordsAdded = false;
        try
        {
            igObjectEvaluationMax = FileOps.i_CountLinesInTextFile(sSourceFile) + 2;
            igObjectEvaluationCount = 0;
            sgObject = new String[igObjectEvaluationMax];
            sgObjectEvaluation = new String[igObjectEvaluationMax];
            igObjectEvaluationStrength = new int[igObjectEvaluationMax];
            BufferedReader rReader;
            if(options.bgForceUTF8)
                rReader = new BufferedReader(new InputStreamReader(new FileInputStream(sSourceFile), "UTF8"));
            else
                rReader = new BufferedReader(new FileReader(sSourceFile));
            String sLine;
            while((sLine = rReader.readLine()) != null) 
                if(sLine != "" && sLine.indexOf("##") != 0 && sLine.indexOf("\t") > 0)
                {
                    String sData[] = sLine.split("\t");
                    if(sData.length > 2 && sData[2].indexOf("##") != 0)
                    {
                        sgObject[++igObjectEvaluationCount] = sData[0];
                        sgObjectEvaluation[igObjectEvaluationCount] = sData[1];
                        try
                        {
                            igObjectEvaluationStrength[igObjectEvaluationCount] = Integer.parseInt(sData[2].trim());
                            if(igObjectEvaluationStrength[igObjectEvaluationCount] > 0)
                                igObjectEvaluationStrength[igObjectEvaluationCount]--;
                            else
                            if(igObjectEvaluationStrength[igObjectEvaluationCount] < 0)
                                igObjectEvaluationStrength[igObjectEvaluationCount]++;
                        }
                        catch(NumberFormatException e)
                        {
                            System.out.println("Failed to identify integer weight for object/evaluation! Ignoring object/evaluation");
                            System.out.println((new StringBuilder("Line: ")).append(sLine).toString());
                            igObjectEvaluationCount--;
                        }
                    } else
                    if(sData[0].indexOf(" ") > 0)
                        try
                        {   //如果该评论以\t分割下来左边是短语右边是强度评分就加到短语列表里
                            iStrength = Integer.parseInt(sData[1].trim());
                            idiomList.addExtraIdiom(sData[0], iStrength, false);
                            bIdiomsAdded = true;
                        }
                        catch(NumberFormatException e)
                        {
                            System.out.println("Failed to identify integer weight for idiom in additional file! Ignoring it");
                            System.out.println((new StringBuilder("Line: ")).append(sLine).toString());
                        }
                    else
                        try
                        {   //如果该评论以\t分割下来左边不是短语右边是强度评分就加到情感单词里
                            iStrength = Integer.parseInt(sData[1].trim());
                            sentimentWords.addOrModifySentimentTerm(sData[0], iStrength, false);
                            bSentimentWordsAdded = true;
                        }
                        catch(NumberFormatException e)
                        {
                            System.out.println("Failed to identify integer weight for sentiment term in additional file! Ignoring it");
                            System.out.println((new StringBuilder("Line: ")).append(sLine).toString());
                            igObjectEvaluationCount--;
                        }
                }
            rReader.close();
            if(igObjectEvaluationCount > 0)
                options.bgUseObjectEvaluationTable = true;
            if(bSentimentWordsAdded)
                sentimentWords.sortSentimentList();
            if(bIdiomsAdded)
                idiomList.convertIdiomStringsToWordLists();
        }
        catch(FileNotFoundException e)
        {
            System.out.println((new StringBuilder("Could not find additional (object/evaluation) file: ")).append(sSourceFile).toString());
            e.printStackTrace();
            return false;
        }
        catch(IOException e)
        {
            System.out.println((new StringBuilder("Found additional (object/evaluation) file but could not read from it: ")).append(sSourceFile).toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
