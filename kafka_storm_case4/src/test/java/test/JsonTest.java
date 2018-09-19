package test;

import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;

/**
 * @description json格式的数据解析案例
 * @author tzc
 *
 */
public class JsonTest {

	@Test
	public void test1(){//key/value键值对的方式
		String line="{\"key\":\"value\",\"key1\":\"value1\"}";
		line="{\"key\":true,\"key1\":100}";
		JSONObject jsonObj=JSONObject.fromObject(line);
		System.out.println(jsonObj);
		int tmpValue=jsonObj.getInt("key1");
		System.out.println(tmpValue);
	}
	
	@Test
	public void test2(){
		String line="[\"123\",true,88,1.2,{\"key\":\"value\"}]";
		JSONArray array=JSONArray.fromObject(line);
		Iterator it=array.iterator();
		while(it.hasNext()){
			Object obj=it.next();
			System.out.println(obj.getClass().getName());
		}
	}
	
	@Test
	public void test3(){
		String line="{\"key\":[1,true,123,{\"key1\":123}]}";
		JSONObject jsonObj=JSONObject.fromObject(line);
		Object obj=jsonObj.get("key");
		System.out.println(obj);
		System.out.println(obj.getClass().getName());
	}
}
