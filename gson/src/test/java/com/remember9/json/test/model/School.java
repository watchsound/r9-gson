package com.remember9.json.test.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.R9Ref;
import com.google.gson.annotations.R9RefCollectionValue;
import com.google.gson.annotations.R9RefMapValue;

public class School {
	@R9Ref
    public Name name;
    public String address;
   @R9RefCollectionValue
    public List<Student> students = new ArrayList<Student>();
    
    @R9RefMapValue
    public Map<String, Classroom> classrooms = new HashMap<String, Classroom>();
}
