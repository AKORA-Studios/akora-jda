package utilitys;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class JsonReader {
    public static JSONObject readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            JSONObject json = new JSONObject(readAll(rd));
            return json;
        } finally {
            is.close();
        }
    }

    public static JSONArray readJsonArrayFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            JSONArray json = new JSONArray(readAll(rd));
            return json;
        } finally {
            is.close();
        }
    }

    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
