package analysis.ua.parser;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description user agent解析工具
 * @author tzc
 *
 */
public class UserAgentParserUtils {
	private static String[] platforms={"micromessenger","baiduboxapp","__weibo__"};//平台
	private static String[] spider={"spider","bot","rabbit"};//爬虫标识
	private static Map<String, String> browserTypeMap=null;//浏览器类型

	static {
		browserTypeMap = new LinkedHashMap<String, String>();

		// pc 端只关注浏览器，国产浏览器本质上只是 ie 的壳子，因此要优先 ie 匹配
		browserTypeMap.put("TaoBrowser", "淘宝浏览器");
		browserTypeMap.put("MicroMessenger", "微信");
		browserTypeMap.put("baiduboxapp", "手机百度");
		browserTypeMap.put("SamsungBrowser", "三星系统浏览器");
		browserTypeMap.put("Vivo", "vivo浏览器");
		browserTypeMap.put("BIDUBrowser", "百度浏览器");
		browserTypeMap.put("UBrowser", "UB三维浏览器");
		browserTypeMap.put("UCBrowser", "UC浏览器");
		browserTypeMap.put("UCWEB", "UC浏览器");
		browserTypeMap.put("The World", "世界之窗浏览器");
		browserTypeMap.put("MiuiBrowser", "小米浏览器");
		browserTypeMap.put("metasr", "搜狗浏览器");
		browserTypeMap.put("SogouMobileBrowser", "搜狗浏览器");
		browserTypeMap.put("SogouMSE", "搜狗浏览器");
		browserTypeMap.put("LBBROWSER", "猎豹浏览器");
		browserTypeMap.put("LieBaoFast", "猎豹浏览器");
		browserTypeMap.put("Maxthon", "遨游浏览器");
		browserTypeMap.put("baidubrowser", "百度浏览器");
		browserTypeMap.put("MQQBrowser", "QQ浏览器");
		browserTypeMap.put("QQBrowser", "QQ浏览器");
		browserTypeMap.put("TencentTraveler", "QQ浏览器");
		browserTypeMap.put("360SE", "360浏览器");
		browserTypeMap.put("360EE", "360浏览器");
		browserTypeMap.put("360 Aphone Browser", "360浏览器");
		browserTypeMap.put("Opera", "欧朋浏览器");
		browserTypeMap.put("OppoBrowser", "欧朋浏览器");
		browserTypeMap.put("2345Explorer", "2345浏览器");
		browserTypeMap.put("momoWebView", "陌陌");
		browserTypeMap.put("com.tencent.wifimanager", "腾讯WIFI管家");
		browserTypeMap.put("wkbrowser", "WebKit浏览器");
		browserTypeMap.put("Firefox", "火狐浏览器");
		browserTypeMap.put("_SQ_", "手机QQ");
		browserTypeMap.put("EUI", "乐视浏览器");
		browserTypeMap.put("Trident", "IE浏览器");
		browserTypeMap.put("MSIE", "IE浏览器");
		browserTypeMap.put("QQ", "QQ");
		browserTypeMap.put("SE", "搜狗浏览器");
		browserTypeMap.put("Chrome/", "Chrome浏览器");
		browserTypeMap.put("Safari", "Safari浏览器");

	}

	/**
	 * @description 解析User Agent数据
	 * @param userAgent 浏览器消息头
	 * @return 返回解析后的对象
	 */
	public static BrowserInfo parserOfUA(String userAgent){
		BrowserInfo info=null;
		if(StringUtils.isNotBlank(userAgent)){
			boolean isFlag=ifUserAgent(userAgent);
			if(isFlag){//判断是否是正确的UA头
				info=new BrowserInfo();
				String tmpUserAgent= userAgent.toLowerCase();//全部转换成小写
				//先判断平台类型
				platformParser(info,tmpUserAgent);
				
				//获取浏览器名称
				getBrowserName(info,userAgent);
				
				//客户端类型
				if(tmpUserAgent.contains("mobile")){
					info.setCilentType("mobile");
				}else{
					info.setCilentType("pc");
				}
				
				//系统
				String systemStr=getSystemOfString(userAgent);
				if(StringUtils.isBlank(systemStr)){
					if(userAgent.contains("Android"))
						info.setSystemType("Android");
				}else{
					setSystemType(info,systemStr);
				}
				
				
				//版本
				versionParser(info,userAgent);
			}
		}
		return info;
	}
	
