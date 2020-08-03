package modules.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import utilitys.Colors;
import utilitys.EmbedGenerator;
import utilitys.Module;
import utilitys.ModuleType;

import java.util.ArrayList;
import java.util.Arrays;

public class Color extends Module {

    public Color() {
        super("Color", new ArrayList<String>(Arrays.asList(new String[]{"color", "col"})),
                "color {HEX-Color} | {r} {g} {b}", "Zeigt dir Informationen über eine Farbe an",
                new ArrayList<Permission>(Arrays.asList()),
                ModuleType.MISC
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedGenerator generator = getEmbedGenerator();
        EmbedBuilder emb = generator.newEmbed(msg.getAuthor());
        TextChannel channel = msg.getTextChannel();

        java.awt.Color color;

        if (args.length > 2) {
            try {
                int[] rgb = {0, 0, 0};
                rgb[0] = Integer.parseInt(args[0]);
                rgb[1] = Integer.parseInt(args[1]);
                rgb[2] = Integer.parseInt(args[2]);

                color = new java.awt.Color(rgb[0], rgb[1], rgb[2]);
            } catch (NumberFormatException e) {
                formatException(msg, e);
                return;
            } catch (IllegalArgumentException e) {
                formatException(msg, e);
                return;
            }
        } else if (args.length != 0) {
            if (args[0].length() < 6 || args[0].length() > 7) {
                msg.getChannel().sendMessage(
                        emb.setTitle("Fehler")
                                .setDescription("Die Angegebene Hex Farbe ist ungültig")
                                .addField("Beispiel:", "#123DEF", true)
                                .setColor(Colors.ERROR.color())
                                .build()
                ).queue();
                return;
            } else {
                try {
                    color = java.awt.Color.decode(args[0]);
                } catch (NumberFormatException e) {
                    formatException(msg, e);
                    return;
                }
            }
        } else {
            msg.getChannel().sendMessage(
                    emb.setTitle("Fehler")
                            .setDescription("Die Angegebenen Parameter sind keine Farbe")
                            .addField("HEX-Beispiel:", "`#123DEF`", true)
                            .addField("RGB-Beispiel:", "`0 128 255`", true)
                            .setColor(Colors.ERROR.color())
                            .build()
            ).queue();
            return;
        }

        emb.addField("Hex","`"+
                String.format("#%02X%02X%02X",
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue())+"`",
                false)
                .addField("Red", "`"+color.getRed()+"`", true)
                .addField("Green", "`"+color.getGreen()+"`", true)
                .addField("Blue", "`"+color.getBlue()+"`", true);
        emb.setColor(color);

        channel.sendMessage(emb.build()).queue();
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