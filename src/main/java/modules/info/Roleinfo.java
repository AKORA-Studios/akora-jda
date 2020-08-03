package modules.info;

import main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import utilitys.Colors;
import utilitys.EmbedGenerator;
import utilitys.Module;
import utilitys.ModuleType;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Roleinfo extends Module {

    public Roleinfo() {
        super("Roleinfo", new ArrayList<String>(Arrays.asList(new String[]{"roleinfo", "rinfo"})),
                "roleinfo {Mention}", "Zeigt dir Informationen über eine Rolle an",
                new ArrayList<Permission>(Arrays.asList()),
                ModuleType.INFO
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedGenerator generator = getEmbedGenerator();
        EmbedBuilder emb = generator.newEmbed(msg.getAuthor());
        TextChannel channel = msg.getTextChannel();
        Guild guild = Bot.jda.getGuildById(msg.getGuild().getIdLong());
        Role role;
        String created;

        String members = "";
        String permissions = "";

        if (msg.getMentionedRoles().isEmpty()) {
            msg.getChannel().sendMessage(
                    emb.setTitle("Fehler")
                            .setDescription("Du musst eine Rolle erwähnen ._.")
                            .setColor(Colors.ERROR.color())
                            .build()
            ).queue();
            return;
        } else {
            role = msg.getMentionedRoles().get(0);
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

        created = dtf.format(role.getTimeCreated());

        if (role.getPermissions().isEmpty()) {
            permissions = "Keine";
        } else {
            for (Permission perm : role.getPermissions()) {
                String name = perm.getName().substring(0, 1) + perm.getName().substring(1).toLowerCase();
                permissions += "`" + name + "`, ";
            }
            permissions = permissions.substring(0, permissions.length() - 2);
        }

        List<Member> members_with_role = msg.getGuild().getMembersWithRoles(role);
        for (Member member : members_with_role) {
            members += member.getAsMention() + ", ";
        }

        if (members_with_role.size() == 0) {
            members = "Keine";
        } else if (members.length() < 500 && members_with_role.size() > 25) {
            members = "";
            for (Member member : members_with_role.subList(0, 25)) {
                members += member.getAsMention() + ", ";
            }
        } else {
            members = members.substring(0, members.length() - 2);
        }

        emb.addField("Displayname", role.getAsMention(), true)
                .addField("Erwähnbar", role.isMentionable() ? ":white_check_mark:" : ":x:", true)
                .addField("Gruppieren", role.isHoisted() ? ":white_check_mark:" : ":x:", true)
                .addField("Farbe", "`" +
                        String.format("#%02X%02X%02X",
                                role.getColor().getRed(),
                                role.getColor().getGreen(),
                                role.getColor().getBlue()) +
                        "`", true)
                .addField("Erstellt", created, true)
                .addField("ID", "`" + role.getId() + "`", true)
                .addField("Member", members, false)
                .addField("Berechtigungen", permissions, false);


        emb.setColor(role.getColor());

        channel.sendMessage(emb.build()).queue();
    }
}