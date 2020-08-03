package database;

import main.Bot;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Config {
    private JSONObject json;
    private String filepath;

    public String token;
    public String test_token;
    public String prefix;
    public String test_prefix;

    public String pixabay_api_key;

    public HashMap<String, String> dbConfig;
    public HashMap<String, String> guild_structure;
    public HashMap<String, String> economy_structure;
    public ArrayList<String> owner;

    public Config(String path) {
        //Variablen intialisieren
        dbConfig = new HashMap();
        guild_structure = new HashMap();
        economy_structure = new HashMap();
        owner = new ArrayList<String>();
        filepath = path;

        //Config parsen
        parseJSON(filepath);
        MapJSON();
    }

    private void MapJSON() {
        json.keySet().forEach((keyObj) -> {
            String key = keyObj.toString();
            if (key.contains("dbAddress")) {
                dbConfig.put("address", json.get(keyObj).toString());
            } else if (key.contains("dbPort")) {
                dbConfig.put("port", json.get(keyObj).toString());
            } else if (key.contains("dbUser")) {
                dbConfig.put("user", json.get(keyObj).toString());
            } else if (key.contains("dbPassword")) {
                dbConfig.put("password", json.get(keyObj).toString());
            } else if (key.contains("dbName")) {
                dbConfig.put("name", json.get(keyObj).toString());
            } else if (key.contains("guilds_table")) {
                dbConfig.put("guilds_table", json.get(keyObj).toString());
            } else if (key.contains("economy_table")) {
                dbConfig.put("economy_table", json.get(keyObj).toString());
            }



            if (key.contains("token") && !key.contains("test_token")) {
                token = json.get(keyObj).toString();
            }

            if (key.contains("test_token")) {
                test_token = json.get(keyObj).toString();
            }


            if (key.contains("prefix") && !key.contains("test_prefix")) {
                prefix = json.get(keyObj).toString();
            }

            if (key.contains("test_prefix")) {
                test_prefix = json.get(keyObj).toString();
            }

            if (key.contains("pixabay_api_key")) {
                pixabay_api_key = json.get(keyObj).toString();
            }



            if (key.contains("owner")) {
                JSONArray arr = (JSONArray) json.get(keyObj);
                arr.forEach((id) -> {
                    owner.add(id.toString());
                });
            }

            if (key.contains("guilds_structure")) {
                JSONObject obj = (JSONObject) json.get(keyObj);
                obj.keySet().forEach((k_o) -> {
                    String k = k_o.toString();
                    guild_structure.put(k, obj.get(k).toString());
                });
            }

            if (key.contains("economy_structure")) {
                JSONObject obj = (JSONObject) json.get(keyObj);
                obj.keySet().forEach((k_o) -> {
                    String k = k_o.toString();
                    economy_structure.put(k, obj.get(k).toString());
                });
            }
        });
    }

    private void parseJSON(String path) {
        JSONParser parser = new JSONParser();
        String absolute_path = new File(".").getAbsolutePath();
        absolute_path = absolute_path.substring(0, absolute_path.length()-1);
        path = absolute_path + path;

        //NUR FÜR DOCKER
        if (Bot.docker) {
            path = "/files" + path;
        }
        //NUR FÜR DOCKER

        System.out.println("pwd: "+absolute_path);
        System.out.println("path: "+path+"\n");

        try {
            Object obj  = parser.parse(new FileReader(path));
            json = (JSONObject) obj;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }
}