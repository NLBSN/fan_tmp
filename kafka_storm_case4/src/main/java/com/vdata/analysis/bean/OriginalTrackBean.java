package com.vdata.analysis.bean;


/**
 * @description 用于存储原始日志
 * @author fan
 *
 */
public class OriginalTrackBean implements LogBean {
	private String cookieID = "";
	private String catePath = ""; // cateid的fullpaths，23,192,3456
	private String areaPath = ""; // areaid的fullpaths
	private String url = ""; // 当前URL
	private String epoch = ""; // 时间戳
	private String userIp = ""; // 用户ip
	private String siteName = ""; // 站点名称
	private String pageVersion = ""; // 前端页面版本
	private String loginUid = ""; // 当前登陆用户id
	private String postCount = ""; // 列表页结果数
	private String outKWord = ""; // 站外关键词
	private String pageType = ""; // 当前页面业务类型
	private String refDomain = ""; // referer域名
	private String windowSize = ""; // 客户端页面分辨率大小
	private String referrer = ""; // 来源url
	private String infoId = ""; // 当前帖子id
	private String infoType = ""; // 当前帖子类型
	private String userId = ""; // 当前帖子用户id
	private String userType = ""; // 当前帖子用户类型
	private String pm58 = ""; // 最终页跳转参数 注：pm58 seo相关参数
	private String inKWord = ""; // 站内关键词
	private String loadTime = ""; // 页面加载时间
	private String status = ""; // 访问状态
	private String trackUrl = ""; // 公共参数集合
	private String supplyCount = ""; // 特殊参数集合 注：源代码中json4fe.supplycount的内容
	private String gautma = ""; // ga的判断唯一用户参数
	private String unitParams = ""; // 单元参数
	private String queryParams = ""; // 查询参数
	private String userAgent = ""; // 浏览器请求 UA 头的一些信息，比如移动平台，浏览器种类等
	private String bizName = "other"; // 业务线名称

	//用于识别实时日志更新问题
	private String version="v1.1";

	public String getCookieID() {
		return cookieID;
	}

	public void setCookieID(String cookieID) {
		this.cookieID = cookieID;
	}

	public String getCatePath() {
		return catePath;
	}

	public void setCatePath(String catePath) {
		this.catePath = catePath;
	}

	public String getAreaPath() {
		return areaPath;
	}

	public void setAreaPath(String areaPath) {
		this.areaPath = areaPath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEpoch() {
		return epoch;
	}

	public void setEpoch(String epoch) {
		this.epoch = epoch;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getPageVersion() {
		return pageVersion;
	}

	public void setPageVersion(String pageVersion) {
		this.pageVersion = pageVersion;
	}

	public String getLoginUid() {
		return loginUid;
	}

	public void setLoginUid(String loginUid) {
		this.loginUid = loginUid;
	}

	public String getPostCount() {
		return postCount;
	}

	public void setPostCount(String postCount) {
		this.postCount = postCount;
	}

	public String getOutKWord() {
		return outKWord;
	}

	public void setOutKWord(String outKWord) {
		this.outKWord = outKWord;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public String getRefDomain() {
		return refDomain;
	}

	public void setRefDomain(String refDomain) {
		this.refDomain = refDomain;
	}

	public String getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(String windowSize) {
		this.windowSize = windowSize;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public String getInfoId() {
		return infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPm58() {
		return pm58;
	}

	public void setPm58(String pm58) {
		this.pm58 = pm58;
	}

	public String getInKWord() {
		return inKWord;
	}

	public void setInKWord(String inKWord) {
		this.inKWord = inKWord;
	}

	public String getLoadTime() {
		return loadTime;
	}

	public void setLoadTime(String loadTime) {
		this.loadTime = loadTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTrackUrl() {
		return trackUrl;
	}

	public void setTrackUrl(String trackUrl) {
		this.trackUrl = trackUrl;
	}

	public String getSupplyCount() {
		return supplyCount;
	}

	public void setSupplyCount(String supplyCount) {
		this.supplyCount = supplyCount;
	}

	public String getGautma() {
		return gautma;
	}

	public void setGautma(String gautma) {
		this.gautma = gautma;
	}

	public String getUnitParams() {
		return unitParams;
	}

	public void setUnitParams(String unitParams) {
		this.unitParams = unitParams;
	}

	public String getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(String queryParams) {
		this.queryParams = queryParams;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	//格式化之后的数据组合成的字符串
	public String toEtlString() {
		return cookieID + "\001" + catePath + "\001" + areaPath + "\001"
				+ url + "\001" + epoch + "\001" + userIp + "\001" + siteName
				+ "\001" + pageVersion + "\001" + loginUid + "\001"
				+ postCount + "\001" + outKWord + "\001" + pageType + "\001"
				+ refDomain + "\001" + windowSize + "\001" + referrer
				+ "\001" + infoId + "\001" + infoType + "\001" + userId
				+ "\001" + userType + "\001" + pm58 + "\001" + inKWord
				+ "\001" + loadTime + "\001" + status + "\001" + trackUrl
				+ "\001" + supplyCount + "\001" + gautma + "\001"
				+ unitParams + "\001" + queryParams + "\001" + userAgent
				+ "\001" + bizName + "\001" + version;
	}
	
	
}
