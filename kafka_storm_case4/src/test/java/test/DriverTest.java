package test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.junit.Test;

import com.vdata.analysis.tools.CommonAnalysisToolsDriver;
import com.vdata.analysis.tools.LogBean;
import com.vdata.analysis.tools.LogToolsTag;
import com.vdata.analysis.tools.OriginalTrackBean;
import com.vdata.analysis.utils.URLDecodeUtils;

public class DriverTest {

	@Test
	public void test1() throws Exception{
		String logline="潘金莲";
		LogBean bean=CommonAnalysisToolsDriver.parserToInstance(logline, LogToolsTag.TRACK_ORIGINAL_LOG);
		
	}
	
	@Test
	public void test2() throws Exception{
		String logline="akRyHVfSIro3KIbgelFICA==1473430799.388183.32.188.190-09/Sep/2016:22:19:59 +0800GET /pc/empty.js.gif?site_name=58&tag=pvstatall&referrer=http%3A%2F%2Fzs.58.com%2F%3Futm_source%3Dlink%26spm%3Ds-35712086275855-pe-f-803.psy_chonwu1&post_count=-1&_trackParams=&userid=&smsc=&window_size=978x636&_ga_utma=1.1.1.1.1.2&trackURL={%27GTID%27:%270d000000-0000-0271-c97b-852aa20152a3%27,%27infoid%27:%27%27,%27infotype%27:%27%27,%27usertype%27:%27%27,%27als%27:%270%27,%27utm_source%27:%27link%27,%27utm_campaign%27:%27%27,%27spm%27:%27s-35712086275855-pe-f-803.psy_chonwu1%27,%27new_session%27:%270%27,%27init_refer%27:%27%27,%27new_uv%27:%272%27,%27UUID%27:%27393e9c64-3223-4876-8c0d-1730f330d0b5%27,%27bangbangid%27:%27%27,%27navtype%27:%270%27,%27sc%27:%271024,768%27,%27sid%27:%27%27,%27pagetype%27:%27login%27,%27GA_pageview%27:%27%27}&rand_id=0.696652888553217 HTTP/1.120035http://passport.58.com/login?path=http%3A%2F%2Fmy.58.com%2FMozilla/5.0 (Windows NT 5.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36-0.000--";
		logline="05dvZ1XhXvFV8yqgg72hAg==1473430799.37742.92.178.234-09/Sep/2016:22:19:59 +0800GET /pc/empty.js.gif?site_name=58&tag=pvstatall&referrer=http%3A%2F%2Fpinglun.1ting.com%2Fcomment%3Ftype%3Dsong%26id%3D1074513&post_count=-1&_trackParams=&userid=&smsc=&window_size=0x0&_ga_utma=1.1.1.1.1.1&trackURL={%27GTID%27:%270d000000-0000-0bab-3c73-69bfa94e281c%27,%27infoid%27:%27%27,%27infotype%27:%27%27,%27usertype%27:%27%27,%27als%27:%27%27,%27utm_source%27:%27%27,%27utm_campaign%27:%27%27,%27spm%27:%27%27,%27new_session%27:%270%27,%27init_refer%27:%27http%3A%2F%2Fpinglun.1ting.com%2Fcomment%3Ftype%3Dsong%26id%3D1074513%27,%27new_uv%27:%271%27,%27UUID%27:%2791b381ea-6f0a-404f-a293-c564e256070a%27,%27bangbangid%27:%27%27,%27navtype%27:%270%27,%27sc%27:%271366,768%27,%27sid%27:%27%27,%27cate%27:%27%27,%27area%27:%27%27,%27pagetype%27:%27sou%27,%27page%27:%27list%27,%27GA_pageview%27:%27/sou/list%27}&rand_id=0.623080623568967 HTTP/1.120035http://cast.58.com/11?key012=%E8%BD%A6%E9%97%B4%E5%B7%A5%E4%BA%BA%E6%8B%9B%E8%81%98Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36-0.000--";
		OriginalTrackBean bean=(OriginalTrackBean)CommonAnalysisToolsDriver.parserToInstance(logline, LogToolsTag.TRACK_ORIGINAL_LOG);
		String url=bean.getUrl();
		System.out.println(url);
		String line=URLDecodeUtils.parser(url);
		System.out.println(line);
		
	}
	
