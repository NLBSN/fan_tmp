package com.old_kafka_storm_cycle_case4.analysis.tools;

import com.old_kafka_storm_cycle_case4.analysis.bean.LogBean;
import com.old_kafka_storm_cycle_case4.analysis.bean.OriginalTrackBean;
import com.old_kafka_storm_cycle_case4.analysis.utils.CookieIdUtils;
import com.old_kafka_storm_cycle_case4.analysis.utils.GetObjectToClassUtils;
import com.old_kafka_storm_cycle_case4.analysis.utils.TrackUrlUtils;
import com.old_kafka_storm_cycle_case4.analysis.utils.URLDecodeUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fan
 * @description OriginalTrack日志解析
 */
public class OriginalTrackAnalysis extends CommonAnalysis {

    @Override
    protected LogBean analysis(String line) {
        OriginalTrackBean bean = null;
        if (StringUtils.isNotBlank(line)) {
            Map<String, String> map = null;
            String[] lines = line.split("\001", -1);
            if (lines != null && lines.length != 0) {
                if (lines.length >= 14) {
                    if (map == null)
                        map = new HashMap<String, String>();
                    parser(map, lines);//日志解析
                }
            }
            Object obj = GetObjectToClassUtils.getObjectByMap(OriginalTrackBean.class, map);
            if (obj != null)
                bean = (OriginalTrackBean) obj;
        }
        return bean;
    }

    //日志解析具体逻辑
    private void parser(Map<String, String> map, String[] lines) {
        String cookieId = lines[0];//cookieId
        if (StringUtils.isBlank(cookieId))//如果为空，赋值一个随机数
            cookieId = CookieIdUtils.getCookieIdOfRand();
        map.put("cookieID", cookieId);

        String epoch = lines[1];//时间戳
        map.put("epoch", epoch);

        String userIp = lines[2];//用户ip
        map.put("userIp", userIp);

        //请求串解析，此处为主要的业务逻辑内容
        String requestStr = lines[5].split("empty.js.gif\\?", -1)[1];
        if (StringUtils.isNotBlank(requestStr)) {
            String[] requestStrs = requestStr.split(" ", -1);
            parserRequestStr(requestStrs[0], map);
        }

        //请求状态
        String status = lines[6].trim();
        if (StringUtils.isNotBlank(status))
            map.put("status", status);

        String url = lines[8];
        parserUrl(url, map);//解析url

        String userAgent = lines[9];//ua头
        map.put("userAgent", userAgent);
    }

    // 请求串进行解析
    private void parserRequestStr(String requestStr, Map<String, String> map) {
        if (StringUtils.isNotBlank(requestStr)) {
            StringTokenizer strRequestParameters = new StringTokenizer(
                    requestStr, "&");
            while (strRequestParameters.hasMoreTokens()) {
                String tmpString = strRequestParameters.nextToken().trim();
                parserParam(tmpString, map);
            }
        }
    }

