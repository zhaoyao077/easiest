//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package uk.ac.wlv.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 情绪强度（旧版本）
 *
 * @author 13986
 * @date 2023/03/06
 */
public class SentiStrengthOld {
    /**
     * 情绪词
     */
    private String[] sgSentimentWords;
    /**
     * 情绪词强度
     */
    private int[] igSentimentWordsStrengthTake1;
    /**
     * 情绪词数计
     */
    private int igSentimentWordsCount = 0;
    /**
     * 表情符号
     */
    private String[] sgEmoticon;
    /**
     * 表情符号强度
     */
    private int[] igEmoticonStrength;
    /**
     * 表情符号计数
     */
    private int igEmoticonCount = 0;
    /**
     * 表情符号最大值
     */
    private int igEmoticonMax = 0;
    /**
     * 正确单词
     */
    private String[] sgCorrectWord;
    /**
     * 正确单词计数
     */
    private int igCorrectWordCount = 0;
    /**
     * 正确单词最大值
     */
    private int igCorrectWordMax = 0;
    /**
     * 俚语词
     */
    private String[] sgSlangWord;
    /**
     * 俚语标准字
     */
    private String[] sgSlangStandardWord;
    /**
     * 俚语词计数
     */
    private int igSlangWordCount = 0;
    /**
     * 俚语词最大值
     */
    private int igSlangWordMax = 0;
    /**
     * 助词
     */
    private String[] sgBoosterWord;
    /**
     * 助词强度
     */
    private int[] igBoosterWordStrength;
    /**
     * 助词计数
     */
    private int igBoosterWordCount = 0;
    /**
     * 否定词
     */
    private String[] sgNegatingWord;
    /**
     * 否定词计数
     */
    private int igNegatingWordCount = 0;
    /**
     * 否定词最大值
     */
    private int igNegatingWordMax = 0;
    /**
     * 情感强度文件夹
     */
    private String sgSentiStrengthFolder = "/SentiStrength_Data/";
    /**
     * 情绪词文件
     */
    private String sgSentimentWordsFile = "EmotionLookupTable.txt";
    /**
     * 表情符号查找表
     */
    private String sgEmoticonLookupTable = "EmoticonLookupTable.txt";
    /**
     * 正确拼写文件名
     */
    private String sgCorrectSpellingFileName = "EnglishWordList.txt";
    /**
     * 俚语查找表
     */
    private String sgSlangLookupTable = "SlangLookupTable_NOT_USED.txt";
    /**
     * 否定单词列表文件
     */
    private String sgNegatingWordListFile = "NegatingWordList.txt";
    /**
     * 助推词列表文件
     */
    private String sgBoosterListFile = "BoosterWordList.txt";
    /**
     * 成语查找表文件
     */
    private String sgIdiomLookupTableFile = "IdiomLookupTable_NOT_USED.txt";
    /**
     * 情感段结合方法
     */
    int igEmotionParagraphCombineMethod = 0;
    /**
     * 情感段结合最大值
     */
    final int igEmotionParagraphCombineMax = 0;
    /**
     * 情感段组合平均值
     */
    final int igEmotionParagraphCombineAverage = 1;
    /**
     * 情感语句组合方法
     */
    int igEmotionSentenceCombineMethod = 0;
    /**
     * 情感语句组合最大值
     */
    final int igEmotionSentenceCombineMax = 0;
    /**
     * 情感语句组合平均值
     */
    final int igEmotionSentenceCombineAverage = 1;
    /**
     * 是否忽略负面情绪问题语句
     */
    boolean bgIgnoreNegativeEmotionInQuestionSentences = true;
    /**
     * 未命中计数是否为Plus2
     */
    boolean bgMissCountsAsPlus2 = true;
    /**
     * 除非语句否定，第二人称是否为Plus2
     */
    boolean bgYouOrYourIsPlus2UnlessSentenceNegative = false;
    /**
     * 感叹号计数是否为Plus2
     */
    boolean bgExclamationCountsAsPlus2 = true;
    /**
     * 是否使用习语查找表
     */
    boolean bgUseIdiomLookupTable_NOT_USED = true;
    /**
     * 是否将中性情绪算作积极的强调
     */
    boolean bgCountNeutralEmotionsAsPositiveForEmphasis = true;
    /**
     * 是否允许多个正面词汇增加积极情绪
     */
    boolean bgAllowMultiplePositiveWordsToIncreasePositiveEmotion = true;
    /**
     * 是否允许多个负面词汇增加消极情绪
     */
    boolean bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion = true;
    /**
     * 是否忽略否定词后的助词
     */
    boolean bgIgnoreBoosterWordsAfterNegatives = true;
    /**
     * 是否将多个字母计算为情感助词
     */
    boolean bgCountMultipleLettersAsEmotionBoosters = true;
    /**
     * 是否认为助词增加感情
     */
    boolean bgBoosterWordsIncreaseEmotion = true;
    /**
     * 是否认为否定词语翻转情感
     */
    boolean bgNegatingWordsFlipEmotion = true;
    /**
     * 是否认为带重复字母的拼写正确
     */
    boolean bgCorrectSpellingsWithRepeatedLetter = true;
    /**
     * 是否使用表情符号
     */
    boolean bgUseEmoticons = true;
    /**
     * 用于推助的最少重复字母
     */
    int igMinRepeatedLettersForBoost = 2;
    /**
     * 从否定到翻转的最大单词数
     */
    int igMaxWordsSinceNegativeToFlip = 1;
    /**
     * 词原
     */
    private final int igWordOriginal = 0;
    /**
     * 词翻译
     */
    private final int igWordTranslated = 1;
    /**
     * 词强调
     */
    private final int igWordEmphasis = 2;
    /**
     * 双关语单词源
     */
    private final int igWordPuncOrig = 3;
    /**
     * 双关语单词翻译
     */
    private final int igWordPuncTrans = 4;
    /**
     * 双关语单词强调
     */
    private final int igWordPuncEmph = 5;
    /**
     * 表情文字源
     */
    private final int igWordEmoOrig = 6;
    /**
     * 表情文字强调
     */
    private final int igWordEmoEmph = 7;
    /**
     * 标记语句数
     */
    private int igTaggedSentenceCount = 0;
    /**
     * 标记语句最大值
     */
    private int igTaggedSentenceMax = 1000;
    /**
     * 标记语句
     */
    private String[] sgTaggedSentence;
    /**
     * 临时表情符号强调
     */
    private String sgTempEmoticonEmphasis;
    /**
     * 错误日志
     */
    private String sgErrorLog;
    /**
     * 原始文本
     */
    private String sgOriginalText;
    /**
     * 标记文本
     */
    private String sgTaggedText;
    /**
     * 文本位置
     */
    private int igTextPos;
    /**
     * 文本否定
     */
    private int igTextNeg;

