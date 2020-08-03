package modules.moderation;

import com.sun.management.OperatingSystemMXBean;
import main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;
import utilitys.Colors;
import utilitys.Module;
import utilitys.ModuleType;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Array;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Kick extends Module {

    private EmbedBuilder emb;

    public Kick() {
        super(
                "Kick", new ArrayList<String>(Arrays.asList(new String[]{"kick"})),
                "kick [Mention] {Mention}...", "Kickt ein oder mehrere Member",
                new ArrayList<Permission>(Arrays.asList(Permission.KICK_MEMBERS)),
                ModuleType.MODERATION
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedBuilder emb = getEmbedGenerator().newEmbed(msg.getAuthor());
        Member selfMember = msg.getGuild().getSelfMember();
        TextChannel channel = msg.getTextChannel();

        if (msg.getMentionedMembers().isEmpty()) {
            emb.setColor(Colors.ERROR.color()).setTitle("Fehler").setDescription("Du musst jemanden Erwähnen");
            msg.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        if (!msg.getGuild().getSelfMember().hasPermission(Permission.KICK_MEMBERS)) {
            emb.setColor(Colors.ERROR.color()).setTitle("Fehler").setDescription("Ich habe nicht die nötigen Berechtigungen ._.");
            msg.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        List<Member> mentionedMembers = msg.getMentionedMembers();

        msg.getChannel().sendMessage("◈ **Kicked** ◈").queue();

        for (Member member : mentionedMembers) {
            EmbedBuilder e = getEmbedGenerator().newEmbed(msg.getAuthor());

            if (!selfMember.canInteract(member)) {
                e.setColor(Colors.ERROR.color()).setTitle("Fehler").setDescription(member.getAsMention() + " ist mir überlegen :0");
                msg.getChannel().sendMessage(e.build()).queue();
                continue;
            }

            msg.getGuild().kick(member).queue(
                    success -> {
                        msg.getChannel().sendMessage(" > `" + member.getUser().getName()+"#"+member.getUser().getDiscriminator()+"`").queue();
                    },
                    error -> {
                        EmbedBuilder embed = getEmbedGenerator().newEmbed(msg.getAuthor());

                        if (error instanceof PermissionException) {
                            PermissionException exc = (PermissionException) error;
                            Permission missingPerm = exc.getPermission();

                            embed.setColor(Colors.ERROR.color()).setTitle("Berechtigungsfehler")
                                    .setDescription(member.getAsMention() + " hat einen Zauber angewendet um nicht gekickt zu werden :0")
                                    .addField("Berechtigung", "`" + missingPerm.getName() + "`", false)
                                    .addField("Fehler", error.getClass().getSimpleName(), false);
                        } else {
                            embed.setColor(Colors.ERROR.color()).setTitle("Berechtigungsfehler")
                                    .setDescription(member.getAsMention() + " hat einen Zauber angewendet um nicht gekickt zu werden :0")
                                    .addField("Fehler", error.getClass().getSimpleName(), false);
                        }

                        msg.getChannel().sendMessage(embed.build()).queue();
                    }
            );
        }
    }
}