    // 对每个参数进行解析
    private void parserParam(String param, Map<String, String> map) {
        if (getFlagOfParam(param, "site_name=")) {
            String tmpValue = getLastParam(param);
            if (StringUtils.isNotBlank(tmpValue)) {
                map.put("siteName", tmpValue);
            }
        } else if (getFlagOfParam(param, "version=")) {
            String tmpValue = getLastParam(param);
            if (StringUtils.isNotBlank(tmpValue)) {
                map.put("pageVersion", tmpValue);
            }
        } else if (getFlagOfParam(param, "post_count=")) {
            String tmpValue = getLastParam(param);
            if (StringUtils.isNotBlank(tmpValue)) {
                map.put("postCount", tmpValue);
            }
        } else if (getFlagOfParam(param, "window_size=")) {
            String tmpValue = getLastParam(param);
            if (StringUtils.isNotBlank(tmpValue)) {
                map.put("windowSize", tmpValue);
            }
        } else if (getFlagOfParam(param, "userid=")) {
            String tmpValue = getLastParam(param);
            if (StringUtils.isNotBlank(tmpValue)) {
                map.put("userId", tmpValue);
            }
        } else if (getFlagOfParam(param, "loadtime=")) {
            String tmpValue = getLastParam(param);
            if (StringUtils.isNotBlank(tmpValue)) {
                map.put("loadTime", tmpValue);
            }
        } else if (getFlagOfParam(param, "trackURL=")) {
            String tmpValue = getLastParam(param);
            if (StringUtils.isNotBlank(tmpValue)) {
                String trackUrl = parserTrackUrl(tmpValue, map);
                if (StringUtils.isNotBlank(trackUrl))
                    map.put("trackUrl", trackUrl);
            }

        } else if (getFlagOfParam(param, "_ga_utma=")) {
            String tmpValue = getLastParam(param);
            if (StringUtils.isNotBlank(tmpValue)) {
                map.put("gautma", tmpValue);
            }
        } else if (getFlagOfParam(param, "_trackParams=")) {
            String tmpValue = getLastParam(param);
            if (StringUtils.isNotBlank(tmpValue)) {
                try {
                    tmpValue = URLDecoder.decode(tmpValue, "utf-8");
                    map.put("unitParams", tmpValue);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        } else if (getFlagOfParam(param, "smsc=")) {
            String tmpValue = getLastParam(param);
            if (StringUtils.isNotBlank(tmpValue)) {
                try {
//					tmpValue = EncryptHelper.desDecrypt(tmpValue);
                    map.put("queryParams", tmpValue);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        } else if (getFlagOfParam(param, "referrer=")) {
            String tmpValue = getLastParam(param);
            if (StringUtils.isNotBlank(tmpValue)) {
                try {
                    tmpValue = URLDecodeUtils.parser(tmpValue);
                } catch (Exception e) {
                    tmpValue = URLDecodeUtils.getSearchEngines(tmpValue);
                }
                if (StringUtils.isNotBlank(tmpValue)) {
                    map.put("referrer", tmpValue);

                    String refdomain = URLDecodeUtils.getDomain(tmpValue);
                    map.put("refDomain", refdomain);
                }
            }
        } else if (getFlagOfParam(param, "supplycount.")) {// 特殊参数集合
            String tmpValue = getLastParam(param);
            if (StringUtils.isNotBlank(tmpValue)) {
                String[] params = param.split("=", 2);
                String[] keys = params[0].split(".", 2);
                if (keys.length == 2) {// supplycount.sortid
                    String tmpKey = keys[1];
                    String tmpSupplycount = map.get("supplyCount");
                    if (StringUtils.isNotBlank(tmpSupplycount)) {
                        try {
                            Map<String, String> tmpMap = new HashMap<String, String>();
                            tmpMap.put(tmpKey, tmpValue);
                            JSONObject json = JSONObject
                                    .fromObject(tmpSupplycount);
                            json.putAll(tmpMap);
                            map.put("supplyCount", json.toString());
                        } catch (Exception e) {// 如果无法正确解析，但需保留pv
                            // e.printStackTrace();
                        }
                    } else {
                        try {
                            Map<String, String> tmpMap = new HashMap<String, String>();
                            tmpMap.put(tmpKey, tmpValue);
                            JSONObject json = JSONObject.fromObject(tmpMap);
                            map.put("supplyCount", json.toString());
                        } catch (Exception e) {
                            // e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    // 解析trackurl
    private String parserTrackUrl(String trackUrl, Map<String, String> map) {
        String tmpline = null;
        trackUrl = TrackUrlUtils.formatTrackUrl(trackUrl);
        if (StringUtils.isNotBlank(trackUrl)) {
            JSONObject obj = null;
            try {
                obj = JSONObject.fromObject(trackUrl);
            } catch (Exception e) {
                return null;
            }// 以上解析方式能正常解析99%的数据，对于极少的个别错误数据直接抛弃trackurl

            if (obj != null) {
                boolean flag = obj.containsKey("init_refer");
                if (flag) {
                    String init_refer = obj.getString("init_refer");
                    if (StringUtils.isNotBlank(init_refer)) {

                        // 获取站外关键词,此处需要注意，init_refer从trackurl中提取出来需要先
                        //进行解码
                        String outWord = URLDecodeUtils.getWord(init_refer);
                        if (StringUtils.isNotBlank(outWord)) {
                            outWord = outWord.replace(".", "");
                            map.put("outKWord", outWord);
                        }

                        obj.put("init_refer", init_refer);
                    }
                }

                // catePath解析
                boolean cateFlag = obj.containsKey("cate");
                if (cateFlag) {
                    String catePath = obj.getString("cate");
                    if (StringUtils.isNotBlank(catePath))
                        map.put("catePath", catePath);
                }

                // areaPath解析
                boolean areaFlag = obj.containsKey("area");
                if (areaFlag) {
                    String areaPath = obj.getString("area");
                    areaPath = URLDecodeUtils.parser(areaPath);
                    if (StringUtils.isNotBlank(areaPath)) {
                        if (areaPath.contains("?"))
                            areaPath = "";
                        obj.put("area", areaPath);
                        map.put("areaPath", areaPath);
                    }
                }

                // infoId
                boolean infoidFlag = obj.containsKey("infoid");
                if (infoidFlag) {
                    String infoId = obj.getString("infoid");
                    if (StringUtils.isNotBlank(infoId))
                        map.put("infoId", infoId);
                }

                // infoType
                boolean infotypeFlag = obj.containsKey("infotype");
                if (infotypeFlag) {
                    String infoType = obj.getString("infotype");
                    if (StringUtils.isNotBlank(infoType))
                        map.put("infoType", infoType);
                }

                // userType
                boolean usertypeFlag = obj.containsKey("usertype");
                if (usertypeFlag) {
                    String userType = obj.getString("usertype");
                    if (StringUtils.isNotBlank(userType))
                        map.put("userType", userType);
                }

                // pageType
                boolean pageTypeFlag = obj.containsKey("pagetype");
                if (pageTypeFlag) {
                    String pageType = obj.getString("pagetype");
                    if (StringUtils.isNotBlank(pageType)) {
                        map.put("pageType", pageType);
                    } else {
                        map.put("pageType", "other");
                    }
                }

                // 兼容旧版工具类
                tmpline = obj.toString();
                tmpline = tmpline.replaceAll("'", "");
                tmpline = tmpline.replaceAll("\"", "'");
            }
        }
        return tmpline;
    }

    // 对每个参数进行判断是否符合规则
    private boolean getFlagOfParam(String param, String paramName) {
        return param.indexOf(paramName) == 0 && !param.equals(paramName);
    }

    // 分隔参数
    private String getLastParam(String param) {
        String tmpParam = null;
        if (StringUtils.isNotBlank(param)) {
            String[] params = param.split("=", 2);
            if (params.length == 2 && StringUtils.isNotBlank(params[1])
                    && !params[1].equals("NA"))
                tmpParam = params[1];
        }
        return tmpParam;
    }


    //解析url和站内关键
    private void parserUrl(String url, Map<String, String> map) {
        if (StringUtils.isNotBlank(url)) {
            try {
                url = URLDecodeUtils.parser(url);
            } catch (Exception e) {//如果解析出错，采用最大保留url上面的信息
                url = URLDecodeUtils.getSearchEngines(url);
            }

            if (StringUtils.isNotBlank(url)) {
                map.put("url", url);

                //提取站内关键词
                String inkWord = getWordByUrl(url);
                if (StringUtils.isBlank(inkWord)) {
                    String referer = map.get("referrer");
                    if (StringUtils.isNotBlank(referer)) {
                        inkWord = getWordByUrl(referer);
                    }
                }

                if (StringUtils.isNotBlank(inkWord))
                    map.put("inKWord", inkWord);
            }
        }
    }

    // 从url中获取关键词key
    private String getWordByUrl(String url) {
        String tmpWord = null;
        String domain = URLDecodeUtils.getSearchEngines(url);
        if (StringUtils.isNotBlank(domain)) {
            String pattern = "(&|\\?)(key=)[^&]+";
            Pattern p_a = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher m_a = p_a.matcher(url);
            while (m_a.find()) {
                String a = m_a.group();
                if (a.length() > 5) {// key=的长度{
                    a = a.substring(5, a.length());
                    if (StringUtils.isNotBlank(a)) {
                        tmpWord = a;
                        break;
                    }
                }
            }
        }
        return tmpWord;
    }

}
