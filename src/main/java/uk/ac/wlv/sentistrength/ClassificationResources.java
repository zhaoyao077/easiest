// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   ClassificationResources.java

package uk.ac.wlv.sentistrength;

import java.io.File;

import uk.ac.wlv.sentistrength.resources.*;
import uk.ac.wlv.utilities.FileOps;

/**
 * The class defines the uk.resources for classification.
 * 该类定义了分类用到的资源
 * related UC: UC-26.
 */



public class ClassificationResources {
  public BoosterWordsList boosterWords;
  public CorrectSpellingsList correctSpellings;
  public EmoticonsList emoticons;
  public EvaluativeTerms evaluativeTerms;
  public IdiomList idiomList;
  public IronyList ironyList;
  public Lemmatiser lemmatiser;
  public NegatingWordList negatingWords;
  public QuestionWords questionWords;
  public SentimentWords sentimentWords;
  public String sgSentiStrengthFolder;
  public String sgSentimentWordsFile;
  public String sgSentimentWordsFile2;
  public String sgEmoticonLookupTable;
  public String sgCorrectSpellingFileName;
  public String sgCorrectSpellingFileName2;
  public String sgSlangLookupTable;
  public String sgNegatingWordListFile;
  public String sgBoosterListFile;
  public String sgIdiomLookupTableFile;
  public String sgQuestionWordListFile;
  public String sgIronyWordListFile;
  public String sgAdditionalFile;
  public String sgLemmaFile;

  /**
   * Constructor.
   * 构造器
   */
  public ClassificationResources() {
    boosterWords = BoosterWordsList.getInstance();
    correctSpellings = CorrectSpellingsList.getInstance();
    emoticons = EmoticonsList.getInstance();
    evaluativeTerms = EvaluativeTerms.getInstance();
    idiomList = IdiomList.getInstance();
    ironyList = IronyList.getInstance();
    lemmatiser = Lemmatiser.getInstance();
    negatingWords = NegatingWordList.getInstance();
    questionWords = QuestionWords.getInstance();
    sentimentWords = SentimentWords.getInstance();
    sgSentiStrengthFolder = System.getProperty("user.dir") + "/src/SentStrength_Data/";
    sgSentimentWordsFile = "EmotionLookupTable.txt";
    sgSentimentWordsFile2 = "SentimentLookupTable.txt";
    sgEmoticonLookupTable = "EmoticonLookupTable.txt";
    sgCorrectSpellingFileName = "Dictionary.txt";
    sgCorrectSpellingFileName2 = "EnglishWordList.txt";
    sgSlangLookupTable = "SlangLookupTable_NOT_USED.txt";
    sgNegatingWordListFile = "NegatingWordList.txt";
    sgBoosterListFile = "BoosterWordList.txt";
    sgIdiomLookupTableFile = "IdiomLookupTable.txt";
    sgQuestionWordListFile = "QuestionWords.txt";
    sgIronyWordListFile = "IronyTerms.txt";
    sgAdditionalFile = "";
    sgLemmaFile = "";
  }

  /**
   * Initialise the options.
   * 根据输入内容初始化资源
   *
   * @param options classification options
   * @return flag of success or failure
   */
  public boolean initialise(ClassificationOptions options) {
    int iExtraLinesToReserve = 0;
    if (sgAdditionalFile.compareTo("") != 0) {
      iExtraLinesToReserve = FileOps.i_CountLinesInTextFile(
              (new StringBuilder(String.valueOf(sgSentiStrengthFolder)))
                      .append(sgAdditionalFile)
                      .toString()
      );
      if (iExtraLinesToReserve < 0) {
        System.out.println(
                (new StringBuilder("No lines found in additional file! Ignoring "))
                        .append(sgAdditionalFile)
                        .toString()
        );
        return false;
      }
    }
    if (options.bgUseLemmatisation && !lemmatiser.initialise(
            (new StringBuilder(String.valueOf(sgSentiStrengthFolder)))
                    .append(sgLemmaFile).toString(),
            false
    )) {
      System.out.println(
              (new StringBuilder("Can't load lemma file! "))
                      .append(sgLemmaFile)
                      .toString()
      );
      return false;
    }
    File f = new File(
            (new StringBuilder(String.valueOf(sgSentiStrengthFolder)))
                    .append(sgSentimentWordsFile)
                    .toString()
    );
    if (!f.exists() || f.isDirectory()) {
      sgSentimentWordsFile = sgSentimentWordsFile2;
    }
    File f2 = new File(
            (new StringBuilder(String.valueOf(sgSentiStrengthFolder)))
                    .append(sgCorrectSpellingFileName)
                    .toString()
    );
    if (!f2.exists() || f2.isDirectory()) {
      sgCorrectSpellingFileName = sgCorrectSpellingFileName2;
    }
    if (emoticons.initialise(
            (new StringBuilder(String.valueOf(sgSentiStrengthFolder)))
                    .append(sgEmoticonLookupTable)
                    .toString(),
            options)
            &&
            correctSpellings.initialise(
                    (new StringBuilder(String.valueOf(sgSentiStrengthFolder)))
                            .append(sgCorrectSpellingFileName)
                            .toString(),
                    options)
            &&
            sentimentWords.initialise(
                    (new StringBuilder(String.valueOf(sgSentiStrengthFolder)))
                            .append(sgSentimentWordsFile)
                            .toString(),
                    options,
                    iExtraLinesToReserve)
            &&
            negatingWords.initialise(
                    (new StringBuilder(String.valueOf(sgSentiStrengthFolder)))
                            .append(sgNegatingWordListFile)
                            .toString(),
                    options)
            &&
            questionWords.initialise(
                    (new StringBuilder(String.valueOf(sgSentiStrengthFolder)))
                            .append(sgQuestionWordListFile)
                            .toString(),
                    options)
            &&
            ironyList.initialise(
                    (new StringBuilder(String.valueOf(sgSentiStrengthFolder)))
                            .append(sgIronyWordListFile)
                            .toString(),
                    options)
            &&
            boosterWords.initialise(
                    (new StringBuilder(String.valueOf(sgSentiStrengthFolder)))
                            .append(sgBoosterListFile)
                            .toString(),
                    options,
                    iExtraLinesToReserve)
            &&
            idiomList.initialise(
                    (new StringBuilder(String.valueOf(sgSentiStrengthFolder)))
                            .append(sgIdiomLookupTableFile)
                            .toString(),
                    options,
                    iExtraLinesToReserve)
    ) {
      if (iExtraLinesToReserve > 0) {
        return evaluativeTerms.initialise(
                (new StringBuilder(String.valueOf(sgSentiStrengthFolder)))
                        .append(sgAdditionalFile)
                        .toString(),
                options,
                idiomList,
                sentimentWords);
      } else {
        return true;
      }
    } else {
      return false;
    }
  }
}
