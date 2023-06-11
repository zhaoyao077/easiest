package uk.ac.wlv.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.wlv.data.AnalyzeData;
import uk.ac.wlv.data.HighLevelOptions;
import uk.ac.wlv.sentistrength.SentiStrength;
import uk.ac.wlv.utilities.FileOps;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.stream.Collectors;


@RestController
@CrossOrigin
public class SentiStrengthController {
  String inputFileName;
  ArrayList<String>params = new ArrayList<>();

  boolean overwrite = false;

  boolean classifyId = false;

  @CrossOrigin
  @RequestMapping(value = "/upload", method = RequestMethod.POST)
  public InputStream upload(@RequestParam("filename") MultipartFile file, HttpServletResponse response)throws Exception {
    //在这里response也可以设置跨域
    SentiStrength classifier = new SentiStrength();
    String filename = file.getOriginalFilename();
    inputFileName = filename;

    String []args = new String[6];
    InputStream inputStream = file.getInputStream();
    String result = new BufferedReader(new InputStreamReader(inputStream))
            .lines().collect(Collectors.joining(System.lineSeparator()));
    File file1 = new File("src/inputFolder/" + filename);
    FileWriter fileWriter;
    try{
      fileWriter = new FileWriter(file1);
      fileWriter.write(result);
      fileWriter.close();
    }catch(IOException e){
      e.printStackTrace();

    }
    args[0] = "input";
    args[1] = "src/inputFolder/"+filename;
    args[2] = "inputfolder";
    args[3] = "src/inputFolder/";
    args[4] = "outputfolder";
    args[5] = "src/outputFolder/";
    classifier.initialiseAndRun(args);
    params.clear();
    return null;
  }


