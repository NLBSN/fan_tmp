package com.fan.analysis.bean;

/**
 * @description etl之后的日志承载对象
 * @author fan
 *
 */
public class ETLTrackBean implements LogBean {
	// 注意：实体的顺序严格要求与日志位置一致

	private String cookieID = "";// cookie的id
	private String catePath = "";// cateid的fullpaths，注：处理的时候会默认在前面加个0,以表示全站
	private String areaPath = "";// areaid的fullpaths，注：处理的时候会默认在前面加个0,以表示全站
	private String url = "";// 当前访问url
	private String epoch = "";// 当前时间戳
	private String userIp = "";// 客户端用户ip
	private String siteName = "";// 站点名称
	private String pageVersion = "";// 前端页面版本
	private String loginUid = "";// 当前登陆用户id
	private String postCount = "";// 列表页结果数
	private String outKWord = "";// 站外搜索关键词,截取referrer地址wd、word、q、query、w、kw标识参数的值
	private String pageType = "";// 当前页面业务类型(list|detail|post等)
	private String refDomain = "";// 来路域名
	private String windowSize = "";// 客户端页面大小
	private String referrer = "";// 来路访问url
	private String infoUid = "";// 当前帖子的用户编号
	private String pm58 = "";// 最终页跳转参数
	private String inKWord = "";// 站内搜索关键词，截取url地址上key、jh_、key_标识参数的值
	private String loadTime = "";// 加载时间
	private String status = "";// Track访问状态
	private String trackUrl = "";// 公共参数集合
	private String supplyCount = "";// 特殊参数集合
	private String busine = "";// 业务线的fullpaths
	private String gautma = "";// ga的判断唯一用户参数
	private String unitParams = "";// 单元参数
	private String queryParams = "";// query串
	private String userAgent = "";// userAgent串
	private String bizName_New = "";// 数据分析组划分的新的业务线规则，用来代替 tkBusine
	private String sessionId = "";// session的id
	
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
	public String getInfoUid() {
		return infoUid;
	}
	public void setInfoUid(String infoUid) {
		this.infoUid = infoUid;
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
	public String getBusine() {
		return busine;
	}
	public void setBusine(String busine) {
		this.busine = busine;
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
	public String getBizName_New() {
		return bizName_New;
	}
	public void setBizName_New(String bizName_New) {
		this.bizName_New = bizName_New;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	@Override
	public String toString() {
		return "ETLTrackBean [cookieID=" + cookieID + ", catePath=" + catePath
				+ ", areaPath=" + areaPath + ", url=" + url + ", epoch="
				+ epoch + ", userIp=" + userIp + ", siteName=" + siteName
				+ ", pageVersion=" + pageVersion + ", loginUid=" + loginUid
				+ ", postCount=" + postCount + ", outKWord=" + outKWord
				+ ", pageType=" + pageType + ", refDomain=" + refDomain
				+ ", windowSize=" + windowSize + ", referrer=" + referrer
				+ ", infoUid=" + infoUid + ", pm58=" + pm58 + ", inKWord="
				+ inKWord + ", loadTime=" + loadTime + ", status=" + status
				+ ", trackUrl=" + trackUrl + ", supplyCount=" + supplyCount
				+ ", busine=" + busine + ", gautma=" + gautma + ", unitParams="
				+ unitParams + ", queryParams=" + queryParams + ", userAgent="
				+ userAgent + ", bizName_New=" + bizName_New + ", sessionId="
				+ sessionId + "]";
	}
	
	
}