	//获取版本
	private static void versionParser(BrowserInfo info,String userAgent){
		String pattern="(version/)[^\\s]+(\\s|;)";
		String version=match(userAgent,pattern);
		//先识别version类型
		if(StringUtils.isNotBlank(version)){
			int tmpIndex=version.indexOf("/");
			String vNumber=version.substring(tmpIndex+1, version.length());
			if(vNumber.contains(";"))//由于有部分版本存在‘;’，所以需要进行过滤
				vNumber=vNumber.replace(";", "");
			info.setBrowserVersion(vNumber);
			return;
		}
		
		String browerTypeString=info.getBrowserName();
		if("IE浏览器".equals(browerTypeString)){//IE版本单独解析
			String iePattern="(MSIE\\s+)\\d+.\\d";
			String msie=match(userAgent,iePattern);
			if(StringUtils.isNotBlank(msie)){
				String[] vs=msie.split(" ", -1);
				if(vs.length>=2){
					String v=vs[1].trim();
					if(StringUtils.isNotBlank(v)){
						String tmpVs=v.substring(0,v.length());
						info.setBrowserVersion(tmpVs);
						return;
					}
				}
			}
		}
		
		//如果既不是ie也无版本号
		if(StringUtils.isNotBlank(browerTypeString)){
			String otherPatter="("+browerTypeString+"/)[^\\s]+";
			String otherV=match(userAgent,otherPatter);
			if(StringUtils.isNotBlank(otherV)){
				int tmpIndex=otherV.indexOf("/");
				String vNumber=otherV.substring(tmpIndex+1, otherV.length());
				info.setBrowserVersion(vNumber);
				return;
			}
		}else{//再一次判断ie,因为有的ie没有浏览器名
			String iePatternS="(MSIE\\s+)";
			String ieSecond=match(userAgent,iePatternS);
			if(StringUtils.isNotBlank(ieSecond)){
				info.setBrowserName("IE浏览器");
				versionParser(info,userAgent);
			}
		}
		
	}
	
	//设置系统类型
	private static void setSystemType(BrowserInfo info,String sysStr){
		if(StringUtils.isNotBlank(sysStr)){
			if(sysStr.contains("Windows Phone")){
				info.setSystemType("WindowsPhone");
			}else if(sysStr.contains("Windows NT")){
				if(sysStr.contains("NT 5.0")){
					info.setSystemType("windows 2000");
				}else if(sysStr.contains("NT 5.1")){
					info.setSystemType("windows XP");
				}else if(sysStr.contains("NT 5.2")){
					info.setSystemType("windows 2003");
				}else if(sysStr.contains("NT 6.0")){
					info.setSystemType("windows vista");
				}else if(sysStr.contains("NT 6.1")){
					info.setSystemType("windows 7");
				}else if(sysStr.contains("NT 6.2")){
					info.setSystemType("windows 8");
				}else if(sysStr.contains("NT 6.3")){
					info.setSystemType("windows 8.1");
				}else if(sysStr.contains("NT 6.4")){
					info.setSystemType("windows 9");
				}else if(sysStr.contains("NT 10.0")){
					info.setSystemType("windows 10");
				}else{
					info.setSystemType("WindowsXP");
				}
			}else if(sysStr.contains("Mac OS X")){
				info.setSystemType("Mac OS X");
				
			}else if(sysStr.contains("iPhone")){
				info.setSystemType("iPhone");
				
			}else if(sysStr.contains("Android")){
				info.setSystemType("Android");
				
			}else if(sysStr.contains("Windows")){
				info.setSystemType("Windows");
				
			}else if(sysStr.contains("Linux")){
				info.setSystemType("Linux");
				
			}else{
				info.setSystemType("other");
			}
		}
	}
	
	//将包含得有系统的字符串截取出来
	private static String getSystemOfString(String userAgent){
		String tmpSys=null;
		int leftIndex=userAgent.indexOf("(");
		if(leftIndex!=-1){
			String tmpUA=userAgent.substring(leftIndex+1, userAgent.length());
			int rightIndex=tmpUA.indexOf(")");
			if(rightIndex!=-1){
				tmpSys=tmpUA.substring(0, rightIndex);
			}
		}
		return tmpSys;
	}
	
	//获取浏览器名称
	private static void getBrowserName(BrowserInfo info,String userAgent){
		for (Entry<String, String> entry : browserTypeMap.entrySet()) {
			String key=entry.getKey().toLowerCase();
			String value=entry.getValue();
			String tmpUA=userAgent.toLowerCase();
			if(tmpUA.contains(key)){
				info.setBrowserName(value);
				return;
			}
		}
	}
	
	//判断平台类型
	private static void platformParser(BrowserInfo info,String userAgent){
		for(String sd:spider){//爬虫
			if(userAgent.contains(sd)){
				info.setPlatformType("spider");
				return;
			}
		}
		
		for(String pl:platforms){//平台
			if(userAgent.contains(pl)){
				info.setPlatformType(pl);
				return;
			}
		}
		
		info.setPlatformType("browser");//如果不是平台，则标识为浏览器
	}
	
	//判断是否是正确的UA头
	private static boolean ifUserAgent(String userAgent){
		String pattern="(\\p{Upper}).+(/)";
		String ua=match(userAgent,pattern);
		if(StringUtils.isNotBlank(ua)&&(userAgent.indexOf(ua)==0)){
			return true;
		}
		return false;
	}
	
	//用于判断是否匹配某种规则
	private static String match(String src,String pattern){
		Pattern  p_a = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE);   
		Matcher m_a = p_a.matcher(src);
		while(m_a.find()){ 
			return m_a.group().trim();
		}
		return null;
	}
	
	@Test
	public void test1(){
		String url="Mozilla/5.0 (Linux; U; Android 4.3; zh-cn; HM 1SC Build/JLS36C) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 momoWebView/5.8 android/272(HM 1SC;android 4.3;zh_CN;23;netType/1)";
		url="Mozilla/5.0 (Linux; U; Android 5.1.1; zh-cn; SM-N9100 Build/LMY47X) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0 Chrome/37.0.0.0 MQQBrowser/6.4 Mobile Safari/537.36";
		url="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 BIDUBrowser/7.6 Safari/537.36";
		BrowserInfo info=parserOfUA(url);
		System.out.println(info);
		
	}
}
