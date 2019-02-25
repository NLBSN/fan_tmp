package testData;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: traffic_fan
 * @author: fan
 * @Date: Created in 2018/12/5 8:45
 * @Modified By:
 */
public class XianInDian {
    public static void main(String[] args) throws Exception {
        // File writeD = new File("");
        // writeD.mkdirs();
        // File writeX = new File("");
        // writeX.mkdirs();

        BufferedReader bufferedReaderD = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:\\Users\\fan\\Desktop\\dian.txt")), "utf-8"));
        BufferedReader bufferedReaderX = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:\\Users\\fan\\Desktop\\xian.txt")), "utf-8"));
        // BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(write), "utf-8"));
        List list = new ArrayList();
        String tmp = new String();
        while ((tmp = bufferedReaderD.readLine()) != null) {
            list.add(tmp);
        }
        String tmpX = new String();
        while ((tmpX = bufferedReaderX.readLine()) != null) {
            JSONArray jsonArray = JSON.parseArray(tmpX);
            wc:for (Object tmpa : jsonArray) {
                if (list.contains(tmpa.toString())) {
                    continue wc;
                } else {
                    System.out.println(tmpa);
                }
            }
        }


    }
}
