package com.example.calendate;

import androidx.annotation.Keep;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

@Keep
public class Calendar {
    String name;
    String pass;
    String jsonString;


    public Calendar(String n, String p)
    {
        name = n;
        pass = p;
        jsonString = "";

    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public String getJson() {
        return jsonString;
    }

    public void setJson(String json) {
        this.jsonString = json;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
