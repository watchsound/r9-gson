package com.google.gson;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class R9ObjProxy implements java.lang.reflect.InvocationHandler {

    private Object obj;

    public static Object newInstance(Object obj, Field field, Object parent) {
        return java.lang.reflect.Proxy.newProxyInstance(
            obj.getClass().getClassLoader(),
            obj.getClass().getInterfaces(),
            new R9ObjProxy(obj,field,parent));
    }
    public static Object newInstance(Object obj, List list) {
        return java.lang.reflect.Proxy.newProxyInstance(
            obj.getClass().getClassLoader(),
            obj.getClass().getInterfaces(),
            new R9ObjProxy(obj,list));
    }
    public static Object newInstance(Object obj, Map map) {
        return java.lang.reflect.Proxy.newProxyInstance(
            obj.getClass().getClassLoader(),
            obj.getClass().getInterfaces(),
            new R9ObjProxy(obj,map));
    }
    public static Object newInstance(Object obj, Set set) {
        return java.lang.reflect.Proxy.newProxyInstance(
            obj.getClass().getClassLoader(),
            obj.getClass().getInterfaces(),
            new R9ObjProxy(obj,set));
    }
    private Field field;
    private Object parent;
    
    private List list;
    private Map map;
    private Set set;
    
    private R9ObjProxy(Object obj, Field field, Object parent) {
        this.obj = obj;
        this.field = field;
        this.parent = parent;
    }
    private R9ObjProxy(Object obj, List list) {
        this.obj = obj;
        this.list = list; 
    }
    private R9ObjProxy(Object obj, Map map) {
        this.obj = obj;
        this.map = map; 
    }
    private R9ObjProxy(Object obj, Set set) {
        this.obj = obj;
        this.set = set; 
    }
    public Object invoke(Object proxy, Method m, Object[] args)
        throws Throwable
    {
        Object result;
        try {
            System.out.println("before method " + m.getName());
            result = m.invoke(obj, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " +
                                       e.getMessage());
        } finally {
            System.out.println("after method " + m.getName());
        }
        return result;
    }
}
