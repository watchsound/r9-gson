# intro : google-gson extension: 
code is cloned from https://github.com/google/gson

The extension of JSON in this project involves adding support for the serialization and deserialization of general Java objects. This means that beyond the standard JSON functionality, the system will be capable of converting complex Java objects into JSON format and vice versa. This extension ensures that Java objects, with their various attributes and nested structures, can be seamlessly converted to JSON for storage, transmission, or processing, and then accurately reconstructed back into Java objects when needed.

NOTE!!! most part of code is from [Gson](https://github.com/google/gson), I only modified several classes. 

NOTE!!! this project was written 5 years ago, and never maintained. check latest Gson code https://github.com/google/gson to see if they support function of serialization and deserialization of general Java objects.

# example

### use tags @R9Ref @R9RefCollectionValue
```
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

```
###  serialization and deserialization
```
  Gson gson = new Gson();
  String json =  gson.toR9Json(school, School.class);
  ... 
  School result = gson.fromR9Json(json, School.class);
	...  
```