	@Test
	public void test3() throws Exception{
		String logline="akRyHVfSIro3KIbgelFICA==1473430799.388183.32.188.190-09/Sep/2016:22:19:59 +0800GET /pc/empty.js.gif?site_name=58&tag=pvstatall&referrer=http%3A%2F%2Fzs.58.com%2F%3Futm_source%3Dlink%26spm%3Ds-35712086275855-pe-f-803.psy_chonwu1&post_count=-1&_trackParams=&userid=&smsc=&window_size=978x636&_ga_utma=1.1.1.1.1.2&trackURL={%27GTID%27:%270d000000-0000-0271-c97b-852aa20152a3%27,%27infoid%27:%27%27,%27infotype%27:%27%27,%27usertype%27:%27%27,%27als%27:%270%27,%27utm_source%27:%27link%27,%27utm_campaign%27:%27%27,%27spm%27:%27s-35712086275855-pe-f-803.psy_chonwu1%27,%27new_session%27:%270%27,%27init_refer%27:%27%27,%27new_uv%27:%272%27,%27UUID%27:%27393e9c64-3223-4876-8c0d-1730f330d0b5%27,%27bangbangid%27:%27%27,%27navtype%27:%270%27,%27sc%27:%271024,768%27,%27sid%27:%27%27,%27pagetype%27:%27login%27,%27GA_pageview%27:%27%27}&rand_id=0.696652888553217 HTTP/1.120035http://passport.58.com/login?path=http%3A%2F%2Fmy.58.com%2FMozilla/5.0 (Windows NT 5.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36-0.000--";
		logline="05dvZ1XhXvFV8yqgg72hAg==1473430799.37742.92.178.234-09/Sep/2016:22:19:59 +0800GET /pc/empty.js.gif?site_name=58&tag=pvstatall&referrer=http%3A%2F%2Fpinglun.1ting.com%2Fcomment%3Ftype%3Dsong%26id%3D1074513&post_count=-1&_trackParams=&userid=&smsc=&window_size=0x0&_ga_utma=1.1.1.1.1.1&trackURL={%27GTID%27:%270d000000-0000-0bab-3c73-69bfa94e281c%27,%27infoid%27:%27%27,%27infotype%27:%27%27,%27usertype%27:%27%27,%27als%27:%27%27,%27utm_source%27:%27%27,%27utm_campaign%27:%27%27,%27spm%27:%27%27,%27new_session%27:%270%27,%27init_refer%27:%27http%3A%2F%2Fpinglun.1ting.com%2Fcomment%3Ftype%3Dsong%26id%3D1074513%27,%27new_uv%27:%271%27,%27UUID%27:%2791b381ea-6f0a-404f-a293-c564e256070a%27,%27bangbangid%27:%27%27,%27navtype%27:%270%27,%27sc%27:%271366,768%27,%27sid%27:%27%27,%27cate%27:%271,18,236%27,%27area%27:%27%27,%27pagetype%27:%27sou%27,%27page%27:%27list%27,%27GA_pageview%27:%27/sou/list%27}&rand_id=0.623080623568967 HTTP/1.120035http://cast.58.com/11?key012=%E8%BD%A6%E9%97%B4%E5%B7%A5%E4%BA%BA%E6%8B%9B%E8%81%98Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36-0.000--";
		OriginalTrackBean bean=(OriginalTrackBean)CommonAnalysisToolsDriver.parserToInstance(logline, LogToolsTag.TRACK_ORIGINAL_LOG);
		System.out.println(bean.toEtlString());
		
	}
	
