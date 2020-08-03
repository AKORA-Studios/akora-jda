package modules.music;

import main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import utilitys.Colors;
import utilitys.EmbedGenerator;
import utilitys.Module;
import utilitys.ModuleType;

import java.util.ArrayList;
import java.util.Arrays;

public class Leave extends Module {

    public Leave() {
        super("Leave", new ArrayList<String>(Arrays.asList(new String[]{"leave", "l"})),
                "leave", "Verlässt deinen Voice Channel",
                new ArrayList<Permission>(Arrays.asList()),
                ModuleType.MUSIC
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedGenerator generator = getEmbedGenerator();
        EmbedBuilder emb = generator.newEmbed(msg.getAuthor());

        Member member = msg.getMember();
        TextChannel channel = msg.getTextChannel();

        AudioManager audioManager = msg.getGuild().getAudioManager();

        if (!audioManager.isConnected()) {
            emb.setTitle("Fehler").setDescription("Ich bin in keinem Voice Channel").setColor(Colors.ERROR.color());
            channel.sendMessage(emb.build()).queue();
            return;
        }

        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if (!voiceChannel.getMembers().contains(msg.getMember())) {
            emb.setTitle("Fehler").setDescription("Du musst in dem Voice Channel sein um diesen Befehl ausführen zu können").setColor(Colors.ERROR.color());
            channel.sendMessage(emb.build()).queue();
            return;
        }

        audioManager.closeAudioConnection();
        emb.setTitle("Verlassen...").setDescription("Akora hat den Voice Channel `"+voiceChannel.getName()+"` verlassen").setColor(Colors.SUCCESS.color());
        channel.sendMessage(emb.build()).queue();
        return;
    }
}
