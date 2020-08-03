package main.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEmoteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class ReactionListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        Member member = event.getMember();
        Guild guild = event.getGuild();
        MessageReaction reaction = event.getReaction();
        TextChannel channel = event.getChannel();

        /*
        channel.retrieveMessageById(event.getMessageIdLong()).queue(
                msg -> {
                    System.out.println(reaction.getReactionEmote().getName());
                    msg.removeReaction(reaction.getReactionEmote().getEmoji(), member.getUser()).queue();
                }, throwable -> {
                    throwable.printStackTrace();
                }
        );
        */
    }

    @Override
    public void onGuildMessageReactionRemoveEmote(@Nonnull GuildMessageReactionRemoveEmoteEvent event) {
        Guild guild = event.getGuild();
        MessageReaction reaction = event.getReaction();
        TextChannel channel = event.getChannel();

        System.out.println("AA");

        /*
        channel.retrieveMessageById(event.getMessageIdLong()).queue(
                msg -> {
                    System.out.println(reaction.getReactionEmote().getEmoji());
                    msg.addReaction(reaction.getReactionEmote().getEmoji()).queue();
                }, throwable -> {
                    throwable.printStackTrace();
                }
        );
         */
    }
}
