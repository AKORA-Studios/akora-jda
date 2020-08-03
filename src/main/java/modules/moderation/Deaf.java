package modules.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import utilitys.Colors;
import utilitys.Module;
import utilitys.ModuleType;

import java.util.ArrayList;
import java.util.Arrays;

public class Deaf extends Module {

    private EmbedBuilder emb;

    public Deaf() {
        super(
                "Deaf", new ArrayList<String>(Arrays.asList(new String[]{"deaf"})),
                "deaf {Mention}", "Schaltet das erwähnte Member taub",
                new ArrayList<Permission>(Arrays.asList(Permission.VOICE_DEAF_OTHERS)),
                ModuleType.MODERATION
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedBuilder emb = getEmbedGenerator().newEmbed(msg.getAuthor());
        TextChannel channel = msg.getTextChannel();

        if (msg.getMentionedMembers().isEmpty()) {
            emb.setColor(Colors.ERROR.color()).setTitle("Fehler").setDescription("Du musst jemanden Erwähnen");
            msg.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        if (!msg.getGuild().getSelfMember().hasPermission(Permission.VOICE_DEAF_OTHERS)) {
            emb.setColor(Colors.ERROR.color()).setTitle("Fehler").setDescription("Ich habe nicht die nötigen Berechtigungen ._.");
            msg.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        Member member = msg.getMentionedMembers().get(0);
        GuildVoiceState vState = member.getVoiceState();

        if (vState.getChannel() == null) {
            emb.setColor(Colors.ERROR.color()).setTitle("Fehler").setDescription("Das erwähnte Member muss sich in einem Voice Channel befinden ._.");
            msg.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        member.deafen(!vState.isGuildDeafened()).queue(success -> {
            emb.setTitle("Änderung").setDescription(member.getAsMention() + ", " + (vState.isGuildDeafened() ? " ist jetzt taub" : "darf wieder zuhören")).setColor(Colors.SUCCESS.color());
            channel.sendMessage(emb.build()).queue();
        });
    }
}