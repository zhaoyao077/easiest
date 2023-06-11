// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   Lemmatiser.java

package uk.ac.wlv.sentistrength.resources;

import java.io.*;


import uk.ac.wlv.utilities.FileOps;
import uk.ac.wlv.utilities.Sort;

public class Lemmatiser
{
    /**
     * 单词字符串列表
     */
    private String sgWord[];
    /**
     * 引理字符串列表
     */
    private String sgLemma[];
    /**
     * 最后一个单词的索引
    */
    private int igWordLast;
    /**
     * 构造函数
     */
    private Lemmatiser()
    {
        igWordLast = -1;
    }

    /**.
     * 构造实例
     */
    private static Lemmatiser lemmatiser = null;

    /**.
     * 获得实例
     */
    public static Lemmatiser getInstance(){
        if(lemmatiser == null){
            lemmatiser = new Lemmatiser();
        }
        return lemmatiser;
    }

    /**
     * 初始化引理列表
     * @param sFileName 文件名
     * @param bForceUTF8 是否强制UTF8格式
     * @return 是否初始化成功
     */
    public boolean initialise(String sFileName, boolean bForceUTF8)
    {
        int iLinesInFile = 0;
        if(sFileName.equals(""))
        {
            System.out.println("No lemma file specified!");
            return false;
        }
        File f = new File(sFileName);
        if(!f.exists())
        {
            System.out.println((new StringBuilder("Could not find lemma file: ")).append(sFileName).toString());
            return false;
        }
        iLinesInFile = FileOps.i_CountLinesInTextFile(sFileName);
        if(iLinesInFile < 2)
        {
            System.out.println((new StringBuilder("Less than 2 lines in sentiment file: ")).append(sFileName).toString());
            return false;
        }
        sgWord = new String[iLinesInFile + 1];
        sgLemma = new String[iLinesInFile + 1];
        igWordLast = -1;
        try
        {
            BufferedReader rReader;
            if(bForceUTF8)
                rReader = new BufferedReader(new InputStreamReader(new FileInputStream(sFileName), "UTF8"));
            else
                rReader = new BufferedReader(new FileReader(sFileName));
            String sLine;
            while((sLine = rReader.readLine()) != null) 
                if(sLine != "")
                {
                    int iFirstTabLocation = sLine.indexOf("\t");
                    if(iFirstTabLocation >= 0)
                    {
                        int iSecondTabLocation = sLine.indexOf("\t", iFirstTabLocation + 1);
                        sgWord[++igWordLast] = sLine.substring(0, iFirstTabLocation);
                        if(iSecondTabLocation > 0)
                            sgLemma[igWordLast] = sLine.substring(iFirstTabLocation + 1, iSecondTabLocation);
                        else
                            sgLemma[igWordLast] = sLine.substring(iFirstTabLocation + 1);
                        if(sgWord[igWordLast].indexOf(" ") >= 0)
                            sgWord[igWordLast] = sgWord[igWordLast].trim();
                        if(sgLemma[igWordLast].indexOf(" ") >= 0)
                            sgLemma[igWordLast] = sgLemma[igWordLast].trim();
                    }
                }
            rReader.close();
            Sort.quickSortStringsWithStrings(sgWord, sgLemma, 0, igWordLast);
        }
        catch(FileNotFoundException e)
        {
            System.out.println((new StringBuilder("Couldn't find lemma file: ")).append(sFileName).toString());
            e.printStackTrace();
            return false;
        }
        catch(IOException e)
        {
            System.out.println((new StringBuilder("Found lemma file but couldn't read from it: ")).append(sFileName).toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断是否是引理
     * @param sWord 单词字符串
     * @return 引理字符串
     */
    public String lemmatise(String sWord)
    {
        int iLemmaID = Sort.i_FindStringPositionInSortedArray(sWord, sgWord, 0, igWordLast);
        if(iLemmaID >= 0)
            return sgLemma[iLemmaID];
        else
            return sWord;
    }
}
