package net.crossager.spotifyforspigot;

import net.crossager.tactical.api.gui.inventory.ItemUtils;
import net.crossager.tactical.api.gui.inventory.components.TacticalGUIContainer;
import net.crossager.tactical.api.gui.inventory.components.TacticalStaticGUIComponent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class GUIs {
    private final SpotifyForSpigot plugin;

    public GUIs(SpotifyForSpigot plugin) {
        this.plugin = plugin;
    }

    public TacticalGUIContainer getStatusContainer() {
        TacticalGUIContainer statusContainer = TacticalGUIContainer.create(7, 4).setComponent(1, 1, TacticalStaticGUIComponent.of(player -> {
            if (plugin.getProfile(player).isPaused()) {
                return ItemUtils.setName(new ItemStack(Material.RED_CONCRETE), "§cResume");
            } else {
                return ItemUtils.setName(new ItemStack(Material.GREEN_CONCRETE), "§aPause");
            }
        }).onClick(click -> {
            plugin.getProfile(click.player()).pauseSwitch();
            click.gui().updateDisplay(click.player());
        })).setComponent(3, 1, TacticalStaticGUIComponent.of(player -> { // song display
            SpotifyProfile profile = plugin.getProfile(player);
            if (profile.song() == null) return ItemUtils.setName(new ItemStack(Material.BARRIER), "§cNo song playing");
            return ItemUtils.setLore(ItemUtils.setName(new ItemStack(Material.ENCHANTED_BOOK),
                    "§6" + profile.song().name()),
                    List.of(
                            "§9" + profile.song().sequence().tickLength() + " ticks",
                            "§8" + profile.song().file(),
                            "§8" + profile.song().sequence().bpm() + " BPM"
                    ));
        })).setComponent(5, 1, TacticalStaticGUIComponent.of(player -> { // loop button
            SpotifyProfile profile = plugin.getProfile(player);
            if (profile.isLooping()) {
                return ItemUtils.setName(new ItemStack(Material.REDSTONE_TORCH), "§aLooping on");
            } else {
                return ItemUtils.setName(new ItemStack(Material.LEVER), "§cLooping off");
            }
        }).onClick(click -> {
            SpotifyProfile profile = plugin.getProfile(click.player());
            profile.setLooping(!profile.isLooping());
            click.gui().updateDisplay(click.player());
        }));
        // create progress bar
        for (int i = 0; i < 7; i++) {
            int index = i;
            statusContainer.setComponent(i, 3, TacticalStaticGUIComponent.of(player -> {
                SpotifyProfile profile = plugin.getProfile(player);
                String progressString = new StringBuilder().append("§a").append("|".repeat(80)).insert((int) (profile.progress() * 79 + 2), "§8").toString();
                Material material = profile.progress() > (index / 7F) ? Material.GREEN_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE;
                return ItemUtils.setName(new ItemStack(material), progressString);
            }).onClick(click -> { // make the progress bar clickable
                SpotifyProfile profile = plugin.getProfile(click.player());
                profile.setProgress(index / 7F);
            }));
        }
        return statusContainer;
    }

    public TacticalGUIContainer getSongContainer() {
        AtomicInteger page = new AtomicInteger(0);
        AtomicReference<List<SpotifySong>> songs = new AtomicReference<>(plugin.getSongs().subList(Math.min(plugin.getSongs().size(), 21 * page.get()), Math.min(plugin.getSongs().size(), page.get() * 21 + 21)));
        TacticalGUIContainer songContainer = TacticalGUIContainer.create(7, 4);
        songContainer.setComponent(2, 3, TacticalStaticGUIComponent.of(
                        ItemUtils.setName(new ItemStack(Material.ARROW), "§aPrevious Page")
                ).onClick(click -> {
                    if (page.get() == 0) return;
                    page.addAndGet(-1);
                    songs.set(plugin.getSongs().subList(Math.min(plugin.getSongs().size(), 21 * page.get()), Math.min(plugin.getSongs().size(), page.get() * 21 + 21)));
                    click.gui().updateDisplay(click.player());
                }))
                .setComponent(4, 3, TacticalStaticGUIComponent.of(
                        ItemUtils.setName(new ItemStack(Material.ARROW), "§aNext Page")
                ).onClick(click -> {
                    page.addAndGet(1);
                    songs.set(plugin.getSongs().subList(Math.min(plugin.getSongs().size(), 21 * page.get()), Math.min(plugin.getSongs().size(), page.get() * 21 + 21)));
                    click.gui().updateDisplay(click.player());
                }));
        for (int i = 0; i < 21; i++) {
            int index = i;
            songContainer.setComponent(i % 7, i / 7, TacticalStaticGUIComponent.of(player -> {
                        if (index >= songs.get().size()) return ItemUtils.AIR;
                        SpotifySong song = songs.get().get(index);
                        return ItemUtils.setLore(ItemUtils.setName(new ItemStack(Material.BOOK),
                                        "§6" + song.name()),
                                List.of(
                                        "§9" + song.sequence().tickLength() + " ticks",
                                        "§8" + song.file(),
                                        "§8" + song.sequence().bpm() + " BPM"
                                ));
                    }
                    ).onClick(click -> {
                        if (index >= songs.get().size()) return;
                        SpotifyProfile profile = plugin.getProfile(click.player());
                        SpotifySong song = songs.get().get(index);
                        profile.play(song);
                        click.player().sendMessage("§aPlaying " + song.name());
                    }));
        }
        return songContainer;
    }
}
