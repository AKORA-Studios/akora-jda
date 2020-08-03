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

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;

public class Profile extends Module {

    public Profile() {
        super("Profile", new ArrayList<String>(Arrays.asList(new String[]{"profile"})),
                "profile {Mention}", "Zeigt dir das Profil eines Benutzers an",
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

        emb.addField("XP", eco.xp+"", true)
                .addField("Level", eco.level+"", true)
                .addField("Balance", "`"+eco.balance+"` ͼ", false);

        emb.setThumbnail(member.getUser().getEffectiveAvatarUrl());

        channel.sendMessage(emb.build()).complete();
    }
}