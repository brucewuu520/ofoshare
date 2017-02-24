package com.ofo.share.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author brucewuu Created on 16/4/12.
 */
public final class GsonUtils {

    static volatile Gson gson;

    static {
        if (null == gson) {
            gson = new Gson();
        }
    }

    @SuppressWarnings({"unchecked", "UnusedDeclaration"}) // Checked by runtime cast. Public API.
    public static <T> T fromJson(String json, Class<T> cls) {
        synchronized (GsonUtils.class) {
            return gson.fromJson(json, cls);
        }
    }

    public static <T> T fromJson(JsonElement json, Class<T> cls) {
        synchronized (GsonUtils.class) {
            return gson.fromJson(json, cls);
        }
    }

    public static <T> T fromArray(String json, Type typeOfT) {
        synchronized (GsonUtils.class) {
            return gson.fromJson(json, typeOfT);
        }
    }

    public static <T> T fromArray(JsonElement json, Type typeOfT) {
        synchronized (GsonUtils.class) {
            return gson.fromJson(json, typeOfT);
        }
    }

    public static <T> String toJson(T t) {
        synchronized (GsonUtils.class) {
            return gson.toJson(t);
        }
    }

    public static <T> String toArray(List<T> list) {
        synchronized (GsonUtils.class) {
            return gson.toJson(list);
        }
    }
}
