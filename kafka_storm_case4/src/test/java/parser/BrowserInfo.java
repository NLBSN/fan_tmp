package parser;

public class BrowserInfo {

	private String systemType="other";//系统类型，如android\phone
	private String cilentType="other";//客户端类型，如手机或pc
	private String browserName="other";//浏览器名称
	private String browserVersion="other";//浏览器版本
	private String platformType="other";//平台类型，如百度盒子，微信或爬虫等的应用
	
	public String getSystemType() {
		return systemType;
	}
	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}
	public String getCilentType() {
		return cilentType;
	}
	public void setCilentType(String cilentType) {
		this.cilentType = cilentType;
	}
	public String getBrowserName() {
		return browserName;
	}
	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}
	public String getBrowserVersion() {
		return browserVersion;
	}
	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}
	public String getPlatformType() {
		return platformType;
	}
	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}
	@Override
	public String toString() {
		return "BrowserInfo [systemType=" + systemType + ", cilentType="
				+ cilentType + ", browserName=" + browserName
				+ ", browserVersion=" + browserVersion + ", platformType="
				+ platformType + "]";
	}
	
	
	
	
}
