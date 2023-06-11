// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   IronyList.java

package uk.ac.wlv.sentistrength.resources;

import java.io.*;


import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.utilities.FileOps;
import uk.ac.wlv.utilities.Sort;

// Referenced classes of package uk.ac.wlv.sentistrength:
//            ClassificationOptions

public class IronyList
{
    /**
     * 讽刺术语数组
     */
    private String sgIronyTerm[];
    /**
     * 讽刺术语个数
     */
    private int igIronyTermCount;
    /**
     * 讽刺术语最大个数
     */
    private int igIronyTermMax;

    /**
     * 构造函数
     */
    private IronyList()
    {
        igIronyTermCount = 0;
        igIronyTermMax = 0;
    }

    /**.
     * 构造实例
     */
    private static IronyList ironyList = null;

    /**.
     * 获得实例
     */
    public static IronyList getInstance(){
        if(ironyList == null){
            ironyList = new IronyList();
        }
        return ironyList;
    }

    /**
     * 使用utilities包中的Sort类中的方法查询该术语是否在数组中判断术语是否是讽刺
     * @param term 术语字符串
     * @return 是否是讽刺的
     */
    public boolean termIsIronic(String term)
    {
        int iIronyTermCount = Sort.i_FindStringPositionInSortedArray(term, sgIronyTerm, 1, igIronyTermCount);
        return iIronyTermCount >= 0;
    }

    /**
     * 初始化讽刺术语
     * @param sSourceFile 源文件名
     * @param options 分类选项
     * @return 是否初始化成功
     */
    public boolean initialise(String sSourceFile, ClassificationOptions options)
    {
        if(igIronyTermCount > 0)
            return true;
        File f = new File(sSourceFile);
        if(!f.exists())
            return true;
        try
        {
            igIronyTermMax = FileOps.i_CountLinesInTextFile(sSourceFile) + 2;
            igIronyTermCount = 0;
            String sIronyTermTemp[] = new String[igIronyTermMax];
            sgIronyTerm = sIronyTermTemp;
            BufferedReader rReader;
            if(options.bgForceUTF8)
                rReader = new BufferedReader(new InputStreamReader(new FileInputStream(sSourceFile), "UTF8"));
            else
                rReader = new BufferedReader(new FileReader(sSourceFile));
            String sLine;
            while((sLine = rReader.readLine()) != null) 
                if(sLine != "")
                {
                    String sData[] = sLine.split("\t");
                    if(sData.length > 0)
                        sgIronyTerm[++igIronyTermCount] = sData[0];
                }
            rReader.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println((new StringBuilder("Could not find IronyTerm file: ")).append(sSourceFile).toString());
            e.printStackTrace();
            return false;
        }
        catch(IOException e)
        {
            System.out.println((new StringBuilder("Found IronyTerm file but could not read from it: ")).append(sSourceFile).toString());
            e.printStackTrace();
            return false;
        }
        Sort.quickSortStrings(sgIronyTerm, 1, igIronyTermCount);
        return true;
    }
}
