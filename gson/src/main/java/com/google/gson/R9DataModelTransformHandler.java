package com.google.gson;

 
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry; 
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonWriter;
 

public class R9DataModelTransformHandler {
 

	public static class ToJsonMetaData{
		public List<String> classNameList;
		public Map<String, List<Object>> typeToObjects = new HashMap<String, List<Object>>();
		//public T data; 
	}
	
	public static class FromJsonMetaData0<T>{
		@JsonAdapter(R9ClassNameListTypeAdapter.class) 
		public List<String> classNameList ;
		
		@JsonAdapter(R9TypeToObjectsAdapter.class) 
		public Map<String, List<Object>> typeToObjects = new HashMap<String, List<Object>>();
	 
	}
	
	public static class FromJsonMetaData<T>{
	//	@JsonAdapter(R9ClassNameListTypeAdapter.class) 
	//	public List<String> classNameList ;
		
	//	@JsonAdapter(R9TypeToObjectsAdapter.class) 
	//	public Map<String, List<Object>> typeToObjects = new HashMap<String, List<Object>>();
		
		@JsonAdapter(R9DataModelTypeAdapter.class)
		public T data; 
	}
		
	public Map<String, List<Ref>> tojson_className2ObjectList = new HashMap<String, List<Ref>>();
	
	public List<String> classNameList = new ArrayList<String>();
	
	public Map<String, List<Object>> toobj_className2ObjectList = new HashMap<String, List<Object>>();
	
	public Type dataType;
	public int roundCount; 
	public int missingCount;
	public List<Class> topClassList = new ArrayList<Class>();
	
	public List<R9PlaceHolder>  placeHolderList = new ArrayList<R9PlaceHolder>();
	
	public synchronized ToJsonMetaData  toMetaData(){
		ToJsonMetaData data = new ToJsonMetaData();
		data.classNameList = classNameList;
		for(Entry<String, List<Ref>> entry: this.tojson_className2ObjectList.entrySet()) {
			List<Object> list = new ArrayList<Object>();
			data.typeToObjects.put(entry.getKey(), list);
			for(Ref p : entry.getValue()) {
				list.add(p.obj);
			}
			if( Gson.debug ) System.out.println( "toMeta::: type = " + entry.getKey() + "  value-size=" + entry.getValue().size());
		}
		return data;
	}
	
	public synchronized void reset() {
		tojson_className2ObjectList.clear();
		classNameList.clear();
		toobj_className2ObjectList.clear();
		topClassList.clear();
		dataType = null; 
		roundCount = 0; 
		missingCount = 0;
		placeHolderList.clear();
	}
//	public static void main(String[] arvg) {
//		DummyMan test = new DummyMan();
//		test.age = 10;
//		test.name = new PName();
//		test.name.fname = "AA";
//		test.name.lname = "BB";
//		
//		R9DataModelTransformHandler handler = new R9DataModelTransformHandler();
//		DummyManWrapper wrapper = handler.fromModel(test);
//		Gson gson = new Gson();
//		String s =gson.toJson(wrapper, DummyManWrapper.class);
//		try {
//			u.stringToFile(s, new File("dummy.test.json"));
//		} catch (IOException e) { 
//			e.printStackTrace();
//		}
//		
//	    s = u.fileToString(new File("dummy.test.json"));
//	    DummyManWrapper result = gson.fromJson(s, DummyManWrapper.class);
//	    DummyMan man = handler.toModel(result);
//	    System.out.println( man.age +":" + man.name.fname +":" + man.name.lname);
//	}
	
	public R9DataModelTransformHandler() { 
		//Gson gson = new Gson();
	//	gson.
	}
	public synchronized Class getClassByTypeId(String id) {
		try {
			int pos = Integer.parseInt(id);
			String clazz = classNameList.get(pos);
			return Class.forName(clazz);
		}catch(Exception ex) {
			return null;
		}
	}
	
	public synchronized String getClassId(Class clazz) {
		String cn = clazz.getName();
		int pos = classNameList.indexOf(cn);
		if( pos >= 0) return pos +"";
		classNameList.add(cn);
		return ( classNameList.size()-1 ) + "";
	}
	public synchronized Ref getOriginal(String type, String id) {
		List<Object> list = toobj_className2ObjectList.get(type);
		if(list == null ) {
			Class clazz = this.getClassByTypeId(type);
			if( clazz != null) {
				list = new ArrayList();
				toobj_className2ObjectList.put(type, list);
			}
		}
		try {
			int pos = Integer.parseInt(id);
			return new Ref(type, id,  list.get(pos));
		}catch(Exception ex) {
			return new Ref(type, id,  null);
		}
	}
	public synchronized Ref getByOrigin(Object obj) {
		String classtypeid = getClassId(obj.getClass() );
		List<Ref> list = this.tojson_className2ObjectList.get(classtypeid);
		if( list == null) {
			list = new ArrayList<Ref>();
			this.tojson_className2ObjectList.put(classtypeid, list);
		}
		for(Ref p : list) {
			if( p.obj == obj)
				return p;
		}
		 
		Ref ref = new Ref(classtypeid, list.size() + "",   obj); 
		list.add(ref); 
		this.missingCount ++;
		if( Gson.debug )	System.out.println("::: type=" + ref.type + " id=" + ref.refid);
		return ref; 
	}
	 
	public synchronized void replaceAllPlaceholder() {
		for(int i = this.placeHolderList.size()-1; i>=0; i--) {
			R9PlaceHolder  ph = this.placeHolderList.get(i);
			Ref obj = getOriginal(ph.getType(), ph.getRefid()) ;
			if( obj.obj == null ) {
				System.err.println("Error for " + ph.getType() + "  :  " + ph.getRefid() );
				continue;
			}
			ph.replace(obj.obj);
		}
	}

}
