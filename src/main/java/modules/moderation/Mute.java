package modules.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.WidgetUtil;
import utilitys.Colors;
import utilitys.Module;
import utilitys.ModuleType;

import java.util.ArrayList;
import java.util.Arrays;

public class Mute extends Module {

    private EmbedBuilder emb;

    public Mute() {
        super(
                "Mute", new ArrayList<String>(Arrays.asList(new String[]{"mute"})),
                "mute {Mention}", "Schaltet das erwähnte Member stumm",
                new ArrayList<Permission>(Arrays.asList(Permission.VOICE_MUTE_OTHERS)),
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

        if (!msg.getGuild().getSelfMember().hasPermission(Permission.VOICE_MUTE_OTHERS)) {
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

        member.mute(!vState.isGuildMuted()).queue(success -> {
            emb.setTitle("Änderung").setDescription(member.getAsMention() + ", "+(vState.isGuildMuted() ? " ist jetzt stumm" : "darf wieder reden")).setColor(Colors.SUCCESS.color());
            channel.sendMessage(emb.build()).queue();
        });
    }
}