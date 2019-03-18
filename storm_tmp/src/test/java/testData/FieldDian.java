package testData;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.*;

/**
 * @Description: traffic_fan
 * @author: fan
 * @Date: Created in 2018/12/11 11:17
 * @Modified By:
 */
public class FieldDian {
    public static void main(String[] args) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/data/5kmpoint.txt")), "utf8"));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("d:/data/5kmpoint_fan.txt")), "utf-8"));
        String string;
        while ((string = bufferedReader.readLine()) != null) {
            String[] strings = string.split(" ", -1);
            for (String tmp : strings) {
                if (StringUtils.isNotBlank(tmp)) {
                    bufferedWriter.write(tmp.trim());
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            }
        }
        bufferedWriter.close();
        bufferedReader.close();
    }
    @Test
    public void dfsd(){
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotBlank(jsonObject.toString())){

            System.out.println(jsonObject.toString().length());
        }
    }

}
