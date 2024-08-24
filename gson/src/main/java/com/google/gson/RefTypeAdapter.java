package com.google.gson;


import java.io.IOException;
import java.sql.Time;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
 




public final class RefTypeAdapter extends TypeAdapter<Object> {
  public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
    @SuppressWarnings("unchecked") // we use a runtime check to make sure the 'T's equal
    @Override public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
      return typeToken.getRawType() == Time.class ? (TypeAdapter<T>) new RefTypeAdapter(gson) : null;
    }
  };
  Gson gson;
  public RefTypeAdapter(Gson gson) {
	  this.gson = gson;
  }
 

  @Override public synchronized Object read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    Ref ref = gson.readR9Ref(in);
    return ref.obj;
  }

  @Override public synchronized void write(JsonWriter out, Object value) throws IOException {
	  if( value == null) {
		  out.value( (String) null  );
	  } else {
		 gson.writeR9Ref(out, value); 
		  if( context.r9handler.roundCount == 0 ){
			  
		 } 
	  }
   
  }
}
