package com.google.gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class R9ClassNameListTypeAdapter extends TypeAdapter<List<String>> {
   @Override public void write(JsonWriter out, List<String> classNameList) throws IOException {
	     // implement write: combine firstName and lastName into name
//     out.beginObject();
//     out.name("name");
//     out.value(user.firstName + " " + user.lastName);
//     out.endObject();
     // implement the write method
   }
   @Override public List<String> read(JsonReader in) throws IOException {
	   List<String> collection = new ArrayList<String>();
	   super.getContext().r9handler.classNameList = collection;
	   TypeAdapter<String> elementTypeAdapter = super.getContext().getAdapter(TypeToken.get(String.class));
	   in.beginArray();
	      while (in.hasNext()) {
	        String instance = (String) elementTypeAdapter.read(in);
	        collection.add(instance);
	      }
	      in.endArray();
	  return collection;
   }
 }
