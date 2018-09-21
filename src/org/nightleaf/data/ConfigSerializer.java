package org.nightleaf.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ConfigSerializer {

    /**
     * Saves the config to json.
     */
    public static void save() {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	String json = gson.toJson(Config.attributes);

	try {
	    Writer writer = new FileWriter(Config.CONFIG_FILE);
	    BufferedWriter bw = new BufferedWriter(writer);
	    bw.write(json);
	    bw.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Loads the config from json.
     */
    public static void load() {
	/**
	 * Creates the json file for the eventual save.
	 */
	if (!Config.CONFIG_FILE.exists()) {
	    try {
		Config.CONFIG_FILE.createNewFile();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	Gson gson = new Gson();
	String json = "";
	Type type = new TypeToken<Map<String, String>>() {
	}.getType();

	try {
	    FileReader fr = new FileReader(Config.CONFIG_FILE);
	    BufferedReader br = new BufferedReader(fr);
	    String line;
	    while ((line = br.readLine()) != null) {
		json += line;
	    }
	    br.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	Map<String, String> map = gson.fromJson(json, type);
	if (map == null) {
	    return;
	}
	Config.attributes = map;
    }

}
