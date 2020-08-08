package com.samriddha.covid19trackerkotlin.data.network;

import com.samriddha.covid19trackerkotlin.pojo.DistrictName;
import com.samriddha.covid19trackerkotlin.pojo.DistrictValue;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DistrictJsonDeserializerClass implements JsonDeserializer<DistrictName>{


    @Override
    public DistrictName deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {


        try {
            final HashMap<String, DistrictValue> map = readServiceUrlMap(json.getAsJsonObject());
            DistrictName results = new DistrictName(map) ;
            if (map != null)
                results.setDistrictValue(map);

            return results;

        } catch (JsonSyntaxException e) {

            return null;
        }

    }




    private HashMap<String, DistrictValue> readServiceUrlMap(final JsonObject jsonObject) throws JsonSyntaxException {

        if (jsonObject == null)
            return null;


        Gson gson = new Gson();
        HashMap<String, DistrictValue> data = new HashMap<>();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {

            String key = entry.getKey();
            DistrictValue value = gson.fromJson(entry.getValue(), DistrictValue.class);
            data.put(key, value);
        }

        return data;

    }


}
