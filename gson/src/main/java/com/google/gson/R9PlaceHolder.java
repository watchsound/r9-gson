package com.google.gson;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class R9PlaceHolder { 
	private String refid  ;
	private String type  ; 
	
    private Field field;
    private Object parent;
    
    private List list;
    private int index;
    private Map map;
    private String key;
    private Set set;
    
	public R9PlaceHolder(String refid, String type, Field field, Object parent) {
		this.refid = refid;
		this.type = type;
		this.field = field;
		this.parent = parent;
	}

	public R9PlaceHolder(String refid, String type, List list, int index) {
		this.refid = refid;
		this.type = type;
		this.list = list;
		this.index = index;
	}

	public R9PlaceHolder(String refid, String type, Map map, String key) {
		this.refid = refid;
		this.type = type;
		this.map = map;
		this.key = key;
	}

	public R9PlaceHolder(String refid, String type, Set set) {
		this.refid = refid;
		this.type = type;
		this.set = set;
	}
	
	public void replace(Object value) {
		if( field != null) {
			try {
				field.set(parent, value);
			} catch ( Exception e) {
				 e.printStackTrace();
			}  
		}
		else if( list != null ) {
			try {
				list.set(index, value);
			} catch ( Exception e) {
				 e.printStackTrace();
			}   
		}
		else if ( set != null ) {
			try {
				set.add(value);
			} catch ( Exception e) {
				 e.printStackTrace();
			} 
		}
		else if ( map != null ) {
			try {
				map.put(key, value);
			} catch ( Exception e) {
				 e.printStackTrace();
			} 
		}
	}
	

	public String getRefid() {
		return refid;
	}

	public void setRefid(String refid) {
		this.refid = refid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Object getParent() {
		return parent;
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public Set getSet() {
		return set;
	}

	public void setSet(Set set) {
		this.set = set;
	}
    
    
	
}
