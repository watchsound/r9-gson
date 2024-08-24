 
package com.google.gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
 
public final class R9TypeToObjectsAdapter extends TypeAdapter<Map<String,  List<Object>>> {
 

    @Override public Map<String,  List<Object>> read(JsonReader in) throws IOException {
      JsonToken peek = in.peek();
      if (peek == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      TypeAdapter<String> keyTypeAdapter = getContext().getAdapter(TypeToken.get(String.class));
      
      Map<String,  List<Object>> map =  getContext().r9handler.toobj_className2ObjectList; 
      if( map == null || map.isEmpty()) {
    	  map =  new HashMap<String, List<Object>>();
    	  getContext().r9handler.toobj_className2ObjectList = map;
      }
      
  
      if (peek == JsonToken.BEGIN_ARRAY) {
        in.beginArray();
        while (in.hasNext()) {
          in.beginArray(); // entry array
          String key = (String)keyTypeAdapter.read(in);
         
          CollectionTypeAdapterFactory factory = (CollectionTypeAdapterFactory) this.getContext().getPreInstalled(CollectionTypeAdapterFactory.class);
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
         // CollectionTypeAdapterFactory factory = (CollectionTypeAdapterFactory) this.getContext().getPreInstalled(CollectionTypeAdapterFactory.class);
          Class type = getContext().r9handler.getClassByTypeId((String)key);
       //   TypeToken typeToken = TypeToken.get(type);
          TypeAdapter  adapter =  new ListAdapter(getContext(), type );// factory.create(getContext(), typeToken);
          
          List value = (List) adapter.read(in);
          List replaced = map.put(key, value);
          if (replaced != null) {
       //     throw new JsonSyntaxException("duplicate key: " + key);
          }
        }
        in.endObject();
      }
      return  map;
    }

    @Override public void write(JsonWriter out, Map<String,  List<Object>> map) throws IOException {
      if (map == null) {
        out.nullValue();
        return;
      }
      
    }
 
    
    private static final class ListAdapter<E> extends TypeAdapter<Collection<E>> {
        private final TypeAdapter<E> elementTypeAdapter;
    
        public ListAdapter(Gson context,  Class clazz ) {
         TypeAdapter<?> elementTypeAdapter = context.getAdapter(TypeToken.get(clazz));
          this.elementTypeAdapter =
              new TypeAdapterRuntimeTypeWrapper(context, elementTypeAdapter, TypeToken.get(clazz).getType());
        
        }

        @Override public Collection<E> read(JsonReader in) throws IOException {
          if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
          }

          Collection<E> collection = new ArrayList();
          in.beginArray();
          while (in.hasNext()) {
            E instance = elementTypeAdapter.read(in);
            collection.add(instance);
          }
          in.endArray();
          return collection;
        }

        @Override public void write(JsonWriter out, Collection<E> collection) throws IOException {
          if (collection == null) {
            out.nullValue();
            return;
          }

          out.beginArray();
          for (E element : collection) {
            elementTypeAdapter.write(out, element);
          }
          out.endArray();
        }
      }
}
