package modules.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.ChannelManager;
import utilitys.Colors;
import utilitys.Module;
import utilitys.ModuleType;

import java.util.ArrayList;
import java.util.Arrays;

public class Slowmode extends Module {

    private EmbedBuilder emb;

    public Slowmode() {
        super(
                "Slowmode", new ArrayList<String>(Arrays.asList(new String[]{"slowmode", "smode"})),
                "slowmode {seconds} [minutes] [hours]", "Setzt Slowmode für den Channel\n> `0` deaktiviert den Slowmode",
                new ArrayList<Permission>(Arrays.asList(Permission.MANAGE_CHANNEL)),
                ModuleType.MODERATION
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedBuilder emb = getEmbedGenerator().newEmbed(msg.getAuthor());
        TextChannel channel = msg.getTextChannel();
        ChannelManager manager = channel.getManager();
        int s = 0, m = 0, h = 0;

        if (!msg.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            emb.setColor(Colors.ERROR.color()).setTitle("Fehler").setDescription("Ich habe nicht die nötigen Berechtigungen ._.");
            msg.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        if (args.length == 0) {
            manager.setSlowmode(0).sync();
            emb.setColor(Colors.SUCCESS.color()).setTitle("Änderung").setDescription("Der Slowmode ist für diesen Channel nun deaktiviert");
            msg.getChannel().sendMessage(emb.build()).queue();
            return;
        } else if (args[0] == "0") {
            manager.setSlowmode(0).sync();
            emb.setColor(Colors.SUCCESS.color()).setTitle("Änderung").setDescription("Der Slowmode ist für diesen Channel nun deaktiviert");
            msg.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        try {
            s = Integer.parseInt(args[0]);
            if (args.length > 1) m = Integer.parseInt(args[1]);
            if (args.length > 2) h = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            emb.setColor(Colors.ERROR.color()).setTitle("Fehler").setDescription("Du hast ungültige Zahlen angegeben ._.");
            msg.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        int time = s + (m * 60) + (h * 60 * 60);
        if (time > 21600) {
            emb.setTitle("Fehler").setDescription("Die Angegebene Zeit ist zu lang(max 21600 Sekunden)").setColor(Colors.ERROR.color());
            channel.sendMessage(emb.build());
            return;
        }

        manager.setSlowmode(time).queue();

        emb.setTitle("Änderung").setColor(Colors.SUCCESS.color());
        if (time != 0) {
            emb.setDescription(
                    "Der Slowmode für diesen Channel wurde auf "
                            + "`" + s + "` Sekunden"
                            + (m != 0 ? (", `" + m + "` Minuten") : "")
                            + (h != 0 ? (", `" + h + "` Stunden") : "")
                            + " gesetzt");
        } else {
            emb.setDescription("Der Slowmode wurde für diesen Channel deaktiviert");
        }

        channel.sendMessage(emb.build()).queue();
    }
}