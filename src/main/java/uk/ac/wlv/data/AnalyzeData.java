package uk.ac.wlv.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalyzeData {
  String outputMode;
  Boolean keyWordMode;
  String keyWord;
  String afterKey;
  String beforeKey;
  String textToAnalyze;
  public AnalyzeData(){

  }
}
