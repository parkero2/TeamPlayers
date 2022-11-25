package com.parkerdev.teamplayers;

import com.google.gson.JsonElement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import com.parkerdev.teamplayers.DiscordWebhook;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public final class Teamplayers extends JavaPlugin implements Listener {
    FileConfiguration config = getConfig();
    private static final String webhook = "https://discord.com/api/webhooks/1045616741339910204/9T2JtxruXKsuQFYa2T5NjaOrvdQrIfqLia1Mv5FGpb9RLB1pzu6VRHkYKHlr4R1qqpOY";
    DiscordWebhook webhook1 = new DiscordWebhook(webhook);
    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Teamplayers has been enabled!");
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        webhook1.setContent("The server has started!");
        try {
            webhook1.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.webhook1 = new DiscordWebhook(webhook);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Teamplayers has been disabled!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Get the player's name
        Player player = event.getPlayer();
        String UUID = event.getPlayer().getUniqueId().toString();
        Bukkit.broadcastMessage(UUID + " has joined the server!");
        if (player.getScoreboard().getPlayerTeam(player) != null) {
            return;
        }
        if (config.get(UUID) == null) {
            player.kickPlayer("You are not on a team! Please contact an admin to get on a team.");
            return;
        }
        //Player is not in team but does have a color assigned
        //convert config.get(UUID) to instance of ChatColor
        ChatColor color = ChatColor.valueOf(config.get(UUID).toString());
        //Get the scoreboard
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Team t = manager.getMainScoreboard().registerNewTeam(player.getName());
        t.setAllowFriendlyFire(false);
        t.setCanSeeFriendlyInvisibles(true);
        t.setColor(color);
        t.addEntry(player.getName());
    }

    @EventHandler
    //player death
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        //Get killer
        Entity killer = player.getKiller();
        if (killer == null) {
            return;
        }
        //Get the scoreboard
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        //Remove player from their current team
        Team t = manager.getMainScoreboard().getEntryTeam(player.getName());
        manager.getMainScoreboard().getTeam(t.getName()).removeEntry(player.getName());
        //Add player to killer's team
        Team t2 = manager.getMainScoreboard().getEntryTeam(killer.getName());
        manager.getMainScoreboard().getTeam(t2.getName()).addEntry(player.getName());
        Bukkit.broadcastMessage(player.getName() + " has been killed by " + killer.getName() + "! They are now on team " + t2.getName() + "!");
        webhook1.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Player Death")
                .setDescription(player.getName() + " has been killed by " + killer.getName() + "! They are now on team " + t2.getName() + "!")
                .setColor(Color.RED)
                .setFooter("Teamplayers", null));
        try {
            webhook1.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.webhook1 = new DiscordWebhook(webhook);
    }
}
