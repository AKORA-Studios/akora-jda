package modules.info;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
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

public class Serverinfo extends Module {

    public Serverinfo() {
        super("Serverinfo", new ArrayList<String>(Arrays.asList(new String[]{"serverinfo", "sinfo"})),
                "serverinfo", "Zeigt dir Informationen über den Server an",
                new ArrayList<Permission>(Arrays.asList()),
                ModuleType.INFO
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedGenerator generator = getEmbedGenerator();
        EmbedBuilder emb = generator.newEmbed(msg.getAuthor());
        TextChannel channel = msg.getTextChannel();
        Guild guild = msg.getGuild();
        String created;
        boolean boosted = false;
        final int[] members = {0};
        final int[] admins = {0};
        final int[] bots = {0};
        final String[] roles = {""};

        ZonedDateTime zdt = ZonedDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

        created = dtf.format(guild.getTimeCreated());
        if (guild.getBoostCount() != 0) {
            boosted = true;
        }

        guild.getRoles().forEach(role -> {
            roles[0] += role.getAsMention()+", ";
        });
        roles[0] = roles[0].substring(0, roles[0].length()-2);

        guild.getMembers().forEach(m -> {
            if (m.getUser().isBot()) {
                bots[0]++;
            } else {
                if (m.hasPermission(Permission.ADMINISTRATOR)) {
                    admins[0]++;
                }
                members[0]++;
            }
        });

        emb.addField("Name", guild.getName(), true)
                .addField("ID", guild.getId(), true)
                .addField("Owner", guild.getOwner().getAsMention(), true)
                .addField("Kategorien", guild.getCategories().size()+"", true)
                .addField("Textchannel", guild.getTextChannels().size()+"", true)
                .addField("Voicechannel", guild.getVoiceChannels().size()+"", true)
                .addField("Member", members[0]+"", true)
                .addField("Admins", admins[0]+"", true)
                .addField("Bots", bots[0]+"", true)
                .addField("Erstellt", created, true)
                .addField("Region",guild.getRegion().getName(), true);

        if (boosted) {
            final String[] booster = {""};
            guild.getBoosters().forEach(m -> {
                booster[0] += m.getAsMention()+", ";
            });
            booster[0] = booster[0].substring(0, booster[0].length()-2);

            emb.addField("Boost Tier", guild.getBoostTier().getKey()+"", true)
                    .addField("Booster", booster[0], false);
        }

        emb.addField("Rollen", roles[0], false);

        emb.setThumbnail(guild.getIconUrl());

        channel.sendMessage(emb.build()).complete();
    }
}