package modules.economy;

import database.UserEconomy;
import main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import utilitys.Colors;
import utilitys.EmbedGenerator;
import utilitys.Module;
import utilitys.ModuleType;

import java.util.ArrayList;
import java.util.Arrays;

public class Pay extends Module {

    public Pay() {
        super("Pay", new ArrayList<String>(Arrays.asList(new String[]{"pay"})),
                "pay [Mention] [Betrag]", "Überweißt einem anderen nutzer Geld",
                new ArrayList<Permission>(Arrays.asList()),
                ModuleType.ECONOMY
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedGenerator generator = getEmbedGenerator();
        EmbedBuilder emb = generator.newEmbed(msg.getAuthor());
        TextChannel channel = msg.getTextChannel();
        Member member;
        int amount = 0;

        if (msg.getMentionedMembers().size() > 0) {
            member = msg.getMentionedMembers().get(0);
        } else {
            msg.getChannel().sendMessage(emb.setTitle("Fehler").setDescription("Du musst einen Nutzer erwähnen").setColor(Colors.ERROR.color()).build()).queue();
            return;
        }

        if (args.length < 2) {
            msg.getChannel().sendMessage(emb.setTitle("Fehler").setDescription("Du musst einen Betrag angeben").setColor(Colors.ERROR.color()).build()).queue();
            return;
        } else {
            try {
                if (Integer.parseInt(args[1]) != 0) {
                    amount = Integer.parseInt(args[1]);
                } else {
                    msg.getChannel().sendMessage(emb.setTitle("Fehler").setDescription("Der Betrag muss eine Zahl sein").setColor(Colors.ERROR.color()).build()).queue();
                    return;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        UserEconomy from = Bot.dbHandler.getUserEconomy(msg.getMember().getIdLong());

        if (from.balance < amount) {
            msg.getChannel().sendMessage(emb.setTitle("Fehler").setDescription("Du hast nicht genug Geld").setColor(Colors.ERROR.color()).build()).queue();
            return;
        }

        Bot.dbHandler.checkUserEconomy((int) member.getIdLong());
        UserEconomy to = Bot.dbHandler.getUserEconomy(member.getIdLong());

        from.balance -= amount;
        to.balance += amount;

        Bot.dbHandler.setUserEconomy(from);
        Bot.dbHandler.setUserEconomy(to);

        emb.setTitle("Transaktion erfolgreich!").setDescription("Du hast "+member.getAsMention()+" `"+amount+"` ͼ gezahlt");

        channel.sendMessage(emb.build()).queue();
    }
}