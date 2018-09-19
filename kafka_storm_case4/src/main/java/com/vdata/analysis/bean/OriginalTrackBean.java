package com.vdata.analysis.bean;

public class OriginalTrackBean implements LogBean {
    private String cookieID = "";
    private String catePath = "";
    private String areaPath = "";
    private String url = "";
    private String epoch = "";
    private String userIp = "";
    private String siteName = "";
    private String pageVersion = "";
    private String loginUid = "";
    private String postCount = "";
    private String outKWord = "";
    private String pageType = "";
    private String refDomain = "";
    private String windowSize = "";
    private String referrer = "";
    private String infoId = "";
    private String infoType = "";
    private String userId = "";
    private String userType = "";
    private String pm58 = "";
    private String inKWord = "";
    private String loadTime = "";
    private String status = "";
    private String trackUrl = "";
    private String supplyCount = "";
    private String gautma = "";
    private String unitParams = "";
    private String queryParams = "";
    private String userAgent = "";
    private String bizName = "other";

    private String version = "v1.1";

    public String getCookieID() {
        return this.cookieID;
    }

    public void setCookieID(String cookieID) {
        this.cookieID = cookieID;
    }

    public String getCatePath() {
        return this.catePath;
    }

    public void setCatePath(String catePath) {
        this.catePath = catePath;
    }

    public String getAreaPath() {
        return this.areaPath;
    }

    public void setAreaPath(String areaPath) {
        this.areaPath = areaPath;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEpoch() {
        return this.epoch;
    }

    public void setEpoch(String epoch) {
        this.epoch = epoch;
    }

    public String getUserIp() {
        return this.userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getSiteName() {
        return this.siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getPageVersion() {
        return this.pageVersion;
    }

    public void setPageVersion(String pageVersion) {
        this.pageVersion = pageVersion;
    }

    public String getLoginUid() {
        return this.loginUid;
    }

    public void setLoginUid(String loginUid) {
        this.loginUid = loginUid;
    }

    public String getPostCount() {
        return this.postCount;
    }

    public void setPostCount(String postCount) {
        this.postCount = postCount;
    }

    public String getOutKWord() {
        return this.outKWord;
    }

    public void setOutKWord(String outKWord) {
        this.outKWord = outKWord;
    }

    public String getPageType() {
        return this.pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getRefDomain() {
        return this.refDomain;
    }

    public void setRefDomain(String refDomain) {
        this.refDomain = refDomain;
    }

    public String getWindowSize() {
        return this.windowSize;
    }

    public void setWindowSize(String windowSize) {
        this.windowSize = windowSize;
    }

    public String getReferrer() {
        return this.referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getInfoId() {
        return this.infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getInfoType() {
        return this.infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPm58() {
        return this.pm58;
    }

    public void setPm58(String pm58) {
        this.pm58 = pm58;
    }

    public String getInKWord() {
        return this.inKWord;
    }

    public void setInKWord(String inKWord) {
        this.inKWord = inKWord;
    }

    public String getLoadTime() {
        return this.loadTime;
    }

    public void setLoadTime(String loadTime) {
        this.loadTime = loadTime;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrackUrl() {
        return this.trackUrl;
    }

    public void setTrackUrl(String trackUrl) {
        this.trackUrl = trackUrl;
    }

    public String getSupplyCount() {
        return this.supplyCount;
    }

    public void setSupplyCount(String supplyCount) {
        this.supplyCount = supplyCount;
    }

    public String getGautma() {
        return this.gautma;
    }

    public void setGautma(String gautma) {
        this.gautma = gautma;
    }

    public String getUnitParams() {
        return this.unitParams;
    }

    public void setUnitParams(String unitParams) {
        this.unitParams = unitParams;
    }

    public String getQueryParams() {
        return this.queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getBizName() {
        return this.bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String toEtlString() {
        return this.cookieID + "\001" + this.catePath + "\001" + this.areaPath + "\001" +
                this.url + "\001" + this.epoch + "\001" + this.userIp + "\001" + this.siteName +
                "\001" + this.pageVersion + "\001" + this.loginUid + "\001" +
                this.postCount + "\001" + this.outKWord + "\001" + this.pageType + "\001" +
                this.refDomain + "\001" + this.windowSize + "\001" + this.referrer +
                "\001" + this.infoId + "\001" + this.infoType + "\001" + this.userId +
                "\001" + this.userType + "\001" + this.pm58 + "\001" + this.inKWord +
                "\001" + this.loadTime + "\001" + this.status + "\001" + this.trackUrl +
                "\001" + this.supplyCount + "\001" + this.gautma + "\001" +
                this.unitParams + "\001" + this.queryParams + "\001" + this.userAgent +
                "\001" + this.bizName + "\001" + this.version;
    }
}