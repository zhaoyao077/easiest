// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   UnusedTermsClassificationIndex.java

package uk.ac.wlv.sentistrength;

import java.io.*;

import uk.ac.wlv.utilities.Trie;

/**
 * 未使用的术语类索引
 * related UC: UC-16,UC-19,UC-21,UC-22,UC-23,UC-24
 */

public class UnusedTermsClassificationIndex
{

    private String sgTermList[];
    private int igTermListCount;
    private int igTermListMax;
    private int igTermListLessPtr[];
    private int igTermListMorePtr[];
    private int igTermListFreq[];
    private int igTermListFreqTemp[];
    private int igTermListPosClassDiff[];
    private int iTermsAddedIDTemp[];
    private int igTermListNegClassDiff[];
    private int igTermListScaleClassDiff[];
    private int igTermListBinaryClassDiff[];
    private int igTermListTrinaryClassDiff[];
    private int iTermsAddedIDTempCount;
    private int igTermListPosCorrectClass[][];
    private int igTermListNegCorrectClass[][];
    private int igTermListScaleCorrectClass[][];
    private int igTermListBinaryCorrectClass[][];
    private int igTermListTrinaryCorrectClass[][];

    /**
     * 构造函数
     */
    public UnusedTermsClassificationIndex()
    {
        sgTermList = null;
        igTermListCount = 0;
        igTermListMax = 50000;
    }

    /**
     * main方法
     * @param args1
     */
    public static void main(String args1[])
    {
    }

    /**
     * 将当前term加入索引列表
     * @param sTerm 当前term
     */
    public void addTermToNewTermIndex(String sTerm)
    {
        if(sgTermList == null)
            initialise(true, true, true, true);
        if(sTerm == "")
            return;
        boolean bDontAddMoreElements = false;
        if(igTermListCount == igTermListMax)
            bDontAddMoreElements = true;
        int iTermID = Trie.i_GetTriePositionForString(sTerm, sgTermList, igTermListLessPtr, igTermListMorePtr, 1, igTermListCount, bDontAddMoreElements);
        if(iTermID > 0)
        {
            iTermsAddedIDTemp[++iTermsAddedIDTempCount] = iTermID;
            igTermListFreqTemp[iTermID]++;
            if(iTermID > igTermListCount)
                igTermListCount = iTermID;
        }
    }

    /**
     * add Pos + Neg to main index
     * @param iCorrectPosClass 正确的积极情绪值
     * @param iEstPosClass 预估的积极情绪值
     * @param iCorrectNegClass 正确的消极情绪值
     * @param iEstNegClass 预估的消极情绪值
     */
    public void addNewIndexToMainIndexWithPosNegValues(int iCorrectPosClass, int iEstPosClass, int iCorrectNegClass, int iEstNegClass)
    {
        if(iCorrectNegClass > 0 && iCorrectPosClass > 0)
        {
            for(int iTerm = 1; iTerm <= iTermsAddedIDTempCount; iTerm++)
            {
                int iTermID = iTermsAddedIDTemp[iTerm];
                if(igTermListFreqTemp[iTermID] != 0)
                    try
                    {
                        igTermListNegCorrectClass[iTermID][iCorrectNegClass - 1]++;
                        igTermListPosCorrectClass[iTermID][iCorrectPosClass - 1]++;
                        igTermListPosClassDiff[iTermID] += iCorrectPosClass - iEstPosClass;
                        igTermListNegClassDiff[iTermID] += iCorrectNegClass + iEstNegClass;
                        igTermListFreq[iTermID]++;
                        iTermsAddedIDTemp[iTerm] = 0;
                    }
                    catch(Exception e)
                    {
                        System.out.println((new StringBuilder("[UnusedTermsClassificationIndex] Error trying to add Pos + Neg to index. ")).append(e.getMessage()).toString());
                    }
            }

        }
        iTermsAddedIDTempCount = 0;
    }

