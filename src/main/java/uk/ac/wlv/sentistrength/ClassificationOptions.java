//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package uk.ac.wlv.sentistrength;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * The class defines options for classification.
 * 该类定义了用于分类的配置项
 * related UC: UC-26.
 */
public class ClassificationOptions {
  public boolean bgTensiStrength = false;
  public String sgProgramName = "SentiStrength";
  public String sgProgramMeasuring = "sentiment";
  public String sgProgramPos = "positive sentiment";
  public String sgProgramNeg = "negative sentiment";
  //模式
  public boolean bgScaleMode = false;
  public boolean bgTrinaryMode = false;
  public boolean bgBinaryVersionOfTrinaryMode = false;
  public int igDefaultBinaryClassification = 1;
  public int igEmotionParagraphCombineMethod = 0;
  final int igCombineMax = 0;
  final int igCombineAverage = 1;
  final int igCombineTotal = 2;
  public int igEmotionSentenceCombineMethod = 0;
  public float fgNegativeSentimentMultiplier = 1.5F;
  public boolean bgReduceNegativeEmotionInQuestionSentences = false;
  public boolean bgMissCountsAsPlus2 = true;
  public boolean bgYouOrYourIsPlus2UnlessSentenceNegative = false;
  public boolean bgExclamationInNeutralSentenceCountsAsPlus2 = false;
  public int igMinPunctuationWithExclamationToChangeSentenceSentiment = 0;
  public boolean bgUseIdiomLookupTable = true;
  public boolean bgUseObjectEvaluationTable = false;
  public boolean bgCountNeutralEmotionsAsPositiveForEmphasis1 = true;
  public int igMoodToInterpretNeutralEmphasis = 1;
  public boolean bgAllowMultiplePositiveWordsToIncreasePositiveEmotion = true;
  public boolean bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion = true;
  public boolean bgIgnoreBoosterWordsAfterNegatives = true;
  public boolean bgCorrectSpellingsUsingDictionary = true;
  public boolean bgCorrectExtraLetterSpellingErrors = true;
  public String sgIllegalDoubleLettersInWordMiddle = "ahijkquvxyz";
  public String sgIllegalDoubleLettersAtWordEnd = "achijkmnpqruvwxyz";
  public boolean bgMultipleLettersBoostSentiment = true;
  public boolean bgBoosterWordsChangeEmotion = true;
  public boolean bgAlwaysSplitWordsAtApostrophes = false;
  public boolean bgNegatingWordsOccurBeforeSentiment = true;
  public int igMaxWordsBeforeSentimentToNegate = 0;
  public boolean bgNegatingWordsOccurAfterSentiment = false;
  public int igMaxWordsAfterSentimentToNegate = 0;
  public boolean bgNegatingPositiveFlipsEmotion = true;
  public boolean bgNegatingNegativeNeutralisesEmotion = true;
  public boolean bgNegatingWordsFlipEmotion = false;
  public float fgStrengthMultiplierForNegatedWords = 0.5F;
  public boolean bgCorrectSpellingsWithRepeatedLetter = true;
  public boolean bgUseEmoticons = true;
  public boolean bgCapitalsBoostTermSentiment = false;
  public int igMinRepeatedLettersForBoost = 2;
  public String[] sgSentimentKeyWords = null;
  public boolean bgIgnoreSentencesWithoutKeywords = false;
  public int igWordsToIncludeBeforeKeyword = 4;
  public int igWordsToIncludeAfterKeyword = 4;
  public boolean bgExplainClassification = false;
  public boolean bgEchoText = false;
  public boolean bgForceUTF8 = false;
  public boolean bgUseLemmatisation = false;
  public int igMinSentencePosForQuotesIrony = 10;
  public int igMinSentencePosForPunctuationIrony = 10;
  public int igMinSentencePosForTermsIrony = 10;

  /**
   * Constructor.
   * 构造器
   */
  public ClassificationOptions() {
  }

  /**
   * Parse the input String into a keyword list.
   * 将一个字符串解析为关键词列表
   *
   * @param sKeywordList A String
   */
  public void parseKeywordList(String sKeywordList) {
    this.sgSentimentKeyWords = sKeywordList.split(",");
    this.bgIgnoreSentencesWithoutKeywords = true;
  }

