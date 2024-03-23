package net.crossager.spotifyforspigot;

import net.crossager.tactical.api.music.TacticalMusicPlayer;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class SpotifyProfile {
    private final Player player;
    private TacticalMusicPlayer musicPlayer;
    private SpotifySong song;
    private boolean isLooping = false;

    public SpotifyProfile(Player player) {
        this.player = player;
    }

    public void pauseSwitch() {
        if (musicPlayer == null) return;
        if (musicPlayer.isStopped()) return;
        if (musicPlayer.isPlaying())
            musicPlayer.pause();
        else
            musicPlayer.resume();
    }

    public void play(SpotifySong song) {
        this.song = song;
        if (musicPlayer != null && !musicPlayer.isStopped()) musicPlayer.stop();
        musicPlayer = song.sequence().playFor(player, SoundCategory.MASTER);
        musicPlayer.onEnd(p -> {
            if (isLooping) play(song);
        });
    }

    public SpotifySong song() {
        return song;
    }

    public float progress() {
        if (musicPlayer == null) return 0;
        return (float) musicPlayer.currentTick() / musicPlayer.tickLength();
    }

    public boolean isPaused() {
        if (musicPlayer == null) return true;
        return !musicPlayer.isPlaying();
    }

    public void setProgress(float progress) {
        if (musicPlayer == null) return;
        if (musicPlayer.isStopped()) return;
        musicPlayer.currentTick((int) (musicPlayer.tickLength() * progress));
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    public boolean isLooping() {
        return isLooping;
    }
}
