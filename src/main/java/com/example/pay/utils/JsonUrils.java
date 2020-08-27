package com.example.pay.utils;

import com.google.gson.Gson;

import java.util.UUID;

public class JsonUrils {

    public static String BeanToJsonString(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }

    public static  <T> T JsonStringToBean(String json, Class<T> classOfT) {
        Gson gson = new Gson();
        T t = gson.fromJson(json, classOfT);
        return t;
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString().replaceAll("-",""));
    }


}
