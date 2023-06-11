// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   Utilities.java

package uk.ac.wlv.wkaclass;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utilities
{

    public Utilities()
    {
    }

    /**
     * 打印当前的类路径。
     */
    public static void printClasspath()
    {
        ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
        URL urls[] = ((URLClassLoader)sysClassLoader).getURLs();
        for(int i = 0; i < urls.length; i++)
            System.out.println(urls[i].getFile());

    }

    /**
     *将指定路径添加到类路径中。
     * @param s  参数 s 表示需要添加的路径。
     * @throws Exception
     */
    public static void addToClassPath(String s)
            throws Exception
    {
        File f = new File(s);
        URL u = f.toURL();
        URLClassLoader urlClassLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
        Class urlClass =URLClassLoader.class;
        Method method = urlClass.getDeclaredMethod("addURL", new Class[] {URL.class});
        method.setAccessible(true);
        method.invoke(urlClassLoader, new Object[] {
                u
        });
    }

    /**
     *打印方法名称和警告信息。（有回车）
     * @param methodName 参数 methodName 表示需要打印警告信息的方法名称。
     */
    public static void printlnNameAndWarning(String methodName)
    {
        System.out.println((new StringBuilder("Starting ")).append(methodName).append(" ").append(now()).append(" ... ").toString());
    }

    /**
     *打印方法名称和警告信息。（没有回车）
     * @param methodName 参数 methodName 表示需要打印警告信息的方法名称。
     */
    public static void printNameAndWarning(String methodName)
    {
        System.out.print((new StringBuilder("Starting ")).append(methodName).append(" ").append(now()).append(" ... ").toString());
    }

    /**
     * 返回当前时间的字符串表示，格式为“yyyy-MM-dd HH：mm：ss”
     * @return
     */
    public static String now()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    /**
     * 获取当前时间。
     * @return
     */
    public static Date getNow()
    {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    /**
     * 计算两个日期之间的时间差
     * @param firstDate 参数 firstDate 表示第一个日期。
     * @param secondDate 参数 secondDate 表示第二个日期。
     * @return
     */
    public static String timeGap(Date firstDate, Date secondDate)
    {
        Calendar calFirst = Calendar.getInstance();
        Calendar calSecond = Calendar.getInstance();
        calFirst.setTime(firstDate);
        calSecond.setTime(secondDate);
        long firstMilis = calFirst.getTimeInMillis();
        long secondMilis = calSecond.getTimeInMillis();
        String timeGapReport = (new StringBuilder(String.valueOf(Long.toString((secondMilis - firstMilis) / 1000L)))).append(" secs").toString();
        return timeGapReport;
    }
}
