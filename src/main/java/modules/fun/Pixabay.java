package modules.fun;

import main.Bot;
import modules.misc.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utilitys.Module;
import utilitys.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Pixabay extends Module {

    public Pixabay() {
        super("Image", new ArrayList<String>(Arrays.asList(new String[]{"image", "pixabay"})),
                "image {search}", "Zeigt dir ein Bild von Pixabay an, jenachdem was du gesucht hast",
                new ArrayList<Permission>(Arrays.asList()),
                ModuleType.FUN
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedGenerator generator = getEmbedGenerator();
        EmbedBuilder emb = generator.newEmbed(msg.getAuthor());
        String api_key = Bot.config.pixabay_api_key;

        if (args.length < 1) {
            emb.setTitle("Fehler").setDescription("Du musst ein Suchbegriff angeben qwq").setColor(Colors.ERROR.color());
            msg.getTextChannel().sendMessage(emb.build());
            return;
        }

        String search = "";
        for (String word : args) {
            search += word + " ";
        }
        search = search.substring(0, search.length() - 1);
        String api_search = search.replace(' ', '+');

        String search_url = "https://pixabay.com/api/?key=" + api_key + "&q=" + api_search + "&image_type=photo";

        JSONObject hit = getImage(search_url);

        if (hit != null) {
            int hits = hit.getInt("hits");
            String largeImageURL = hit.getString("largeImageURL");
            String pageURL = hit.getString("pageURL");
            int views = hit.getInt("views");
            int downloads = hit.getInt("downloads");
            int likes = hit.getInt("likes");
            String tags = hit.getString("tags");
            String user = hit.getString("user");
            String userImageURL = hit.getString("userImageURL");

            emb.setImage(largeImageURL)
                    .setAuthor(user, pageURL, (userImageURL.length() == 0 ? null : userImageURL))
                    .setFooter("1/" + hits + " ◈ " + tags)
                    .addField(views + "", ":eyes:", true)
                    .addField(downloads + "", ":arrow_down:", true)
                    .addField(likes + "", ":thumbsup:", true)
                    .setColor(Colors.SUCCESS.color());
        } else {
            emb.setTitle("Keine Ergebnis qwq").setColor(Colors.ERROR.color());
        }
        msg.getTextChannel().sendMessage(emb.build()).queue();
        return;
    }

    public static JSONObject getImage(String url) {

        JSONObject values = null;
        //imageURL, views, downloads, likes, tags, user, userImageURL
        JSONObject json_obj = null;
        JSONArray hits = null;

        try {
            json_obj = JsonReader.readJsonFromUrl(url);
            if (json_obj.getInt("totalHits") == 0) {
                return null;
            }
            hits = new JSONArray(json_obj.get("hits").toString());
            values = hits.getJSONObject(0);
            values.put("hits", json_obj.getInt("total"));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return values;
    }
}