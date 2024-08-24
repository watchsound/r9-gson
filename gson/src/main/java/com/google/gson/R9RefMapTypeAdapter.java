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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
 
public final class R9RefMapTypeAdapter<K, V> extends TypeAdapter<Map<K, V>> {
    private final TypeAdapter<K> keyTypeAdapter;
    private final TypeAdapter<V> valueTypeAdapter;
    private final ObjectConstructor<? extends Map<K, V>> constructor;
    Type type;
    public R9RefMapTypeAdapter(Gson context,  TypeToken<?> typeToken ) {
    	super(context);
    	     type = typeToken.getType();
 
    	    Class<?> rawTypeOfSrc = $Gson$Types.getRawType(type);
    	    Type[] keyAndValueTypes = $Gson$Types.getMapKeyAndValueTypes(type, rawTypeOfSrc);
    	    TypeAdapter<?> keyAdapter = getKeyAdapter(context, keyAndValueTypes[0]);
    	    TypeAdapter<?> valueAdapter = context.getAdapter(TypeToken.get(keyAndValueTypes[1]));
    	    ObjectConstructor<?> constructor = context.constructorConstructor.get(typeToken);
    	    
    	    
      this.keyTypeAdapter =
        new TypeAdapterRuntimeTypeWrapper(context, keyAdapter, keyAndValueTypes[0]);
      this.valueTypeAdapter =
        new TypeAdapterRuntimeTypeWrapper(context, valueAdapter,  keyAndValueTypes[1]);
      this.constructor = (ObjectConstructor<? extends Map<K, V>>) constructor;
    }

    @Override public Map<K, V> read(JsonReader in) throws IOException {
      JsonToken peek = in.peek();
      if (peek == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
    //  if( Gson.debug )
    //	  System.out.println( type);

      Map<K, V> map = constructor.construct();

      if (peek == JsonToken.BEGIN_ARRAY) {
        in.beginArray();
        while (in.hasNext()) {
          in.beginArray(); // entry array
          K key = keyTypeAdapter.read(in);
			Ref fieldValue =    context.readR9Ref(in);
			if( fieldValue.obj == null && fieldValue.refid != null) {
				if( key instanceof String) {
					R9PlaceHolder h  = new R9PlaceHolder(fieldValue.refid, fieldValue.type, (Map)map, (String) key);
				    context.r9handler.placeHolderList.add(h);  
				} 
			} 
            map.put(key, (V)fieldValue.obj); 
          in.endArray();
        }
        in.endArray();
      } else {
        in.beginObject();
        while (in.hasNext()) {
          JsonReaderInternalAccess.INSTANCE.promoteNameToValue(in);
          K key = keyTypeAdapter.read(in);
          Ref fieldValue =    context.readR9Ref(in);
			if( fieldValue.obj == null) {
				if( key instanceof String) {
					R9PlaceHolder h  = new R9PlaceHolder(fieldValue.refid, fieldValue.type, (Map)map, (String) key);
				    context.r9handler.placeHolderList.add(h);  
				} 
			} 
          map.put(key, (V)fieldValue.obj); 
        }
        in.endObject();
      }
      return map;
    }

	@Override
	public void write(JsonWriter out, Map<K, V> map) throws IOException {
		if (map == null) {
			out.nullValue();
			return;
		}

		out.beginObject();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			out.name(String.valueOf(entry.getKey()));
			context.writeR9Ref(out, entry.getValue());
			if( context.r9handler.roundCount == 0 ){
				 valueTypeAdapter.write(out, entry.getValue());
			}  
		}
		out.endObject();
		return;

	}
 
    
    private TypeAdapter<?> getKeyAdapter(Gson context, Type keyType) {
        return (keyType == boolean.class || keyType == Boolean.class)
            ? TypeAdapters.BOOLEAN_AS_STRING
            : context.getAdapter(TypeToken.get(keyType));
      }
  } 
