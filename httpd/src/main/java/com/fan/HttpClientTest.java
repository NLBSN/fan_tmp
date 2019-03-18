package com.fan;



import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HttpClientTest {
    @Test
    public void get() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("https://xingzhu-song.chinacloudsites.cn/kvMapping?orgId=b11647e8-0e36-44fd-84fa-262c4fcfbe43");
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");
        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entry = response.getEntity();
        int code = response.getStatusLine().getStatusCode();
        System.out.println("\tcode:" + code);
        String result = EntityUtils.toString(entry);
        System.out.println("\tresult:" + result);
        HttpClientUtils.closeQuietly(response);
        HttpClientUtils.closeQuietly(httpClient);
    }

    @Test
    public void postForm() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost("https://xingzhu-song.chinacloudsites.cn/monitoring/metric/definitions");
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("orgId", "b11647e8-0e36-44fd-84fa-262c4fcfbe43"));
        list.add(new BasicNameValuePair("instanceId", "1b0acfbb-d7f2-4f0d-a026-734c686ee4ba"));
        request.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entry = response.getEntity();
        int code = response.getStatusLine().getStatusCode();
        System.out.println("\tcode:" + code);
        String result = EntityUtils.toString(entry);
        System.out.println("\tresult:" + result);
        HttpClientUtils.closeQuietly(response);
        HttpClientUtils.closeQuietly(httpClient);
    }

    @Test
    public void postJSON() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost("https://xingzhu-song.chinacloudsites.cn/kvMapping/valid?orgId=b11647e8-0e36-44fd-84fa-262c4fcfbe43");
        String body = "{" +
                "  \"serviceId\": \"pip\",\n" +
                "  \"key\": \"pipDnsLabel\",\n" +
                "  \"value\": \"abcdefg\",\n" +
                "  \"dependenceOn\": {\"pipLocation\":\"chinanorth\"}\n" +
                "}";
        request.setEntity(new StringEntity(body, "UTF-8"));
        request.setHeader("content-type", "application/json");
        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entry = response.getEntity();
        int code = response.getStatusLine().getStatusCode();
        System.out.println("\tcode:" + code);
        String result = EntityUtils.toString(entry);
        System.out.println("\tresult:" + result);
        HttpClientUtils.closeQuietly(response);
        HttpClientUtils.closeQuietly(httpClient);
    }

    @Test
    public void testUrl() throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder("https://xingzhu-song.chinacloudsites.cn");
        uriBuilder.setPath("kvMapping");
        uriBuilder.setCharset(Charset.forName("UTF-8"));
        uriBuilder.setParameter("key1", "value1");
        uriBuilder.setParameter("key2", "value2");
        //uriBuilder.setUserInfo("username","password"); //https://username:password@xingzhu-song.chinacloudsites.cn/kvMapping?key1=value1&key2=value2
        URI uri = uriBuilder.build();
        System.out.println(uri);//https://xingzhu-song.chinacloudsites.cn/kvMapping?key1=value1&key2=value2
    }
}