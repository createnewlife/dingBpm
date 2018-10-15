package utils;
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
import org.json.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;

public class FileUtils {
	public static final String FILEPATH="Seting_Data";
	public synchronized static Boolean write2File(Object json,String fileName){
		Boolean returnVale=true;
		BufferedWriter writer=null;
		File filePaht=new File(FILEPATH);
		JSONObject eJSON=null;
		if(!filePaht.exists()&&!filePaht.isDirectory()){
			filePaht.mkdirs();
		}
		File file=new File(FILEPATH+File.separator+fileName+".xml");
		System.out.println("path:" + file.getPath() + " abs path:" + file.getAbsolutePath());
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				System.out.println("CreateNewFile Failed");
				returnVale=false;
				e.printStackTrace();
			}
		} else {
			eJSON = (JSONObject) read2JSON(fileName);
		}
		try{
			writer=new BufferedWriter(new FileWriter(file));
			if(eJSON==null){
				writer.write(json.toString());
			}else{
				Object[] array=((JSONObject)json).keySet().toArray();
				for(int i=0;i<array.length;i++){
					eJSON.put(array[i].toString(), ((JSONObject)json).get(array[i].toString()));
				}
				writer.write(eJSON.toString());
			}
		}catch(IOException e){
			returnVale=false;
			e.printStackTrace();
		}finally{
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (IOException e) {
					returnVale=false;
					e.printStackTrace();
				}
			}
		return returnVale;
}
	
	public static JSONObject read2JSON(String fileName){
		File file=new File(FILEPATH+File.separator+fileName+".xml");
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
		return (JSONObject)JSON.parse(laststr);
	}
	
	public static Object getValue(String fileName,String key){
		JSONObject eJSON=null;
		eJSON=(JSONObject)read2JSON(fileName);
		if(null!=eJSON&&eJSON.containsKey(key)){
			@SuppressWarnings("unchecked")
			Map<String,Object> values=JSON.parseObject(eJSON.toString(),Map.class);
			return values.get(key);
		}else{
			return null;
		}
	}

	public static HashMap<String,String> toHashMap(JSONObject js){
		if(js==null){
			return null;
		}
		HashMap<String,String> data=new HashMap<String,String>();
		Set<String> set = js.keySet();
		Iterator<String> it=set.iterator();
		while(it.hasNext()){
			String key=String.valueOf(it.next());
			String keyString=String.valueOf(key);
			
			String value = js.getString(key);
			String valueString;
			if(TextUtils.isEmpty(value)){
				valueString=js.getString(key);
			}else{
				valueString=String.valueOf(value);
			}
			data.put(keyString, valueString);
		}
		return data;
	}
	
	public static JSONObject objToJSONObject(Object obj){
		return (JSONObject) JSONObject.toJSON(obj);
	}
	
}