    /**
     * 情感强度（旧版本）
     */
    public SentiStrengthOld() {
        this.sgTaggedSentence = new String[this.igTaggedSentenceMax + 1];
        this.sgTempEmoticonEmphasis = "";
        this.sgErrorLog = "";
        this.sgOriginalText = "";
        this.sgTaggedText = "";
        this.igTextPos = 1;
        this.igTextNeg = -1;
    }

    /**
     * 主要
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("sentiStrength configuration data must be in C:\\SentStrength_Data\\\" for applet");
        System.out.println("For SentiStrength class call as follows:");
        System.out.println("ss = new sentiStrength();");
        System.out.println("ss.setInitalisationFilesFolder( \"[your SentiStrength_Data folder path]\")");
        System.out.println("ss.initialise(); // reads from SentiStrength_Data folder - returns false if can't read data from above folder");
        System.out.println("ss.detectEmotionInText(\"[your text to be classified]\");");
        System.out.println("Positive sentiment of text will be in + getPositiveClassification(), negative in ss.getNegativeClassification()");
    }

    /**
     * 设置初始化文件文件夹
     *
     * @param folderName 文件夹名称
     */
    public void setInitalisationFilesFolder(String folderName) {
        this.sgSentiStrengthFolder = folderName;
    }

    /**
     * 初始化
     *
     * @return boolean
     */
    public boolean initialise() {
        this.sgErrorLog = "";
        if (!this.b_LoadEmoticonLookupTable(false)) {
            this.sgErrorLog = this.sgErrorLog + "Can't load emoticons from " + this.sgSentiStrengthFolder + this.sgEmoticonLookupTable + "\n";
        }

        if (!this.b_LoadSentimentWords()) {
            this.sgErrorLog = this.sgErrorLog + "Can't load sentiment words from " + this.sgSentiStrengthFolder + this.sgSentimentWordsFile + "\n";
        }

        if (!this.b_LoadCorrectSpellingWords()) {
            this.sgErrorLog = this.sgErrorLog + "Can't load dictionary from " + this.sgSentiStrengthFolder + this.sgCorrectSpellingFileName + "\n";
        }

        if (!this.b_LoadBoosterWords()) {
            this.sgErrorLog = this.sgErrorLog + "Can't load booster words from " + this.sgSentiStrengthFolder + this.sgBoosterListFile + "\n";
        }

        if (!this.b_LoadNegatingWords()) {
            this.sgErrorLog = this.sgErrorLog + "Can't load negating words from " + this.sgSentiStrengthFolder + this.sgNegatingWordListFile + "\n";
        }

        if (this.sgErrorLog != "") {
            return false;
        } else {
            Sort.quickSortStrings(this.sgEmoticon, 1, this.igEmoticonCount);
            return true;
        }
    }

    /**
     * 检测文本中情感
     *
     * @param textToClassify 文本分类
     */
    public void detectEmotionInText(String textToClassify) {
        this.sgOriginalText = textToClassify;
        this.DetectEmotionInText();
    }

    /**
     * 得到积极分类
     *
     * @return int
     */
    public int getPositiveClassification() {
        return this.igTextPos;
    }

    /**
     * 得到消极分类
     *
     * @return int
     */
    public int getNegativeClassification() {
        return this.igTextNeg;
    }

    /**
     * 得到原始文本
     *
     * @return {@link String}
     */
    public String getOriginalText() {
        return this.sgOriginalText;
    }

    /**
     * 得到标记文本
     *
     * @return {@link String}
     */
    public String getTaggedText() {
        return this.sgTaggedText;
    }

    /**
     * 得到错误日志
     *
     * @return {@link String}
     */
    public String getErrorLog() {
        return this.sgErrorLog;
    }

    /**
     * 对文件中的所有文本进行分类
     *
     * @param sInFilenameAndPath  年代文件名和路径
     * @param sOutFilenameAndPath 年代文件名和路径
     * @return boolean
     */
    public boolean classifyAllTextInFile(String sInFilenameAndPath, String sOutFilenameAndPath) {
        try {
            BufferedReader rReader = new BufferedReader(new FileReader(sInFilenameAndPath));
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sOutFilenameAndPath));

            while(rReader.ready()) {
                String sLine = rReader.readLine();
                if (sLine != "") {
                    int iTabPos = sLine.lastIndexOf("\t");
                    if (iTabPos >= 0) {
                        sLine = sLine.substring(iTabPos + 1);
                    }

                    this.sgOriginalText = sLine;
                    this.DetectEmotionInText();
                    wWriter.write(sLine + "\t" + this.sgTaggedText + "\t" + this.igTextPos + "\t" + this.igTextNeg + "\n");
                }
            }