	@Test
	public void decodeTest() throws Exception{
		String line="%";
		String tmp=URLEncoder.encode(line,"utf-8");
		String tmp1=URLEncoder.encode(tmp,"utf-8");
		System.out.println(tmp1);
	}
	
	@Test
	public void uaTest()throws Exception{
		String logline="akRyHVfSIro3KIbgelFICA==1473430799.388183.32.188.190-09/Sep/2016:22:19:59 +0800GET /pc/empty.js.gif?site_name=58&tag=pvstatall&referrer=http%3A%2F%2Fzs.58.com%2F%3Futm_source%3Dlink%26spm%3Ds-35712086275855-pe-f-803.psy_chonwu1&post_count=-1&_trackParams=&userid=&smsc=&window_size=978x636&_ga_utma=1.1.1.1.1.2&trackURL={%27GTID%27:%270d000000-0000-0271-c97b-852aa20152a3%27,%27infoid%27:%27%27,%27infotype%27:%27%27,%27usertype%27:%27%27,%27als%27:%270%27,%27utm_source%27:%27link%27,%27utm_campaign%27:%27%27,%27spm%27:%27s-35712086275855-pe-f-803.psy_chonwu1%27,%27new_session%27:%270%27,%27init_refer%27:%27%27,%27new_uv%27:%272%27,%27UUID%27:%27393e9c64-3223-4876-8c0d-1730f330d0b5%27,%27bangbangid%27:%27%27,%27navtype%27:%270%27,%27sc%27:%271024,768%27,%27sid%27:%27%27,%27pagetype%27:%27login%27,%27GA_pageview%27:%27%27}&rand_id=0.696652888553217 HTTP/1.120035http://passport.58.com/login?path=http%3A%2F%2Fmy.58.com%2FMozilla/5.0 (Windows NT 5.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36-0.000--";
		logline="05dvZ1XhXvFV8yqgg72hAg==1473430799.37742.92.178.234-09/Sep/2016:22:19:59 +0800GET /pc/empty.js.gif?site_name=58&tag=pvstatall&referrer=http%3A%2F%2Fpinglun.1ting.com%2Fcomment%3Ftype%3Dsong%26id%3D1074513&post_count=-1&_trackParams=&userid=&smsc=&window_size=0x0&_ga_utma=1.1.1.1.1.1&trackURL={%27GTID%27:%270d000000-0000-0bab-3c73-69bfa94e281c%27,%27infoid%27:%27%27,%27infotype%27:%27%27,%27usertype%27:%27%27,%27als%27:%27%27,%27utm_source%27:%27%27,%27utm_campaign%27:%27%27,%27spm%27:%27%27,%27new_session%27:%270%27,%27init_refer%27:%27http%3A%2F%2Fpinglun.1ting.com%2Fcomment%3Ftype%3Dsong%26id%3D1074513%27,%27new_uv%27:%271%27,%27UUID%27:%2791b381ea-6f0a-404f-a293-c564e256070a%27,%27bangbangid%27:%27%27,%27navtype%27:%270%27,%27sc%27:%271366,768%27,%27sid%27:%27%27,%27cate%27:%27%27,%27area%27:%27%27,%27pagetype%27:%27sou%27,%27page%27:%27list%27,%27GA_pageview%27:%27/sou/list%27}&rand_id=0.623080623568967 HTTP/1.120035http://cast.58.com/11?key012=%E8%BD%A6%E9%97%B4%E5%B7%A5%E4%BA%BA%E6%8B%9B%E8%81%98Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36-0.000--";
		OriginalTrackBean bean=(OriginalTrackBean)CommonAnalysisToolsDriver.parserToInstance(logline, LogToolsTag.TRACK_ORIGINAL_LOG);
		String ua=bean.getUserAgent();
		System.out.println(ua);
		
	}
}
