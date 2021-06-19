package com.utils;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {
    public static final Gson customGson = getGson();

    private static Gson getGson() {
    	GsonBuilder builder = new GsonBuilder();
    	//builder.registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter());
    	builder.registerTypeAdapter(Date.class, UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter());
    	return builder.create();
    }
}
