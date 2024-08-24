package com.google.gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
 
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class R9DataModelTypeAdapter extends TypeAdapter<Object> {
   @Override public void write(JsonWriter out, Object classNameList) throws IOException {
	     // implement write: combine firstName and lastName into name
//     out.beginObject();
//     out.name("name");
//     out.value(user.firstName + " " + user.lastName);
//     out.endObject();
     // implement the write method
   }
   @Override public Object read(JsonReader in) throws IOException {
	   Type type = getContext().r9handler.dataType; 
	   
	   TypeAdapterFactory  factory =  ObjectTypeAdapter.FACTORY ;
      
       TypeToken typeToken = TypeToken.get(type);
       TypeAdapter  adapter =  getContext().getAdapter(typeToken);
     //  TypeAdapter  adapter = factory.create(getContext(), typeToken);
	 //  TypeAdapter adapter = new ObjectTypeAdapter(getContext());
       
       return adapter.read(in);
   }
 }
