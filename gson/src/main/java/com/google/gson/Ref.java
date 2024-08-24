package com.google.gson;

import com.google.gson.annotations.Expose;

public class Ref { 
	public String refid;
	public String type;
	
	@Expose(serialize=false,deserialize=false)
	public Object obj;
	
	public Ref() {}
	public Ref(String type, String refid,  Object obj) {
		this.refid = refid;
		this.type = type;
		this.obj = obj;
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
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	} 
}