    /**
     * add scale values to main index
     * @param iCorrectScaleClass 正确的Scale一元情绪值
     * @param iEstScaleClass 预估的Scale一元情绪值
     */
    public void addNewIndexToMainIndexWithScaleValues(int iCorrectScaleClass, int iEstScaleClass)
    {
        for(int iTerm = 1; iTerm <= iTermsAddedIDTempCount; iTerm++)
        {
            int iTermID = iTermsAddedIDTemp[iTerm];
            if(igTermListFreqTemp[iTermID] != 0)
                try
                {
                    igTermListScaleCorrectClass[iTermID][iCorrectScaleClass + 4]++;
                    igTermListScaleClassDiff[iTermID] += iCorrectScaleClass - iEstScaleClass;
                    igTermListFreq[iTermID]++;
                    iTermsAddedIDTemp[iTerm] = 0;
                }
                catch(Exception e)
                {
                    System.out.println((new StringBuilder("Error trying to add scale values to index. ")).append(e.getMessage()).toString());
                }
        }

        iTermsAddedIDTempCount = 0;
    }

    /**
     * add trinary values to main index
     * @param iCorrectTrinaryClass 正确的Trinary三元情绪值
     * @param iEstTrinaryClass 预估的Trinary三元情绪值
     */
    public void addNewIndexToMainIndexWithTrinaryValues(int iCorrectTrinaryClass, int iEstTrinaryClass)
    {
        for(int iTerm = 1; iTerm <= iTermsAddedIDTempCount; iTerm++)
        {
            int iTermID = iTermsAddedIDTemp[iTerm];
            if(igTermListFreqTemp[iTermID] != 0)
                try
                {
                    igTermListTrinaryCorrectClass[iTermID][iCorrectTrinaryClass + 1]++;
                    igTermListTrinaryClassDiff[iTermID] += iCorrectTrinaryClass - iEstTrinaryClass;
                    igTermListFreq[iTermID]++;
                    iTermsAddedIDTemp[iTerm] = 0;
                }
                catch(Exception e)
                {
                    System.out.println((new StringBuilder("Error trying to add trinary values to index. ")).append(e.getMessage()).toString());
                }
        }

        iTermsAddedIDTempCount = 0;
    }

    /**
     * add binary values to main index
     * @param iCorrectBinaryClass 正确的Binary二元情绪值
     * @param iEstBinaryClass 预估的Binary二元情绪值
     */
    public void addNewIndexToMainIndexWithBinaryValues(int iCorrectBinaryClass, int iEstBinaryClass)
    {
        for(int iTerm = 1; iTerm <= iTermsAddedIDTempCount; iTerm++)
        {
            int iTermID = iTermsAddedIDTemp[iTerm];
            if(igTermListFreqTemp[iTermID] != 0)
                try
                {
                    igTermListBinaryClassDiff[iTermID] += iCorrectBinaryClass - iEstBinaryClass;
                    if(iCorrectBinaryClass == -1)
                        iCorrectBinaryClass = 0;
                    igTermListBinaryCorrectClass[iTermID][iCorrectBinaryClass]++;
                    igTermListFreq[iTermID]++;
                    iTermsAddedIDTemp[iTerm] = 0;
                }
                catch(Exception e)
                {
                    System.out.println((new StringBuilder("Error trying to add scale values to index. ")).append(e.getMessage()).toString());
                }
        }

        iTermsAddedIDTempCount = 0;
    }

