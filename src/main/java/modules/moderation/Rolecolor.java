package modules.moderation;

import main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.w3c.dom.ls.LSOutput;
import utilitys.Colors;
import utilitys.EmbedGenerator;
import utilitys.Module;
import utilitys.ModuleType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rolecolor extends Module {

    public Rolecolor() {
        super("Rolecolor", new ArrayList<String>(Arrays.asList(new String[]{"rolecolor", "rcolor"})),
                "rolecolor {Mention} [HEX-Color]",
                "Ändert die Farbe einer Rolle\n > Wenn du vorher den `color` Befehl genutzt hast kopiert der Bot die angegebene Farbe",
                new ArrayList<Permission>(Arrays.asList(Permission.MANAGE_ROLES)),
                ModuleType.MODERATION
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedGenerator generator = getEmbedGenerator();
        EmbedBuilder emb = generator.newEmbed(msg.getAuthor());
        TextChannel channel = msg.getTextChannel();
        Role role = msg.getMentionedRoles().isEmpty() ? null : msg.getMentionedRoles().get(0);

        final Message[] emb_msg = new Message[1];
        final MessageEmbed[] msg_emb = new MessageEmbed[1];
        final java.awt.Color[] color = new java.awt.Color[1];
        color[0] = null;

        Runnable color_not_found = () -> {
            msg.getChannel().sendMessage(
                    emb.setTitle("Fehler")
                            .setDescription("Keine Farbe gefunden ._.")
                            .setColor(Colors.ERROR.color())
                            .build()
            ).queue();
        };

        Runnable color_found = () -> {
            if (color[0] == null) {
                color[0] = msg_emb[0].getColor();
            }

            if (msg.getGuild().getSelfMember().canInteract(role)) {
                role.getManager().setColor(color[0]).queue(success -> {
                    msg.getChannel().sendMessage(
                            emb.setTitle("Änderung")
                                    .setDescription("Die Rolle " + role.getAsMention() + ", hat jetzt die Farbe `" +
                                            String.format("#%02X%02X%02X",
                                                    color[0].getRed(),
                                                    color[0].getGreen(),
                                                    color[0].getBlue())
                                            + "`")
                                    .setColor(color[0])
                                    .build()
                    ).queue();
                });
            } else {
                msg.getChannel().sendMessage(
                        emb.setTitle("Fehler")
                                .setDescription("Die Angegebene Rolle ist mir überlegen ._.")
                                .setColor(Colors.ERROR.color())
                                .build()
                ).queue();
            }
        };

        Runnable not_valid_color = () -> {
            msg.getChannel().sendMessage(
                    emb.setTitle("Fehler")
                            .setDescription("Die Angegebene Hex Farbe ist ungültig")
                            .addField("Beispiel:", "#123DEF", true)
                            .setColor(Colors.ERROR.color())
                            .build()
            ).queue();
        };

        Runnable not_valid_role = () -> {
            msg.getChannel().sendMessage(
                    emb.setTitle("Fehler")
                            .setDescription("Du musst eine Rolle angeben ._.")
                            .setColor(Colors.ERROR.color())
                            .build()
            ).queue();
        };


        if (role == null) {
            not_valid_role.run();
            return;
        } else {
            if (args.length > 1) {
                if (args[1].length() < 6 || args[1].length() > 7) {
                    not_valid_color.run();
                    return;
                } else {
                    try {
                        color[0] = java.awt.Color.decode(args[1]);
                        color_found.run();
                        return;
                    } catch (NumberFormatException e) {
                        formatException(msg, e);
                        return;
                    }
                }
            } else {
                msg.getTextChannel().getHistoryBefore(msg.getIdLong(), 1).queue(result -> {
                    List<Message> history = result.getRetrievedHistory();

                    if (history.isEmpty()) {
                        color_not_found.run();
                        return;
                    } else {
                        emb_msg[0] = history.get(0);
                        List<MessageEmbed> emb_list = emb_msg[0].getEmbeds();
                        if (emb_list.isEmpty()) {
                            color_not_found.run();
                            return;
                        } else {
                            msg_emb[0] = emb_list.get(0);
                            if (msg_emb[0].isEmpty()
                                    || Arrays.asList(Colors.values()).contains(msg_emb[0].getColor())//Checken ob das Embed die Farbe einer normalen Nachricht hat
                                    || msg_emb[0].getColorRaw() == 0) {

                                color_not_found.run();
                                return;
                            } else {
                                if (msg_emb[0].getFields().size() != 4) {
                                    color_not_found.run();
                                    return;
                                } else if (!msg_emb[0].getFields().get(0).getName().contains("Hex")) {
                                    color_not_found.run();
                                    return;
                                }

                                color_found.run();
                                return;
                            }
                        }
                    }
                });
                return;
            }
        }
    }

    public void formatException(Message msg, Exception e) {
        EmbedBuilder emb = getEmbedGenerator().newEmbed(msg.getAuthor());
        msg.getChannel().sendMessage(
                emb.setTitle("Fehler")
                        .setDescription("Die Angegebenen Parameter sind keine Farbe")
                        .addField("HEX-Beispiel:", "#123DEF", true)
                        .addField("RGB-Beispiel:", "0 128 255", true)
                        //.addField("Fehler", "**" + e.getClass().getSimpleName() + ":** `" + e.getMessage() + "`", false)
                        .setColor(Colors.ERROR.color())
                        .build()
        ).queue();
        //e.printStackTrace();
    }
}