package utilitys;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.util.Date;

import static main.Bot.jda;

public class EmbedGenerator {

    public static EmbedBuilder newEmbed(User user) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setFooter(jda.getSelfUser().getAsTag(),jda.getSelfUser().getEffectiveAvatarUrl())
                .setAuthor(user.getAsTag(), user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl())
                .setTimestamp(new Date().toInstant());
        return builder;
    }

    public static EmbedBuilder newEmbed(User user, String title, String description) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title)
                .setDescription(description)
                .setFooter(jda.getSelfUser().getAsTag(),jda.getSelfUser().getEffectiveAvatarUrl())
                .setAuthor(user.getAsTag(), user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl())
                .setTimestamp(new Date().toInstant());
        return builder;
    }
}