    /**
     * 初始化term列表
     * @param bInitialiseScale 是否是Scale类
     * @param bInitialisePosNeg 是否是PosNeg类
     * @param bInitialiseBinary 是否是Binary类
     * @param bInitialiseTrinary 是否是Trinary类
     */
    public void initialise(boolean bInitialiseScale, boolean bInitialisePosNeg, boolean bInitialiseBinary, boolean bInitialiseTrinary)
    {
        igTermListCount = 0;
        igTermListMax = 50000;
        iTermsAddedIDTempCount = 0;
        sgTermList = new String[igTermListMax];
        igTermListLessPtr = new int[igTermListMax + 1];
        igTermListMorePtr = new int[igTermListMax + 1];
        igTermListFreq = new int[igTermListMax + 1];
        igTermListFreqTemp = new int[igTermListMax + 1];
        iTermsAddedIDTemp = new int[igTermListMax + 1];
        if(bInitialisePosNeg)
        {
            igTermListNegCorrectClass = new int[igTermListMax + 1][5];
            igTermListPosCorrectClass = new int[igTermListMax + 1][5];
            igTermListNegClassDiff = new int[igTermListMax + 1];
            igTermListPosClassDiff = new int[igTermListMax + 1];
        }
        if(bInitialiseScale)
        {
            igTermListScaleCorrectClass = new int[igTermListMax + 1][9];
            igTermListScaleClassDiff = new int[igTermListMax + 1];
        }
        if(bInitialiseBinary)
        {
            igTermListBinaryCorrectClass = new int[igTermListMax + 1][2];
            igTermListBinaryClassDiff = new int[igTermListMax + 1];
        }
        if(bInitialiseTrinary)
        {
            igTermListTrinaryCorrectClass = new int[igTermListMax + 1][3];
            igTermListTrinaryClassDiff = new int[igTermListMax + 1];
        }
    }

    /**
     * 将Pos+Neg值输出到指定文件中
     * @param sOutputFile 输出文件名
     * @param iMinFreq 最少提升数
     */
    public void printIndexWithPosNegValues(String sOutputFile, int iMinFreq)
    {
        try
        {
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sOutputFile));
            wWriter.write((new StringBuilder("Term\tTermFreq >= ")).append(iMinFreq).append("\t").append("PosClassDiff (correct-estimate)\t").append("NegClassDiff\t").append("PosClassAvDiff\t").append("NegClassAvDiff\t").toString());
            for(int i = 1; i <= 5; i++)
                wWriter.write((new StringBuilder("CorrectClass")).append(i).append("pos\t").toString());

            for(int i = 1; i <= 5; i++)
                wWriter.write((new StringBuilder("CorrectClass")).append(i).append("neg\t").toString());

