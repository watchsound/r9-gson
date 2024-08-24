package com.remember9.json.test.model;
 
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.R9Ref;
import com.google.gson.annotations.R9RefCollectionValue;

public class Student {
	public int age;
	
	public Name name;
	
	@R9Ref
	public School school;
	
	@R9RefCollectionValue
	public List<Subject> registedClasses = new ArrayList<Subject>();
}