            rReader.close();
            wWriter.close();
            return true;
        } catch (FileNotFoundException var8) {
            var8.printStackTrace();
            return false;
        } catch (IOException var9) {
            var9.printStackTrace();
            return false;
        }
    }

    /**
     * 标记原始文本
     *
     * @return boolean
     */
    private boolean b_TagOriginalText() {
        this.sgTaggedText = "";
        int iCharacter =1;
        int iPunctuation =1;
        int iSpace =1;
        int iEmoticon =1;
        int iPos = 0;
        int iStartOfElement = 0;
        int iLastCharType = 0;
        String sEmoticon = "";
        String sEm = "";
        String sChar = "";

        for(int iTextLength = this.sgOriginalText.length(); iPos < iTextLength; ++iPos) {
            sChar = this.sgOriginalText.substring(iPos, iPos + 1);
            if (sChar.compareTo("'") != 0 && (sChar.compareToIgnoreCase("a") < 0 || sChar.compareToIgnoreCase("z") > 0)) {
                if (sChar.compareTo(" ") == 0) {
                    switch(iLastCharType) {
                    case 1:
                        this.sgTaggedText = this.sgTaggedText + this.s_MakeWordTag(this.sgOriginalText.substring(iStartOfElement, iPos));
                        iStartOfElement = iPos;
                        break;
                    case 2:
                        this.sgTaggedText = this.sgTaggedText + this.s_PunctuationTag(this.sgOriginalText.substring(iStartOfElement, iPos));
                        iStartOfElement = iPos;
                    case 3:
                    default:
                        break;
                    case 4:
                        iStartOfElement = iPos;
                    }

                    iLastCharType = 3;
                } else if (sChar.compareTo("<") == 0) {
                    switch(iLastCharType) {
                    case 1:
                        this.sgTaggedText = this.sgTaggedText + this.s_MakeWordTag(this.sgOriginalText.substring(iStartOfElement, iPos));
                        iStartOfElement = iPos;
                        break;
                    case 2:
                    case 3:
                        this.sgTaggedText = this.sgTaggedText + this.sgOriginalText.substring(iStartOfElement, iPos);
                        iStartOfElement = iPos;
                        break;
                    case 4:
                        iStartOfElement = iPos;
                    }

                    if (iPos + 3 < iTextLength && this.sgOriginalText.substring(iPos, iPos + 4).compareToIgnoreCase("<br>") == 0) {
                        if (iLastCharType == 2) {
                            this.sgTaggedText = this.sgTaggedText + this.s_PunctuationTag(this.sgOriginalText.substring(iStartOfElement, iPos));
                            iStartOfElement = iPos;
                        }

                        iPos += 3;
                        iLastCharType = 3;
                    } else {
                        iLastCharType = 2;
                    }
                } else if (sChar.compareTo(">") != 0 && sChar.compareTo("(") != 0 && sChar.compareTo(")") != 0 && sChar.compareTo("\"") != 0 && sChar.compareTo(",") != 0 && sChar.compareTo(":") != 0 && sChar.compareTo(";") != 0 && sChar.compareTo(".") != 0 && sChar.compareTo("?") != 0 && sChar.compareTo("!") != 0 && sChar.compareTo("~") != 0 && sChar.compareTo("-") != 0 && sChar.compareTo("*") != 0) {
                    switch(iLastCharType) {
                    case 3:
                        this.sgTaggedText = this.sgTaggedText + this.sgOriginalText.substring(iStartOfElement, iPos);
                        iStartOfElement = iPos;
                    case 1:
                    case 2:
                    default:
                        iLastCharType = 1;
                    }
                } else {
                    switch(iLastCharType) {
                    case 1:
                        this.sgTaggedText = this.sgTaggedText + this.s_MakeWordTag(this.sgOriginalText.substring(iStartOfElement, iPos));
                        iStartOfElement = iPos;
                    case 2:
                    default:
                        break;
                    case 3:
                        this.sgTaggedText = this.sgTaggedText + this.sgOriginalText.substring(iStartOfElement, iPos);
                        iStartOfElement = iPos;
                        break;
                    case 4:
                        iStartOfElement = iPos;
                    }

                    sEmoticon = this.s_ExtractEmoticon(this.sgOriginalText, iPos);
                    sEm = this.sgTempEmoticonEmphasis;
                    if (sEmoticon != "") {
                        this.sgTaggedText = this.sgTaggedText + "<e em=\"" + sEm + "\">" + sEmoticon + "</e>";
                        iPos += sEmoticon.length() - 1;
                        iStartOfElement = 10000;
                        iLastCharType = 4;
                    } else {
                        iLastCharType = 2;
                    }
                }
            } else {
                switch(iLastCharType) {
                case 1:
                default:
                    break;
                case 2:
                    this.sgTaggedText = this.sgTaggedText + this.s_PunctuationTag(this.sgOriginalText.substring(iStartOfElement, iPos));
                    iStartOfElement = iPos;
                    break;
                case 3:
                    this.sgTaggedText = this.sgTaggedText + this.sgOriginalText.substring(iStartOfElement, iPos);
                    iStartOfElement = iPos;
                    break;
                case 4:
                    iStartOfElement = iPos;
                }

                iLastCharType = 1;
            }
        }

        switch(iLastCharType) {
        case 1:
            this.sgTaggedText = this.sgTaggedText + this.s_MakeWordTag(this.sgOriginalText.substring(iStartOfElement));
            break;
        case 2:
            this.sgTaggedText = this.sgTaggedText + this.s_PunctuationTag(this.sgOriginalText.substring(iStartOfElement));
            break;
        case 3:
            this.sgTaggedText = this.sgTaggedText + this.sgOriginalText.substring(iStartOfElement);
        }

        return true;
    }

    /**
     * 标点符号标记
     *
     * @param sPunctuation 标点符号
     * @return {@link String}
     */
    private String s_PunctuationTag(String sPunctuation) {
        String sPuncNew = "";
        String sEm = "";
        String sBr = "";
        String sChar = "";
        int iLast = sPunctuation.length() - 1;

        for(int i = 0; i <= iLast; ++i) {
            sChar = sPunctuation.substring(i, i + 1);
            if (sChar.compareTo(".") == 0 || sChar.compareTo("!") == 0 || sChar.compareTo("?") == 0) {
                sBr = "<br>";
                break;
            }
        }

        if (sPunctuation.length() > 1) {
            sEm = sPunctuation.substring(1);
            sPuncNew = sPunctuation.substring(0, 1);
        }

        return sEm == "" ? "<p>" + sPunctuation + "</p>" + sBr : "<p equiv=\"" + sPuncNew + "\" em=\"" + sEm + "\">" + sPunctuation + "</p>" + sBr;
    }

    /**
     * 提取表情符号
     *
     * @param sText     文本
     * @param iStartPos 开始位置
     * @return {@link String}
     */
    private String s_ExtractEmoticon(String sText, int iStartPos) {
        if (!this.bgUseEmoticons) {
            return "";
        } else {
            int iRemainingChars = sText.length() - iStartPos;
            String sEmoticon = "";
            this.sgTempEmoticonEmphasis = "";
            if (iRemainingChars < 2) {
                return "";
            } else if (sText.substring(iStartPos, iStartPos + 1).compareTo(".") == 0) {
                return "";
            } else {
                int iEndpos = sText.indexOf(" ", iStartPos + 1);
                int iPos = sText.indexOf("<br>", iStartPos + 1);
                if (iEndpos < 0) {
                    if (iPos > 0) {
                        iEndpos = iPos;
                    } else {
                        iEndpos = iRemainingChars + iStartPos;
                    }
                } else if (iPos > 0 && iPos < iEndpos) {
                    iEndpos = iPos;
                }

                if (iEndpos - iStartPos < 2) {
                    return "";
                } else {
                    sEmoticon = sText.substring(iStartPos, iEndpos);
                    int iEmoticon = Sort.i_FindStringPositionInSortedArray(sEmoticon, this.sgEmoticon, 1, this.igEmoticonCount);
                    if (iEmoticon < 1) {
                        return "";
                    } else {
                        switch(this.igEmoticonStrength[iEmoticon]) {
                        case -1:
                            this.sgTempEmoticonEmphasis = "-";
                            break;
                        case 0:
                            this.sgTempEmoticonEmphasis = "";
                            break;
                        case 1:
                            this.sgTempEmoticonEmphasis = "+";
                        }

                        return sEmoticon;
                    }
                }
            }
        }
    }

    /**
     * 标记词
     *
     * @param sWord 词
     * @return {@link String}
     */
    private String s_MakeWordTag(String sWord) {
        String sWordNew = "";
        String sEm = "";
        int iSameCount = 0;
        int iLastCopiedPos = 0;
        if (!this.bgCorrectSpellingsWithRepeatedLetter) {
            return "<w>" + sWord + "</w>";
        } else {
            int iWordEnd = sWord.length() - 1;

            int iPos;
            for(iPos = 1; iPos <= iWordEnd; ++iPos) {
                if (sWord.substring(iPos, iPos + 1).compareTo(sWord.substring(iPos - 1, iPos)) == 0) {
                    ++iSameCount;
                } else {
                    if (iSameCount > 0 && "ahijkquvxyz".indexOf(sWord.substring(iPos - 1, iPos)) >= 0) {
                        ++iSameCount;
                    }

                    if (iSameCount > 1) {
                        if (sEm == "") {
                            sWordNew = sWord.substring(0, iPos - iSameCount + 1);
                            sEm = sWord.substring(iPos - iSameCount, iPos - 1);
                            iLastCopiedPos = iPos;
                        } else {
                            sWordNew = sWordNew + sWord.substring(iLastCopiedPos, iPos - iSameCount + 1);
                            sEm = sEm + sWord.substring(iPos - iSameCount, iPos - 1);
                            iLastCopiedPos = iPos;
                        }
                    }

                    iSameCount = 0;
                }
            }

            if (iSameCount > 0 && "achijkmnpqruvwxyz".indexOf(sWord.substring(iPos - 1, iPos)) >= 0) {
                ++iSameCount;
            }

            if (iSameCount > 1) {
                if (sEm == "") {
                    sWordNew = sWord.substring(0, iPos - iSameCount + 1);
                    sEm = sWord.substring(iPos - iSameCount + 1);
                } else {
                    sWordNew = sWordNew + sWord.substring(iLastCopiedPos, iPos - iSameCount + 1);
                    sEm = sEm + sWord.substring(iPos - iSameCount + 1);
                }
            } else if (sEm != "") {
                sWordNew = sWordNew + sWord.substring(iLastCopiedPos);
            }

            if (sWordNew == "") {
                sWordNew = sWord;
            }

            this.sgTempEmoticonEmphasis = sEm;
            sWordNew = this.s_CorrectSpellingInWord(sWordNew);
            sEm = this.sgTempEmoticonEmphasis;
            if (sWordNew.indexOf(" ") > 0) {
                if (sEm == "") {
                    return sWordNew + "<w equiv=\"" + sWordNew.substring(sWordNew.indexOf(" ")) + "\">" + sWord + "</w>";
                } else {
                    return sWordNew + "<w equiv=\"" + sWordNew.substring(sWordNew.indexOf(" ")) + "\" em=\"" + sEm + "\">" + sWord + "</w>";
                }
            } else if (sEm == "" && sWordNew.compareTo(sWord) == 0) {
                return "<w>" + sWord + "</w>";
            } else {
                return "<w equiv=\"" + sWordNew + "\" em=\"" + sEm + "\">" + sWord + "</w>";
            }
        }
    }

    /**
     * 加载表情符号查找表
     *
     * @param bOverrideAllLoadedData 覆盖所有加载数据
     * @return boolean
     */
    private boolean b_LoadEmoticonLookupTable(boolean bOverrideAllLoadedData) {
        if (this.igEmoticonCount > 0 && !bOverrideAllLoadedData) {
            return true;
        } else {
            this.igEmoticonMax = FileOps.i_CountLinesInTextFile(this.sgSentiStrengthFolder + this.sgEmoticonLookupTable) + 1;
            if (this.igEmoticonMax == -1) {
                return false;
            } else {
                this.igEmoticonCount = 0;
                String[] sEmoticonTemp = new String[this.igEmoticonMax];
                this.sgEmoticon = sEmoticonTemp;
                int[] iEmoticonStrengthTemp = new int[this.igEmoticonMax];
                this.igEmoticonStrength = iEmoticonStrengthTemp;

                try {
                    BufferedReader rReader = new BufferedReader(new FileReader(this.sgSentiStrengthFolder + this.sgEmoticonLookupTable));

                    while(rReader.ready()) {
                        String sLine = rReader.readLine();
                        if (sLine != "") {
                            String[] sData = sLine.split("\t");
                            if (sData.length >= 1) {
                                ++this.igEmoticonCount;
                                this.sgEmoticon[this.igEmoticonCount] = sData[0];
                                this.igEmoticonStrength[this.igEmoticonCount] = Integer.parseInt(sData[1]);
                            }
                        }
                    }

                    rReader.close();
                    return true;
                } catch (FileNotFoundException var8) {
                    var8.printStackTrace();
                    return false;
                } catch (IOException var9) {
                    var9.printStackTrace();
                    return false;
                }
            }
        }
    }

    /**
     * 加载情绪词
     *
     * @return boolean
     */
    private boolean b_LoadSentimentWords() {
        int iLinesInFile =0;
        int iWordStrength =0;
        if (this.sgSentimentWordsFile == "") {
            return false;
        } else {
            iLinesInFile = FileOps.i_CountLinesInTextFile(this.sgSentiStrengthFolder + this.sgSentimentWordsFile);
            if (iLinesInFile < 2) {
                return false;
            } else {
                String[] sSentiWordTemp = new String[iLinesInFile + 1];
                int[] iSentimentWordsStrength = new int[iLinesInFile + 1];
                this.igSentimentWordsStrengthTake1 = iSentimentWordsStrength;
                this.sgSentimentWords = sSentiWordTemp;
                this.igSentimentWordsCount = 0;

                try {
                    BufferedReader rReader = new BufferedReader(new FileReader(this.sgSentiStrengthFolder + this.sgSentimentWordsFile));

                    while(rReader.ready()) {
                        String sLine = rReader.readLine();
                        if (sLine != "") {
                            int iFirstTabLocation = sLine.indexOf("\t");
                            if (iFirstTabLocation >= 0) {
                                int iSecondTabLocation = sLine.indexOf("\t", iFirstTabLocation + 1);
                                try {
                                    if (iSecondTabLocation > 0) {
                                        iWordStrength = Integer.parseInt(sLine.substring(iFirstTabLocation + 1, iSecondTabLocation));
                                    } else {
                                        iWordStrength = Integer.parseInt(sLine.substring(iFirstTabLocation + 1));
                                    }
                                } catch (NumberFormatException var10) {
                                    this.sgErrorLog = this.sgErrorLog + "Number format exception at line: " + sLine;
                                    var10.printStackTrace();
                                    return false;
                                }

                                sLine = sLine.substring(0, iFirstTabLocation);
                                if (sLine.indexOf(" ") >= 0) {
                                    sLine = sLine.trim();
                                }

                                if (sLine != "") {
                                    ++this.igSentimentWordsCount;
                                    this.sgSentimentWords[this.igSentimentWordsCount] = sLine;
                                    if (iWordStrength > 0) {
                                        --iWordStrength;
                                    } else {
                                        ++iWordStrength;
                                    }

                                    this.igSentimentWordsStrengthTake1[this.igSentimentWordsCount] = iWordStrength;
                                }
                            }
                        }
                    }

                    rReader.close();
                    return true;
                } catch (FileNotFoundException var11) {
                    var11.printStackTrace();
                    return false;
                } catch (IOException var12) {
                    var12.printStackTrace();
                    return false;
                }
            }
        }
    }

    /**
     * 加载助词
     *
     * @return boolean
     */
    private boolean b_LoadBoosterWords() {
        int iLinesInFile =0;
        int iWordStrength =0;
        if (this.sgBoosterListFile == "") {
            return false;
        } else {
            iLinesInFile = FileOps.i_CountLinesInTextFile(this.sgSentiStrengthFolder + this.sgBoosterListFile);
            if (iLinesInFile < 2) {
                return false;
            } else {
                String[] sTemp = new String[iLinesInFile + 1];
                int[] iTemp = new int[iLinesInFile + 1];
                this.igBoosterWordStrength = iTemp;
                this.sgBoosterWord = sTemp;
                this.igBoosterWordCount = 0;

                try {
                    BufferedReader rReader = new BufferedReader(new FileReader(this.sgSentiStrengthFolder + this.sgBoosterListFile));

                    while(rReader.ready()) {
                        String sLine = rReader.readLine();
                        if (sLine != "") {
                            int iFirstTabLocation = sLine.indexOf("\t");
                            if (iFirstTabLocation >= 0) {
                                int iSecondTabLocation = sLine.indexOf("\t", iFirstTabLocation + 1);
                                try {
                                    if (iSecondTabLocation > 0) {
                                        iWordStrength = Integer.parseInt(sLine.substring(iFirstTabLocation + 1, iSecondTabLocation));
                                    } else {
                                        iWordStrength = Integer.parseInt(sLine.substring(iFirstTabLocation + 1));
                                    }
                                } catch (NumberFormatException var10) {
                                    this.sgErrorLog = this.sgErrorLog + "Number format exception at line: " + sLine;
                                    var10.printStackTrace();
                                    return false;
                                }

                                sLine = sLine.substring(0, iFirstTabLocation);
                                if (sLine.indexOf(" ") >= 0) {
                                    sLine = sLine.trim();
                                }

                                if (sLine != "") {
                                    ++this.igBoosterWordCount;
                                    this.sgBoosterWord[this.igBoosterWordCount] = sLine;
                                    this.igBoosterWordStrength[this.igBoosterWordCount] = iWordStrength;
                                }
                            }
                        }
                    }

                    rReader.close();
                    return true;
                } catch (FileNotFoundException var11) {
                    var11.printStackTrace();
                    return false;
                } catch (IOException var12) {
                    var12.printStackTrace();
                    return false;
                }
            }
        }
    }

    /**
     * 正确拼写单词
     *
     * @param sWord 词
     * @return {@link String}
     */
    private String s_CorrectSpellingInWord(String sWord) {
        int iLastChar = sWord.length() - 1;

        for(int iPos = 1; iPos <= iLastChar; ++iPos) {
            if (sWord.substring(iPos, iPos + 1).compareTo(sWord.substring(iPos - 1, iPos)) == 0) {
                String sReplaceWord = sWord.substring(0, iPos) + sWord.substring(iPos + 1);
                if (Sort.i_FindStringPositionInSortedArray(sWord, this.sgCorrectWord, 1, this.igCorrectWordCount) > 0) {
                    return sWord;
                }

                if (Sort.i_FindStringPositionInSortedArray(sReplaceWord, this.sgCorrectWord, 1, this.igCorrectWordCount) > 0) {
                    this.sgTempEmoticonEmphasis = this.sgTempEmoticonEmphasis + sWord.substring(iPos, iPos + 1);
                    return sReplaceWord;
                }
            }
        }

        if (iLastChar > 5) {
            if (sWord.indexOf("haha") > 0) {
                this.sgTempEmoticonEmphasis = this.sgTempEmoticonEmphasis + sWord.substring(3, sWord.indexOf("haha") + 2);
                return "haha";
            }

            if (sWord.indexOf("hehe") > 0) {
                this.sgTempEmoticonEmphasis = this.sgTempEmoticonEmphasis + sWord.substring(3, sWord.indexOf("hehe") + 2);
                return "hehe";
            }
        }

        return sWord;
    }

    /**
     * 加载否定词
     *
     * @return boolean
     */
    private boolean b_LoadNegatingWords() {
        if (this.igNegatingWordMax > 0) {
            return true;
        } else {
            this.igNegatingWordMax = FileOps.i_CountLinesInTextFile(this.sgSentiStrengthFolder + this.sgNegatingWordListFile) + 1;
            if (this.igNegatingWordMax == -1) {
                return false;
            } else {
                this.sgNegatingWord = new String[this.igNegatingWordMax];
                this.igNegatingWordCount = 0;

                try {
                    BufferedReader rReader = new BufferedReader(new FileReader(this.sgSentiStrengthFolder + this.sgNegatingWordListFile));

                    while(rReader.ready()) {
                        String sLine = rReader.readLine();
                        if (sLine != "") {
                            ++this.igNegatingWordCount;
                            this.sgNegatingWord[this.igNegatingWordCount] = sLine;
                        }
                    }

                    rReader.close();
                    return true;
                } catch (FileNotFoundException var4) {
                    var4.printStackTrace();
                    return false;
                } catch (IOException var5) {
                    var5.printStackTrace();
                    return false;
                }
            }
        }
    }

    /**
     * 加载正确拼写单词
     *
     * @return boolean
     */
    private boolean b_LoadCorrectSpellingWords() {
        if (this.igCorrectWordMax > 0) {
            return true;
        } else {
            this.igCorrectWordMax = FileOps.i_CountLinesInTextFile(this.sgSentiStrengthFolder + this.sgCorrectSpellingFileName) + 1;
            if (this.igCorrectWordMax == -1) {
                return false;
            } else {
                this.sgCorrectWord = new String[this.igCorrectWordMax];
                this.igCorrectWordCount = 0;

                try {
                    BufferedReader rReader = new BufferedReader(new FileReader(this.sgSentiStrengthFolder + this.sgCorrectSpellingFileName));

                    while(rReader.ready()) {
                        String sLine = rReader.readLine();
                        if (sLine != "") {
                            ++this.igCorrectWordCount;
                            this.sgCorrectWord[this.igCorrectWordCount] = sLine;
                        }
                    }

                    rReader.close();
                    return true;
                } catch (FileNotFoundException var4) {
                    var4.printStackTrace();
                    return false;
                } catch (IOException var5) {
                    var5.printStackTrace();
                    return false;
                }
            }
        }
    }

    /**
     * 取代等效俚语词或不使用短语
     *
     * @param sWord 词
     * @return {@link String}
     */
    private String s_ReplaceWithEquivalentSlangWordOrPhrase_dont_use(String sWord) {
        int iNewWord =0;
        if (this.igSlangWordCount > 0) {
            iNewWord = Sort.i_FindStringPositionInSortedArray(sWord, this.sgSlangWord, 1, this.igSlangWordCount);
            if (iNewWord > 0) {
                return this.sgSlangStandardWord[iNewWord];
            }
        }

        return sWord;
    }

    /**
     * 加载俚语
     *
     * @return boolean
     */
    private boolean b_LoadSlang() {
        int iLinesInFile =0;
        if (this.sgSlangLookupTable == "") {
            return false;
        } else {
            iLinesInFile = FileOps.i_CountLinesInTextFile(this.sgSentiStrengthFolder + this.sgSlangLookupTable);
            if (iLinesInFile < 2) {
                return false;
            } else {
                this.igSlangWordMax = iLinesInFile + 1;
                String[] sSlangTemp1 = new String[this.igSlangWordMax];
                String[] sSlangTemp2 = new String[this.igSlangWordMax];
                this.sgSlangWord = sSlangTemp1;
                this.sgSlangStandardWord = sSlangTemp2;
                this.igSlangWordCount = 0;

                try {
                    BufferedReader rReader = new BufferedReader(new FileReader(this.sgSentiStrengthFolder + this.sgSlangLookupTable));

                    while(rReader.ready()) {
                        String sLine = rReader.readLine();
                        if (sLine != "") {
                            int iFirstTabLocation = sLine.indexOf("\t");
                            if (iFirstTabLocation >= 0) {
                                ++this.igSlangWordCount;
                                int iSecondTabLocation = sLine.indexOf("\t", iFirstTabLocation + 1);
                                if (iSecondTabLocation > 0) {
                                    this.sgSlangStandardWord[this.igSlangWordCount] = sLine.substring(iFirstTabLocation + 1, iSecondTabLocation);
                                } else {
                                    this.sgSlangStandardWord[this.igSlangWordCount] = sLine.substring(iFirstTabLocation + 1);
                                }

                                sLine = sLine.substring(0, iFirstTabLocation);
                                if (sLine.indexOf(" ") >= 0) {
                                    sLine = sLine.trim();
                                }

                                if (sLine != "") {
                                    this.sgSlangWord[this.igSlangWordCount] = sLine;
                                } else {
                                    --this.igSlangWordCount;
                                }
                            }
                        }
                    }

                    rReader.close();
                    return true;
                } catch (FileNotFoundException var9) {
                    var9.printStackTrace();
                    return false;
                } catch (IOException var10) {
                    var10.printStackTrace();
                    return false;
                }
            }
        }
    }

    /**
     * 检测文本中情感
     */
    private void DetectEmotionInText() {
        int iPosition = 0;
        int[] iPosSent = new int[1000];
        int[] iNegSent = new int[1000];
        int iPosTotal = 0;
        int iPosMax = 0;
        int iNegTotal = 0;
        int iNegMax = 0;
        if (this.sgOriginalText != "") {
            if (this.b_TagOriginalText()) {
                this.igTextPos = 1;
                this.igTextNeg = -1;
                this.igTaggedSentenceCount = 0;
                if (this.igSentimentWordsCount >= 1 || this.initialise()) {
                    iPosition = this.sgTaggedText.indexOf("<br>", iPosition + 1);
                    if (iPosition < 0) {
                        iPosition = this.sgTaggedText.length();
                    }

                    int iLastEnd = 0;

                    while(iPosition > 0 && this.igTaggedSentenceCount < this.igTaggedSentenceMax) {
                        ++this.igTaggedSentenceCount;
                        this.sgTaggedSentence[this.igTaggedSentenceCount] = this.sgTaggedText.substring(iLastEnd, iPosition);
                        this.DetectEmotionInSentence(iPosSent, iNegSent, this.igTaggedSentenceCount);
                        iLastEnd = iPosition + 4;
                        if (iPosition < this.sgTaggedText.length()) {
                            iPosition = this.sgTaggedText.indexOf("<br>", iPosition + 1);
                            if (iPosition < 0 && iLastEnd < this.sgTaggedText.length()) {
                                iPosition = this.sgTaggedText.length();
                            }
                        } else {
                            iPosition = 0;
                        }
                    }

                    this.igTextPos = 0;
                    this.igTextNeg = 0;

                    for(int iSentence = 1; iSentence <= this.igTaggedSentenceCount; ++iSentence) {
                        iNegTotal += iNegSent[iSentence];
                        if (iNegMax > iNegSent[iSentence]) {
                            iNegMax = iNegSent[iSentence];
                        }

                        iPosTotal += iPosSent[iSentence];
                        if (iPosMax < iPosSent[iSentence]) {
                            iPosMax = iPosSent[iSentence];
                        }
                    }

                    if (this.igEmotionParagraphCombineMethod == 1) {
                        this.igTextPos = (int)(((double)iPosTotal + 0.5D) / (double)this.igTaggedSentenceCount);
                        this.igTextNeg = (int)(((double)iNegTotal + 0.5D) / (double)this.igTaggedSentenceCount);
                    } else if (this.igEmotionParagraphCombineMethod == 0) {
                        this.igTextPos = iPosMax;
                        this.igTextNeg = iNegMax;
                    }

                    if (this.igTextPos == 0) {
                        this.igTextPos = 1;
                    }

                    if (this.igTextNeg == 0) {
                        this.igTextNeg = -1;
                    }

                }
            }
        }
    }

    /**
     * 检测语句中的情感
     *
     * @param iPositive 积极
     * @param iNegative 消极
     * @param iSentence 语句
     */
    private void DetectEmotionInSentence(int[] iPositive, int[] iNegative, int iSentence) {
        float[] fWordEmotion = new float[1000];
        int iMaxPos = 0;
        int iTotalPos = 0;
        int iMaxNeg = 0;
        int iTotalNeg = 0;
        int iWordTotal = this.i_GetEmotionWordList(fWordEmotion, iSentence);
        if (iWordTotal == 0) {
            iPositive[iSentence] = 0;
            iNegative[iSentence] = 0;
        } else {
            for(int iWord = 1; iWord <= iWordTotal; ++iWord) {
                if (fWordEmotion[iWord] < 0.0F) {
                    iTotalNeg = (int)((float)iTotalNeg + fWordEmotion[iWord]);
                    if ((float)iMaxNeg > fWordEmotion[iWord]) {
                        iMaxNeg = Math.round(fWordEmotion[iWord]);
                    }
                } else if (fWordEmotion[iWord] > 0.0F) {
                    iTotalPos = (int)((float)iTotalPos + fWordEmotion[iWord]);
                    if ((float)iMaxPos < fWordEmotion[iWord]) {
                        iMaxPos = Math.round(fWordEmotion[iWord]);
                    }
                }
            }

            --iMaxNeg;
            ++iMaxPos;
            iTotalNeg -= iWordTotal;
            iTotalPos += iWordTotal;
            if (this.igEmotionSentenceCombineMethod == 1) {
                iPositive[iSentence] = (int)(((double)iTotalPos + 0.5D) / (double)iWordTotal);
                iNegative[iSentence] = (int)(((double)iTotalNeg + 0.5D) / (double)iWordTotal);
            } else if (this.igEmotionSentenceCombineMethod == 0) {
                iPositive[iSentence] = iMaxPos;
                iNegative[iSentence] = iMaxNeg;
            }

            if (this.bgIgnoreNegativeEmotionInQuestionSentences && iNegative[iSentence] < -1 && (this.sgTaggedSentence[iSentence].indexOf("?") > 0 || this.sgTaggedSentence[iSentence].indexOf("what's") > 0 || this.sgTaggedSentence[iSentence].indexOf("whats ") > 0 || this.sgTaggedSentence[iSentence].indexOf("what<") > 0)) {
                iNegative[iSentence] = -1;
            }

            if (iPositive[iSentence] == 1 && this.bgMissCountsAsPlus2 && this.sgTaggedSentence[iSentence].indexOf(">miss<") >= 0) {
                iPositive[iSentence] = 2;
            }

            if (this.bgExclamationCountsAsPlus2 && iPositive[iSentence] == 1 && iNegative[iSentence] == -1 && this.sgTaggedSentence[iSentence].indexOf("!") >= 0) {
                iPositive[iSentence] = 2;
            }

            if (this.bgYouOrYourIsPlus2UnlessSentenceNegative && iPositive[iSentence] == 1 && iNegative[iSentence] == -1 && this.sgTaggedSentence[iSentence].indexOf(">you") >= 0) {
                iPositive[iSentence] = 2;
            }

            if (iPositive[iSentence] > 5) {
                iPositive[iSentence] = 5;
            }

            if (iNegative[iSentence] < -5) {
                iNegative[iSentence] = -5;
            }

        }
    }

    /**
     * 获取情感单词列表
     *
     * @param fWordEmotion 情感词
     * @param iSentence    语句
     * @return int
     */
    private int i_GetEmotionWordList(float[] fWordEmotion, int iSentence) {
        String[] sWord = new String[8];
        String sNextChar = "";
        int iPos = 0;
        int iWordID =0;
        int iWordsSinceNegative = 0;
        int iWordTotal = 0;
        int iLastWordBoosterStrength = 0;
        int iEnd = this.sgTaggedSentence[iSentence].length() - 1;
        boolean bLastWordNegates = false;

        while(iPos < iEnd && iWordTotal < 100) {
            sNextChar = this.sgTaggedSentence[iSentence].substring(iPos, iPos + 1);
            if (sNextChar.compareTo("<") != 0) {
                ++iPos;
            } else {
                sNextChar = this.sgTaggedSentence[iSentence].substring(iPos + 1, iPos + 2);
                int var10002;
                if (sNextChar.compareTo("w") == 0) {
                    iPos = this.i_GetWordFromTaggedText(iSentence, iPos, sWord);
                    if (sWord[1] != "") {
                        ++iWordTotal;
                        iWordID = Sort.i_FindStringPositionInSortedArrayWithWildcardsInArray(sWord[1].toLowerCase(), this.sgSentimentWords, 1, this.igSentimentWordsCount);
                        if (iWordID >= 0) {
                            fWordEmotion[iWordTotal] = (float)this.igSentimentWordsStrengthTake1[iWordID];
                        } else {
                            fWordEmotion[iWordTotal] = 0.0F;
                        }

                        if (this.bgCountMultipleLettersAsEmotionBoosters && sWord[2].length() >= this.igMinRepeatedLettersForBoost) {
                            if (fWordEmotion[iWordTotal] < 0.0F) {
                                fWordEmotion[iWordTotal] = (float)((double)fWordEmotion[iWordTotal] - 0.6D);
                            } else if ((this.bgCountNeutralEmotionsAsPositiveForEmphasis || fWordEmotion[iWordTotal] > 1.0F) && sWord[2].indexOf("xx") < 0 && sWord[2].indexOf("ww") < 0 && (sWord[2].indexOf("h") < 0 || sWord[2].indexOf("a") < 0)) {
                                fWordEmotion[iWordTotal] = (float)((double)fWordEmotion[iWordTotal] + 0.6D);
                            }
                        }

                        if (iLastWordBoosterStrength != 0 && this.bgBoosterWordsIncreaseEmotion) {
                            if (fWordEmotion[iWordTotal] < 0.0F) {
                                fWordEmotion[iWordTotal] -= (float)iLastWordBoosterStrength;
                            } else if (fWordEmotion[iWordTotal] > 1.0F) {
                                fWordEmotion[iWordTotal] += (float)iLastWordBoosterStrength;
                            }
                        }

                        iWordID = Sort.i_FindStringPositionInSortedArrayWithWildcardsInArray(sWord[1].toLowerCase(), this.sgBoosterWord, 1, this.igBoosterWordCount);
                        if (iWordID >= 0) {
                            iLastWordBoosterStrength = this.igBoosterWordStrength[iWordID];
                        } else {
                            iLastWordBoosterStrength = 0;
                        }

                        if (fWordEmotion[iWordTotal] != 0.0F && bLastWordNegates && this.bgNegatingWordsFlipEmotion && iWordsSinceNegative <= this.igMaxWordsSinceNegativeToFlip) {
                            fWordEmotion[iWordTotal] = -fWordEmotion[iWordTotal];
                        }

                        iWordID = Sort.i_FindStringPositionInSortedArrayWithWildcardsInArray(sWord[1].toLowerCase(), this.sgNegatingWord, 1, this.igNegatingWordCount);
                        if (iWordID >= 0) {
                            bLastWordNegates = true;
                            iWordsSinceNegative = 0;
                        } else if (iLastWordBoosterStrength == 0 || !this.bgIgnoreBoosterWordsAfterNegatives) {
                            ++iWordsSinceNegative;
                        }

                        if (iWordTotal > 1) {
                            if (fWordEmotion[iWordTotal] > 1.0F && fWordEmotion[iWordTotal - 1] > 1.0F) {
                                if (this.bgAllowMultiplePositiveWordsToIncreasePositiveEmotion) {
                                    var10002 = (int) fWordEmotion[iWordTotal]++;
                                }
                            } else if (fWordEmotion[iWordTotal] < -1.0F && fWordEmotion[iWordTotal - 1] < -1.0F && this.bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion) {
                                var10002 = (int) fWordEmotion[iWordTotal]--;
                            }
                        }
                    }
                } else if (sNextChar.compareTo("p") == 0) {
                    iPos = this.i_GetPunctuationFromTaggedText(iSentence, iPos, sWord);
                    if (sWord[5] != "" && sWord[5].indexOf("!") >= 0) {
                        if (fWordEmotion[iWordTotal] < 0.0F) {
                            fWordEmotion[iWordTotal] = (float)((double)fWordEmotion[iWordTotal] - 0.6D);
                        } else if (this.bgCountNeutralEmotionsAsPositiveForEmphasis || fWordEmotion[iWordTotal] > 1.0F) {
                            fWordEmotion[iWordTotal] = (float)((double)fWordEmotion[iWordTotal] + 0.6D);
                        }
                    }
                } else if (sNextChar.compareToIgnoreCase("b") == 0) {
                    ++iPos;
                } else if (sNextChar.compareTo("e") == 0) {
                    iPos = this.i_GetEmoticonFromTaggedText(iSentence, iPos, sWord);
                    if (iWordTotal == 0) {
                        iWordTotal = 1;
                    }

                    if (sWord[7] != "") {
                        if (sWord[7].indexOf("-") >= 0) {
                            var10002 = (int) fWordEmotion[iWordTotal]--;
                        } else {
                            var10002 = (int) fWordEmotion[iWordTotal]++;
                        }
                    }
                } else {
                    ++iPos;
                }
            }
        }

        return iWordTotal;
    }

    /**
     * 从标记文本获取消息
     *
     * @param iSentence    语句
     * @param iPos         位置
     * @param sWordAspects 特性词
     * @return int
     */
    private int i_GetWordFromTaggedText(int iSentence, int iPos, String[] sWordAspects) {
        int iOriginalStart = 0;
        if (sWordAspects[0] != "") {
            sWordAspects[0] = "";
        }

        if (sWordAspects[1] != "") {
            sWordAspects[1] = "";
        }

        if (sWordAspects[2] != "") {
            sWordAspects[2] = "";
        }

        int iOriginalEnd = this.sgTaggedSentence[iSentence].indexOf("</w>", iPos + 3);
        if (iOriginalEnd > 0) {
            iOriginalStart = this.sgTaggedSentence[iSentence].lastIndexOf(">", iOriginalEnd - 1);
        }

        if (iOriginalStart > 0) {
            sWordAspects[0] = this.sgTaggedSentence[iSentence].substring(iOriginalStart + 1, iOriginalEnd);
            sWordAspects[1] = sWordAspects[0];
            int iTranslatedStart = this.sgTaggedSentence[iSentence].indexOf("equiv=\"", iPos + 2);
            if (iTranslatedStart > 0 && iTranslatedStart < iOriginalStart) {
                iTranslatedStart += 7;
                int iTranslatedEnd = this.sgTaggedSentence[iSentence].indexOf("\"", iTranslatedStart + 1);
                if (iTranslatedEnd > 0) {
                    sWordAspects[1] = this.sgTaggedSentence[iSentence].substring(iTranslatedStart, iTranslatedEnd);
                }
            }

            int iEmStart = this.sgTaggedSentence[iSentence].indexOf("em=\"", iPos + 3);
            if (iEmStart > 0 && iEmStart < iOriginalStart) {
                iEmStart += 4;
                int iEmEnd = this.sgTaggedSentence[iSentence].indexOf("\"", iEmStart + 1);
                if (iEmEnd > 0) {
                    sWordAspects[2] = this.sgTaggedSentence[iSentence].substring(iEmStart, iEmEnd);
                }
            }

            iPos = iOriginalEnd + 4;
        } else {
            iPos = this.sgTaggedSentence[iSentence].length() + 1;
        }

        return iPos;
    }

    /**
     * 从标记文本获取把标点符号
     *
     * @param iSentence    语句
     * @param iPos         位置
     * @param sWordAspects 特性词
     * @return int
     */
    private int i_GetPunctuationFromTaggedText(int iSentence, int iPos, String[] sWordAspects) {
        int iOriginalStart = 0;
        if (sWordAspects[3] != "") {
            sWordAspects[3] = "";
        }

        if (sWordAspects[4] != "") {
            sWordAspects[4] = "";
        }

        if (sWordAspects[5] != "") {
            sWordAspects[5] = "";
        }

        int iOriginalEnd = this.sgTaggedSentence[iSentence].indexOf("</p>", iPos + 3);
        if (iOriginalEnd > 0) {
            iOriginalStart = this.sgTaggedSentence[iSentence].lastIndexOf(">", iOriginalEnd - 1);
        }

        if (iOriginalStart > 0) {
            sWordAspects[3] = this.sgTaggedSentence[iSentence].substring(iOriginalStart + 1, iOriginalEnd);
            sWordAspects[4] = sWordAspects[3];
            int iTranslatedStart = this.sgTaggedSentence[iSentence].indexOf("equiv=\"", iPos + 2);
            if (iTranslatedStart > 0 && iTranslatedStart < iOriginalStart) {
                iTranslatedStart += 7;
                int iTranslatedEnd = this.sgTaggedSentence[iSentence].indexOf("\"", iTranslatedStart + 1);
                if (iTranslatedEnd > 0) {
                    sWordAspects[4] = this.sgTaggedSentence[iSentence].substring(iTranslatedStart, iTranslatedEnd);
                }
            }

            int iEmStart = this.sgTaggedSentence[iSentence].indexOf("em=\"", iPos + 3);
            if (iEmStart > 0 && iEmStart < iOriginalStart) {
                iEmStart += 4;
                int iEmEnd = this.sgTaggedSentence[iSentence].indexOf("\"", iEmStart + 1);
                if (iEmEnd > 0) {
                    sWordAspects[5] = this.sgTaggedSentence[iSentence].substring(iEmStart, iEmEnd);
                }
            }

            iPos = iOriginalEnd + 4;
        } else {
            iPos = this.sgTaggedSentence[iSentence].length() + 1;
        }

        return iPos;
    }

    /**
     * 从标记文本获取表情符号
     *
     * @param iSentence    语句
     * @param iPos         位置
     * @param sWordAspects 特性词
     * @return int
     */
    private int i_GetEmoticonFromTaggedText(int iSentence, int iPos, String[] sWordAspects) {
        int iOriginalStart = 0;
        if (sWordAspects[6] != "") {
            sWordAspects[6] = "";
        }

        if (sWordAspects[7] != "") {
            sWordAspects[7] = "";
        }

        int iOriginalEnd = this.sgTaggedSentence[iSentence].indexOf("</e>", iPos + 2);
        if (iOriginalEnd > 0) {
            iOriginalStart = this.sgTaggedSentence[iSentence].lastIndexOf(">", iOriginalEnd - 1);
        }

        if (iOriginalStart > 0) {
            sWordAspects[6] = this.sgTaggedSentence[iSentence].substring(iOriginalStart + 1, iOriginalEnd);
            int iEmStart = this.sgTaggedSentence[iSentence].indexOf("em=\"", iPos + 3);
            if (iEmStart > 0 && iEmStart < iOriginalStart) {
                iEmStart += 4;
                int iEmEnd = this.sgTaggedSentence[iSentence].indexOf("\"", iEmStart + 1);
                if (iEmEnd > 0) {
                    sWordAspects[7] = this.sgTaggedSentence[iSentence].substring(iEmStart, iEmEnd);
                }
            }

            iPos = iOriginalEnd + 4;
        } else {
            iPos = this.sgTaggedSentence[iSentence].length() + 1;
        }

        return iPos;
    }
}
