package com.vdata.analysis.track.format;

import org.apache.hadoop.io.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TrackSplitUtils
{
  private static final TrackSplitUtils instance = new TrackSplitUtils();

  public static TrackSplitUtils getInstance() {
    return instance;
  }

  public List<List<String>> getSplitVisit(Text textLine)
  {
    List listText = new ArrayList();
    List listSession = new ArrayList();
    int intSessionNum = 1; int intNextEndNum = 1;
    StringTokenizer commaToker = new StringTokenizer(textLine.toString(), "\n");
    while (commaToker.hasMoreTokens()) {
      String strLine = commaToker.nextToken();
      String[] arrInfo = strLine.split("\001", -1);

      if ((arrInfo == null) || (arrInfo.length < 26)) {
        continue;
      }
      intNextEndNum = strLine.endsWith("\001") ? intSessionNum : 
        Integer.parseInt(arrInfo[(arrInfo.length - 1)].trim());
      if (intSessionNum < intNextEndNum) {
        if ((listSession != null) && (listSession.size() != 0)) {
          listText.add(listSession);
          listSession = new ArrayList();
        }
        intSessionNum = intNextEndNum;
      }
      listSession.add(strLine);
    }
    if ((listSession != null) && (listSession.size() != 0))
      listText.add(listSession);
    else {
      listText = null;
    }
    return listText;
  }
}