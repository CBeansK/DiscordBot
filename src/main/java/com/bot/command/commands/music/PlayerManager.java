package com.bot.command.commands.music;

import com.bot.command.commands.music.lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    // Used to get manager for handling status (playing, pausing, etc...)
    public synchronized GuildMusicManager getGuildMusicManager(Guild guild){
        long guildId = guild.getIdLong();

        // GuildMusicManager is simply a holder for a player and scheduler
        GuildMusicManager musicManager = musicManagers.get(guildId);

        // create new manager for guild if one doesn't exist
        if (musicManager == null){
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }


        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(TextChannel channel, List<String> trackArgs) {
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());

        // ytsearch: first last
        // https://link
        String trackUrl;
        // if not a link, build a new track
        if (!trackArgs.get(0).contains("https://")){

            // Build a search query
            StringBuilder trackUrlBuilder = new StringBuilder();

            trackUrlBuilder.append("ytsearch:").append(trackArgs.get(0));

            for (int i = 1; i < trackArgs.size(); i++){
                trackUrlBuilder.append(trackArgs.get(i)).append(" ");
            }

            trackUrl = trackUrlBuilder.toString().trim();
        } else{
            trackUrl = trackArgs.get(0);
        }
    playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {


        @Override
        public void trackLoaded(AudioTrack track) {
            channel.sendMessageFormat("Adding to queue: %s", track.getInfo().title).queue();

            play(musicManager, track);

        }
        @Override
        public void playlistLoaded(AudioPlaylist playlist) {
            AudioTrack firstTrack = playlist.getSelectedTrack();

            if (firstTrack == null) {
                firstTrack = playlist.getTracks().get(0);
            }

            channel.sendMessageFormat("Adding to queue: %s", firstTrack.getInfo().title).queue();

            play(musicManager, firstTrack);
        }
        @Override
        public void noMatches() {
            channel.sendMessageFormat("Nothing found matching %s", trackUrl).queue();
        }

        @Override
        public void loadFailed(FriendlyException exception) {
            channel.sendMessageFormat("Could not play: ", exception.getMessage()).queue();
        }
    });
}


    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public void pause(GuildMusicManager musicManager){
        musicManager.player.setPaused(true);
    }

    public void resume(GuildMusicManager musicManager){
        musicManager.player.setPaused(false);
    }

    public void skip(GuildMusicManager musicManager){
        musicManager.scheduler.nextTrack();
    }

    public static synchronized PlayerManager getInstance() {
        if (INSTANCE == null){
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}
