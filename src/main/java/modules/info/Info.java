package modules.info;

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

import java.lang.management.ManagementFactory;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;

public class Info extends Module {

    public Info() {
        super("Botinfo", new ArrayList<String>(Arrays.asList(new String[]{"info", "botinfo", "binfo"})),
                "info", "Zeigt dir Informationen über den Bot an",
                new ArrayList<Permission>(Arrays.asList()),
                ModuleType.INFO
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedGenerator generator = getEmbedGenerator();
        EmbedBuilder emb = generator.newEmbed(msg.getAuthor());
        TextChannel channel = msg.getTextChannel();

        Member member = msg.getGuild().getMemberById(Bot.jda.getSelfUser().getId());

        String activity, created, joined, uptime = "";
        final int uptime_ms, years, months, days, hours, minutes, seconds;

        uptime_ms = (int) Math.floor(ManagementFactory.getRuntimeMXBean().getUptime());
        years = (int) Math.floor(uptime_ms / 31104000000L);
        months = (int) Math.floor(uptime_ms / 2592000000L % 12);
        days = (int) Math.floor(uptime_ms / 86400000L % 30);
        hours = (int) Math.floor(uptime_ms / 3600000L % 24);
        minutes = (int) Math.floor(uptime_ms / 60000L % 60);
        seconds = (int) Math.floor(uptime_ms / 1000L % 60);

        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

        created = dtf.format(member.getTimeCreated());
        joined = dtf.format(member.getTimeJoined());

        if (member.getActivities().size() > 0) {
            activity = member.getActivities().get(0).toString();
        } else {
            activity = "Nix";
        }

        if (years > 0) uptime += "`"+years+"` **Jahre**, ";
        if (months > 0) uptime += "`"+months+"` **Monate**, ";
        if (days > 0) uptime += "`"+days+"` **Tage**, ";
        if (hours > 0) uptime += "`"+hours+"` **Stunden**, ";
        if (minutes > 0 && uptime == "") uptime += "`"+minutes+"` **Minuten**, ";
        if (seconds > 0 && uptime == "") uptime += "`"+seconds+"` **Sekunden**, ";
        uptime = uptime.substring(0, uptime.length()-2);

        emb.addField("Displayname", member.getAsMention(), true)
                .addField("Username", member.getUser().getAsTag(), true)
                .addField("ID", member.getId(), true)
                .addField("Status", member.getOnlineStatus().getKey(), true)
                .addField("Beschäftigung", activity, true)
                .addBlankField(true)
                .addField("Erstellt", created, true)
                .addField("Beigetreten", joined, true)
                .addField("Uptime", uptime,true)
                .addBlankField(false)
                .addField("**Bot Owner:**", "<@387655649934311427>", true)
                .addBlankField(true)
                .addField("Support Server uwu", "[Support Server](https://discord.gg/vEkS7Q9)", true)
                .addField("**Unterstützer:**", "<@586905336850677760>", true)
                .addBlankField(true)
                .addField("Invite Link", "[Invite Link](https://discordapp.com/api/oauth2/authorize?client_id="+Bot.jda.getSelfUser().getId()+"&permissions=8&scope=bot)", true);

        emb.setColor(Colors.UNIMPORTANT.color());
        emb.setThumbnail(member.getUser().getEffectiveAvatarUrl());

        channel.sendMessage(emb.build()).complete();
    }
}