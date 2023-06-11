// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   Corpus.java

package uk.ac.wlv.sentistrength;

import java.io.*;


import uk.ac.wlv.utilities.FileOps;
import uk.ac.wlv.utilities.Sort;

// Referenced classes of package uk.ac.wlv.sentistrength:
//            ClassificationOptions, ClassificationResources, UnusedTermsClassificationIndex, Paragraph, 
//            ClassificationStatistics, SentimentWords

@SuppressWarnings("checkstyle:Indentation")

/**
 * 语料资源.
 */
public class Corpus
{
    /**
     * 分类方法选择.
     */
    public ClassificationOptions options;
    /**
     * txt资源.
     */
    public ClassificationResources resources;
    /**
     * 段落列表.
     */
    private Paragraph[] paragraph;
    /**
     * 段落数量.
     */
    private int igParagraphCount;
    /**
     * 正面情绪正确得分.
     */
    private int[] igPosCorrect;
    /**
     * 负面情绪正确得分.
     */
    private int[] igNegCorrect;
    /**
     * 三元分类正确得分.
     */
    private int[] igTrinaryCorrect;
    /**
     * 一元分类正确得分.
     */
    private int[] igScaleCorrect;
    /**
     * 正面情绪类.
     */
    private int[] igPosClass;
    /**
     * 消极情绪类.
     */
    private int[] igNegClass;
    /**
     * 三分类.
     */
    private int[] igTrinaryClass;
    /**
     * 一元类.
     */
    private int[] igScaleClass;
    /**
     * 语料分类是否完成.
     */
    private boolean bgCorpusClassified;
    /**
     * 情感词ID表.
     */
    private int[] igSentimentIDList;
    /**
     * 情感词ID表数目.
     */
    private int igSentimentIDListCount;
    /**
     * 情感词ID段落数量.
     */
    private int[] igSentimentIDParagraphCount;
    /**
     * 情绪ID表是否创建成功.
     */
    private boolean bSentimentIDListMade;
    /**
     * 未用来进行分类的词语.
     */
    UnusedTermsClassificationIndex unusedTermsClassificationIndex;
    /**
     * 子语料，true的语料用作数据集，false的用作测试集.
     */
    private boolean[] bgSupcorpusMember;
    /**
     * 子语料数量.
     */
    int igSupcorpusMemberCount;

    /**
     *构造函数，对成员变量进行初始化并赋值.
     */
    public Corpus()
    {
        options = new ClassificationOptions();
        resources = new ClassificationResources();
        igParagraphCount = 0;
        bgCorpusClassified = false;
        igSentimentIDListCount = 0;
        bSentimentIDListMade = false;
        unusedTermsClassificationIndex = null;
    }

    /**
     * 对分类的语料进行编号.
     */
    public void indexClassifiedCorpus()
    {
        unusedTermsClassificationIndex = new UnusedTermsClassificationIndex();
        if (options.bgScaleMode)
        {
            unusedTermsClassificationIndex.initialise(true, false, false, false);
            for (int i = 1; i <= igParagraphCount; i++) {
                paragraph[i].addParagraphToIndexWithScaleValues(unusedTermsClassificationIndex, igScaleCorrect[i], igScaleClass[i]);
            }
        } else
        if (options.bgTrinaryMode && options.bgBinaryVersionOfTrinaryMode)
        {
            unusedTermsClassificationIndex.initialise(false, false, true, false);
            for (int i = 1; i <= igParagraphCount; i++) {
                paragraph[i].addParagraphToIndexWithBinaryValues(unusedTermsClassificationIndex, igTrinaryCorrect[i], igTrinaryClass[i]);
            }
        } else
        if (options.bgTrinaryMode /*&& !options.bgBinaryVersionOfTrinaryMode*/)
        {
            unusedTermsClassificationIndex.initialise(false, false, false, true);
            for (int i = 1; i <= igParagraphCount; i++) {
                paragraph[i].addParagraphToIndexWithTrinaryValues(unusedTermsClassificationIndex, igTrinaryCorrect[i], igTrinaryClass[i]);
            }
        } else
        {
            unusedTermsClassificationIndex.initialise(false, true, false, false);
            for (int i = 1; i <= igParagraphCount; i++) {
                paragraph[i].addParagraphToIndexWithPosNegValues(unusedTermsClassificationIndex, igPosCorrect[i], igPosClass[i], igNegCorrect[i], igNegClass[i]);
            }
        }
    }

    /**
     * 打印语料中的词语.
     * @param saveFile 保存到的文件名
     * @param iMinFreq 最少提升数
     */
    public void printCorpusUnusedTermsClassificationIndex(String saveFile, int iMinFreq)
    {
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        if (unusedTermsClassificationIndex == null) {
            indexClassifiedCorpus();
        }
        if (options.bgScaleMode) {
            unusedTermsClassificationIndex.printIndexWithScaleValues(saveFile, iMinFreq);
        }
        else
        if (options.bgTrinaryMode && options.bgBinaryVersionOfTrinaryMode) {
            unusedTermsClassificationIndex.printIndexWithBinaryValues(saveFile, iMinFreq);
        }
        else
        if (options.bgTrinaryMode /*&& !options.bgBinaryVersionOfTrinaryMode*/) {
            unusedTermsClassificationIndex.printIndexWithTrinaryValues(saveFile, iMinFreq);
        }
        else {
            unusedTermsClassificationIndex.printIndexWithPosNegValues(saveFile, iMinFreq);
        }
        System.out.println((new StringBuilder("Term weights saved to ")).append(saveFile));
    }

    /**
     * 设置子语料.
     * @param bSubcorpusMember 布尔列表，表示是否包含某个子语料成员
     */
    public void setSubcorpus(boolean[] bSubcorpusMember)
    {
        igSupcorpusMemberCount = 0;
        for (int i = 0; i <= igParagraphCount; i++)
            if (bSubcorpusMember[i])
            {
                bgSupcorpusMember[i] = true;
                igSupcorpusMemberCount++;
            } else
            {
                bgSupcorpusMember[i] = false;
            }

    }

    /**
     * 使用整个语料而不是子语料.
     */
    public void useWholeCorpusNotSubcorpus()
    {
        for (int i = 0; i <= igParagraphCount; i++)
        {
            bgSupcorpusMember[i] = true;
        }
        igSupcorpusMemberCount = igParagraphCount;
    }

    /**
     * 获取语料集的大小.
     * @return igParagraphCount 段落的数量
     */
    public int getCorpusSize()
    {
        return igParagraphCount;
    }

    /**
     * 设置单个文本作为语料.
     * @param sText 文本
     * @param iPosCorrect 正面情绪得分
     * @param iNegCorrect 负面情绪得分
     * @return 是否设置成功
     */
    public boolean setSingleTextAsCorpus(String sText, int iPosCorrect, int iNegCorrect)
    {   //如果资源为空或者资源没有选择分类方法
        if (resources == null && !resources.initialise(options)) {
            return false;
        }
        igParagraphCount = 2;
        paragraph = new Paragraph[igParagraphCount];
        igPosCorrect = new int[igParagraphCount];
        igNegCorrect = new int[igParagraphCount];
        igTrinaryCorrect = new int[igParagraphCount];
        igScaleCorrect = new int[igParagraphCount];
        bgSupcorpusMember = new boolean[igParagraphCount];
        igParagraphCount = 1;
        paragraph[igParagraphCount] = new Paragraph();
        paragraph[igParagraphCount].setParagraph(sText, resources, options);
        igPosCorrect[igParagraphCount] = iPosCorrect;
        //将负面情绪得分取绝对值
        if (iNegCorrect < 0) {
            iNegCorrect *= -1;
        }
        igNegCorrect[igParagraphCount] = iNegCorrect;
        useWholeCorpusNotSubcorpus();
        return true;
    }

