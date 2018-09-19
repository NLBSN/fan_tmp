package com.vdata.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.vdata.analysis.utils.URLDecodeUtils;

public class UrlToolsTest {

	@Test
	public void getSearchTest(){
		String url="http://ty.58.com/chuzu/?key=%E7%A7%9F%E6%88%BF&cmcskey=&final=1&jump=1&specialtype=gls";
		String serarchE=URLDecodeUtils.getSearchEngines(url);
		System.out.println(serarchE);
	}
	
	@Test
	public void getInkWordTest(){
		String url="http://ty.58.com/chuzu/?key=%E7%A7%9F%E6%88%BF&cmcskey=&final=1&jump=1&specialtype=gls";
		String tmpUrl=URLDecodeUtils.parser(url);
		System.out.println(tmpUrl);
		String word=getWordByUrl(tmpUrl);
		System.out.println(word);
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
