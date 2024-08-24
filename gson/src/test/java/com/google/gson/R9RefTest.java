/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson;

import com.google.gson.annotations.R9Ref;
import com.google.gson.reflect.TypeToken;
import com.remember9.json.test.model.Classroom;
import com.remember9.json.test.model.Name;
import com.remember9.json.test.model.School;
import com.remember9.json.test.model.Student;
import com.remember9.json.test.model.Subject;

import junit.framework.TestCase;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Unit tests for the {@link FieldAttributes} class.
 *
 * @author Inderjeet Singh
 * @author Joel Leitch
 */
public class R9RefTest extends TestCase {
	
   School school;	 

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    
  }
  private void createData(boolean emptyArray) {
    school = new School();
    school.name= new Name();
    school.name.firstName ="Oakgrove High";
    school.name.lastName ="Oakgrove High";
    school.address= "nowhere";
    
    Classroom room = new Classroom();
    room.roomNumber = 0;
    room.building = "AA";
    school.classrooms.put("A", room);
    room = new Classroom();
    room.roomNumber = 1;
    room.building = "BB";
    school.classrooms.put("B", room);
    
    if( emptyArray)
    	return;
    Subject sub = new Subject();
    sub.sid= "001";
    sub.title= "math";
    Subject sub2 = new Subject();
    sub2.sid= "002";
    sub2.title= "english";
    Student student = new Student();
    student.age = 16;
    student.name = new Name();
    student.name.firstName = "John";
    student.name.lastName = "Wood";
    student.school = school;
    student.registedClasses.add(sub);
    school.students.add(student);
    
    student = new Student();
    student.age = 16;
    student.name = new Name();
    student.name.firstName = "Yolanda";
    student.name.lastName = "Flower";
    student.school = school;
    student.registedClasses.add(sub);
    student.registedClasses.add(sub2);
    school.students.add(student); 
    
  }
 

  public void testDeclaringClass() throws Exception { 
	  createData(false);
	  Gson gson = new Gson();
	  String json =  gson.toR9Json(school, School.class);
	  System.out.println(json);
	  
	  School result = gson.fromR9Json(json, School.class);
	  
	  assertEquals("", 2, result.students.size()); 
		 
  }

  public void testEmptyArray() throws Exception { 
	  createData(true);
	  Gson gson = new Gson();
	  String json =  gson.toR9Json(school, School.class);
	  System.out.println(json);
	  
	  School result = gson.fromR9Json(json, School.class);
	  
	  assertEquals("", 0, result.students.size()); 
		 
  }
}
