package modules.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.PermissionException;
import utilitys.Colors;
import utilitys.Module;
import utilitys.ModuleType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Ban extends Module {

    private EmbedBuilder emb;

    public Ban() {
        super(
                "Ban", new ArrayList<String>(Arrays.asList(new String[]{"ban"})),
                "ban [Mention] {Mention}...", "Bannt ein oder mehrere Member",
                new ArrayList<Permission>(Arrays.asList(Permission.BAN_MEMBERS)),
                ModuleType.MODERATION
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedBuilder emb = getEmbedGenerator().newEmbed(msg.getAuthor());


        if (msg.getMentionedMembers().isEmpty()) {
            emb.setColor(Colors.ERROR.color()).setTitle("Fehler").setDescription("Du musst jemanden Erwähnen");
            msg.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        if (!msg.getGuild().getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            emb.setColor(Colors.ERROR.color()).setTitle("Fehler").setDescription("Ich habe nicht die nötigen Berechtigungen ._.");
            msg.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        msg.getChannel().sendMessage("◈ **Banned** ◈").queue();

        msg.getChannel().sendMessage("Sicher?").queue(
                success -> {
                    String verify_emote = ":check:";
                    boolean verified = false;
                    success.addReaction(verify_emote).queue();

                    try {
                        wait(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for (MessageReaction reaction : success.getReactions()) {
                        String emote_name = reaction.getReactionEmote().getEmote().getName();
                        System.out.println(emote_name);
                        if (emote_name == verify_emote) {
                            verified = true;
                            banMembers(msg.getMentionedMembers(), msg);
                            return;
                        }
                    }

                    if (!verified) {
                        msg.getChannel().sendMessage(getEmbedGenerator().newEmbed(
                                msg.getAuthor(),
                                "Abbruch",
                                "Aktion abgebrochen qwq"
                        ).setColor(Colors.ERROR.color()).build()).queue();
                    }

                    success.delete().queueAfter(1, TimeUnit.MINUTES);
                }
        );
    }

    private void banMembers(List<Member> banMembers, Message msg) {
        Member selfMember = msg.getGuild().getSelfMember();

        for (Member member : banMembers) {
            EmbedBuilder e = getEmbedGenerator().newEmbed(msg.getAuthor());

            if (!selfMember.canInteract(member)) {
                e.setColor(Colors.ERROR.color()).setTitle("Fehler").setDescription(member.getAsMention() + " ist mir überlegen :0");
                msg.getChannel().sendMessage(e.build()).queue();
                continue;
            }

            msg.getGuild().ban(member, 0, "Banned by " + member.getUser().getAsTag()).queue(
                    success -> {
                        msg.getChannel().sendMessage(" > `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + "`").queue();
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