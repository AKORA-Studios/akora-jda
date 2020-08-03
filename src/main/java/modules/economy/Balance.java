package modules.economy;

import database.UserEconomy;
import main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import utilitys.EmbedGenerator;
import utilitys.Module;
import utilitys.ModuleType;

import java.util.ArrayList;
import java.util.Arrays;

public class Balance extends Module {

    public Balance() {
        super("Balance", new ArrayList<String>(Arrays.asList(new String[]{"balance", "bal"})),
                "balance {Mention}", "Zeigt dir den Kontostand eines Benutzers an",
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

        if (msg.getMentionedMembers().size() > 0) {
            member = msg.getMentionedMembers().get(0);
        } else {
            member = msg.getMember();
        }

        UserEconomy eco = Bot.dbHandler.getUserEconomy(member.getId());

        emb.setTitle("Balance").setDescription("`"+eco.balance+"` ͼ");
        emb.setThumbnail(member.getUser().getEffectiveAvatarUrl());

        channel.sendMessage(emb.build()).complete();
    }
}