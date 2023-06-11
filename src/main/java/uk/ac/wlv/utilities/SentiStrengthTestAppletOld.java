// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   SentiStrengthTestAppletOld.java

package uk.ac.wlv.utilities;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Referenced classes of package uk.ac.wlv.utilities:
//            SentiStrengthOld

/**
 * 情感强度测试程序
 *
 * @author 13986
 * @date 2023/03/06
 */
public class SentiStrengthTestAppletOld extends Applet
    implements ActionListener
{

    /**
     * 串行版本uid
     */
    private static final long serialVersionUID = 0x858280b1L;
    /**
     * 字体
     */
    Font fntTimesNewRoman;
    /**
     * 输入文本
     */
    String sgEnteredText;
    /**
     * 文本框
     */
    TextField tfField;
    /**
     * 单词列表
     */
    String sgWordList[];
    /**
     * 情感强度
     */
    SentiStrengthOld ss;
    /**
     * 情感强度是否ok
     */
    boolean bgSentiStrengthOK;

    /**
     * 情感强度测试程序
     */
    public SentiStrengthTestAppletOld()
    {
        fntTimesNewRoman = new Font("TimesRoman", 1, 36);
        sgEnteredText = "";
        tfField = new TextField(12);
        bgSentiStrengthOK = false;
    }

    /**
     * 初始化
     */
    public void init()
    {
        ss = new SentiStrengthOld();
        bgSentiStrengthOK = ss.initialise();
        setBackground(Color.lightGray);
        tfField.addActionListener(this);
        add(tfField);
    }

    /**
     * 绘图
     *
     * @param g 绘图
     */
    public void paint(Graphics g)
    {
        g.setFont(fntTimesNewRoman);
        if(bgSentiStrengthOK)
        {
            g.drawString("sentiStrength successfully initialised", 100, 75);
        } else
        {
            g.drawString("Error - can't initalise sentiStrength", 100, 75);
            g.drawString(ss.getErrorLog(), 100, 125);
        }
        if(sgEnteredText != "")
            if(sgEnteredText.indexOf("\\") >= 0)
            {
                if(ss.classifyAllTextInFile(sgEnteredText, (new StringBuilder(String.valueOf(sgEnteredText))).append("_output.txt").toString()))
                    g.drawString("No problem with text file classification", 10, 275);
                else
                    g.drawString("Text file classification failed", 10, 275);
            } else
            {
                ss.detectEmotionInText(sgEnteredText);
                g.drawString(ss.getOriginalText(), 10, 225);
                g.drawString("was tagged as:", 100, 275);
                g.drawString(ss.getTaggedText(), 10, 325);
                g.drawString((new StringBuilder("Positive sentiment of text: ")).append(ss.getPositiveClassification()).append(", negative: ").append(ss.getNegativeClassification()).toString(), 10, 375);
            }
    }

    /**
     * 执行操作
     *
     * @param e 执行事件
     */
    public void actionPerformed(ActionEvent e)
    {
        sgEnteredText = tfField.getText();
        repaint();
    }
}