  @RequestMapping(value="download.json")
  public Object download(HttpServletResponse res) throws IOException {
    SentiStrength classifier = new SentiStrength();
    File file;
    if(!overwrite && !classifyId) {
      String resultFileName = FileOps.getNextAvailableFilename(
              FileOps.s_ChopFileNameExtension(inputFileName),
              "_out.txt");
      file = new File("src/outputFolder/" + resultFileName);
      String fileName = resultFileName;
      URLEncoder.encode(fileName, "utf-8");
      res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    }else if(classifyId){
      String resultFileName =
              FileOps.s_ChopFileNameExtension(inputFileName)+
              "_classID.txt";
      file = new File("src/inputFolder/" + resultFileName);
      String fileName = resultFileName;
      URLEncoder.encode(fileName, "utf-8");
      res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
      classifyId =false;
    }else{
       file = new File("src/inputFolder/" + inputFileName);
      String fileName = inputFileName;
      URLEncoder.encode(fileName, "utf-8");
      res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
      overwrite = false;
    }
    byte[] buff = new byte[1024];
    BufferedInputStream bis = null;
    OutputStream os = null;
    try {
      os = res.getOutputStream();
      bis = new BufferedInputStream(new FileInputStream(file));
      int i = bis.read(buff);
      while (i != -1) {
        os.write(buff, 0, buff.length);
        os.flush();
        i = bis.read(buff);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (bis != null) {
        try {
          bis.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  @RequestMapping(value ="/analyzeWithCol",method = RequestMethod.POST)
  public Object analyzeWithCol(@RequestParam("filename") MultipartFile file,@RequestParam("idColumn") String idColumn,@RequestParam("column") String column) throws Exception{
    SentiStrength classifier = new SentiStrength();
    String filename = file.getOriginalFilename();
    inputFileName = filename;

    InputStream inputStream = file.getInputStream();
    String result = new BufferedReader(new InputStreamReader(inputStream))
            .lines().collect(Collectors.joining(System.lineSeparator()));
    File file1 = new File("src/inputFolder/" + filename);
    FileWriter fileWriter;
    try{
      fileWriter = new FileWriter(file1);
      fileWriter.write(result);
      fileWriter.close();
    }catch(IOException e){
      e.printStackTrace();

    }
    params.add("input");
    params.add("src/inputFolder/"+filename);
    params.add("inputFolder");
    params.add("src/inputFolder/");
    params.add("outputfolder");
    params.add("src/outputFolder/");
    if(!idColumn.equals("")){
      params.add("idcol");
      params.add(idColumn);
      params.add("textcol");
      params.add(column);
      classifyId = true;

    }else{
      params.add("annotatecol");
      params.add(column);
      params.add("overwrite");
      overwrite = true;
    }
    String args[] =new String[params.size()];
    for(int i=0;i<params.size();i++){
      args[i] = params.get(i);
    }
    System.out.println(idColumn);
    System.out.println(column);
    classifier.initialiseAndRun(args);
    params.clear();
    return null;
  }
  @CrossOrigin
  @RequestMapping(value= "/options",method = RequestMethod.POST)
  public void setHighOptions(@RequestBody HighLevelOptions highOptions){
      if(highOptions.getChecked1()){
        params.add("noBoosters");
        System.out.println(highOptions.getChecked1());
      }
      if(highOptions.getChecked2()){
        params.add("noNegators");
      }
      if(highOptions.getChecked3()){
        params.add("noNegatingPositiveFlipsEmotion");
      }
      if(highOptions.getChecked4()){
        params.add("noNegatingNegativeNeutralisesEmotion");
      }
      if(highOptions.getChecked7()){
        params.add("noIdioms");
      }
      if(highOptions.getChecked8()){
        params.add("questionsReduceNeg");
      }
      if(highOptions.getChecked9()){
        params.add("noEmoticons");
      }
      if(highOptions.getChecked10()){
        params.add("exclamations2");
        System.out.println(highOptions.getChecked10());
      }
      if(highOptions.getChecked11()){
        params.add("noMultiplePosWords");
      }
      if(highOptions.getChecked12()){
        params.add("noMultipleNegWords");
      }
      if(highOptions.getChecked13()){
        params.add("noIgnoreBoosterWordsAfterNegatives");
      }
      if(highOptions.getChecked14()){
        params.add("noDictionary");
      }
      if(highOptions.getChecked16()){
        params.add("noDeleteExtraDuplicateLetters");
      }
      if(highOptions.getChecked15()){
        params.add("noMultipleLetters");
      }
      if(highOptions.getChecked17()){
        params.add("capitalsBoostTermSentiment");
      }
      if(highOptions.getChecked18()){
        params.add("alwaysSplitWordsAtApostrophes");
      }
      if(highOptions.getChecked6()){
        params.add("negatingWordsDontOccurBeforeSentiment");
      }
      if(!highOptions.getChecked5()){
        params.add("negatingWordsOccurAfterSentiment");
      }
      params.add("negativeMultiplier");
      params.add(String.valueOf(highOptions.getNum1()));
    System.out.println(highOptions.getNum());
      params.add("negatedWordStrengthMultiplier");
      params.add(String.valueOf(highOptions.getNum()));

      params.add("maxWordsBeforeSentimentToNegate");
      params.add(highOptions.getInput1());


      params.add("maxWordsAfterSentimentToNegate");
      params.add(highOptions.getInput2());


      params.add("MinSentencePosForQuotesIrony");
      params.add(highOptions.getInput8());


      params.add("MinSentencePosForPunctuationIrony");
      params.add(highOptions.getInput9());


      params.add("MinSentencePosForTermsIrony");
      params.add(highOptions.getInput10());

      params.add("MinSentencePosForAllIrony");
      params.add(highOptions.getInput11());


      params.add("minPunctuationWithExclamation");
      params.add(highOptions.getInput3());
      System.out.println(highOptions.getInput3());


      params.add("illegalDoubleLettersInWordMiddle");
      params.add(highOptions.getInput5());



      params.add("illegalDoubleLettersAtWordEnd");
      params.add(highOptions.getInput6());


      params.add("mood");
      params.add(highOptions.getInput4());

  }
  @CrossOrigin
  @RequestMapping(value = "/analyze", method = RequestMethod.POST)
  public String analyze(@RequestBody AnalyzeData analyzeData) {
    SentiStrength classifier = new SentiStrength();
    // OutputMode
    params.add(analyzeData.getOutputMode());
    System.out.println(analyzeData.getOutputMode());
    // text
    params.add("text");
    params.add(analyzeData.getTextToAnalyze());
    System.out.println(analyzeData.getTextToAnalyze());
    params.add("explain");
    // keywords
    if (analyzeData.getKeyWordMode()) {

      params.add("keywords");
      params.add(analyzeData.getKeyWord());
      params.add("wordsBeforeKeywords");
      params.add(analyzeData.getBeforeKey());
      params.add("wordsAfterKeywords");
      params.add(analyzeData.getAfterKey());
    }

    int length = params.size();
    String[] args = new String[length];
    for (int i = 0; i < length; i++) {
      args[i] = params.get(i);
    }

    classifier.initialiseAndRun(args);
    params.clear();
    return classifier.textAnalyzeResult;
  }

  @CrossOrigin
  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  public String hello(){
    return "Hello World";//测试端口监听
  }
}
