package uk.ac.wlv.wkaclass;

import java.io.*;

// Referenced classes of package uk.ac.wlv.wkaclass:
//            Arff, PredictClass, WekaCrossValidateNoSelection, WekaCrossValidateInfoGain, 
//            Utilities, WekaDirectTrainClassifyEvaluate

public class WekaMachineLearning
{

    public WekaMachineLearning()
    {
    }

    /**
     * main函数
     * @param args 函数参数
     * @throws Exception
     */
    public static void main(String args[])
        throws Exception
    {
        boolean bArgumentRecognised[] = new boolean[args.length];
        String arffTrainFile = "-";
        String arffEvalFile = "-";
        String classifierName = ",,all,";
        String classifierExclude = "none";
        String resultsFileName = "-";
        String addToClasspath = "";
        String summaryResultsFileName = "-";
        String instructionFilename = "-";
        overallHelp();
        for(int i = 0; i < args.length; i++)
            bArgumentRecognised[i] = false;

        for(int i = 0; i < args.length; i++)
        {
            if(args[i].equalsIgnoreCase("arff"))
            {
                Arff arff = new Arff();
                arff.main(args);
                return;
            }
            if(args[i].equalsIgnoreCase("predict"))
            {
                PredictClass predictClass = new PredictClass();
                PredictClass.main(args);
                return;
            }
            if(args[i].equalsIgnoreCase("noselection"))
            {
                WekaCrossValidateNoSelection wekaCrossValidateNoSelection = new WekaCrossValidateNoSelection();
                WekaCrossValidateNoSelection.main(args);
                return;
            }
            if(args[i].equalsIgnoreCase("infogain"))
            {
                WekaCrossValidateInfoGain wekaCrossValidateInfoGain = new WekaCrossValidateInfoGain();
                WekaCrossValidateInfoGain.main(args);
                return;
            }
            if(args[i].equalsIgnoreCase("pre"))
                bArgumentRecognised[i] = true;
            if(i < args.length - 1)
            {
                if(args[i].equalsIgnoreCase("classifier"))
                {
                    classifierName = (new StringBuilder(",,")).append(args[i + 1].toLowerCase()).append(",").toString();
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if(args[i].equalsIgnoreCase("exclude"))
                {
                    classifierExclude = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if(args[i].equalsIgnoreCase("instructions"))
                {
                    instructionFilename = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if(args[i].equalsIgnoreCase("arffTrain"))
                {
                    arffTrainFile = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if(args[i].equalsIgnoreCase("arffEval"))
                {
                    arffEvalFile = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if(args[i].equalsIgnoreCase("results"))
                {
                    resultsFileName = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if(args[i].equalsIgnoreCase("summary"))
                {
                    summaryResultsFileName = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if(args[i].equalsIgnoreCase("addToClasspath"))
                {
                    addToClasspath = args[i + 1];
                    Utilities.addToClassPath(addToClasspath);
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
            }
        }

        for(int i = 0; i < args.length; i++)
            if(!bArgumentRecognised[i])
            {
                System.out.println((new StringBuilder("Unrecognised command - wrong spelling or case?: ")).append(args[i]).toString());
                return;
            }

        reportParameters(args, classifierName, classifierExclude, addToClasspath, instructionFilename, arffTrainFile, arffEvalFile, resultsFileName, summaryResultsFileName);
        if(instructionFilename.equals("-"))
        {
            if(arffTrainFile.equals("-") || arffEvalFile.equals("-") || resultsFileName.equals("-") || summaryResultsFileName.equals("-"))
            {
                System.out.println("Must specify instructions or arffTrain, arffEval, results, and summary. Giving up.");
                return;
            }
            System.out.println((new StringBuilder("started training with ")).append(arffTrainFile).toString());
            WekaDirectTrainClassifyEvaluate.directClassifyAllArff(arffTrainFile, arffEvalFile, classifierName, classifierExclude, resultsFileName, summaryResultsFileName);
        } else
        {
            File f = new File(instructionFilename);
            if(f.exists())
            {
                BufferedReader reader;
                for(reader = new BufferedReader(new FileReader(instructionFilename)); reader.ready();)
                {
                    arffTrainFile = reader.readLine();
                    if(reader.ready())
                        arffEvalFile = reader.readLine();
                    if(reader.ready())
                        resultsFileName = reader.readLine();
                    if(reader.ready())
                    {
                        summaryResultsFileName = reader.readLine();
                        if(summaryResultsFileName.length() > 4)
                        {
                            System.out.println((new StringBuilder("started training with ")).append(arffTrainFile).toString());
                            WekaDirectTrainClassifyEvaluate.directClassifyAllArff(arffTrainFile, arffEvalFile, classifierName, classifierExclude, resultsFileName, summaryResultsFileName);
                        }
                    }
                }

                reader.close();
            } else
            {
                System.out.println((new StringBuilder(String.valueOf(instructionFilename))).append(" not found. Must contain train file/eval file/results file/summary file instead").toString());
            }
        }
        System.out.println("WekaAutoMachineLearning Done");
    }
    /**
     * 输出程序参数，并对数据集进行多次分类。
     * @param args 参数 args 表示程序参数。
     * @param classifierName 参数 classifierName 表示分类器名称。
     * @param classifierExclude 参数 classifierExclude 表示不参与测试的分类器名称（可以为空）。
     * @param addToClasspath 参数 addToClasspath 表示需要添加到类路径中的库。
     * @param instructionFilename 参数 instructionFilename 表示参数说明文件名。
     * @param arffTrainFile 参数 arffTrainFile 表示训练数据文件路径和名称。
     * @param arffEvalFile 参数 arffEvalFile 表示测试数据文件路径和名称。
     * @param resultsFileName 参数 resultsFileName 表示输出所有结果的文件路径和名称。
     * @param summaryResultsFileName 参数 summaryResultsFileName 表示输出实验统计数据的文件路径和名称。
     */
    public static void reportParameters(String args[], String classifierName, String classifierExclude, String addToClasspath, String instructionFilename, String arffTrainFile, String arffEvalFile, String resultsFileName, 
            String summaryResultsFileName)
    {
        System.out.println("Pre method defaults or set by command line:");
        System.out.println((new StringBuilder(" ")).append(classifierName).append(" [classifier] ALL/SMO/SLOG/BAYES/ADA/SMOreg/JRIP/DEC/J48 /MLP/LibSVM/LibLin -last 3 not in ALL").toString());
        System.out.println((new StringBuilder(" ")).append(classifierExclude).append(" [exclude] SMO/SLOG/BAYES/ADA/SMOreg/JRIP/DEC/J48 classifier to exclude").toString());
        System.out.println((new StringBuilder(" ")).append(instructionFilename).append(" [instructions] file name (train., eval., results file triples list)").toString());
        System.out.println((new StringBuilder(" ")).append(arffTrainFile).append(" [arffTrain] file").toString());
        System.out.println((new StringBuilder(" ")).append(arffEvalFile).append(" [arffEval] file").toString());
        System.out.println((new StringBuilder(" ")).append(resultsFileName).append(" [results] file").toString());
        System.out.println((new StringBuilder(" ")).append(summaryResultsFileName).append(" [summary] results file (just accuracy)").toString());
        System.out.println((new StringBuilder(" ")).append(addToClasspath).append(" [addToClasspath] file to add to classpath").toString());
    }

    /**
     *打印程序的帮助信息。
     */
    public static void overallHelp()
    {
        System.out.println("Approaches available (via command name)");
        System.out.println("  [pre] (default) - input pre-calcuated folds and pre-selected features");
        System.out.println("                  e.g., 10-fold evaluation from SentiStrength");
        System.out.println("  [infogain] process raw single ARFF with feature selection, auto folds");
        System.out.println("  [noselection] process raw single ARFF without feature selection, auto folds");
        System.out.println("  [arff] convert plain text to ARFF, with/wo ML prediction");
        System.out.println("");
    }
}
