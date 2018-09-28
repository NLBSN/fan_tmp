package com.fan.analysis.track.format;


import org.apache.hadoop.io.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @description track日志切割工具类
 * @author fan
 */
public class TrackSplitUtils {

    private TrackSplitUtils() {
    }

    private static final TrackSplitUtils instance = new TrackSplitUtils();

    public static TrackSplitUtils getInstance() {
        return instance;
    }

    public List<List<String>> getSplitVisit(Text textLine) {
        List<List<String>> listText = new ArrayList<List<String>>();
        List<String> listSession = new ArrayList<String>();
        int intSessionNum = 1, intNextEndNum = 1;
        StringTokenizer commaToker = new StringTokenizer(textLine.toString(), "\n");
        while (commaToker.hasMoreTokens()) {
            String strLine = commaToker.nextToken();
            String[] arrInfo = strLine.split("\\|", -1);

            if (arrInfo == null || arrInfo.length < 26)//以前历史数据遗留换行符问题
                continue;

            intNextEndNum = strLine.endsWith("|") ? intSessionNum : Integer
                    .parseInt(arrInfo[arrInfo.length - 1].trim());
            if (intSessionNum < intNextEndNum) {
                listText.add(listSession);
                intSessionNum = intNextEndNum;
                listSession = new ArrayList<String>();
            }
            listSession.add(strLine);
        }
        listText.add(listSession);
        return listText;
    }

}