  /**
   * Print classification options.
   * 打印有内容的分类配置项
   *
   * @param wWriter             a buffer writer
   * @param iMinImprovement     min improvement
   * @param bUseTotalDifference flag of using total difference
   * @param iMultiOptimisations number of multi optimisations
   * @return flag of success or failure
   */
  public boolean printClassificationOptions(BufferedWriter wWriter,
                                            int iMinImprovement,
                                            boolean bUseTotalDifference,
                                            int iMultiOptimisations) {
    try {
      if (this.igEmotionParagraphCombineMethod == 0) {
        wWriter.write("Max");
      } else if (this.igEmotionParagraphCombineMethod == 1) {
        wWriter.write("Av");
      } else {
        wWriter.write("Tot");
      }
      if (this.igEmotionSentenceCombineMethod == 0) {
        wWriter.write("\tMax");
      } else if (this.igEmotionSentenceCombineMethod == 1) {
        wWriter.write("\tAv");
      } else {
        wWriter.write("\tTot");
      }
      if (bUseTotalDifference) {
        wWriter.write("\tTotDiff");
      } else {
        wWriter.write("\tExactCount");
      }
      wWriter.write("\t" + iMultiOptimisations
              + "\t" + this.bgReduceNegativeEmotionInQuestionSentences
              + "\t" + this.bgMissCountsAsPlus2
              + "\t" + this.bgYouOrYourIsPlus2UnlessSentenceNegative
              + "\t" + this.bgExclamationInNeutralSentenceCountsAsPlus2
              + "\t" + this.bgUseIdiomLookupTable
              + "\t" + this.igMoodToInterpretNeutralEmphasis
              + "\t" + this.bgAllowMultiplePositiveWordsToIncreasePositiveEmotion
              + "\t" + this.bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion
              + "\t" + this.bgIgnoreBoosterWordsAfterNegatives
              + "\t" + this.bgMultipleLettersBoostSentiment
              + "\t" + this.bgBoosterWordsChangeEmotion
              + "\t" + this.bgNegatingWordsFlipEmotion
              + "\t" + this.bgNegatingPositiveFlipsEmotion
              + "\t" + this.bgNegatingNegativeNeutralisesEmotion
              + "\t" + this.bgCorrectSpellingsWithRepeatedLetter
              + "\t" + this.bgUseEmoticons
              + "\t" + this.bgCapitalsBoostTermSentiment
              + "\t" + this.igMinRepeatedLettersForBoost
              + "\t" + this.igMaxWordsBeforeSentimentToNegate
              + "\t" + iMinImprovement);
      return true;
    } catch (IOException var6) {
      var6.printStackTrace();
      return false;
    }
  }

  /**
   * Print blank classification options.
   * 打印空白的分类配置项
   *
   * @param wWriter a buffer writer
   * @return flag of success or failure
   */
  public boolean printBlankClassificationOptions(BufferedWriter wWriter) {
    try {
      wWriter.write("~");
      wWriter.write("\t~");
      wWriter.write("\tBaselineMajorityClass");
      wWriter.write("\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~");
      return true;
    } catch (IOException var3) {
      var3.printStackTrace();
      return false;
    }
  }

  /**
   * Print classification options headings.
   * 打印配置项的头部信息
   *
   * @param wWriter a buffer writer
   * @return flag of success or failure
   */
  public boolean printClassificationOptionsHeadings(BufferedWriter wWriter) {
    try {
      wWriter.write("EmotionParagraphCombineMethod\t"
                      + "EmotionSentenceCombineMethod\t"
                      + "DifferenceCalculationMethodForTermWeightAdjustments\t"
                      + "MultiOptimisations\t"
                      + "ReduceNegativeEmotionInQuestionSentences\t"
                      + "MissCountsAsPlus2\t"
                      + "YouOrYourIsPlus2UnlessSentenceNegative\t"
                      + "ExclamationCountsAsPlus2\t"
                      + "UseIdiomLookupTable\t"
                      + "MoodToInterpretNeutralEmphasis\t"
                      + "AllowMultiplePositiveWordsToIncreasePositiveEmotion\t"
                      + "AllowMultipleNegativeWordsToIncreaseNegativeEmotion\t"
                      + "IgnoreBoosterWordsAfterNegatives\t"
                      + "MultipleLettersBoostSentiment\t"
                      + "BoosterWordsChangeEmotion\t"
                      + "NegatingWordsFlipEmotion\t"
                      + "NegatingPositiveFlipsEmotion\t"
                      + "NegatingNegativeNeutralisesEmotion\t"
                      + "CorrectSpellingsWithRepeatedLetter\t"
                      + "UseEmoticons\t"
                      + "CapitalsBoostTermSentiment\t"
                      + "MinRepeatedLettersForBoost\t"
                      + "WordsBeforeSentimentToNegate\t"
                      + "MinImprovement");
      return true;
    } catch (IOException var3) {
      var3.printStackTrace();
      return false;
    }
  }