            wWriter.write("\n");
            if(igTermListCount > 0)
            {
                for(int iTerm = 1; iTerm <= igTermListCount; iTerm++)
                    if(igTermListFreq[iTerm] >= iMinFreq)
                    {
                        wWriter.write((new StringBuilder(String.valueOf(sgTermList[iTerm]))).append("\t").append(igTermListFreq[iTerm]).append("\t").append(igTermListPosClassDiff[iTerm]).append("\t").append(igTermListNegClassDiff[iTerm]).append("\t").append((float)igTermListPosClassDiff[iTerm] / (float)igTermListFreq[iTerm]).append("\t").append((float)igTermListNegClassDiff[iTerm] / (float)igTermListFreq[iTerm]).append("\t").toString());
                        for(int i = 0; i < 5; i++)
                            wWriter.write((new StringBuilder(String.valueOf(igTermListPosCorrectClass[iTerm][i]))).append("\t").toString());

                        for(int i = 0; i < 5; i++)
                            wWriter.write((new StringBuilder(String.valueOf(igTermListNegCorrectClass[iTerm][i]))).append("\t").toString());

                        wWriter.write("\n");
                    }

            } else
            {
                wWriter.write("No terms found in corpus!\n");
            }
            wWriter.close();
        }
        catch(IOException e)
        {
            System.out.println((new StringBuilder("Error printing index to ")).append(sOutputFile).toString());
            e.printStackTrace();
        }
    }

    /**
     * 将Scale值输出到指定文件中
     * @param sOutputFile 输出文件名
     * @param iMinFreq 最少提升数
     */
    public void printIndexWithScaleValues(String sOutputFile, int iMinFreq)
    {
        try
        {
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sOutputFile));
            wWriter.write("Term\tTermFreq\tScaleClassDiff (correct-estimate)\tScaleClassAvDiff\t");
            for(int i = -4; i <= 4; i++)
                wWriter.write((new StringBuilder("CorrectClass")).append(i).append("\t").toString());

            wWriter.write("\n");
            for(int iTerm = 1; iTerm <= igTermListCount; iTerm++)
                if(igTermListFreq[iTerm] > iMinFreq)
                {
                    wWriter.write((new StringBuilder(String.valueOf(sgTermList[iTerm]))).append("\t").append(igTermListFreq[iTerm]).append("\t").append(igTermListScaleClassDiff[iTerm]).append("\t").append((float)igTermListScaleClassDiff[iTerm] / (float)igTermListFreq[iTerm]).append("\t").toString());
                    for(int i = 0; i < 9; i++)
                        wWriter.write((new StringBuilder(String.valueOf(igTermListScaleCorrectClass[iTerm][i]))).append("\t").toString());

                    wWriter.write("\n");
                }

            wWriter.close();
        }
        catch(IOException e)
        {
            System.out.println((new StringBuilder("Error printing Scale index to ")).append(sOutputFile).toString());
            e.printStackTrace();
        }
    }

    /**
     * 将Trinary值输出到指定文件中
     * @param sOutputFile 输出文件名
     * @param iMinFreq 最少提升数
     */
    public void printIndexWithTrinaryValues(String sOutputFile, int iMinFreq)
    {
        try
        {
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sOutputFile));
            wWriter.write("Term\tTermFreq\tTrinaryClassDiff (correct-estimate)\tTrinaryClassAvDiff\t");
            for(int i = -1; i <= 1; i++)
                wWriter.write((new StringBuilder("CorrectClass")).append(i).append("\t").toString());

            wWriter.write("\n");
            for(int iTerm = 1; iTerm <= igTermListCount; iTerm++)
                if(igTermListFreq[iTerm] > iMinFreq)
                {
                    wWriter.write((new StringBuilder(String.valueOf(sgTermList[iTerm]))).append("\t").append(igTermListFreq[iTerm]).append("\t").append(igTermListTrinaryClassDiff[iTerm]).append("\t").append((float)igTermListTrinaryClassDiff[iTerm] / (float)igTermListFreq[iTerm]).append("\t").toString());
                    for(int i = 0; i < 3; i++)
                        wWriter.write((new StringBuilder(String.valueOf(igTermListTrinaryCorrectClass[iTerm][i]))).append("\t").toString());

                    wWriter.write("\n");
                }

            wWriter.close();
        }
        catch(IOException e)
        {
            System.out.println((new StringBuilder("Error printing Trinary index to ")).append(sOutputFile).toString());
            e.printStackTrace();
        }
    }

    /**
     * 将Binary值输出到指定文件中
     * @param sOutputFile 输出文件名
     * @param iMinFreq 最少提升数
     */
    public void printIndexWithBinaryValues(String sOutputFile, int iMinFreq)
    {
        try
        {
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sOutputFile));
            wWriter.write("Term\tTermFreq\tBinaryClassDiff (correct-estimate)\tBinaryClassAvDiff\t");
            wWriter.write("CorrectClass-1\tCorrectClass1\t");
            wWriter.write("\n");
            for(int iTerm = 1; iTerm <= igTermListCount; iTerm++)
                if(igTermListFreq[iTerm] > iMinFreq)
                {
                    wWriter.write((new StringBuilder(String.valueOf(sgTermList[iTerm]))).append("\t").append(igTermListFreq[iTerm]).append("\t").append(igTermListBinaryClassDiff[iTerm]).append("\t").append((float)igTermListBinaryClassDiff[iTerm] / (float)igTermListFreq[iTerm]).append("\t").toString());
                    for(int i = 0; i < 2; i++)
                        wWriter.write((new StringBuilder(String.valueOf(igTermListBinaryCorrectClass[iTerm][i]))).append("\t").toString());

                    wWriter.write("\n");
                }

            wWriter.close();
        }
        catch(IOException e)
        {
            System.out.println((new StringBuilder("Error printing Binary index to ")).append(sOutputFile).toString());
            e.printStackTrace();
        }
    }
}
