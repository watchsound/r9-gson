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

package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 
@Deprecated
public final class R9DataModelAdapterFactory implements TypeAdapterFactory {
  private final ConstructorConstructor constructorConstructor;
  final boolean complexMapKeySerialization;

  public R9DataModelAdapterFactory(ConstructorConstructor constructorConstructor,
      boolean complexMapKeySerialization) {
    this.constructorConstructor = constructorConstructor;
    this.complexMapKeySerialization = complexMapKeySerialization;
  }

  @Override public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
    Type type = typeToken.getType();

    Class<? super T> rawType = typeToken.getRawType();
    if (!Map.class.isAssignableFrom(rawType)) {
      return null;
    }

    Class<?> rawTypeOfSrc = $Gson$Types.getRawType(type);
    Type[] keyAndValueTypes = $Gson$Types.getMapKeyAndValueTypes(type, rawTypeOfSrc);
    TypeAdapter<?> keyAdapter = getKeyAdapter(gson, keyAndValueTypes[0]);
    TypeAdapter<?> valueAdapter = gson.getAdapter(TypeToken.get(keyAndValueTypes[1]));
    ObjectConstructor<T> constructor = constructorConstructor.get(typeToken);

    @SuppressWarnings({"unchecked", "rawtypes"})
    // we don't define a type parameter for the key or value types
    TypeAdapter<T> result = new Adapter(gson, keyAndValueTypes[0], keyAdapter,
        keyAndValueTypes[1], valueAdapter, constructor);
    return result;
  }

  /**
   * Returns a type adapter that writes the value as a string.
   */
  private TypeAdapter<?> getKeyAdapter(Gson context, Type keyType) {
    return (keyType == boolean.class || keyType == Boolean.class)
        ? TypeAdapters.BOOLEAN_AS_STRING
        : context.getAdapter(TypeToken.get(keyType));
  }

  private final class Adapter<K, V> extends TypeAdapter<Map<K,V>> {
    private final TypeAdapter<K> keyTypeAdapter;
 //   private final TypeAdapter<List> valueTypeAdapter;
    private final ObjectConstructor<? extends Map<K, V>> constructor;
   
    public Adapter(Gson context, Type keyType, TypeAdapter<K> keyTypeAdapter,
        Type valueType, TypeAdapter<V> valueTypeAdapter,
        ObjectConstructor<? extends Map<K, V>> constructor) {
    	super(context);
    	 
      this.keyTypeAdapter =
        new TypeAdapterRuntimeTypeWrapper<K>(context, keyTypeAdapter, keyType);
   //   this.valueTypeAdapter =
     //   new TypeAdapterRuntimeTypeWrapper<V>(context, valueTypeAdapter, valueType);
      this.constructor = constructor;
    }

    @Override public Map<K, V> read(JsonReader in) throws IOException {
      JsonToken peek = in.peek();
      if (peek == JsonToken.NULL) {
        in.nextNull();
        return null;
      }

      Map<String,  List<Object>> map =  getContext().r9handler.toobj_className2ObjectList; 
      if( map == null || map.isEmpty()) {
    	  map = (Map<String,  List<Object>>) constructor.construct();
    	  getContext().r9handler.toobj_className2ObjectList = map;
      }
     

      if (peek == JsonToken.BEGIN_ARRAY) {
        in.beginArray();
        while (in.hasNext()) {
          in.beginArray(); // entry array
          String key = (String)keyTypeAdapter.read(in);
         
          CollectionTypeAdapterFactory factory = (CollectionTypeAdapterFactory) getContext().getPreInstalled(CollectionTypeAdapterFactory.class);
          Class type = getContext().r9handler.getClassByTypeId((String)key);
          TypeToken typeToken = TypeToken.get(type);
          TypeAdapter  adapter = factory.create(getContext(), typeToken);
          
          List<Object> value = (List<Object> ) adapter.read(in);
          List replaced = map.put(key, value);
          if (replaced != null) {
            throw new JsonSyntaxException("duplicate key: " + key);
          }
          in.endArray();
        }
        in.endArray();
      } else {
        in.beginObject();
        while (in.hasNext()) {
          JsonReaderInternalAccess.INSTANCE.promoteNameToValue(in);
          String key = (String)  keyTypeAdapter.read(in);
          CollectionTypeAdapterFactory factory = (CollectionTypeAdapterFactory) this.getContext().getPreInstalled(CollectionTypeAdapterFactory.class);
          Class type = getContext().r9handler.getClassByTypeId((String)key);
          TypeToken typeToken = TypeToken.get(type);
          TypeAdapter  adapter = factory.create(getContext(), typeToken);
          
          List value = (List) adapter.read(in);
          List replaced = map.put(key, value);
          if (replaced != null) {
            throw new JsonSyntaxException("duplicate key: " + key);
          }
        }
        in.endObject();
      }
      return (Map<K, V>) map;
    }

    @Override public void write(JsonWriter out, Map<K, V> map) throws IOException {
      if (map == null) {
        out.nullValue();
        return;
      }
      
    }

    private String keyToString(JsonElement keyElement) {
      if (keyElement.isJsonPrimitive()) {
        JsonPrimitive primitive = keyElement.getAsJsonPrimitive();
        if (primitive.isNumber()) {
          return String.valueOf(primitive.getAsNumber());
        } else if (primitive.isBoolean()) {
          return Boolean.toString(primitive.getAsBoolean());
        } else if (primitive.isString()) {
          return primitive.getAsString();
        } else {
          throw new AssertionError();
        }
      } else if (keyElement.isJsonNull()) {
        return "null";
      } else {
        throw new AssertionError();
      }
    }
  }
}
