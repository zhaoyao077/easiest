// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   TextParsingOptions.java

package uk.ac.wlv.sentistrength;


/**
 * The class defines the options for text parsing.
 * 文本分析所需的配置项
 * related UC: UC-11,UC-26.
 */
public class TextParsingOptions {
  public boolean bgIncludePunctuation;
  public int igNgramSize;
  public boolean bgUseTranslations;
  public boolean bgAddEmphasisCode;

  /**
   * Constructor.
   * 构造器，初始化配置
   */
  public TextParsingOptions() {
    bgIncludePunctuation = true;
    igNgramSize = 1;
    bgUseTranslations = true;
    bgAddEmphasisCode = false;
  }
}
