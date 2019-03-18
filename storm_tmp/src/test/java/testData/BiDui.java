package testData;

import org.junit.Test;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description: traffic_fan
 * @author: fan
 * @Date: Created in 2018/12/18 13:59
 * @Modified By:
 */
public class BiDui {
    public static void main(String[] args) throws Exception {
        BufferedReader hou = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:\\data\\fengxian\\ceshi1.txt")), "utf-8"));
        BufferedReader yuan = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:\\data\\fengxian\\ceshiyuan.txt")), "utf-8"));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\data\\fengxian\\bidui.txt")), "utf-8"));

        List list = new LinkedList();
        String yuans = new String();

        while ((yuans = yuan.readLine()) != null) {
            list.add(yuans);
        }

        String hous = new String();
        while ((hous = hou.readLine()) != null) {
            if (!list.toString().contains(hous)) {
                bufferedWriter.write(hous);
                bufferedWriter.newLine();
                System.out.println(hous);
            }
        }
        bufferedWriter.close();
        hou.close();
        yuan.close();
    }

    @Test
    public void st() throws Exception {
        BufferedReader hou = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:\\data\\fengxian\\ceshi1.txt")), "utf-8"));
        BufferedReader yuan = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:\\data\\fengxian\\ceshiyuan.txt")), "utf-8"));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\data\\fengxian\\bidui2.txt")), "utf-8"));

        List list = new LinkedList();
        String yuans = new String();
        String hous = new String();

        while ((hous = hou.readLine()) != null) {
            list.add(hous);
        }

        while ((yuans = yuan.readLine()) != null) {
            if (!list.toString().contains(yuans)) {
                bufferedWriter.write(yuans);
                bufferedWriter.newLine();
                System.out.println(yuans);
            }
        }
        bufferedWriter.close();
        hou.close();
        yuan.close();
    }
}
