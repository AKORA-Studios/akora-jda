package modules.moderation;

import main.Bot;
import main.listener.CommandListener;
import main.listener.GuildEventListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.GuildAction;
import utilitys.Colors;
import utilitys.EmbedGenerator;
import utilitys.Module;
import utilitys.ModuleType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CopyServer extends Module {

    public CopyServer() {
        super("CopyServer", new ArrayList<String>(Arrays.asList(new String[]{"copyserver", "cserver", "backup"})),
                "copyserver",
                "Kopiert den Server mit allen Rollen, Berechtigungen und Channels",
                new ArrayList<Permission>(Arrays.asList(Permission.ADMINISTRATOR, Permission.MANAGE_SERVER)),
                ModuleType.MODERATION
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        /*
        EmbedGenerator generator = getEmbedGenerator();
        EmbedBuilder emb = generator.newEmbed(msg.getAuthor());

        Guild from = msg.getGuild();

        String name = from.getName() + " ◈ Copy";
        GuildAction to = Bot.jda.createGuild(from.getName() + " ◈ Copy");

        HashMap<Long, GuildAction.RoleData> roles = new HashMap<Long, GuildAction.RoleData>();


        //Einstelllungen
        to.setExplicitContentLevel(from.getExplicitContentLevel());
        to.setVerificationLevel(from.getVerificationLevel());
        to.setNotificationLevel(from.getDefaultNotificationLevel());
        to.setRegion(from.getRegion());

        //Rollen
        for (Role role : from.getRoles()) {
            to.newRole()
                    //Berechtigungen
                    .addPermissions(role.getPermissions())
                    //Farbe
                    .setColor(role.getColor())
                    //Bot Rolle
                    .setHoisted(role.isHoisted())
                    //Erwähnbar
                    .setMentionable(role.isMentionable())
                    //Name
                    .setName(role.getName())
                    //Position
                    .setPosition(role.getPosition());
        }

        //Channel
        for (GuildChannel channel : from.getChannels()) {
            GuildAction.ChannelData new_channel = new GuildAction.ChannelData(
                    channel.getType(),
                    channel.getName()
            ).setPosition(channel.getPosition());

            switch (channel.getType()) {
                case TEXT:
                    TextChannel text = (TextChannel) channel;
                    new_channel
                            .setNSFW(text.isNSFW())
                            .setTopic(text.getTopic());
                    for (PermissionOverride override : text.getPermissionOverrides()) {
                        if (roles.get(override.getRole().getIdLong()) != null) {
                            new_channel.addPermissionOverride(roles.get(override.getRole().getIdLong()), override.getAllowed(), override.getDenied());
                        }
                    }
                    break;
                case VOICE:
                    VoiceChannel voice = (VoiceChannel) channel;
                    new_channel
                            .setBitrate(voice.getBitrate())
                            .setUserlimit(voice.getUserLimit());
                    for (PermissionOverride override : voice.getPermissionOverrides()) {
                        if (roles.get(override.getRole().getIdLong()) != null) {
                            new_channel.addPermissionOverride(roles.get(override.getRole().getIdLong()), override.getAllowed(), override.getDenied());
                        }
                    }
                    break;
                case CATEGORY:
                    Category cat = (Category) channel;
                    for (PermissionOverride override : cat.getPermissionOverrides()) {
                        if (roles.get(override.getRole().getIdLong()) != null) {
                            new_channel.addPermissionOverride(roles.get(override.getRole().getIdLong()), override.getAllowed(), override.getDenied());
                        }
                    }
                    break;
            }

            to.addChannel(new_channel).queue();
        }

        //Den Ausführenden Des Befehls Einladen
        to.queue();
        Bot.copyedServer.add(name);
        Bot.copier_id.add(msg.getAuthor().getIdLong());

        msg.getTextChannel().sendMessage(emb.setColor(Colors.SUCCESS.color()).setTitle("Server Kopiert").setDescription("Schau mal in deine DM's :)").build()).queue();
         */
    }
}