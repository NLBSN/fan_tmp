package com.vdata.analysis.utils;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 * @description 用于对trackurl进行解析的工具
 * @author tzc
 *
 */
public class TrackUrlUtils {

	/**
	 * @description 格式化trackurl
	 * @param trackUrl
	 *            trackurl字符串
	 * @return 返回格式化后的字符串
	 */
	public static String formatTrackUrl(String trackUrl) {
		String tmpTrackUrl = null;
		if (StringUtils.isNotBlank(trackUrl)) {
			trackUrl = trackUrl.replace("\\x", "%").replace("%7B", "{")
					.replace("%7D", "}").replace("%7b", "{")
					.replace("%7d", "}").replace("%3A", ":")
					.replace("%3a", ":").replace("\"", "'").replace("%2C", ",")
					.replace("%2c", ",").replace(" ", "").replace("，", ",")
					.replace("｛", "{").replace("｝", "}").replace("：", ":")
					.replace("‘", "'").replace("’", "'").replace("%27", "'")
					.replace("%20", "").replace("':'", "\001")
					.replace("%22", "'").replace("','", "\002")
					.replace("':", "':'").replace(",'", "','")
					.replace("\001", "':'").replace("\002", "','")
					.replace(":'}", "''}").replace(",}", "}");
			int indexPre = trackUrl.indexOf("{");// 前缀
			if (indexPre != 0 && indexPre != -1)
				trackUrl = trackUrl.substring(indexPre, trackUrl.length());

			int indexSuf = trackUrl.lastIndexOf("}");
			if (indexSuf == -1) {
				int tmpIndex = trackUrl.lastIndexOf("':'");
				String prefix = trackUrl.substring(0, tmpIndex + 3);
				String suffix = trackUrl.substring(tmpIndex + 3,
						trackUrl.length());
				suffix = suffix.replace("'", "").replace(",", "")
						.replace("[", "");
				trackUrl = prefix + suffix + "'}";
			} else if (indexSuf != trackUrl.length() - 1) {
				trackUrl = trackUrl.substring(0, indexSuf + 1);
			}

			trackUrl = trackUrl.replace("':'", "\001").replace("','", "\002")
					.replace("'", "").replace("{", "").replace("}", "")
					.replace("\001", "':'").replace("\002", "','");

			int dIndex = trackUrl.lastIndexOf("','");
			int mIndex = trackUrl.lastIndexOf("':'");
			if (dIndex > mIndex)
				trackUrl = trackUrl.substring(0, dIndex);
			String[] trackUrls = trackUrl.split("','", -1);
			if (trackUrls != null && trackUrl.length() != 0) {
				Map<String, String> map = new HashMap<String, String>();
				for (String tmp : trackUrls) {
					if (tmp.contains("':'")) {
						String[] tmps = tmp.split("':'", -1);
						String tmpKey = tmps[0];
						if (StringUtils.isNotBlank(tmpKey)) {
							tmpKey = tmpKey.replace("'", "");
							if (tmpKey.contains("%"))// 包含再去解码，提高效率
								tmpKey = URLDecodeUtils.parser(tmpKey);

							if (StringUtils.isNotBlank(tmpKey)) {
								String tmpValue = tmps[1];
								String value =null;
								if (StringUtils.isNotBlank(tmpValue)) {
									tmpValue = tmpValue.replace("'", "");

									value=tmpValue;
									if (tmpValue.contains("%")){// 包含再去解码，提高效率
										value = URLDecodeUtils
												.parser(tmpValue);
	
										boolean tmpVF = URLDecodeUtils
												.regWord(tmpValue);
										if (!tmpVF)// 如果解码后出现乱码，则保留原样,无乱码则更新值
											value = URLDecodeUtils.getSearchEngines(tmpValue);
									}
								}
								map.put(tmpKey, value);
							}
						}
					}
				}

				if (map != null && map.size() != 0) {
					tmpTrackUrl = JSONObject.fromObject(map).toString();
				}
			}

		}
		return tmpTrackUrl;
	}

}
