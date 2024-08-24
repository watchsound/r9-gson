/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * Adapt a homogeneous collection of objects.
 */
public final class R9RefCollectionTypeAdapter   extends TypeAdapter<Collection<Object>> {
    private final TypeAdapter<Object> elementTypeAdapter;
    private final ObjectConstructor<? extends Collection<Object>> constructor;

    private Type type;
    public R9RefCollectionTypeAdapter(Gson context, TypeToken<?> fieldType ) {
    	super(context);
         type = fieldType.getType(); 
	    Class<?> rawType = fieldType.getRawType(); 
	    Type elementType = $Gson$Types.getCollectionElementType(type, rawType);
	    TypeAdapter<?> elementTypeAdapter = context.getAdapter(TypeToken.get(elementType)); 
       this.elementTypeAdapter =
         new TypeAdapterRuntimeTypeWrapper(context, elementTypeAdapter, elementType);
      this.constructor = (ObjectConstructor<? extends Collection<Object>>) context.constructorConstructor.get( fieldType );
    }

    @Override public Collection<Object> read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      
    //  if( Gson.debug )
    //	  System.out.println( type );

      Collection<Object> collection = constructor.construct();
      in.beginArray();
      while (in.hasNext()) { 
			Ref fieldValue = context.readR9Ref(in);
			if( fieldValue.obj == null && fieldValue.refid != null) {
				R9PlaceHolder h = null;
				if(collection instanceof List)
				   h = new R9PlaceHolder(fieldValue.refid, fieldValue.type, (List)collection, collection.size());
				else if(collection instanceof Set)
				   h = new R9PlaceHolder(fieldValue.refid, fieldValue.type, (Set)collection );
				if( h != null)
				    context.r9handler.placeHolderList.add(h);  
			}
			collection.add(fieldValue.obj);
		
      }
      in.endArray();
      return collection;
    }

    @Override public void write(JsonWriter out, Collection<Object> collection) throws IOException {
      if (collection == null) {
        out.nullValue();
        return;
      }

      out.beginArray();
      for (Object element : collection) {
    	    context.writeR9Ref(out, element); 
	        if( context.r9handler.roundCount == 0 ){
	        	  elementTypeAdapter.write(out, element);
			}   
      }
      out.endArray();
    }
  } 
