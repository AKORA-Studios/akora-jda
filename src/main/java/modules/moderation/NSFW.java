package modules.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;
import utilitys.Colors;
import utilitys.Module;
import utilitys.ModuleType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NSFW extends Module {

    private EmbedBuilder emb;

    public NSFW() {
        super(
                "Nsfw", new ArrayList<String>(Arrays.asList(new String[]{"nsfw", "sfw"})),
                "nsfw|sfw", "Ändert die Nsfw Einstellung des aktuellen Channels",
                new ArrayList<Permission>(Arrays.asList(Permission.MANAGE_CHANNEL)),
                ModuleType.MODERATION
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedBuilder emb = getEmbedGenerator().newEmbed(msg.getAuthor());
        TextChannel channel = msg.getTextChannel();

        if (!msg.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            emb.setColor(Colors.ERROR.color()).setTitle("Fehler").setDescription("Ich habe nicht die nötigen Berechtigungen ._.");
            msg.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        boolean nsfw = msg.getContentRaw().contains("nsfw");
        boolean sfw = msg.getContentRaw().contains("sfw");
        boolean old_nsfw = channel.isNSFW();
        boolean new_nsfw;

        if (sfw && !nsfw) {
            new_nsfw = false;
        } else {
            new_nsfw = true;
        }

        if (new_nsfw == old_nsfw) {
            emb.setTitle("Info").setDescription("Der Channel ist bereits " + (new_nsfw ? "`NSFW`" : "`SFW`") + " qwq").setColor(Colors.WARNING.color());
            channel.sendMessage(emb.build()).queue();
        } else {
            channel.getManager().setNSFW(new_nsfw).queue(succes -> {
                emb.setTitle("Änderung").setDescription("Der Channel ist jetzt " + (new_nsfw ? "`NSFW`" : "`SFW`") + " :0").setColor(Colors.SUCCESS.color());
                channel.sendMessage(emb.build()).queue();
            });
        }
    }
}