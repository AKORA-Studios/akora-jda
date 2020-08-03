package modules.fun;

import main.Bot;
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
import java.util.concurrent.ThreadLocalRandom;

public class RandomPixabay extends Module {

    public RandomPixabay() {
        super("RandomImage", new ArrayList<String>(Arrays.asList(new String[]{"randomimage", "rimage", "randompixabay", "rpixabay"})),
                "randomimage {search}", "Zeigt dir ein zufälliges Bild von Pixabay an, jenachdem was du gesucht hast",
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
            int pos = hit.getInt("pos");
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
                    .setFooter(pos + "/" + hits + " ◈ " + tags)
                    /*
                    .addField(views + "", ":eyes:", true)
                    .addField(downloads + "", "<:download:714430498835005513>", true)
                    .addField(likes + "", ":thumbsup:", true)
                    */
                    .setColor(Colors.SUCCESS.color());

            String v_space = "";
            for (char c : (views + "").toCharArray()) {
                v_space += "⠀";
            }

            String d_space = "";
            for (char c : (downloads + "").toCharArray()) {
                d_space += "⠀";
            }


            emb.addField(views + "⠀⠀⠀" + downloads + "⠀⠀⠀" + likes,
                    ":eyes:" + v_space +
                            "<:download:714430498835005513>" + d_space +
                            ":thumbsup:"
                    , true);
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
            int random = (int) (Math.random()*hits.length());
            values = hits.getJSONObject(random);
            values.put("hits", json_obj.getInt("total"));
            values.put("pos", random);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return values;
    }
}