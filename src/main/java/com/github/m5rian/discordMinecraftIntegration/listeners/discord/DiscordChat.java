package com.github.m5rian.discordMinecraftIntegration.listeners.discord;

import com.github.m5rian.discordMinecraftIntegration.DiscordBridge;
import com.github.m5rian.discordMinecraftIntegration.utils.Config;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;

public class DiscordChat extends ListenerAdapter {
    private final Config config;

    public DiscordChat(DiscordBridge discordBridge) {
        this.config = discordBridge.getConfiguration();
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        try {
            if (filter(event)) return;

            final String message = event.getMessage().getContentDisplay(); // Get message
            final String username = event.getAuthor().getName(); // Get username

            if (message.toLowerCase().startsWith(this.config.getString(""))) return; // Message is command
            Bukkit.getConsoleSender().sendMessage(message.toLowerCase());
            Bukkit.getConsoleSender().sendMessage(this.config.getString("prefix"));

            final TextComponent msg = PlainTextComponentSerializer.plainText().deserialize(String.format("<%s> %s", username, message));
            Bukkit.getServer().sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean filter(MessageReceivedEvent event) {
        if (event.getGuild().getIdLong() != this.config.getLong("discord.guildId")) return true;
        if (event.getChannel().getIdLong() != this.config.getLong("discord.channelId")) return true;

        if (!event.isFromGuild()) return true;
        if (event.getMessage().isWebhookMessage()) return true; // Message is webhook
        if (event.getAuthor().isBot()) return true;
        if (event.getMessage().getContentDisplay().isBlank()) return true;
        else return false;
    }
}
