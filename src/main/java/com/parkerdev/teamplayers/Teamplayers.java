package com.parkerdev.teamplayers;

import com.google.gson.JsonElement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class Teamplayers extends JavaPlugin implements Listener {
    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Teamplayers has been enabled!");
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
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
}
