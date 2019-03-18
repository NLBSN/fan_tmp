package testData;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @description 切片id的计算
 */
public class SlicingCalculation {
    /**
     * 矩形区域范围
     */
    private final double maxLon = 136;
    private final double minLon = 72;
    private final double maxLat = 54;
    private final double minLat = 16;

    /**
     * 计算切片网格点id，格点从左上角开始计算，条件判断为在矩形内部
     *
     * @param level 1：代表的是点级别的数据（格点间距为1度），2：代表的是线级别的数据（格点间距为2度）
     * @param lon
     * @param lat
     * @return
     */
    public String getSliceId(int level, double lon, double lat) {
        if (inside(lon, lat)) {
            int line = (int) (maxLat - lat) / level;
            int column = (int) (lon - minLon) / level;
            return level + autoGenericCode(line, 2) + autoGenericCode(column, 2);
        }
        return null;
    }

    public String getSliceId(int level, JSONArray strs) {
        double lon = Double.parseDouble(strs.getString(0));
        double lat = Double.parseDouble(strs.getString(1));
        if (inside(lon, lat)) {
            int line = (int) (maxLat - lat) / level;
            int column = (int) (lon - minLon) / level;
            return level + autoGenericCode(line, 2) + autoGenericCode(column, 2);
        }
        return null;
    }

    /**
     * 判断点是否在矩形内部（不包含4个边界）
     *
     * @param lon
     * @param lat
     * @return
     */
    private boolean inside(double lon, double lat) {
        if ((lon > minLon && lon < maxLon) && (lat > minLat && lat < maxLat)) {
            return true;
        }
        return false;
    }

    /**
     * 将code修改为指定长度（num）的字符串，不足前面补0
     *
     * @param code
     * @param num
     * @return
     */
    private String autoGenericCode(int code, int num) {
        return String.format("%0" + num + "d", code);
    }

    /**
     * @param tmpLine
     * @param jsonLatLon
     * @return
     * @description id值的计算
     */
    public String calculation(String tmpLine, int id, JSONObject jsonLatLon) {
        // value：要素值_相态值_桩点,
        String[] strs = tmpLine.split(",", 2);
        String[] idNames = strs[0].split("_", 3);
        JSONArray lonLat = jsonLatLon.getJSONArray(idNames[2]);
        String sliceId = getSliceId(id, lonLat);
        return sliceId;
    }
}