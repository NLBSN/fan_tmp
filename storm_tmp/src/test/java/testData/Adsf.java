package testData;

import org.junit.Test;

/**
 * @Description: traffic_fan
 * @author: fan
 * @Date: Created in 2019/1/4 11:07
 * @Modified By:
 */
public class Adsf {
    @Test
    public void test() {
        String key = "112320830201811010100002102300";
        String color = "#0066ff";
        String ph = "300";
        String riskInfo;
        if (ph.equals("300")) {
            riskInfo = riskInfo(key, color, "");
        } else if (ph.equals("301")) {
            // 雨
            riskInfo = riskInfo(key, color, "雨");
        } else if (ph.equals("302")) {
            // 雪
            riskInfo = riskInfo(key, color, "雪");
        } else if (ph.equals("6")) {
            // 雨夹雪
            riskInfo = riskInfo(key, color, "雨夹雪");
        }
    }

    private String riskInfo(String key, String color, String info) {

        if (key.substring(1, 2).equals("1")) {
            // 当前
            switchRiskType(key.substring(2, 3));
            switchColor(color);
        } else if (key.substring(1, 2).equals("2") || key.substring(1, 2).equals(3)) {
            // 预计
        }
        return null;
    }

    private String switchRiskType(String type) {
        switch (type) {
            case "1":
                return "下雨";
            case "2":
                return "能见度";
            case "3":
                return "风力";
            case "4":
                return "结冰";
            case "5":
                return "高温";
            default:
                return "";
        }
    }

    private String switchColor(String color) {
        switch (color) {
            case "#0066ff":
                return "蓝色";
            case "#ffff00":
                return "黄色";
            case "#ff7e00":
                return "橙色";
            case "#ff0000":
                return "红色";
            default:
                return "";
        }
    }
}
