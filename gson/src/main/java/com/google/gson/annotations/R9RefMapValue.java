/*
 * Copyright (C) 2008 Google Inc.
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

package com.google.gson.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that indicates value field of this collection(array, list,map，set) is a reference type. (object will be generated separately)
 *
 * <p>This annotation has no effect unless you build {@link com.google.gson.Gson}
 * with a {@link com.google.gson.GsonBuilder}  
 *
 * <p>Here is an example of how this annotation is meant to be used:
 * <p><pre>
 * public class User {
 *   &#64R9RefCollectionValue private Map<String, Name> amap; 
 * }
 * public class Name {
 *    private String firstName;
 *    private String lastName;
 * }
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface R9RefMapValue {
   
}
