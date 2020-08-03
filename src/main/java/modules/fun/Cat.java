package modules.fun;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.JSONException;
import org.json.JSONObject;
import utilitys.*;
import utilitys.Module;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Cat extends Module {

    public Cat() {
        super("Cat", new ArrayList<String>(Arrays.asList(new String[]{"cat"})),
                "cat", "Zeigt dir ein Bild einer Katze an",
                new ArrayList<Permission>(Arrays.asList()),
                ModuleType.FUN
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedGenerator generator = getEmbedGenerator();
        EmbedBuilder emb = generator.newEmbed(msg.getAuthor());

        String url = getCat();
        if (url != null) {
            emb.setTitle("Katze :3", url).setImage(url).setColor(Colors.SUCCESS.color());
        } else {
            emb.setTitle("Keine Katze ._.").setDescription("Die Katzen sind heute wohl nicht so photogen ._.").setColor(Colors.ERROR.color());
        }
        msg.getTextChannel().sendMessage(emb.build()).queue();
        return;
    }

    public static String getCat() {

        String outputMessage = null;
        JSONObject json = null;
        try {
            json = JsonReader.readJsonFromUrl("http://aws.random.cat/meow");
            outputMessage = json.getString("file");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return outputMessage;
    }
}