  /**
   * Initialise classification options.
   * 初始化分类配置项
   *
   * @param sFilename file name
   * @return flag of success or failure
   */
  public boolean setClassificationOptions(String sFilename) {
    try {
      BufferedReader rReader = new BufferedReader(new FileReader(sFilename));
      while (rReader.ready()) {
        String sLine = rReader.readLine();
        int iTabPos = sLine.indexOf("\t");
        if (iTabPos > 0) {
          String[] sData = sLine.split("\t");
          if (sData[0].equals("EmotionParagraphCombineMethod")) {
            if (sData[1].contains("Max")) {
              this.igEmotionParagraphCombineMethod = 0;
            }

            if (sData[1].contains("Av")) {
              this.igEmotionParagraphCombineMethod = 1;
            }

            if (sData[1].contains("Tot")) {
              this.igEmotionParagraphCombineMethod = 2;
            }
          } else if (sData[0].equals("EmotionSentenceCombineMethod")) {
            if (sData[1].contains("Max")) {
              this.igEmotionSentenceCombineMethod = 0;
            }

            if (sData[1].contains("Av")) {
              this.igEmotionSentenceCombineMethod = 1;
            }

            if (sData[1].contains("Tot")) {
              this.igEmotionSentenceCombineMethod = 2;
            }
          } else if (sData[0].equals("IgnoreNegativeEmotionInQuestionSentences")) {
            this.bgReduceNegativeEmotionInQuestionSentences = Boolean.parseBoolean(sData[1]);
          } else if (sData[0].equals("MissCountsAsPlus2")) {
            this.bgMissCountsAsPlus2 = Boolean.parseBoolean(sData[1]);
          } else if (sData[0].equals("YouOrYourIsPlus2UnlessSentenceNegative")) {
            this.bgYouOrYourIsPlus2UnlessSentenceNegative = Boolean.parseBoolean(sData[1]);
          } else if (sData[0].equals("ExclamationCountsAsPlus2")) {
            this.bgExclamationInNeutralSentenceCountsAsPlus2 = Boolean.parseBoolean(sData[1]);
          } else if (sData[0].equals("UseIdiomLookupTable")) {
            this.bgUseIdiomLookupTable = Boolean.parseBoolean(sData[1]);
          } else if (sData[0].equals("Mood")) {
            this.igMoodToInterpretNeutralEmphasis = Integer.parseInt(sData[1]);
          } else if (sData[0].equals("AllowMultiplePositiveWordsToIncreasePositiveEmotion")) {
            this.bgAllowMultiplePositiveWordsToIncreasePositiveEmotion
                    = Boolean.parseBoolean(sData[1]);
          } else if (sData[0].equals("AllowMultipleNegativeWordsToIncreaseNegativeEmotion")) {
            this.bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion
                    = Boolean.parseBoolean(sData[1]);
          } else if (sData[0].equals("IgnoreBoosterWordsAfterNegatives")) {
            this.bgIgnoreBoosterWordsAfterNegatives = Boolean.parseBoolean(sData[1]);
          } else if (sData[0].equals("MultipleLettersBoostSentiment")) {
            this.bgMultipleLettersBoostSentiment = Boolean.parseBoolean(sData[1]);
          } else if (sData[0].equals("BoosterWordsChangeEmotion")) {
            this.bgBoosterWordsChangeEmotion = Boolean.parseBoolean(sData[1]);
          } else if (sData[0].equals("NegatingWordsFlipEmotion")) {
            this.bgNegatingWordsFlipEmotion = Boolean.parseBoolean(sData[1]);
//          } else if (sData[0].equals("NegatingWordsFlipEmotion")) {
//            this.bgNegatingPositiveFlipsEmotion = Boolean.parseBoolean(sData[1]);
//          } else if (sData[0].equals("NegatingWordsFlipEmotion")) {
//            this.bgNegatingNegativeNeutralisesEmotion = Boolean.parseBoolean(sData[1]);
          } else if (sData[0].equals("CorrectSpellingsWithRepeatedLetter")) {
            this.bgCorrectSpellingsWithRepeatedLetter = Boolean.parseBoolean(sData[1]);
          } else if (sData[0].equals("UseEmoticons")) {
            this.bgUseEmoticons = Boolean.parseBoolean(sData[1]);
          } else if (sData[0].equals("CapitalsAreSentimentBoosters")) {
            this.bgCapitalsBoostTermSentiment = Boolean.parseBoolean(sData[1]);
          } else if (sData[0].equals("MinRepeatedLettersForBoost")) {
            this.igMinRepeatedLettersForBoost = Integer.parseInt(sData[1]);
          } else if (sData[0].equals("WordsBeforeSentimentToNegate")) {
            this.igMaxWordsBeforeSentimentToNegate = Integer.parseInt(sData[1]);
          } else if (sData[0].equals("Trinary")) {
            this.bgTrinaryMode = true;
          } else if (sData[0].equals("Binary")) {
            this.bgTrinaryMode = true;
            this.bgBinaryVersionOfTrinaryMode = true;
          } else {
            if (!sData[0].equals("Scale")) {
              rReader.close();
              return false;
            }

            this.bgScaleMode = true;
          }
        }
      }

      rReader.close();
      return true;
    } catch (FileNotFoundException var7) {
      var7.printStackTrace();
      return false;
    } catch (IOException var8) {
      var8.printStackTrace();
      return false;
    }
  }

  /**
   * Name the type of programme.
   * 给程序命名
   *
   * @param bTensiStrength flag of type
   */
  public void nameProgram(boolean bTensiStrength) {
    this.bgTensiStrength = bTensiStrength;
    if (bTensiStrength) {
      this.sgProgramName = "TensiStrength";
      this.sgProgramMeasuring = "stress and relaxation";
      this.sgProgramPos = "relaxation";
      this.sgProgramNeg = "stress";
    } else {
      this.sgProgramName = "SentiStrength";
      this.sgProgramMeasuring = "sentiment";
      this.sgProgramPos = "positive sentiment";
      this.sgProgramNeg = "negative sentiment";
    }

  }
}
