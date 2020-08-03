package main.listener;

import main.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class GuildEventListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        Guild guild = event.getGuild();

        /*
        if (Bot.copyedServer.contains(guild.getName())) {
            int index = Bot.copyedServer.indexOf(guild.getName());
            User copier = Bot.jda.getUserById(Bot.copier_id.get(index));
            guild.getTextChannels().get(0).createInvite().setMaxUses(3).setMaxAge(0).queue(invite -> {
                copier.openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage(invite.getUrl()).queue();
                });
            });
        }
        */

        Bot.dbHandler.checkGuildConfig((int) guild.getIdLong());
        for (Member member : guild.getMembers()) {
            Bot.dbHandler.checkUserEconomy((int) member.getIdLong());
        }
    }

    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
        Guild guild = event.getGuild();

    }


    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        Member member = event.getMember();
        Guild guild = event.getGuild();

        /*
        if (Bot.copyedServer.contains(guild.getName())) {
            int index = Bot.copyedServer.indexOf(guild.getName());
            User copier = Bot.jda.getUserById(Bot.copier_id.get(index));

            guild.transferOwnership(guild.getMember(copier)).queue();

            Bot.copyedServer.remove(index);
            Bot.copier_id.remove(index);
        }
         */

        Bot.dbHandler.checkUserEconomy((int) member.getIdLong());
    }

    @Override
    public void onGuildMemberRemove(@Nonnull GuildMemberRemoveEvent event) {
        Member member = event.getMember();
        Guild guild = event.getGuild();
    }


    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        VoiceChannel old_vc = event.getOldValue();
        VoiceChannel new_vc = event.getChannelLeft();

        if (new_vc.getMembers().contains(old_vc.getGuild().getSelfMember())) {
            if (new_vc.getMembers().size() == 1) {
                new_vc.getGuild().getAudioManager().closeAudioConnection();
            }
        }
    }
}
