package com.old_kafka_storm_cycle_case4.analysis.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * @description URL乱码的处理
 * @author fan
 */
public class URLDecodeUtils {
	private static final String[] PS = { "`", "~", "!", "@", "#", "$", "^",
			"&", "(", ")", ":", ";", "\"", "'", "<", ",", ">", "?", "/", "=",
			"{", "}", "[", "]" };

	private static String[] psEncode = null;

	private static final String PATTER_GBK_UTF8 = "%[0-9a-fA-F]{2}";
	private static final String PATTER_UNICODE = "%u[0-9a-fA-F]{4}";
	private static final String PATTER_E = "%[^0-9a-fA-F]{2}";
	private static final String PATTER_P = "[^=+\\s\\p{Punct}A-Za-z0-9\u4E00-\u9FA5\u0800-\u4e00\\x3130-\\x318F\\xAC00-\\xD7A3，。！￥（）《》？、”“·‘’；：【】]+";// 过滤乱码
	private static Pattern partternOfGbk_utf8 = null;// 用于截取的正则表达式
	private static Pattern partternOfUnicode = null;
	private static Pattern partternOfP = null;// 过滤乱码的正则对象
	private static Pattern partternOfE = null;// 识别带有%，但不是正确编码的字符
	static {
		psEncode = new String[PS.length];
		for (int i = 0; i < PS.length; i++) {
			try {
				String code = URLEncoder.encode(PS[i], "utf-8");
				psEncode[i] = code;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}

		// 初始化正则表达式对象
		partternOfGbk_utf8 = getPattern(PATTER_GBK_UTF8);
		partternOfUnicode = getPattern(PATTER_UNICODE);
		partternOfP = getPattern(PATTER_P);
		partternOfE = getPattern(PATTER_E);
	}

	private static String parserUrl(String url) throws Exception {
		url = url.replace("\\x", "%");
		url = replaceOfP(url);// 替换%25标识符
		url = replaceOfUP(url);// 替换其它非%的字符
		url = url.replaceAll("%20", " ");// 替换一下20%
		Matcher m_a_unicode = partternOfUnicode.matcher(url);
		boolean unicodeFlag = false;
		while (m_a_unicode.find()) {// unicode解码
			unicodeFlag = true;
			String tmp = m_a_unicode.group();// 如%u3a4f,对其进行标准化处理
			String code1 = "%" + tmp.substring(2, 4);
			String code2 = "%" + tmp.substring(4, 6);
			url = url.replaceAll(tmp, code1 + code2);
		}

		if (unicodeFlag) {// 如果是unicode直接按unicode进行解码
			url = exchangeUp(url);
			String tmpUrl = URLDecoder.decode(url, "unicode");
			return tmpUrl;
		}

		Matcher m_a_gbk_utf8 = partternOfGbk_utf8.matcher(url);
		int number = 0;
		while (m_a_gbk_utf8.find()) {
			number++;
		}

		url = url.replace("%%", "%25%");// 防止出现双%
		if (number % 3 == 0 && number % 2 != 0) {
			String tmpUrl = parserGbk_Utf8(url, "utf-8", "gbk");
			return tmpUrl;
		} else if (number % 3 != 0 && number % 2 == 0) {
			String tmpUrl = parserGbk_Utf8(url, "gbk", "utf-8");
			return tmpUrl;
		} else if (number % 3 == 0 && number % 2 == 0) {
			String tmpUrl = parserGbk_Utf8(url, "utf-8", "gbk");
			return tmpUrl;
		} else {
			String tmpUrl = parserGbk_Utf8(url, "utf-8", "gbk");
			return tmpUrl;
		}

		// 替换符号
	}

	// 对GBK和UTF-8解析交互解析
	private static String parserGbk_Utf8(String url, String codeSrc,
			String newCode) throws Exception {
		String tmpUrl=null;
		try {
			url = exchangeUp(url);
			tmpUrl = URLDecoder.decode(url, codeSrc);
			Matcher m_a_p = partternOfP.matcher(tmpUrl);
			boolean tmpFlag = m_a_p.find();
			if (tmpFlag) {
				tmpUrl = URLDecoder.decode(url, newCode);
			}
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
		}
		return tmpUrl;
	}

	// 获取正则表达式对象
	private static Pattern getPattern(String pattern) {
		return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
	}

	// 将%重新转换成%25
	private static String exchangeUp(String url) {
		List<String> list = null;
		Matcher m_e = partternOfE.matcher(url);
		while (m_e.find()) {
			if (list == null)
				list = new ArrayList<String>();
			String value = m_e.group();
			list.add(value);
		}
		if (list != null && list.size() != 0) {
			for (String tmpStr : list) {
				String tmpCode = tmpStr.replace("%", "%25");
				url = url.replace(tmpStr, tmpCode);
			}
		}

		return url;
	}

	// 替换符号转码后的结果
	private static String replaceOfUP(String url) {
		for (int i = 0; i < psEncode.length; i++) {
			String code = psEncode[i];
			String ps = PS[i];
			if (code.equals("%24"))
				ps = "\\" + PS[i];
			url = url.replaceAll(code, ps);
			url = url.replaceAll(code.toLowerCase(), ps);
		}
		return url;
	}

	/**
	 * @description 递归替换%25
	 * @param line
	 * @return 返回替换掉%25后的数据
	 */
	private static String replaceOfP(String line) {
		if (StringUtils.isNotBlank(line)) {
			if (line.contains("%25")) {
				line = line.replace("%25", "%");
				if (line.contains("%25")) {
					line = replaceOfP(line);
				}
			}
		}
		return line;
	}

	/**
	 * @description 对url进行兼容性解码，自动识别unicode和utf-8和gbk
	 * @param url
	 *            url
	 * @return 返回解码后的url,无法正确解析则返回null
	 */
	public static String parser(String url) {
		if (StringUtils.isBlank(url))
			return null;

		String tmpStr = null;
		try {
			tmpStr = parserUrl(url);
		} catch (Exception e) {// 无法正确解析返回null
//			e.printStackTrace();
		}
		return tmpStr;
	}

	/**
	 * @description 从url中获取搜索引擎网址
	 * @param url
	 *            url
	 * @return 返回搜索引擎网址
	 */
	public static String getSearchEngines(String url) {
		String newUrl = null;
		if (StringUtils.isNotBlank(url)) {
			String pattern = "https://|http://";
			Pattern p_a = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
			Matcher m_a = p_a.matcher(url);
			while (m_a.find()) {
				String a = m_a.group();
				if (StringUtils.isNotBlank(a)) {
					int index = url.indexOf(a);
					String tmpSub = url.substring(index + a.length(),
							url.length());
					int tmpIndex = tmpSub.indexOf("/");
					if (tmpIndex == -1)
						tmpIndex = tmpSub.length();
					String sec = tmpSub.substring(0, tmpIndex);
					newUrl = a + sec;
					break;
				}
			}
		}
		return newUrl;
	}

	/**
	 * @description 获取域名
	 * @param url
	 *            url
	 * @return 返回域名，如果不能正确解析则返回null
	 */
	public static String getDomain(String url) {
		String tmpUrl = null;
		url = getSearchEngines(url);
		if (StringUtils.isNotBlank(url)) {
			String[] urls = url.split("/", -1);
			if (urls.length >= 3)
				tmpUrl = urls[2];
		}
		return tmpUrl;
	}

	/**
	 * @description 获取url中的关键词
	 * @param src
	 *            url
	 * @return 返回关键词
	 */
	public static String getWord(String src) {
		// 站外搜索关键词,截取referrer地址wd、word、q、query、w、kw标识参数的值
		String word = null;
		if (StringUtils.isNotBlank(src)) {
			if(src.contains("baidu.com")){
				if(src.contains("w=0_10_")){
					int tmpIndex = src.indexOf("w=0_10_");
					if (tmpIndex != -1) {
						String tmpInit_refer = src
								.substring(tmpIndex);
						int index = tmpInit_refer.indexOf("/");
						if (index != -1) {
							word = tmpInit_refer
									.substring(7, index);
							if (StringUtils.isNotBlank(word)) 
								word = word.replace(".", "");
							
						}
					}
				}else{
					String pattern = "(\\?|&)(wd=|word=)";
					word = getWordByParttern(src, word, pattern);
				}
			}else if(src.contains("sogou.com")){
				String pattern = "(\\?|&)(query=|keyword=)";
				word = getWordByParttern(src, word, pattern);
			}else if(src.contains("sm.cn")){
				String pattern = "(\\?|&)(q=)";
				word = getWordByParttern(src, word, pattern);
			}else if(src.contains("so.com")){
				String pattern = "(\\?|&)(q=)";
				word = getWordByParttern(src, word, pattern);
			}else{
				String pattern = "(\\?|&)(wd=|word=|q=|query=|w=|kw=|keyword=)";
				word = getWordByParttern(src, word, pattern);
			}
			
			if(StringUtils.isNotBlank(word)){//识别乱码
				boolean tmpFlag=regWord(word);
				if(!tmpFlag)
					word=null;
			}
		}
		return word;
	}

	//通过传入正在表达式获取关键词
	private static String getWordByParttern(String src, String word,
			String pattern) {
		Pattern p_a = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher m_a = p_a.matcher(src);
		while (m_a.find()) {
			String a = m_a.group();
			if (a.contains("?")) {
				a = "\\" + a;
			}
			String[] srcs = src.split(a, -1);
			if (srcs.length > 1) {
				int index = srcs[1].indexOf("&");
				int tmpIndex = index == -1 ? srcs[1].length() : index;
				word = srcs[1].substring(0, tmpIndex);
			} else {
				word = srcs[0];
			}
			break;
		}
		return word;
	}
	

	/**
	 * @description 获取url中的某个参数
	 * @param url
	 *            url地址
	 * @param paramName
	 *            参数名
	 * @return 返回参数的value值，如果不存在则返回null
	 */
	public static String getUrlOfParam(String url, String paramName) {
		String tmpStr = null;
		if (url.contains("?")) {
			String[] referers = url.split("\\?", -1);
			String param = referers[1].trim();
			if (StringUtils.isNotBlank(param)) {
				Map<String, String> map = parserCommonParams(param);
				if (map != null && map.size() != 0) {
					tmpStr = map.get(paramName);
				}
			}
		}
		return tmpStr;
	}

	/**
	 * @description 将url后面的参数集解析成map格式
	 * @param comParams
	 *            url后面的参数集
	 * @return 返回一个map集合，如果不存在则返回null
	 */
	public static Map<String, String> parserCommonParams(String comParams) {
		Map<String, String> map = null;
		if (StringUtils.isNotBlank(comParams)) {
			map = new HashMap<String, String>();
			String[] params = comParams.split("&", -1);
			for (String tmp : params) {
				if (tmp.contains("=")) {
					String[] tmps = tmp.split("=", 2);
					map.put(tmps[0], tmps[1]);
				}
			}
		}
		return map;
	}
	
	
	/**
	 * @description 解码后过滤关键词的符号
	 * @param str 关键词
	 * @return 返回过滤后的结果
	 */
	public static String replaceOfPunct(String str){//替换掉标点字符
		
		String pattern="\\p{Punct}";
		Pattern  p_a = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE);   
		Matcher m_a = p_a.matcher(str);
		while(m_a.find()){ 
			String a = m_a.group();
			str=str.replace(a, " ");
		}
		
		return str;
	}
	
	/**
	 * @description 识别词单词是否包含乱码
	 * @param word 词句
	 * @return 包含乱码返回true
	 */
	public static boolean regWord(String word){
		if(StringUtils.isBlank(word))//空字符串属于正常字符串
			return true;
			
		word=replaceOfPunct(word);//先替换一下标点符号
		try{
			String reg = "^([=+]|[\\s]|[\\p{P}]|[A-Za-z0-9]|[\u4E00-\u9FA5])+$";
			Pattern p = Pattern.compile(reg);
			Matcher m = p.matcher(word);
			boolean flag = m.matches();
			return flag;
		}catch(Throwable  e){
			return false;
		}
	}

}
