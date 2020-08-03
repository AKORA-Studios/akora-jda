package modules.music;

import main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import utilitys.*;
import utilitys.Module;

import java.util.ArrayList;
import java.util.Arrays;

public class Join extends Module {

    public Join() {
        super("Join", new ArrayList<String>(Arrays.asList(new String[]{"join", "j"})),
                "join", "Der Bot betriit deinen Voice Channel",
                new ArrayList<Permission>(Arrays.asList()),
                ModuleType.MUSIC
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedGenerator generator = getEmbedGenerator();
        EmbedBuilder emb = generator.newEmbed(msg.getAuthor());

        Member member = msg.getMember();
        TextChannel text_channel = msg.getTextChannel();

        AudioManager audioManager = msg.getGuild().getAudioManager();

        if (audioManager.isConnected()) {
            emb.setTitle("Error").setDescription("Ich bin schon in einem Anderem Voice Channel").setColor(Colors.ERROR.color());
            text_channel.sendMessage(emb.build()).queue();
            return;
        }

        GuildVoiceState memberVoiceState = msg.getMember().getVoiceState();

        if (!member.getVoiceState().inVoiceChannel()) {
            emb.setTitle("Error").setDescription("Du musst in einem Voice Channel sein").setColor(Colors.ERROR.color());
            text_channel.sendMessage(emb.build()).queue();
            return;
        }

        VoiceChannel voiceChannel = memberVoiceState.getChannel();
        Member selfMember = msg.getGuild().getSelfMember();

        if (!selfMember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
            emb.setTitle("Error").setDescription("Ich darf dem Voice Channel nicht beitreten").setColor(Colors.ERROR.color());
            text_channel.sendMessage(emb.build()).queue();
            return;
        }

        audioManager.openAudioConnection(voiceChannel);

        emb.setTitle("Beitreten...").setDescription("Akora befindet sich nun in `"+voiceChannel.getName()+"`").setColor(Colors.SUCCESS.color());
        text_channel.sendMessage(emb.build()).queue();
    }
}
