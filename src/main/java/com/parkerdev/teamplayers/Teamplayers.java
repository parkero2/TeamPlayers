package com.parkerdev.teamplayers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.JsonParser;

public final class Teamplayers extends JavaPlugin implements Listener {

    public Teamplayers() throws FileNotFoundException {
        System.out.println("NO CONFIG FOUND.");
    }

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

    //Parse /TeamPlayers/playerdata.json
    Object obj = JsonParser.parseReader(new FileReader( getDataFolder() + "\\playerData.json"));

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Get the player's name
        Player player = event.getPlayer();
        String UUID = event.getPlayer().getUniqueId().toString();
        Bukkit.broadcastMessage(UUID + " has joined the server!");
    }
}
