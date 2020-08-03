package modules.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.player.event.TrackEndEvent;
import com.sedmelluq.discord.lavaplayer.player.event.TrackStartEvent;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import main.Bot;
import utilitys.AudioPlayerSendHandler;
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

public class Play extends Module {

    public Play() {
        super("Play", new ArrayList<String>(Arrays.asList(new String[]{"play", "p"})),
                "play [Link]", "Spielt Musik in deinem Voice channel ab",
                new ArrayList<Permission>(Arrays.asList()),
                ModuleType.MUSIC
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        EmbedGenerator generator = getEmbedGenerator();
        EmbedBuilder emb = generator.newEmbed(msg.getAuthor());
        Member selfMember = msg.getGuild().getSelfMember();

        Member member = msg.getMember();
        TextChannel channel = msg.getTextChannel();



        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioPlayer player = playerManager.createPlayer();

        AudioManager audioManager = msg.getGuild().getAudioManager();
        audioManager.setSendingHandler(new AudioPlayerSendHandler(player));

        if (args.length < 1) {
            emb.setTitle("Fehler").setDescription("Du musst einen Link angeben").setColor(Colors.ERROR.color());
            channel.sendMessage(emb.build()).complete();
            return;
        } else if (!args[0].startsWith("https://")) {
            emb.setTitle("Fehler").setDescription("Du musst einen gültigen Link angeben").setColor(Colors.ERROR.color());
            channel.sendMessage(emb.build()).complete();
            return;
        }

        if (audioManager.isConnected()) {
            emb.setTitle("Fehler").setDescription("Ich bin schon in einem Anderem Voice Channel").setColor(Colors.ERROR.color());
            channel.sendMessage(emb.build()).complete();
            return;
        }

        VoiceChannel voiceChannel = msg.getMember().getVoiceState().getChannel();
        //int bitrate = voiceChannel.getBitrate();

        if (!member.getVoiceState().inVoiceChannel()) {
            emb.setTitle("Fehler").setDescription("Du musst in einem Voice Channel sein").setColor(Colors.ERROR.color());
            channel.sendMessage(emb.build()).queue();
            return;
        }

        audioManager.openAudioConnection(voiceChannel);

        player.setVolume(10);
        player.addListener(new AudioEventListener() {
            @Override
            public void onEvent(AudioEvent audioEvent) {
                if (audioEvent instanceof TrackEndEvent) {
                    audioManager.closeAudioConnection();

                    emb.setTitle("Song zuende").setColor(Colors.SUCCESS.color());
                    channel.sendMessage(emb.build()).queue();
                } else if (audioEvent instanceof TrackStartEvent) {
                    TrackStartEvent event = ((TrackStartEvent) audioEvent);
                    AudioTrack track = event.track;

                    emb.setTitle(event.track.getInfo().title)
                            .addField("Author", track.getInfo().author, true)
                            .addField("Länge", "`" + (int) Math.floor(track.getDuration() / 3600000L % 24) + ":"
                                    + (int) Math.floor(track.getDuration() / 60000L % 60) + ":"
                                    + (int) Math.floor(track.getDuration() / 1000L % 60) + "`", true)
                            .setColor(Colors.SUCCESS.color());
                    channel.sendMessage(emb.build()).queue();
                }
            }
        });

        playerManager.loadItem(args[0], new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                player.playTrack(track);
                selfMember.mute(false);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                String lieder = "";
                Long duration = 0L;

                for (AudioTrack track : playlist.getTracks()) {
                    lieder += "`" + track.getInfo().title + "`, ";
                    duration += track.getDuration();
                }
                lieder = lieder.substring(0, lieder.length() - 2);

                player.playTrack(playlist.getTracks().get(0));

                int hours = (int) Math.floor(duration / 3600000L % 24);
                int minutes = (int) Math.floor(duration / 60000L % 60);
                int seconds = (int) Math.floor(duration / 1000L % 60);

                emb.setTitle(playlist.getName())
                        .addField("Titel", lieder, false)
                        .addField("Länge", "`" + hours + ":" + minutes + ":" + seconds + "`", true)
                        .addField("Aktiv", playlist.getTracks().get(0).getInfo().title, true)
                        .setColor(Colors.SUCCESS.color());

                channel.sendMessage(emb.build()).queue();
            }

            @Override
            public void noMatches() {
                emb.setTitle("Fehler").setDescription("Titel nicht gefunden").setColor(Colors.ERROR.color());
                channel.sendMessage(emb.build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException throwable) {
                emb.setTitle("Fehler").setDescription(throwable.getMessage()).setColor(Colors.ERROR.color());
                channel.sendMessage(emb.build()).queue();
                throw throwable;
            }
        });
    }
}