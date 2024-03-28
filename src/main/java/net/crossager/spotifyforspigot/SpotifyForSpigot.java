package net.crossager.spotifyforspigot;

import net.crossager.tactical.api.TacticalCommands;
import net.crossager.tactical.api.TacticalMusic;
import net.crossager.tactical.api.commands.TacticalCommand;
import net.crossager.tactical.api.gui.inventory.*;
import net.crossager.tactical.api.gui.inventory.components.TacticalStaticGUIComponent;
import net.crossager.tactical.api.music.TacticalMidiParsingOptions;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class SpotifyForSpigot extends JavaPlugin {
    private final Map<Player, SpotifyProfile> spotifyProfiles = new HashMap<>();
    private final List<SpotifySong> songs = new ArrayList<>();
    private GUIs guis;

    @Override
    public void onEnable() {
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                loadSongs();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        guis = new GUIs(this);
        TacticalCommands.getInstance().registerCommand(
                TacticalCommand.create(this, "spotify").options().playerOnly(true).command().commandExecutor(command -> {
                    TacticalInventoryGUI.create(5, "Spotify")
                            .createBorder(TacticalStaticGUIComponent.of(ItemUtils.setName(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " ")))
                            .setComponent(1, 0, TacticalStaticGUIComponent.of(ItemUtils.setName(new ItemStack(Material.OAK_SIGN), "§eStatus page")).onClick(click -> {
                                click.gui().setComponent(1, 1, guis.getStatusContainer());
                                click.gui().updateDisplay(click.player());
                            }))
                            .setComponent(2, 0, TacticalStaticGUIComponent.of(ItemUtils.setName(new ItemStack(Material.BOOK), "§eSong page")).onClick(click -> {
                                click.gui().setComponent(1, 1, guis.getSongContainer());
                                click.gui().updateDisplay(click.player());
                            }))
                            .setComponent(1, 1, guis.getStatusContainer())
                            .addAnimationArea(2, 1, 4, 8, 4)
                            .open(command.playerSender());
                })
        );
        TacticalCommands.getInstance().registerCommand(
                TacticalCommand.create(this, "spotifyreload").options().permission("spotify.admin").command().commandExecutor(command -> {
                    command.playerSender().sendMessage("§eReloading...");
                    songs.clear();
                    getServer().getScheduler().runTaskAsynchronously(this, () -> {
                        try {
                            loadSongs();
                            command.playerSender().sendMessage("§aSongs reloaded!");
                        } catch (Exception e) {
                            e.printStackTrace();
                            command.playerSender().sendMessage("§cError!");
                        }
                    });
                })
        );
    }

    private void loadSongs() throws IOException {
        getDataFolder().mkdirs();
        for (File file : Objects.requireNonNull(getDataFolder().listFiles())) {
            if (file.isDirectory()) continue;
            String fileName = file.getName().split("\\.")[0];
            if (file.getName().endsWith(".mid")) {
                songs.add(new SpotifySong(fileName, "Midi", TacticalMusic.getInstance().loadFromMidiFile(file.toPath(), TacticalMidiParsingOptions.createFromDefault())));
            }
            if (file.getName().endsWith(".json")) {
                songs.add(new SpotifySong(fileName, "Json", TacticalMusic.getInstance().loadFromJsonFile(file.toPath())));
            }
        }
    }

    public List<SpotifySong> getSongs() {
        return songs;
    }

    public SpotifyProfile getProfile(Player player) {
        return spotifyProfiles.computeIfAbsent(player, p -> new SpotifyProfile(player));
    }

    @Override
    public void onDisable() {

    }
}
