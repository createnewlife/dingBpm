package com.alibaba.dingtalk.openapi.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.util.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FileUtils {
	public static final String FILEPATH = "Permanent_Data";

	// json鍐欏叆鏂囦欢
	public synchronized static void write2File(Object json, String fileName) {
		BufferedWriter writer = null;
		File filePath = new File(FILEPATH);
		JSONObject eJSON = null;
		
		if (!filePath.exists() && !filePath.isDirectory()) {
			filePath.mkdirs();
		}

		File file = new File(FILEPATH + File.separator + fileName + ".xml");
		System.out.println("path:" + file.getPath() + " abs path:" + file.getAbsolutePath());
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				System.out.println("createNewFile锛屽嚭鐜板紓甯�:");
				e.printStackTrace();
			}
		} else {
			eJSON = (JSONObject) read2JSON(fileName);
		}

		try {
			writer = new BufferedWriter(new FileWriter(file));

			if (eJSON==null) {
				writer.write(json.toString());
			} else {
				Object[] array = ((JSONObject) json).keySet().toArray();
				for(int i=0;i<array.length;i++){
					eJSON.put(array[i].toString(), ((JSONObject) json).get(array[i].toString()));
				}

				writer.write(eJSON.toString());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// 璇绘枃浠跺埌json
	public static JSONObject read2JSON(String fileName) {
		File file = new File(FILEPATH + File.separator + fileName + ".xml");
		if (!file.exists()) {
			return null;
		}

		BufferedReader reader = null;
		String laststr = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (JSONObject) JSON.parse(laststr);
	}

	// 閫氳繃key鍊艰幏鍙栨枃浠朵腑鐨剉alue
	public static Object getValue(String fileName, String key) {
		JSONObject eJSON = null;
		eJSON = (JSONObject) read2JSON(fileName);
		if (null != eJSON && eJSON.containsKey(key)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> values = JSON.parseObject(eJSON.toString(), Map.class);
			return values.get(key);
		} else {
			return null;
		}
	}
   public static HashMap<Long, Long> toHashMap(JSONObject js)  
   {  
	   if(js == null){
		   return null;
	   }
       HashMap<Long, Long> data = new HashMap<Long, Long>();  
       // 灏唈son瀛楃涓茶浆鎹㈡垚jsonObject  
       Set<String> set = js.keySet();
       // 閬嶅巻jsonObject鏁版嵁锛屾坊鍔犲埌Map瀵硅薄  
       Iterator<String>  it = set.iterator();
       while (it.hasNext())  
       {  
           String key = String.valueOf(it.next());
           Long keyLong = Long.valueOf(key);
           
           String value = js.getString(key);
           Long valueLong;
           if(TextUtils.isEmpty(value)){
        	  valueLong = js.getLong(key);
           }else{
	          valueLong = Long.valueOf(value);
           }
           data.put(keyLong, valueLong);  
       }  
       return data;  
   }  


}