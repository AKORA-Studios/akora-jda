package modules.info;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import utilitys.EmbedGenerator;
import utilitys.Module;
import utilitys.ModuleType;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;

public class Userinfo extends Module {

    public Userinfo() {
        super("Userinfo", new ArrayList<String>(Arrays.asList(new String[]{"userinfo", "uinfo", "whois"})),
                "userinfo {Mention}", "Zeigt dir Informationen über einen Benutzer an",
                new ArrayList<Permission>(Arrays.asList()),
                ModuleType.INFO
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedGenerator generator = getEmbedGenerator();
        EmbedBuilder emb = generator.newEmbed(msg.getAuthor());
        TextChannel channel = msg.getTextChannel();
        Member member;
        String activity, bot, created,  joined, boosted;
        final String[] roles = {""};
        final String[] permissions = {""};

        if (msg.getMentionedMembers().size() > 0) {
            member = msg.getMentionedMembers().get(0);
        } else {
            member = msg.getMember();
        }

        ZonedDateTime zdt = ZonedDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

        created = dtf.format(member.getTimeCreated());
        joined = dtf.format(member.getTimeJoined());
        if (member.getTimeBoosted() == null) {
            boosted = "Noch nie";
        } else {
            boosted = dtf.format(member.getTimeBoosted());
        }

        if (member.getActivities().isEmpty()) {
            activity = "Nix";
        } else {
            activity = member.getActivities().get(0).toString();
        }

        if (member.getUser().isBot()) {
            bot = ":ballot_box_with_check:";
        } else {
            bot = ":x:";
        }

        if (member.getPermissions().isEmpty()) {
            permissions[0] = "Keine";
        } else {
            member.getPermissions().forEach(perm -> {
                String name = perm.getName();
                permissions[0] += "`"+name.substring(0, 1) + name.substring(1).toLowerCase()+"`, ";
            });
            permissions[0] = permissions[0].substring(0, permissions[0].length()-2);
        }

        member.getRoles().forEach(role -> {
            roles[0] += role.getAsMention()+", ";
        });
        roles[0] = roles[0].substring(0, roles[0].length()-2);

        emb.addField("Displayname", member.getAsMention(), true)
                .addField("Tag", member.getUser().getDiscriminator(), true)
                .addField("ID", member.getId(), true)
                .addField("Status", member.getOnlineStatus().getKey(), true)
                .addField("Beschäftigung", activity, true)
                .addField("Bot", bot, true)
                .addField("Erstellt", created, true)
                .addField("Beigetreten", joined, true)
                .addField("Zuletzt geboosted", boosted, true)
                .addField("Rollen", roles[0], false)
                .addField("Berechtigungen", permissions[0], false);


        emb.setThumbnail(member.getUser().getEffectiveAvatarUrl());

        channel.sendMessage(emb.build()).complete();
    }
}