    /**
     * 设置语料.
     * @param sInFilenameAndPath 输入文件名和路径
     * @return 是否成功设置语料
     */
    public boolean setCorpus(String sInFilenameAndPath)
    {   //如果资源为空或者资源初始化失败
        if (resources == null && !resources.initialise(options)) {
            return false;
        }
        igParagraphCount = FileOps.i_CountLinesInTextFile(sInFilenameAndPath) + 1;
        //因为FileOps.i_CountLinesInTextFile(sInFilenameAndPath)获取的行数会多一行
        //如果实际行数为0，那么设置失败
        if (igParagraphCount <= 2)
        {
            igParagraphCount = 0;
            return false;
        }
        paragraph = new Paragraph[igParagraphCount];
        igPosCorrect = new int[igParagraphCount];
        igNegCorrect = new int[igParagraphCount];
        igTrinaryCorrect = new int[igParagraphCount];
        igScaleCorrect = new int[igParagraphCount];
        bgSupcorpusMember = new boolean[igParagraphCount];
        igParagraphCount = 0;
        try
        {
            BufferedReader rReader = new BufferedReader(new FileReader(sInFilenameAndPath));
            String sLine;
            if (rReader.ready()){
                sLine = rReader.readLine();
            }
            //逐行读取文本
            while ((sLine = rReader.readLine()) != null)
                if (!sLine.equals("")/* != "" */)
                {
                    paragraph[++igParagraphCount] = new Paragraph();
                    //计算最后一个Tab的索引值
                    int iLastTabPos = sLine.lastIndexOf("\t");
                    //计算第一个Tab的索引值
                    int iFirstTabPos = sLine.indexOf("\t");
                    if (iFirstTabPos < iLastTabPos || iFirstTabPos > 0 && (options.bgTrinaryMode || options.bgScaleMode))
                    {
                        paragraph[igParagraphCount].setParagraph(sLine.substring(iLastTabPos + 1), resources, options);
                        if (options.bgTrinaryMode)
                        {
                            try
                            {
                                igTrinaryCorrect[igParagraphCount] = Integer.parseInt(sLine.substring(0, iFirstTabPos).trim());
                            }
                            catch(Exception e)
                            {
                                System.out.println((new StringBuilder("Trinary classification could not be read and will be ignored!: ")).append(sLine));
                                igTrinaryCorrect[igParagraphCount] = 999;
                            }
                            //如果得分超过范围
                            if (igTrinaryCorrect[igParagraphCount] > 1 || igTrinaryCorrect[igParagraphCount] < -1)
                            {
                                System.out.println((new StringBuilder("Trinary classification out of bounds and will be ignored!: ")).append(sLine));
                                igParagraphCount--;
                            } else
                            if (options.bgBinaryVersionOfTrinaryMode && igTrinaryCorrect[igParagraphCount] == 0) {
                                System.out.println((new StringBuilder("Warning, unexpected 0 in binary classification!: ")).append(sLine));
                            }
                        } else
                        if (options.bgScaleMode)
                        {
                            try
                            {
                                igScaleCorrect[igParagraphCount] = Integer.parseInt(sLine.substring(0, iFirstTabPos).trim());
                            }
                            catch (Exception e)
                            {
                                System.out.println((new StringBuilder("Scale classification could not be read and will be ignored!: ")).append(sLine));
                                igScaleCorrect[igParagraphCount] = 999;
                            }//如果得分超过范围
                            if (igScaleCorrect[igParagraphCount] > 4 || igTrinaryCorrect[igParagraphCount] < -4)
                            {
                                System.out.println((new StringBuilder("Scale classification out of bounds (-4 to +4) and will be ignored!: ")).append(sLine));
                                igParagraphCount--;
                            }
                        } else
                        {
                            try
                            {
                                igPosCorrect[igParagraphCount] = Integer.parseInt(sLine.substring(0, iFirstTabPos).trim());
                                igNegCorrect[igParagraphCount] = Integer.parseInt(sLine.substring(iFirstTabPos + 1, iLastTabPos).trim());
                                if(igNegCorrect[igParagraphCount] < 0) {
                                    igNegCorrect[igParagraphCount] = -igNegCorrect[igParagraphCount];
                                }
                            }
                            catch (Exception e)
                            {
                                System.out.println((new StringBuilder("Positive or negative classification could not be read and will be ignored!: ")).append(sLine).toString());
                                igPosCorrect[igParagraphCount] = 0;
                            }
                            //pos和neg都要在（0，5）的范围之内
                            if (igPosCorrect[igParagraphCount] > 5 || igPosCorrect[igParagraphCount] < 1)
                            {
                                System.out.println((new StringBuilder("Warning, positive classification out of bounds and line will be ignored!: ")).append(sLine).toString());
                                igParagraphCount--;
                            } else
                            if (igNegCorrect[igParagraphCount] > 5 || igNegCorrect[igParagraphCount] < 1)
                            {
                                System.out.println((new StringBuilder("Warning, negative classification out of bounds (must be 1,2,3,4, or 5, with or without -) and line will be ignored!: ")).append(sLine).toString());
                                igParagraphCount--;
                            }
                        }
                    } else
                    {
                        if (iFirstTabPos >= 0)
                        {
                            if (options.bgTrinaryMode) {
                                igTrinaryCorrect[igParagraphCount] = Integer.parseInt(sLine.substring(0, iFirstTabPos).trim());
                            }
                            sLine = sLine.substring(iFirstTabPos + 1);
                        } else
                        if (options.bgTrinaryMode) {
                            igTrinaryCorrect[igParagraphCount] = 0;
                        }
                        paragraph[igParagraphCount].setParagraph(sLine, resources, options);
                        igPosCorrect[igParagraphCount] = 0;
                        igNegCorrect[igParagraphCount] = 0;
                    }
                }
            rReader.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (IOException e)//和上面一个catch的内容一致
        {
            e.printStackTrace();
            return false;
        }
        useWholeCorpusNotSubcorpus();
        System.out.println((new StringBuilder("Number of texts in corpus: ")).append(igParagraphCount).toString());
        return true;
    }

    /**
     * 对resource进行初始化.
     * @return 是否初始化成功
     */
    public boolean initialise()
    {
        return resources.initialise(options);
    }

    /**
     * 重新计算语料情绪得分.
     */
    public void reCalculateCorpusSentimentScores()
    {
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                paragraph[i].recalculateParagraphSentimentScores();
            }
        }
        calculateCorpusSentimentScores();
    }

    /**
     * 获取第i个段落的正面情绪得分.
     * @param i 索引值
     * @return 正面情绪得分
     */
    public int getCorpusMemberPositiveSentimentScore(int i)
    {
        if (i < 1 || i > igParagraphCount) {
            return 0;
        }
        else {
            return paragraph[i].getParagraphPositiveSentiment();
        }
    }
    /**
     * 获取某个段落中负面情绪得分.
     * @param i 段落索引
     * @return 负面情绪得分
     */
    public int getCorpusMemberNegativeSentimentScore(int i)
    {
        if (i < 1 || i > igParagraphCount) {
            return 0;
        }
        else {
            return paragraph[i].getParagraphNegativeSentiment();
        }
    }
    /**
     * 计算语料的情绪得分.
     */
    public void calculateCorpusSentimentScores()
    {
        if (igParagraphCount == 0) {
            return;
        }
        if (igPosClass == null || igPosClass.length < igPosCorrect.length)
        {
            igPosClass = new int[igParagraphCount + 1];
            igNegClass = new int[igParagraphCount + 1];
            igTrinaryClass = new int[igParagraphCount + 1];
            igScaleClass = new int[igParagraphCount + 1];
        }
        for (int i = 1; i <= igParagraphCount; i++)
            if (bgSupcorpusMember[i])
            {
                igPosClass[i] = paragraph[i].getParagraphPositiveSentiment();
                igNegClass[i] = paragraph[i].getParagraphNegativeSentiment();
                if (options.bgTrinaryMode) {
                    igTrinaryClass[i] = paragraph[i].getParagraphTrinarySentiment();
                }
                if (options.bgScaleMode) {
                    igScaleClass[i] = paragraph[i].getParagraphScaleSentiment();
                }
            }

        bgCorpusClassified = true;
    }

    /**
     * 权重改变后将已分类的语料重新进行分类.
     * @param iSentimentWordID 情感词的id
     * @param iMinParasToContainWord 最小包含情感词的段落
     */
    public void reClassifyClassifiedCorpusForSentimentChange(int iSentimentWordID, int iMinParasToContainWord)
    {   //如果段落数为0
        if (igParagraphCount == 0) {
            return;
        }
        if (!bSentimentIDListMade) {
            makeSentimentIDListForCompleteCorpusIgnoringSubcorpus();
        }
        int iSentimentWordIDArrayPos = Sort.i_FindIntPositionInSortedArray(iSentimentWordID, igSentimentIDList, 1, igSentimentIDListCount);
        if (iSentimentWordIDArrayPos == -1 || igSentimentIDParagraphCount[iSentimentWordIDArrayPos] < iMinParasToContainWord) {
            return;
        }
        igPosClass = new int[igParagraphCount + 1];
        igNegClass = new int[igParagraphCount + 1];
        if (options.bgTrinaryMode) {
            igTrinaryClass = new int[igParagraphCount + 1];
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                paragraph[i].reClassifyClassifiedParagraphForSentimentChange(iSentimentWordID);
                igPosClass[i] = paragraph[i].getParagraphPositiveSentiment();
                igNegClass[i] = paragraph[i].getParagraphNegativeSentiment();
                if (options.bgTrinaryMode) {
                    igTrinaryClass[i] = paragraph[i].getParagraphTrinarySentiment();
                }
                if (options.bgScaleMode) {
                    igScaleClass[i] = paragraph[i].getParagraphScaleSentiment();
                }
            }
        }
        bgCorpusClassified = true;
    }

    /**
     * 打印语料情绪得分.
     * @param sOutFilenameAndPath 输出文件名和路径
     * @return 是否打印成功
     */
    public boolean printCorpusSentimentScores(String sOutFilenameAndPath)
    {    //如果语料没有被分类，那么就进行分类
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        try
        {  //将情绪得分写入输出文件
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sOutFilenameAndPath));
            wWriter.write("Correct+\tCorrect-\tPredict+\tPredict-\tText\n");
            for (int i = 1; i <= igParagraphCount; i++) {
                if (bgSupcorpusMember[i]) {
                    wWriter.write((new StringBuilder(String.valueOf(igPosCorrect[i]))).append("\t").append(igNegCorrect[i]).append("\t").append(igPosClass[i]).append("\t").append(igNegClass[i]).append("\t").append(paragraph[i].getTaggedParagraph()).append("\n").toString());
                }
            }
            wWriter.close();
        }
        catch (FileNotFoundException e)
        {   //文件没找到
            e.printStackTrace();
            return false;
        }
        catch (IOException e)
        {  //IO异常
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 计算积极情绪分类的正确比例.
     * @return 准确性得分
     */
    public float getClassificationPositiveAccuracyProportion()
    {
        if (igSupcorpusMemberCount == 0) {
            return 0.0F;
        }
        else {
            return (float) getClassificationPositiveNumberCorrect() / (float) igSupcorpusMemberCount;
        }
    }
    /**
     * 计算消极情绪分类的正确比例.
     * @return 准确性得分
     */
    public float getClassificationNegativeAccuracyProportion() {
        if (igSupcorpusMemberCount == 0) {
            return 0.0F;
        } else {
            return (float) getClassificationNegativeNumberCorrect() / (float) igSupcorpusMemberCount;
        }
    }
    /**
     * 计算消极情绪分类准确性的基准.
     * @return 最低标准
     */
    public double getBaselineNegativeAccuracyProportion()
    {
        if (igParagraphCount == 0) {
            return 0.0D;
        }
        else {
            return ClassificationStatistics.baselineAccuracyMajorityClassProportion(igNegCorrect, igParagraphCount);
        }
    }
    /**
     * 计算积极情绪分类准确性的基准.
     * @return 最低标准
     */
    public double getBaselinePositiveAccuracyProportion()
    {
        if (igParagraphCount == 0) {
            return 0.0D;
        }
        else {
            return ClassificationStatistics.baselineAccuracyMajorityClassProportion(igPosCorrect, igParagraphCount);
        }
    }

    /**
     *计算消极情绪分类的正确数量.
     * @return 正确数量
     */
    public int getClassificationNegativeNumberCorrect()
    {
        if (igParagraphCount == 0) {
            return 0;
        }
        int iMatches = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i] && igNegCorrect[i] == -igNegClass[i]) {
                iMatches++;
            }
        }
        return iMatches;
    }
    /**
     *计算积极情绪分类的正确数量.
     * @return 正确数量
     */
    public int getClassificationPositiveNumberCorrect()
    {
        if (igParagraphCount == 0) {
            return 0;
        }
        int iMatches = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i] && igPosCorrect[i] == igPosClass[i]) {
                iMatches++;
            }
        }
        return iMatches;
    }

    /**
     * 计算积极情绪分类的平均误差.
     * @return 平均误差值
     */
    public double getClassificationPositiveMeanDifference()
    {
        if (igParagraphCount == 0) {
            return 0.0D;
        }
        double fTotalDiff = 0.0D;
        int iTotal = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                fTotalDiff += Math.abs(igPosCorrect[i] - igPosClass[i]);
                iTotal++;
            }
        }
        if (iTotal > 0) {
            return fTotalDiff / (double) iTotal;
        }
        else {
            return 0.0D;
        }
    }
    /**
     * 计算积极情绪分类的总误差.
     * @return 总误差值
     */
    public int getClassificationPositiveTotalDifference()
    {
        if (igParagraphCount == 0) {
            return 0;
        }
        int iTotalDiff = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                iTotalDiff += Math.abs(igPosCorrect[i] - igPosClass[i]);
            }
        }
        return iTotalDiff;
    }

    /**
     * 计算三元分类法分类的正确量.
     * @return 正确量
     */
    public int getClassificationTrinaryNumberCorrect()
    {
        if (igParagraphCount == 0) {
            return 0;
        }
        int iTrinaryCorrect = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i] && igTrinaryCorrect[i] == igTrinaryClass[i]) {
                iTrinaryCorrect++;
            }
        }
        return iTrinaryCorrect;
    }

    /**
     * 计算整个语料一元分类的数据相关性.
     * @return 数据相关性
     */
    public float getClassificationScaleCorrelationWholeCorpus()
    {
        if (igParagraphCount == 0) {
            return 0.0F;
        }
        else {
            return (float) ClassificationStatistics.correlation(igScaleCorrect, igScaleClass, igParagraphCount);
        }
    }

    /**
     * 计算一元分类的正确比例.
     * @return 一元分类的正确比例
     */
    public float getClassificationScaleAccuracyProportion()
    {
        if (igSupcorpusMemberCount == 0) {
            return 0.0F;
        }
        else {
            return (float) getClassificationScaleNumberCorrect() / (float) igSupcorpusMemberCount;
        }
    }

    /**
     * 计算整个语料积极情绪分类的相关性.
     * @return 整个语料积极情绪的相关性
     */
    public float getClassificationPosCorrelationWholeCorpus()
    {
        if (igParagraphCount == 0) {
            return 0.0F;
        }
        else {
            return (float) ClassificationStatistics.correlationAbs(igPosCorrect, igPosClass, igParagraphCount);
        }
    }

    /**
     * 计算整个语料消极情绪分类的正确性.
     * @return 整个语料消极情绪的正确比例
     */
    public float getClassificationNegCorrelationWholeCorpus()
    {
        if (igParagraphCount == 0)
            return 0.0F;
        else
            return (float)ClassificationStatistics.correlationAbs(igNegCorrect, igNegClass, igParagraphCount);
    }

    /**
     * 计算一元分类的正确数目.
     * @return 一元分类的正确数目
     */
    public int getClassificationScaleNumberCorrect()
    {
        if (igParagraphCount == 0)
            return 0;
        int iScaleCorrect = 0;
        if (!bgCorpusClassified)
            calculateCorpusSentimentScores();
        for (int i = 1; i <= igParagraphCount; i++)
            if (bgSupcorpusMember[i] && igScaleCorrect[i] == igScaleClass[i])
                iScaleCorrect++;

        return iScaleCorrect;
    }

    /**
     * 计算消极情绪分类的总误差.
     * @return 总误差量
     */
    public int getClassificationNegativeTotalDifference()
    {
        if (igParagraphCount == 0)
            return 0;
        int iTotalDiff = 0;
        if (!bgCorpusClassified)
            calculateCorpusSentimentScores();
        for (int i = 1; i <= igParagraphCount; i++)
            if(bgSupcorpusMember[i])
                iTotalDiff += Math.abs(igNegCorrect[i] + igNegClass[i]);

        return iTotalDiff;
    }

    /**
     * 计算消极情绪分类的平均误差.
     * @return 平均误差量
     */
    public double getClassificationNegativeMeanDifference()
    {
        if (igParagraphCount == 0)
            return 0.0D;
        double fTotalDiff = 0.0D;
        int iTotal = 0;
        if (!bgCorpusClassified)
            calculateCorpusSentimentScores();
        for (int i = 1; i <= igParagraphCount; i++)
            if (bgSupcorpusMember[i])
            {
                fTotalDiff += Math.abs(igNegCorrect[i] + igNegClass[i]);
                iTotal++;
            }

        if (iTotal > 0)
            return fTotalDiff / (double)iTotal;
        else
            return 0.0D;
    }

    /**
     * 打印分类结果（未实现）.
     * @param sOutFilenameAndPath 文件名和路径
     * @return 是否打印成功
     */
    public boolean printClassificationResultsSummary_NOT_DONE(String sOutFilenameAndPath)
    {
        if (!bgCorpusClassified)
            calculateCorpusSentimentScores();
        try
        {
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sOutFilenameAndPath));
            for (int i = 1; i <= igParagraphCount; i++)
            {
                boolean _tmp = bgSupcorpusMember[i];
            }

            wWriter.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (IOException e)//
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 针对整个语料制作情绪ID表.
     */
    public void makeSentimentIDListForCompleteCorpusIgnoringSubcorpus()
    {
        igSentimentIDListCount = 0;
        for (int i = 1; i <= igParagraphCount; i++)
        {
            paragraph[i].makeSentimentIDList();
            if (paragraph[i].getSentimentIDList() != null)
                igSentimentIDListCount += paragraph[i].getSentimentIDList().length;
        }

        if (igSentimentIDListCount > 0)
        {
            igSentimentIDList = new int[igSentimentIDListCount + 1];
            igSentimentIDParagraphCount = new int[igSentimentIDListCount + 1];
            igSentimentIDListCount = 0;
            for (int i = 1; i <= igParagraphCount; i++)
            {
                int sentenceIDList[] = paragraph[i].getSentimentIDList();
                if (sentenceIDList != null)
                {
                    for (int j = 0; j < sentenceIDList.length; j++) {
                        if (sentenceIDList[j] != 0) {
                            igSentimentIDList[++igSentimentIDListCount] = sentenceIDList[j];
                        }
                    }
                }
            }

            Sort.quickSortInt(igSentimentIDList, 1, igSentimentIDListCount);
            for (int i = 1; i <= igParagraphCount; i++)
            {
                int sentenceIDList[] = paragraph[i].getSentimentIDList();
                if (sentenceIDList != null)
                {
                    for (int j = 0; j < sentenceIDList.length; j++) {
                        if (sentenceIDList[j] != 0) {
                            igSentimentIDParagraphCount[Sort.i_FindIntPositionInSortedArray(sentenceIDList[j], igSentimentIDList, 1, igSentimentIDListCount)]++;
                        }
                    }
                }
            }

        }
        bSentimentIDListMade = true;
    }

    /**
     * 多次运行十折交叉验证，将结果写到BufferedWriter中.
     * @param iMinImprovement 最小提高数
     * @param bUseTotalDifference 是否用总误差量
     * @param iReplications 重复运行次数
     * @param iMultiOptimisations 优化次数
     * @param sWriter 写文件
     * @param wTermStrengthWriter 写单词的情绪得分
     */
    private void run10FoldCrossValidationMultipleTimes(int iMinImprovement, boolean bUseTotalDifference, int iReplications, int iMultiOptimisations, BufferedWriter sWriter, BufferedWriter wTermStrengthWriter)
    {
        for (int i = 1; i <= iReplications; i++) {
            run10FoldCrossValidationOnce(iMinImprovement, bUseTotalDifference, iMultiOptimisations, sWriter, wTermStrengthWriter);
        }
        System.out.println((new StringBuilder("Set of ")).append(iReplications).append(" 10-fold cross validations finished").toString());
    }

    /**
     * 多次运行十折交叉验证，将结果输出到文件中.
     * @param iMinImprovement 最小变化量
     * @param bUseTotalDifference 是否用总误差量
     * @param iReplications 重复运行次数
     * @param iMultiOptimisations 优化次数
     * @param sOutFileName 输出文件名
     */
    public void run10FoldCrossValidationMultipleTimes(int iMinImprovement, boolean bUseTotalDifference, int iReplications, int iMultiOptimisations, String sOutFileName)
    {
        try
        {
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sOutFileName));
            BufferedWriter wTermStrengthWriter = new BufferedWriter(new FileWriter((new StringBuilder(String.valueOf(FileOps.s_ChopFileNameExtension(sOutFileName)))).append("_termStrVars.txt").toString()));
            options.printClassificationOptionsHeadings(wWriter);
            writeClassificationStatsHeadings(wWriter);
            options.printClassificationOptionsHeadings(wTermStrengthWriter);
            resources.sentimentWords.printSentimentTermsInSingleHeaderRow(wTermStrengthWriter);
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wWriter, wTermStrengthWriter);
            wWriter.close();
            wTermStrengthWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 对inputFile中的所有行进行分类，其中text所在列为iTextCol，id所在列为iIDCol，输出文件中只包含id.
     * @param sInputFile 输入文件
     * @param iTextCol text所在列
     * @param iIDCol id所在列
     * @param sOutputFile 输出文件
     */
    public void classifyAllLinesAndRecordWithID(String sInputFile, int iTextCol, int iIDCol, String sOutputFile)
    {
        int iPos = 0;
        int iNeg = 0;
        int iTrinary = -3;
        int iScale = -10;
        int iCount1 = 0;
        String sLine = "";
        try
        {
            BufferedReader rReader = new BufferedReader(new FileReader(sInputFile));
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sOutputFile));
            while (rReader.ready())
            {
                sLine = rReader.readLine();
                iCount1++;
                if (sLine != "")
                {
                    String []sData = sLine.split("\t");
                    if (sData.length > iTextCol && sData.length > iIDCol)
                    {
                        Paragraph paragraph = new Paragraph();
                        paragraph.setParagraph(sData[iTextCol], resources, options);
                        if (options.bgTrinaryMode)
                        {
                            iTrinary = paragraph.getParagraphTrinarySentiment();
                            wWriter.write((new StringBuilder(String.valueOf(sData[iIDCol]))).append("\t").append(iTrinary).append("\n").toString());
                        } else
                        if (options.bgScaleMode)
                        {
                            iScale = paragraph.getParagraphScaleSentiment();
                            wWriter.write((new StringBuilder(String.valueOf(sData[iIDCol]))).append("\t").append(iScale).append("\n").toString());
                        } else
                        {
                            iPos = paragraph.getParagraphPositiveSentiment();
                            iNeg = paragraph.getParagraphNegativeSentiment();
                            wWriter.write((new StringBuilder(String.valueOf(sData[iIDCol]))).append("\t").append(iPos).append("\t").append(iNeg).append("\n").toString());
                        }
                    }
                }
            }
            Thread.sleep(10L);
            if (rReader.ready()) {
                System.out.println("Reader ready again after pause!");
            }
            int character;
            if ((character = rReader.read()) != -1) {
                System.out.println((new StringBuilder("Reader returns char after reader.read() false! ")).append(character).toString());
            }
            rReader.close();
            wWriter.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println((new StringBuilder("Could not find input file: ")).append(sInputFile).toString());
            e.printStackTrace();
        }
        catch (IOException e)
        {
            System.out.println((new StringBuilder("Error reading or writing from file: ")).append(sInputFile).toString());
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.out.println((new StringBuilder("Error reading from or writing to file: ")).append(sInputFile).toString());
            e.printStackTrace();
        }
        System.out.println((new StringBuilder("Processed ")).append(iCount1).append(" lines from file: ").append(sInputFile).append(". Last line was:\n").append(sLine).toString());
    }

    /**
     * 对输入文件中的所有行进行标注.
     * @param sInputFile 输入文件
     * @param iTextCol 第几列（以\t分割）
     */
    public void annotateAllLinesInInputFile(String sInputFile, int iTextCol)
    {
        int iPos = 0;
        int iNeg = 0;
        int iTrinary = -3;
        int iScale = -10;
        String sTempFile = (new StringBuilder(String.valueOf(sInputFile))).append("_temp").toString();
        try
        {
            BufferedReader rReader = new BufferedReader(new FileReader(sInputFile));
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sTempFile));
            while (rReader.ready())
            {
                String sLine = rReader.readLine();
                if (sLine != "")
                {
                    String sData[] = sLine.split("\t");
                    if (sData.length > iTextCol)
                    {
                        Paragraph paragraph = new Paragraph();
                        paragraph.setParagraph(sData[iTextCol], resources, options);
                        if (options.bgTrinaryMode)
                        {
                            iTrinary = paragraph.getParagraphTrinarySentiment();
                            wWriter.write((new StringBuilder(String.valueOf(sLine))).append("\t").append(iTrinary).append("\n").toString());
                        } else
                        if (options.bgScaleMode)
                        {
                            iScale = paragraph.getParagraphScaleSentiment();
                            wWriter.write((new StringBuilder(String.valueOf(sLine))).append("\t").append(iScale).append("\n").toString());
                        } else
                        {
                            iPos = paragraph.getParagraphPositiveSentiment();
                            iNeg = paragraph.getParagraphNegativeSentiment();
                            wWriter.write((new StringBuilder(String.valueOf(sLine))).append("\t").append(iPos).append("\t").append(iNeg).append("\n").toString());
                        }
                    } else
                    {
                        wWriter.write((new StringBuilder(String.valueOf(sLine))).append("\n").toString());
                    }
                }
            }
            rReader.close();
            wWriter.close();
            File original = new File(sInputFile);
            original.delete();
            File newFile = new File(sTempFile);
            newFile.renameTo(original);
        }
        catch (FileNotFoundException e)
        {
            System.out.println((new StringBuilder("Could not find input file: ")).append(sInputFile).toString());
            e.printStackTrace();
        }
        catch (IOException e)
        {
            System.out.println((new StringBuilder("Error reading or writing from file: ")).append(sInputFile).toString());
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.out.println((new StringBuilder("Error reading from or writing to file: ")).append(sInputFile).toString());
            e.printStackTrace();
        }
    }

    /**
     * 分对输入文件中的所有行进行标注，输出到另一文件中.
     * @param sInputFile 输入文件
     * @param iTextCol text所在列
     * @param sOutputFile 输出文件
     */
    public void classifyAllLinesInInputFile(String sInputFile, int iTextCol, String sOutputFile)
    {
        int iPos = 0;
        int iNeg = 0;
        int iTrinary = -3;
        int iScale = -10;
        int iFileTrinary = -2;
        int iFileScale = -9;
        int iClassified = 0;
        int iCorrectPosCount = 0;
        int iCorrectNegCount = 0;
        int iCorrectTrinaryCount = 0;
        int iCorrectScaleCount = 0;
        int iPosAbsDiff = 0;
        int iNegAbsDiff = 0;
        int [][]confusion = {
                new int[3], new int[3], new int[3]
        };
        int maxClassifyForCorrelation = 20000;
        int []iPosClassCorr = new int[maxClassifyForCorrelation];
        int []iNegClassCorr = new int[maxClassifyForCorrelation];
        int []iPosClassPred = new int[maxClassifyForCorrelation];
        int []iNegClassPred = new int[maxClassifyForCorrelation];
        int []iScaleClassCorr = new int[maxClassifyForCorrelation];
        int []iScaleClassPred = new int[maxClassifyForCorrelation];
        String sRationale = "";
        String sOutput = "";
        try
        {
            BufferedReader rReader;
            BufferedWriter wWriter;
            if (options.bgForceUTF8)
            {
                wWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sOutputFile), "UTF8"));
                rReader = new BufferedReader(new InputStreamReader(new FileInputStream(sInputFile), "UTF8"));
            } else
            {
                wWriter = new BufferedWriter(new FileWriter(sOutputFile));
                rReader = new BufferedReader(new FileReader(sInputFile));
            }
            if (options.bgTrinaryMode || options.bgScaleMode) {
                wWriter.write("Overall\tText");
            }
            else
            if (options.bgTensiStrength) {
                wWriter.write("Relax\tStress\tText");
            }
            else {
                wWriter.write("Positive\tNegative\tText");
            }
            if (options.bgExplainClassification) {
                wWriter.write("\tExplanation\n");
            }
            else {
                wWriter.write("\n");
            }
            while (rReader.ready())
            {
                String sLine = rReader.readLine();
                if (sLine != "")
                {
                    int iTabPos = sLine.lastIndexOf("\t");
                    int iFilePos = 0;
                    int iFileNeg = 0;
                    if (iTabPos >= 0) {
                        String []sData = sLine.split("\t");
                        if (sData.length > 1)
                            if (iTextCol > -1)
                            {
                                wWriter.write((new StringBuilder(String.valueOf(sLine))).append("\t").toString());
                                if (iTextCol < sData.length)
                                    sLine = sData[iTextCol];
                            } else
                            if (options.bgTrinaryMode)
                            {
                                iFileTrinary = -2;
                                try
                                {
                                    iFileTrinary = Integer.parseInt(sData[0].trim());
                                    if (iFileTrinary > 1 || iFileTrinary < -1)
                                    {
                                        System.out.println((new StringBuilder("Invalid trinary sentiment ")).append(iFileTrinary).append(" (expected -1,0,1) at line: ").append(sLine).toString());
                                        iFileTrinary = 0;
                                    }
                                }
                                catch(NumberFormatException numberformatexception) { }//
                            } else
                            if (options.bgScaleMode)
                            {
                                iFileScale = -9;
                                try
                                {
                                    iFileScale = Integer.parseInt(sData[0].trim());
                                    if (iFileScale > 4 || iFileScale < -4)
                                    {
                                        System.out.println((new StringBuilder("Invalid overall sentiment ")).append(iFileScale).append(" (expected -4 to +4) at line: ").append(sLine).toString());
                                        iFileScale = 0;
                                    }
                                }
                                catch (NumberFormatException numberformatexception1) { }//
                            } else
                            {
                                try
                                {
                                    iFilePos = Integer.parseInt(sData[0].trim());
                                    iFileNeg = Integer.parseInt(sData[1].trim());
                                    if (iFileNeg < 0) {
                                        iFileNeg = -iFileNeg;
                                    }
                                }
                                catch (NumberFormatException numberformatexception2) { }//
                            }
                        sLine = sLine.substring(iTabPos + 1);
                    }
                    Paragraph paragraph = new Paragraph();
                    paragraph.setParagraph(sLine, resources, options);
                    if (options.bgTrinaryMode)
                    {
                        iTrinary = paragraph.getParagraphTrinarySentiment();
                        if(options.bgExplainClassification)
                            sRationale = (new StringBuilder("\t")).append(paragraph.getClassificationRationale()).toString();
                        sOutput = (new StringBuilder(String.valueOf(iTrinary))).append("\t").append(sLine).append(sRationale).append("\n").toString();
                    } else
                    if (options.bgScaleMode)
                    {
                        iScale = paragraph.getParagraphScaleSentiment();
                        if (options.bgExplainClassification) {
                            sRationale = (new StringBuilder("\t")).append(paragraph.getClassificationRationale()).toString();
                        }
                        sOutput = (new StringBuilder(String.valueOf(iScale))).append("\t").append(sLine).append(sRationale).append("\n").toString();
                    } else
                    {
                        iPos = paragraph.getParagraphPositiveSentiment();
                        iNeg = paragraph.getParagraphNegativeSentiment();
                        if (options.bgExplainClassification) {
                            sRationale = (new StringBuilder("\t")).append(paragraph.getClassificationRationale()).toString();
                        }
                        sOutput = (new StringBuilder(String.valueOf(iPos))).append("\t").append(iNeg).append("\t").append(sLine).append(sRationale).append("\n").toString();
                    }
                    wWriter.write(sOutput);
                    if (options.bgTrinaryMode)
                    {
                        if (iFileTrinary > -2 && iFileTrinary < 2 && iTrinary > -2 && iTrinary < 2)
                        {
                            iClassified++;
                            if(iFileTrinary == iTrinary)
                                iCorrectTrinaryCount++;
                            confusion[iTrinary + 1][iFileTrinary + 1]++;
                        }
                    } else
                    if (options.bgScaleMode)
                    {
                        if (iFileScale > -9)
                        {
                            iClassified++;
                            if (iFileScale == iScale) {
                                iCorrectScaleCount++;
                            }
                            if (iClassified < maxClassifyForCorrelation) {
                                iScaleClassCorr[iClassified] = iFileScale;
                            }
                            iScaleClassPred[iClassified] = iScale;
                        }
                    } else
                    if (iFileNeg != 0)
                    {
                        iClassified++;
                        if (iPos == iFilePos) {
                            iCorrectPosCount++;
                        }
                        iPosAbsDiff += Math.abs(iPos - iFilePos);
                        if (iClassified < maxClassifyForCorrelation) {
                            iPosClassCorr[iClassified] = iFilePos;
                        }
                        iPosClassPred[iClassified] = iPos;
                        if (iNeg == -iFileNeg) {
                            iCorrectNegCount++;
                        }
                        iNegAbsDiff += Math.abs(iNeg + iFileNeg);
                        if (iClassified < maxClassifyForCorrelation) {
                            iNegClassCorr[iClassified] = iFileNeg;
                        }
                        iNegClassPred[iClassified] = iNeg;
                    }
                }
            }
            rReader.close();
            wWriter.close();
            if (iClassified > 0)
                if (options.bgTrinaryMode)
                {
                    System.out.println((new StringBuilder("Trinary correct: ")).append(iCorrectTrinaryCount).append(" (").append(((float)iCorrectTrinaryCount / (float)iClassified) * 100F).append("%).").toString());
                    System.out.println("Correct -> -1   0   1");
                    System.out.println((new StringBuilder("Est = -1   ")).append(confusion[0][0]).append(" ").append(confusion[0][1]).append(" ").append(confusion[0][2]).toString());
                    System.out.println((new StringBuilder("Est =  0   ")).append(confusion[1][0]).append(" ").append(confusion[1][1]).append(" ").append(confusion[1][2]).toString());
                    System.out.println((new StringBuilder("Est =  1   ")).append(confusion[2][0]).append(" ").append(confusion[2][1]).append(" ").append(confusion[2][2]).toString());
                } else
                if (options.bgScaleMode)
                {
                    System.out.println((new StringBuilder("Scale correct: ")).append(iCorrectScaleCount).append(" (").append(((float)iCorrectScaleCount / (float)iClassified) * 100F).append("%) out of ").append(iClassified).toString());
                    System.out.println((new StringBuilder("  Correlation: ")).append(ClassificationStatistics.correlation(iScaleClassCorr, iScaleClassPred, iClassified)).toString());
                } else
                {
                    System.out.print((new StringBuilder(String.valueOf(options.sgProgramPos))).append(" correct: ").append(iCorrectPosCount).append(" (").append(((float)iCorrectPosCount / (float)iClassified) * 100F).append("%).").toString());
                    System.out.println((new StringBuilder(" Mean abs diff: ")).append((float)iPosAbsDiff / (float)iClassified).toString());
                    if (iClassified < maxClassifyForCorrelation)
                    {
                        System.out.println((new StringBuilder(" Correlation: ")).append(ClassificationStatistics.correlationAbs(iPosClassCorr, iPosClassPred, iClassified)));
                        int corrWithin1 = ClassificationStatistics.accuracyWithin1(iPosClassCorr, iPosClassPred, iClassified, false);
                        System.out.println((new StringBuilder(" Correct +/- 1: ")).append(corrWithin1).append(" (").append((float)(100 * corrWithin1) / (float)iClassified).append("%)"));
                    }
                    System.out.print((new StringBuilder(String.valueOf(options.sgProgramNeg))).append(" correct: ").append(iCorrectNegCount).append(" (").append(((float)iCorrectNegCount / (float)iClassified) * 100F).append("%).").toString());
                    System.out.println((new StringBuilder(" Mean abs diff: ")).append((float)iNegAbsDiff / (float)iClassified).toString());
                    if (iClassified < maxClassifyForCorrelation)
                    {
                        System.out.println((new StringBuilder(" Correlation: ")).append(ClassificationStatistics.correlationAbs(iNegClassCorr, iNegClassPred, iClassified)));
                        int corrWithin1 = ClassificationStatistics.accuracyWithin1(iNegClassCorr, iNegClassPred, iClassified, true);
                        System.out.println((new StringBuilder(" Correct +/- 1: ")).append(corrWithin1).append(" (").append((float)(100 * corrWithin1) / (float)iClassified).append("%)"));
                    }
                }
        }
        catch (FileNotFoundException e)
        {
            System.out.println((new StringBuilder("Could not find input file: ")).append(sInputFile).toString());
            e.printStackTrace();
        }
        catch (IOException e)
        {
            System.out.println((new StringBuilder("Error reading from input file: ")).append(sInputFile).append(" or writing to output file ").append(sOutputFile).toString());
            e.printStackTrace();
        }
    }

    /**
     *写入分类的标题.
     * @param w 写文件对象
     * @throws IOException 输出异常
     */
    private void writeClassificationStatsHeadings(BufferedWriter w)
            throws IOException
    {
        String sPosOrScale;
        if (options.bgScaleMode) {
            sPosOrScale = "ScaleCorrel";
        }
        else {
            sPosOrScale = "PosCorrel";
        }
        w.write((new StringBuilder("\tPosCorrect\tiPosCorrect/Total\tNegCorrect\tNegCorrect/Total\tPosWithin1\tPosWithin1/Total\tNegWithin1\tNegWithin1/Total\t")).append(sPosOrScale).append("\tNegCorrel").append("\tPosMPE\tNegMPE\tPosMPEnoDiv\tNegMPEnoDiv").append("\tTrinaryOrScaleCorrect\tTrinaryOrScaleCorrect/TotalClassified").append("\tTrinaryOrScaleCorrectWithin1\tTrinaryOrScaleCorrectWithin1/TotalClassified").append("\test-1corr-1\test-1corr0\test-1corr1").append("\test0corr-1\test0corr0\test0corr1").append("\test1corr-1\test1corr0\test1corr1").append("\tTotalClassified\n").toString());
    }

    /**
     * 运行10折交叉验证，对其中的每一个选项进行尝试.
     * @param iMinImprovement 最小提升量
     * @param bUseTotalDifference 是否使用总误差
     * @param iReplications 重复次数
     * @param iMultiOptimisations 优化次数
     * @param sOutFileName 输出文件名
     */
    public void run10FoldCrossValidationForAllOptionVariations(int iMinImprovement, boolean bUseTotalDifference, int iReplications, int iMultiOptimisations, String sOutFileName)
    {
        try
        {
            BufferedWriter wResultsWriter = new BufferedWriter(new FileWriter(sOutFileName));
            BufferedWriter wTermStrengthWriter = new BufferedWriter(new FileWriter((new StringBuilder(String.valueOf(FileOps.s_ChopFileNameExtension(sOutFileName)))).append("_termStrVars.txt").toString()));
            if (igPosClass == null || igPosClass.length < igPosCorrect.length)
            {
                igPosClass = new int[igParagraphCount + 1];
                igNegClass = new int[igParagraphCount + 1];
                igTrinaryClass = new int[igParagraphCount + 1];
            }
            options.printClassificationOptionsHeadings(wResultsWriter);
            writeClassificationStatsHeadings(wResultsWriter);
            options.printClassificationOptionsHeadings(wTermStrengthWriter);
            resources.sentimentWords.printSentimentTermsInSingleHeaderRow(wTermStrengthWriter);
            System.out.println("About to start classifications for 20 different option variations");
            if (options.bgTrinaryMode) {
                ClassificationStatistics.baselineAccuracyMakeLargestClassPrediction(igTrinaryCorrect, igTrinaryClass, igParagraphCount, false);
            }
            else
            if (options.bgScaleMode)
            {
                ClassificationStatistics.baselineAccuracyMakeLargestClassPrediction(igScaleCorrect, igScaleClass, igParagraphCount, false);
            } else
            {
                ClassificationStatistics.baselineAccuracyMakeLargestClassPrediction(igPosCorrect, igPosClass, igParagraphCount, false);
                ClassificationStatistics.baselineAccuracyMakeLargestClassPrediction(igNegCorrect, igNegClass, igParagraphCount, true);
            }
            options.printBlankClassificationOptions(wResultsWriter);
            if (options.bgTrinaryMode) {
                printClassificationResultsRow(igPosClass, igNegClass, igTrinaryClass, wResultsWriter);
            }
            else {
                printClassificationResultsRow(igPosClass, igNegClass, igScaleClass, wResultsWriter);
            }
            options.printClassificationOptions(wResultsWriter, igParagraphCount, bUseTotalDifference, iMultiOptimisations);
            calculateCorpusSentimentScores();
            if (options.bgTrinaryMode) {
                printClassificationResultsRow(igPosClass, igNegClass, igTrinaryClass, wResultsWriter);
            }
            else {
                printClassificationResultsRow(igPosClass, igNegClass, igScaleClass, wResultsWriter);
            }
            options.printBlankClassificationOptions(wTermStrengthWriter);
            resources.sentimentWords.printSentimentValuesInSingleRow(wTermStrengthWriter);
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.igEmotionParagraphCombineMethod = 1 - options.igEmotionParagraphCombineMethod;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.igEmotionParagraphCombineMethod = 1 - options.igEmotionParagraphCombineMethod;
            options.igEmotionSentenceCombineMethod = 1 - options.igEmotionSentenceCombineMethod;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.igEmotionSentenceCombineMethod = 1 - options.igEmotionSentenceCombineMethod;
            options.bgReduceNegativeEmotionInQuestionSentences = !options.bgReduceNegativeEmotionInQuestionSentences;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgReduceNegativeEmotionInQuestionSentences = !options.bgReduceNegativeEmotionInQuestionSentences;
            options.bgMissCountsAsPlus2 = !options.bgMissCountsAsPlus2;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgMissCountsAsPlus2 = !options.bgMissCountsAsPlus2;
            options.bgYouOrYourIsPlus2UnlessSentenceNegative = !options.bgYouOrYourIsPlus2UnlessSentenceNegative;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgYouOrYourIsPlus2UnlessSentenceNegative = !options.bgYouOrYourIsPlus2UnlessSentenceNegative;
            options.bgExclamationInNeutralSentenceCountsAsPlus2 = !options.bgExclamationInNeutralSentenceCountsAsPlus2;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgExclamationInNeutralSentenceCountsAsPlus2 = !options.bgExclamationInNeutralSentenceCountsAsPlus2;
            options.bgUseIdiomLookupTable = !options.bgUseIdiomLookupTable;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgUseIdiomLookupTable = !options.bgUseIdiomLookupTable;
            int iTemp = options.igMoodToInterpretNeutralEmphasis;
            options.igMoodToInterpretNeutralEmphasis = -options.igMoodToInterpretNeutralEmphasis;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.igMoodToInterpretNeutralEmphasis = 0;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.igMoodToInterpretNeutralEmphasis = iTemp;
            System.out.println("About to start 10th option variation classification");
            options.bgAllowMultiplePositiveWordsToIncreasePositiveEmotion = !options.bgAllowMultiplePositiveWordsToIncreasePositiveEmotion;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgAllowMultiplePositiveWordsToIncreasePositiveEmotion = !options.bgAllowMultiplePositiveWordsToIncreasePositiveEmotion;
            options.bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion = !options.bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion = !options.bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion;
            options.bgIgnoreBoosterWordsAfterNegatives = !options.bgIgnoreBoosterWordsAfterNegatives;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgIgnoreBoosterWordsAfterNegatives = !options.bgIgnoreBoosterWordsAfterNegatives;
            options.bgMultipleLettersBoostSentiment = !options.bgMultipleLettersBoostSentiment;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgMultipleLettersBoostSentiment = !options.bgMultipleLettersBoostSentiment;
            options.bgBoosterWordsChangeEmotion = !options.bgBoosterWordsChangeEmotion;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgBoosterWordsChangeEmotion = !options.bgBoosterWordsChangeEmotion;
            if (options.bgNegatingWordsFlipEmotion)
            {
                options.bgNegatingWordsFlipEmotion = !options.bgNegatingWordsFlipEmotion;
                run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
                options.bgNegatingWordsFlipEmotion = !options.bgNegatingWordsFlipEmotion;
            } else
            {
                options.bgNegatingPositiveFlipsEmotion = !options.bgNegatingPositiveFlipsEmotion;
                run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
                options.bgNegatingPositiveFlipsEmotion = !options.bgNegatingPositiveFlipsEmotion;
                options.bgNegatingNegativeNeutralisesEmotion = !options.bgNegatingNegativeNeutralisesEmotion;
                run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
                options.bgNegatingNegativeNeutralisesEmotion = !options.bgNegatingNegativeNeutralisesEmotion;
            }
            options.bgCorrectSpellingsWithRepeatedLetter = !options.bgCorrectSpellingsWithRepeatedLetter;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgCorrectSpellingsWithRepeatedLetter = !options.bgCorrectSpellingsWithRepeatedLetter;
            options.bgUseEmoticons = !options.bgUseEmoticons;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgUseEmoticons = !options.bgUseEmoticons;
            options.bgCapitalsBoostTermSentiment = !options.bgCapitalsBoostTermSentiment;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgCapitalsBoostTermSentiment = !options.bgCapitalsBoostTermSentiment;
            if (iMinImprovement > 1) {
                run10FoldCrossValidationMultipleTimes(iMinImprovement - 1, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            }
            run10FoldCrossValidationMultipleTimes(iMinImprovement + 1, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            wResultsWriter.close();
            wTermStrengthWriter.close();
            SummariseMultiple10FoldValidations(sOutFileName, (new StringBuilder(sOutFileName)).append("_sum.txt").toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 用于在一次十折交叉验证中运行情感分析.
     * @param iMinImprovement 一个整数，表示必须达到的最小改进量。
     * @param bUseTotalDifference 一个布尔值，指示是否使用总差异而不是平均差异来判断是否达到了最小改进量。
     * @param iMultiOptimisations 一个整数，表示要进行的情感词典优化次数。
     * @param wWriter 一个BufferedWriter对象，表示输出文件的写入器。
     * @param wTermStrengthWriter 一个BufferedWriter对象，表示情感词典权重输出文件的写入器。
     */
    private void run10FoldCrossValidationOnce(int iMinImprovement, boolean bUseTotalDifference, int iMultiOptimisations, BufferedWriter wWriter, BufferedWriter wTermStrengthWriter)
    {
        int iTotalSentimentWords = resources.sentimentWords.getSentimentWordCount();
        int []iParagraphRand = new int[igParagraphCount + 1];
        int []iPosClassAll = new int[igParagraphCount + 1];
        int []iNegClassAll = new int[igParagraphCount + 1];
        int []iTrinaryOrScaleClassAll = new int[igParagraphCount + 1];
        int iTotalClassified = 0;
        Sort.makeRandomOrderList(iParagraphRand);
        int []iOriginalSentimentStrengths = new int[iTotalSentimentWords + 1];
        for (int i = 1; i < iTotalSentimentWords; i++) {
            iOriginalSentimentStrengths[i] = resources.sentimentWords.getSentiment(i);
        }
        for (int iFold = 1; iFold <= 10; iFold++)
        {
            selectDecileAsSubcorpus(iParagraphRand, iFold, true);
            reCalculateCorpusSentimentScores();
            optimiseDictionaryWeightingsForCorpusMultipleTimes(iMinImprovement, bUseTotalDifference, iMultiOptimisations);
            options.printClassificationOptions(wTermStrengthWriter, iMinImprovement, bUseTotalDifference, iMultiOptimisations);
            resources.sentimentWords.printSentimentValuesInSingleRow(wTermStrengthWriter);
            selectDecileAsSubcorpus(iParagraphRand, iFold, false);
            reCalculateCorpusSentimentScores();
            for (int i = 1; i <= igParagraphCount; i++) {
                if (bgSupcorpusMember[i]) {
                    iPosClassAll[i] = igPosClass[i];
                    iNegClassAll[i] = igNegClass[i];
                    if (options.bgTrinaryMode)
                        iTrinaryOrScaleClassAll[i] = igTrinaryClass[i];
                    else
                        iTrinaryOrScaleClassAll[i] = igScaleClass[i];
                }
            }
            iTotalClassified += igSupcorpusMemberCount;
            for (int i = 1; i < iTotalSentimentWords; i++) {
                resources.sentimentWords.setSentiment(i, iOriginalSentimentStrengths[i]);
            }
        }

        useWholeCorpusNotSubcorpus();
        options.printClassificationOptions(wWriter, iMinImprovement, bUseTotalDifference, iMultiOptimisations);
        printClassificationResultsRow(iPosClassAll, iNegClassAll, iTrinaryOrScaleClassAll, wWriter);
    }

    /**
     *逐行打印分类结果.
     * @param iPosClassAll 所有的积极情绪类
     * @param iNegClassAll 所有的消极情绪类
     * @param iTrinaryOrScaleClassAll 所有的三元或一元类
     * @param wWriter 写文件
     * @return 是否打印成功
     */
    private boolean printClassificationResultsRow(int []iPosClassAll, int []iNegClassAll, int []iTrinaryOrScaleClassAll, BufferedWriter wWriter)
    {
        int iPosCorrect = -1;
        int iNegCorrect = -1;
        int iPosWithin1 = -1;
        int iNegWithin1 = -1;
        int iTrinaryCorrect = -1;
        int iTrinaryCorrectWithin1 = -1;
        double fPosCorrectPoportion = -1D;
        double fNegCorrectPoportion = -1D;
        double fPosWithin1Poportion = -1D;
        double fNegWithin1Poportion = -1D;
        double fTrinaryCorrectPoportion = -1D;
        double fTrinaryCorrectWithin1Poportion = -1D;
        double fPosOrScaleCorr = 9999D;
        double fNegCorr = 9999D;
        double fPosMPE = 9999D;
        double fNegMPE = 9999D;
        double fPosMPEnoDiv = 9999D;
        double fNegMPEnoDiv = 9999D;
        int [][]estCorr = {
                new int[3], new int[3], new int[3]
        };
        try
        {
            if (options.bgTrinaryMode)
            {
                iTrinaryCorrect = ClassificationStatistics.accuracy(igTrinaryCorrect, iTrinaryOrScaleClassAll, igParagraphCount, false);
                iTrinaryCorrectWithin1 = ClassificationStatistics.accuracyWithin1(igTrinaryCorrect, iTrinaryOrScaleClassAll, igParagraphCount, false);
                fTrinaryCorrectPoportion = (float)iTrinaryCorrect / (float)igParagraphCount;
                fTrinaryCorrectWithin1Poportion = (float)iTrinaryCorrectWithin1 / (float)igParagraphCount;
                ClassificationStatistics.TrinaryOrBinaryConfusionTable(iTrinaryOrScaleClassAll, igTrinaryCorrect, igParagraphCount, estCorr);
            } else
            if (options.bgScaleMode)
            {
                iTrinaryCorrect = ClassificationStatistics.accuracy(igScaleCorrect, iTrinaryOrScaleClassAll, igParagraphCount, false);
                iTrinaryCorrectWithin1 = ClassificationStatistics.accuracyWithin1(igScaleCorrect, iTrinaryOrScaleClassAll, igParagraphCount, false);
                fTrinaryCorrectPoportion = (float)iTrinaryCorrect / (float)igParagraphCount;
                fTrinaryCorrectWithin1Poportion = (float)iTrinaryCorrectWithin1 / (float)igParagraphCount;
                fPosOrScaleCorr = ClassificationStatistics.correlation(igScaleCorrect, iTrinaryOrScaleClassAll, igParagraphCount);
            } else
            {
                iPosCorrect = ClassificationStatistics.accuracy(igPosCorrect, iPosClassAll, igParagraphCount, false);
                iNegCorrect = ClassificationStatistics.accuracy(igNegCorrect, iNegClassAll, igParagraphCount, true);
                iPosWithin1 = ClassificationStatistics.accuracyWithin1(igPosCorrect, iPosClassAll, igParagraphCount, false);
                iNegWithin1 = ClassificationStatistics.accuracyWithin1(igNegCorrect, iNegClassAll, igParagraphCount, true);
                fPosOrScaleCorr = ClassificationStatistics.correlationAbs(igPosCorrect, iPosClassAll, igParagraphCount);
                fNegCorr = ClassificationStatistics.correlationAbs(igNegCorrect, iNegClassAll, igParagraphCount);
                fPosMPE = ClassificationStatistics.absoluteMeanPercentageError(igPosCorrect, iPosClassAll, igParagraphCount, false);
                fNegMPE = ClassificationStatistics.absoluteMeanPercentageError(igNegCorrect, iNegClassAll, igParagraphCount, true);
                fPosMPEnoDiv = ClassificationStatistics.absoluteMeanPercentageErrorNoDivision(igPosCorrect, iPosClassAll, igParagraphCount, false);
                fNegMPEnoDiv = ClassificationStatistics.absoluteMeanPercentageErrorNoDivision(igNegCorrect, iNegClassAll, igParagraphCount, true);
                fPosCorrectPoportion = (float)iPosCorrect / (float)igParagraphCount;
                fNegCorrectPoportion = (float)iNegCorrect / (float)igParagraphCount;
                fPosWithin1Poportion = (float)iPosWithin1 / (float)igParagraphCount;
                fNegWithin1Poportion = (float)iNegWithin1 / (float)igParagraphCount;
            }
            wWriter.write((new StringBuilder("\t")).append(iPosCorrect).append("\t").append(fPosCorrectPoportion).append("\t").append(iNegCorrect).append("\t").append(fNegCorrectPoportion).append("\t").append(iPosWithin1).append("\t").append(fPosWithin1Poportion).append("\t").append(iNegWithin1).append("\t").append(fNegWithin1Poportion).append("\t").append(fPosOrScaleCorr).append("\t").append(fNegCorr).append("\t").append(fPosMPE).append("\t").append(fNegMPE).append("\t").append(fPosMPEnoDiv).append("\t").append(fNegMPEnoDiv).append("\t").append(iTrinaryCorrect).append("\t").append(fTrinaryCorrectPoportion).append("\t").append(iTrinaryCorrectWithin1).append("\t").append(fTrinaryCorrectWithin1Poportion).append("\t").append(estCorr[0][0]).append("\t").append(estCorr[0][1]).append("\t").append(estCorr[0][2]).append("\t").append(estCorr[1][0]).append("\t").append(estCorr[1][1]).append("\t").append(estCorr[1][2]).append("\t").append(estCorr[2][0]).append("\t").append(estCorr[2][1]).append("\t").append(estCorr[2][2]).append("\t").append(igParagraphCount).append("\n").toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 十折交叉验证中，用于选定数据集与验证集.
     * @param iParagraphRand 随机排列的段落id
     * @param iDecile 十分值
     * @param bInvert 是否反转
     */
    private void selectDecileAsSubcorpus(int []iParagraphRand, int iDecile, boolean bInvert)
    {
        if (igParagraphCount == 0)
            return;
        int iMin = (int)(((float)igParagraphCount / 10F) * (float)(iDecile - 1)) + 1;
        int iMax = (int)(((float)igParagraphCount / 10F) * (float)iDecile);
        if (iDecile == 10)
            iMax = igParagraphCount;
        if (iDecile == 0)
            iMin = 0;
        igSupcorpusMemberCount = 0;
        for (int i = 1; i <= igParagraphCount; i++) {
            if (i >= iMin && i <= iMax) {
                bgSupcorpusMember[iParagraphRand[i]] = !bInvert;
                if (!bInvert)
                    igSupcorpusMemberCount++;
            } else {
                bgSupcorpusMember[iParagraphRand[i]] = bInvert;
                if (bInvert)
                    igSupcorpusMemberCount++;
            }
        }
    }

    /**
     * 在语料库中多次优化情感词典的权重.
     * @param iMinImprovement 一个整数，表示必须达到的最小改进量
     * @param bUseTotalDifference 一个布尔值，指示是否使用总差异而不是平均差异来判断是否达到了最小改进量
     * @param iOptimisationTotal 一个整数，表示要进行的优化次数
     */
    public void optimiseDictionaryWeightingsForCorpusMultipleTimes(int iMinImprovement, boolean bUseTotalDifference, int iOptimisationTotal) {
        if (iOptimisationTotal < 1) {
            return;
        }
        if (iOptimisationTotal == 1) {
            optimiseDictionaryWeightingsForCorpus(iMinImprovement, bUseTotalDifference);
            return;
        }
        int iTotalSentimentWords = resources.sentimentWords.getSentimentWordCount();
        int[] iOriginalSentimentStrengths = new int[iTotalSentimentWords + 1];
        for (int j = 1; j <= iTotalSentimentWords; j++) {
            iOriginalSentimentStrengths[j] = resources.sentimentWords.getSentiment(j);
        }
        int[] iTotalWeight = new int[iTotalSentimentWords + 1];
        for (int j = 1; j <= iTotalSentimentWords; j++){
            iTotalWeight[j] = 0;
        }
        for (int i = 0; i < iOptimisationTotal; i++)
        {
            optimiseDictionaryWeightingsForCorpus(iMinImprovement, bUseTotalDifference);
            for (int j = 1; j <= iTotalSentimentWords; j++) {
                iTotalWeight[j] += resources.sentimentWords.getSentiment(j);
            }
            for (int j = 1; j <= iTotalSentimentWords; j++) {
                resources.sentimentWords.setSentiment(j, iOriginalSentimentStrengths[j]);
            }
        }

        for (int j = 1; j <= iTotalSentimentWords; j++) {
            resources.sentimentWords.setSentiment(j, (int) ((double) ((float) iTotalWeight[j] / (float) iOptimisationTotal) + 0.5D));
        }
        optimiseDictionaryWeightingsForCorpus(iMinImprovement, bUseTotalDifference);
    }

    /**
     * 根据不同的模式，选择优化权重的方式.
     * @param iMinImprovement 最小提升量
     * @param bUseTotalDifference 是否使用完全不同对比方式
     */
    public void optimiseDictionaryWeightingsForCorpus(int iMinImprovement, boolean bUseTotalDifference)
    {
        if (options.bgTrinaryMode) {
            optimiseDictionaryWeightingsForCorpusTrinaryOrBinary(iMinImprovement);
        }
        else
        if (options.bgScaleMode) {
            optimiseDictionaryWeightingsForCorpusScale(iMinImprovement);
        }
        else {
            optimiseDictionaryWeightingsForCorpusPosNeg(iMinImprovement, bUseTotalDifference);
        }
    }

    /**
     * 为一元语料优化权重字典.
     * @param iMinImprovement 最小提升量
     */
    public void optimiseDictionaryWeightingsForCorpusScale(int iMinImprovement)
    {
        boolean bFullListChanges = true;
        int iLastScaleNumberCorrect = getClassificationScaleNumberCorrect();
        int iNewScaleNumberCorrect = 0;
        int iTotalSentimentWords = resources.sentimentWords.getSentimentWordCount();
        int []iWordRand = new int[iTotalSentimentWords + 1];
        while (bFullListChanges)
        {
            Sort.makeRandomOrderList(iWordRand);
            bFullListChanges = false;
            for (int i = 1; i <= iTotalSentimentWords; i++)
            {
                int iOldTermSentimentStrength = resources.sentimentWords.getSentiment(iWordRand[i]);
                boolean bCurrentIDChange = false;
                int iAddOneImprovement = 0;
                int iSubtractOneImprovement = 0;
                if(iOldTermSentimentStrength < 4)
                {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldTermSentimentStrength + 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                    iNewScaleNumberCorrect = getClassificationScaleNumberCorrect();
                    iAddOneImprovement = iNewScaleNumberCorrect - iLastScaleNumberCorrect;
                    if (iAddOneImprovement >= iMinImprovement)
                    {
                        bCurrentIDChange = true;
                        iLastScaleNumberCorrect += iAddOneImprovement;
                    }
                }
                if (iOldTermSentimentStrength > -4 && !bCurrentIDChange)
                {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldTermSentimentStrength - 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                    iNewScaleNumberCorrect = getClassificationScaleNumberCorrect();
                    iSubtractOneImprovement = iNewScaleNumberCorrect - iLastScaleNumberCorrect;
                    if (iSubtractOneImprovement >= iMinImprovement)
                    {
                        bCurrentIDChange = true;
                        iLastScaleNumberCorrect += iSubtractOneImprovement;
                    }
                }
                if (bCurrentIDChange)
                {
                    bFullListChanges = true;
                } else
                {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldTermSentimentStrength);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                }
            }

        }
    }

    /**
     * 为三元或二元语料集优化权重字典.
     * @param iMinImprovement 最小提升量
     */
    public void optimiseDictionaryWeightingsForCorpusTrinaryOrBinary(int iMinImprovement)
    {
        boolean bFullListChanges = true;
        int iLastTrinaryCorrect = getClassificationTrinaryNumberCorrect();
        int iNewTrinary = 0;
        int iTotalSentimentWords = resources.sentimentWords.getSentimentWordCount();
        int []iWordRand = new int[iTotalSentimentWords + 1];
        while (bFullListChanges)
        {
            Sort.makeRandomOrderList(iWordRand);
            bFullListChanges = false;
            for (int i = 1; i <= iTotalSentimentWords; i++)
            {
                int iOldSentimentStrength = resources.sentimentWords.getSentiment(iWordRand[i]);
                boolean bCurrentIDChange = false;
                int iAddOneImprovement = 0;
                int iSubtractOneImprovement = 0;
                if (iOldSentimentStrength < 4)
                {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldSentimentStrength + 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                    iNewTrinary = getClassificationTrinaryNumberCorrect();
                    iAddOneImprovement = iNewTrinary - iLastTrinaryCorrect;
                    if (iAddOneImprovement >= iMinImprovement)
                    {
                        bCurrentIDChange = true;
                        iLastTrinaryCorrect += iAddOneImprovement;
                    }
                }
                if (iOldSentimentStrength > -4 && !bCurrentIDChange)
                {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldSentimentStrength - 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                    iNewTrinary = getClassificationTrinaryNumberCorrect();
                    iSubtractOneImprovement = iNewTrinary - iLastTrinaryCorrect;
                    if (iSubtractOneImprovement >= iMinImprovement)
                    {
                        bCurrentIDChange = true;
                        iLastTrinaryCorrect += iSubtractOneImprovement;
                    }
                }
                if (bCurrentIDChange)
                {
                    bFullListChanges = true;
                } else
                {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldSentimentStrength);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                }
            }

        }
    }

    /**
     *为正面负面情绪语料优化权重字典.
     * @param iMinImprovement 最小提升量
     * @param bUseTotalDifference 是否使用总误差
     */
    public void optimiseDictionaryWeightingsForCorpusPosNeg(int iMinImprovement, boolean bUseTotalDifference)
    {
        boolean bFullListChanges = true;
        int iLastPos = 0;
        int iLastNeg = 0;
        int iLastPosTotalDiff = 0;
        int iLastNegTotalDiff = 0;
        if (bUseTotalDifference)
        {
            iLastPosTotalDiff = getClassificationPositiveTotalDifference();
            iLastNegTotalDiff = getClassificationNegativeTotalDifference();
        } else
        {
            iLastPos = getClassificationPositiveNumberCorrect();
            iLastNeg = getClassificationNegativeNumberCorrect();
        }
        int iNewPos = 0;
        int iNewNeg = 0;
        int iNewPosTotalDiff = 0;
        int iNewNegTotalDiff = 0;
        int iTotalSentimentWords = resources.sentimentWords.getSentimentWordCount();
        int []iWordRand = new int[iTotalSentimentWords + 1];
        while (bFullListChanges)
        {
            Sort.makeRandomOrderList(iWordRand);
            bFullListChanges = false;
            for (int i = 1; i <= iTotalSentimentWords; i++)
            {
                int iOldSentimentStrength = resources.sentimentWords.getSentiment(iWordRand[i]);
                boolean bCurrentIDChange = false;
                if (iOldSentimentStrength < 4)
                {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldSentimentStrength + 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                    if (bUseTotalDifference)
                    {
                        iNewPosTotalDiff = getClassificationPositiveTotalDifference();
                        iNewNegTotalDiff = getClassificationNegativeTotalDifference();
                        if (((iNewPosTotalDiff - iLastPosTotalDiff) + iNewNegTotalDiff) - iLastNegTotalDiff <= -iMinImprovement)
                            bCurrentIDChange = true;
                    } else
                    {
                        iNewPos = getClassificationPositiveNumberCorrect();
                        iNewNeg = getClassificationNegativeNumberCorrect();
                        if (((iNewPos - iLastPos) + iNewNeg) - iLastNeg >= iMinImprovement)
                            bCurrentIDChange = true;
                    }
                }
                if (iOldSentimentStrength > -4 && !bCurrentIDChange)
                {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldSentimentStrength - 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                    if (bUseTotalDifference)
                    {
                        iNewPosTotalDiff = getClassificationPositiveTotalDifference();
                        iNewNegTotalDiff = getClassificationNegativeTotalDifference();
                        if (((iNewPosTotalDiff - iLastPosTotalDiff) + iNewNegTotalDiff) - iLastNegTotalDiff <= -iMinImprovement) {
                            bCurrentIDChange = true;
                        }
                    } else
                    {
                        iNewPos = getClassificationPositiveNumberCorrect();
                        iNewNeg = getClassificationNegativeNumberCorrect();
                        if (((iNewPos - iLastPos) + iNewNeg) - iLastNeg >= iMinImprovement) {
                            bCurrentIDChange = true;
                        }
                    }
                }
                if (bCurrentIDChange)
                {
                    if (bUseTotalDifference)
                    {
                        iLastNegTotalDiff = iNewNegTotalDiff;
                        iLastPosTotalDiff = iNewPosTotalDiff;
                        bFullListChanges = true;
                    } else
                    {
                        iLastNeg = iNewNeg;
                        iLastPos = iNewPos;
                        bFullListChanges = true;
                    }
                } else
                {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldSentimentStrength);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                }
            }

        }
    }

    /**
     *10折交叉验证结果汇总.
     * @param sInputFile 输入文件
     * @param sOutputFile 输出文件
     */
    public void SummariseMultiple10FoldValidations(String sInputFile, String sOutputFile)
    {
        int iDataRows = 28;
        int iLastOptionCol = 24;
        BufferedReader rResults = null;
        BufferedWriter wSummary = null;
        String sLine = null;
        String []sPrevData = null;
        String []sData = null;
        float []total = new float[iDataRows];
        int iRows = 0;
        int i = 0;
        try
        {
            rResults = new BufferedReader(new FileReader(sInputFile));
            wSummary = new BufferedWriter(new FileWriter(sOutputFile));
            sLine = rResults.readLine();
            wSummary.write((new StringBuilder(String.valueOf(sLine))).append("\tNumber\n").toString());
            while (rResults.ready())
            {
                sLine = rResults.readLine();
                sData = sLine.split("\t");
                boolean bMatching = true;
                if (sPrevData != null)
                    for(i = 0; i < iLastOptionCol; i++)
                        if (!sData[i].equals(sPrevData[i]))
                            bMatching = false;

                if (!bMatching)
                {
                    for (i = 0; i < iLastOptionCol; i++) {
                        wSummary.write((new StringBuilder(String.valueOf(sPrevData[i]))).append("\t").toString());
                    }
                    for (i = 0; i < iDataRows; i++) {
                        wSummary.write((new StringBuilder(String.valueOf(total[i] / (float) iRows))).append("\t").toString());
                    }
                    wSummary.write((new StringBuilder(String.valueOf(iRows))).append("\n").toString());
                    for (i = 0; i < iDataRows; i++) {
                        total[i] = 0.0F;
                    }
                    iRows = 0;
                }
                for (i = iLastOptionCol; i < iLastOptionCol + iDataRows; i++) {
                    try {
                        total[i - iLastOptionCol] += Float.parseFloat(sData[i]);
                    } catch (Exception e) {
                        total[i - iLastOptionCol] += 9999999F;
                    }
                }
                iRows++;
                sPrevData = sLine.split("\t");
            }
            for (i = 0; i < iLastOptionCol; i++) {
                wSummary.write((new StringBuilder(String.valueOf(sPrevData[i]))).append("\t").toString());
            }
            for (i = 0; i < iDataRows; i++) {
                wSummary.write((new StringBuilder(String.valueOf(total[i] / (float) iRows))).append("\t").toString());
            }
            wSummary.write((new StringBuilder(String.valueOf(iRows))).append("\n").toString());
            wSummary.close();
            rResults.close();
        }
        catch (IOException e)
        {
            System.out.println((new StringBuilder("SummariseMultiple10FoldValidations: File I/O error: ")).append(sInputFile));
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.out.println((new StringBuilder("SummariseMultiple10FoldValidations: Error at line: ")).append(sLine));
            System.out.println((new StringBuilder("Value of i: ")).append(i));
            e.printStackTrace();
        }
    }
}
