package modules.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.InviteAction;
import utilitys.Colors;
import utilitys.EmbedGenerator;
import utilitys.Module;
import utilitys.ModuleType;

import java.util.ArrayList;
import java.util.Arrays;

public class Invite extends Module {

    public Invite() {
        super("Invite", new ArrayList<String>(Arrays.asList(new String[]{"invite", "link"})),
                "invite [uses] [maxAge]", "Erstellt einen Invitelink für den Server",
                new ArrayList<Permission>(Arrays.asList(Permission.CREATE_INSTANT_INVITE)),
                ModuleType.MISC
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedGenerator generator = getEmbedGenerator();
        EmbedBuilder emb = generator.newEmbed(msg.getAuthor());
        TextChannel channel = msg.getTextChannel();

        int maxUses = 0;
        int maxAge = 0;

        if (!msg.getGuild().getSelfMember().hasPermission(Permission.CREATE_INSTANT_INVITE)) {
            emb.setColor(Colors.ERROR.color()).setTitle("Fehler").setDescription("Ich habe nicht die nötigen Berechtigungen ._.");
            msg.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        java.awt.Color color;

        if (args.length > 1) {
            try {
                maxUses = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                formatException(msg, e);
                return;
            } catch (IllegalArgumentException e) {
                formatException(msg, e);
                return;
            }


            if (args.length > 2) {
                try {
                    maxAge = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    formatException(msg, e);
                    return;
                } catch (IllegalArgumentException e) {
                    formatException(msg, e);
                    return;
                }
            }
        }

        InviteAction inviteBuilder = msg.getTextChannel().createInvite()
                .setMaxUses(maxUses)
                .setMaxAge(maxAge)
                .setTemporary(false);

        inviteBuilder.queue(invite -> {
            emb.setColor(Colors.SUCCESS.color()).setTitle("Link erstellt", invite.getUrl()).setDescription(
                    "Der Invite "+invite.getUrl()+", wurde für den Channel "+msg.getTextChannel().getAsMention()+" erstellt"
            );
            channel.sendMessage(emb.build()).queue();
        }, failure -> {
            msg.getChannel().sendMessage(
                    emb.setTitle("Fehler")
                            .setDescription("Etwas ist beim erstellen der Einladung schiefgelaufen ._.")
                            //.addField("Fehler", "**" + e.getClass().getSimpleName() + ":** `" + e.getMessage() + "`", false)
                            .setColor(Colors.ERROR.color())
                            .build()
            ).queue();
        });
    }

    public void formatException(Message msg, Exception e) {
        EmbedBuilder emb = getEmbedGenerator().newEmbed(msg.getAuthor());
        msg.getChannel().sendMessage(
                emb.setTitle("Fehler")
                        .setDescription("Die Angegebenen Parameter sind keine gültigen Nummern")
                        //.addField("Fehler", "**" + e.getClass().getSimpleName() + ":** `" + e.getMessage() + "`", false)
                        .setColor(Colors.ERROR.color())
                        .build()
        ).queue();
        //e.printStackTrace();
    }
}