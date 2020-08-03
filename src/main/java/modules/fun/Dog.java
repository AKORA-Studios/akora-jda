package modules.fun;

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

public class Dog extends Module {

    public Dog() {
        super("Dog", new ArrayList<String>(Arrays.asList(new String[]{"dog"})),
                "dog", "Zeigt dir ein Bild eines Hundes an",
                new ArrayList<Permission>(Arrays.asList()),
                ModuleType.FUN
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedGenerator generator = getEmbedGenerator();
        EmbedBuilder emb = generator.newEmbed(msg.getAuthor());

        String url = getDog();
        if (url != null) {
            emb.setTitle("Hund -.-", url).setImage(url).setColor(Colors.SUCCESS.color());
        } else {
            emb.setTitle("Keine Hund qwq").setDescription("Die Hunde sind gerade anderweilig beschäftigt qwq").setColor(Colors.ERROR.color());
        }
        msg.getTextChannel().sendMessage(emb.build()).queue();
        return;
    }

    public static String getDog() {

        String outputMessage = null;
        JSONArray json_arr = null;
        JSONObject json_obj = null;
        try {
            json_arr = JsonReader.readJsonArrayFromUrl("https://api.thedogapi.com/v1/images/search");
            json_obj = new JSONObject(json_arr.get(0).toString());
            outputMessage = json_obj.getString("url");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return outputMessage;
    }
}