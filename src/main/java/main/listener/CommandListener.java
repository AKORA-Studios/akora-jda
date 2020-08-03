package main.listener;

import main.Bot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utilitys.Colors;
import utilitys.EmbedGenerator;
import utilitys.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class CommandListener extends ListenerAdapter {
    private static EmbedGenerator embedGenerator;

    public CommandListener() {
        embedGenerator = new EmbedGenerator();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();

        if (message.getContentRaw().startsWith(Bot.prefix) && !message.getMember().getUser().isBot()) {
            ArrayList<String> content = new ArrayList<String>(Arrays.asList(message.getContentRaw().substring(Bot.prefix.length()).split(" ")));
            String command = content.get(0);

            if (command.equalsIgnoreCase("restart")) {
                if (Bot.config.owner.contains(message.getAuthor().getId())) {
                    System.out.println("RESTART");
                    message.getChannel().sendMessage(embedGenerator.newEmbed(message.getAuthor(), "RESTART", "Der Bot startet neu").build()).complete();
                    Bot.restartBot(true);
                } else {
                    message.getChannel().sendMessage(embedGenerator.newEmbed(message.getAuthor(), "ERROR", "Nur der Bot Owner kann das").build()).complete();
                }
            }

            String[] arguments;
            if (content.size() > 1) {
                content.remove(0);
                arguments = content.toArray(new String[content.size()]);
            } else {
                arguments = new String[]{};
            }

            for (Module module : Bot.modules) {
                if (module.getCommand().contains(command)) {
                    if (message.getMember().hasPermission(module.getPermissions())) {
                        //Befehl ausführen
                        //message.getTextChannel().sendTyping().queue();
                        module.onExecute(message, arguments);

                        //Wenn die Nachricht eine Erwähnung enthält, die Nachricht löschen
                        if (message.getMentionedRoles().size() != 0
                                || message.getMentionedMembers().size() != 0
                                || message.getMentionedUsers().size() != 0) {
                            message.delete().queueAfter(1, TimeUnit.SECONDS);
                        }

                        //In der Console ausgeben was wer ausgeführt hat qwq
                        System.out.println(message.getMember().getUser().getAsTag() + " executed: " + message.getContentDisplay());
                    } else {
                        String needed = "";
                        for (Permission perm : module.getPermissions()) {
                            if (!message.getMember().getPermissions().contains(perm)) {
                                needed += "`" + perm.getName() + "`, ";
                            }
                        }
                        needed = needed.substring(0, needed.length() - 2);

                        MessageEmbed emb = embedGenerator.newEmbed(message.getMember().getUser(), "Fehlende Berechtigungen:", needed).setColor(Colors.ERROR.color()).build();
                        message.getChannel().sendMessage(emb).queue();
                        System.out.println(message.getMember().getUser().getAsTag() + " tried to execute: " + message.getContentDisplay());
                    }
                }
            }
        }
    }
}
