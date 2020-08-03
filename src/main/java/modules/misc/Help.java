package modules.misc;

import main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import utilitys.Colors;
import utilitys.Module;
import utilitys.ModuleType;

import java.util.ArrayList;
import java.util.Arrays;

public class Help extends Module {
    public Help() {
        super(
                "Help",
                new ArrayList<String>(Arrays.asList(new String[]{"help", "h"})), "help {command}",
                "Liefert Hilfe für den Bot",
                new ArrayList<Permission>(Arrays.asList()),
                ModuleType.MISC
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        TextChannel channel = msg.getTextChannel();
        EmbedBuilder emb = getEmbedGenerator().newEmbed(msg.getAuthor());
        if (args.length == 0) {
            emb.setTitle(getName());
            ArrayList<ModuleType> types = new ArrayList<ModuleType>(Arrays.asList(ModuleType.values()));
            MessageEmbed.Field temp_field;
            ArrayList<MessageEmbed.Field> fields = new ArrayList<MessageEmbed.Field>();

            for (ModuleType type : types) {
                if (type != ModuleType.INVISIBLE) {
                    String name = type.toString();
                    name = "**" + name.substring(0, 1) + name.substring(1).toLowerCase() + "**";
                    String value = "";

                    for (Module mod : Bot.modules) {
                        if (mod.getType() == type) {
                            value += "`" + mod.getCommand().get(0) + "`, ";
                        }
                    }

                    if (value.length() != 0) {
                        value = value.substring(0, value.length() - 2);
                    }

                    temp_field = new MessageEmbed.Field(name, value, false, true);
                    fields.add(temp_field);
                }
            }

            for (MessageEmbed.Field field : fields) {
                emb.addField(field);
            }

            emb.addBlankField(false)
                    .addField("**Bot Owner:**", "<@387655649934311427>", true)
                    .addBlankField(true)
                    .addField("Support Server uwu", "[Support Server](https://discord.gg/vEkS7Q9)", true)
                    .addField("**Unterstützer:**", "<@586905336850677760>", true)
                    .addBlankField(true)
                    .addField("Invite Link", "[Invite Link](https://discordapp.com/api/oauth2/authorize?client_id=" + Bot.jda.getSelfUser().getId() + "&permissions=8&scope=bot)", true);

        } else {
            for (Module mod : Bot.modules) {
                if (mod.getCommand().contains(args[0])) {
                    String command = "";
                    emb.setTitle(mod.getName())
                            .addField("Syntax:", mod.getSyntax(), false)
                            .addField("Beschreibung:", mod.getDescription(), false);
                    for (String cmd : mod.getCommand()) {
                        command += "`" + cmd + "`, ";
                    }
                    command = command.substring(0, command.length() - 2);
                    emb.addField("Alias:", command, false);
                }
            }
        }

        emb.setColor(Colors.INFO.color());
        channel.sendMessage(emb.build()).complete();
    }
}