package com.bot;

import com.bot.command.CommandManager;
import com.bot.command.commands.game.XPSystem;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();
    private final XPSystem xpSystem = new XPSystem();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        String prefix = Config.get("prefix");
        String raw = event.getMessage().getContentRaw();
        String channel = event.getChannel().getId();

        if (!channel.equals(Config.get("CHANNEL_ID"))){
            return;
        }

        if (user.isBot() || event.isWebhookMessage()){
            return;
        }

        if (raw.equalsIgnoreCase(prefix + "shutdown")
                && user.getId().equals(Config.get("owner_id"))){
            LOGGER.info("Shutting down");
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());

            return;
        }

        if (raw.startsWith(prefix)){
            manager.handle(event);
        }
    }

    // Called whenever a user enters a voice channel
    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event){
        if (xpSystem.canGetXp(event.getMember())){
            LOGGER.info(event.getMember().getNickname() + "is now gaining xp.");
            xpSystem.startTimer(event.getMember());

            // Testing
            LOGGER.info("" + xpSystem.getPlayerXp(event.getMember()));
        }
    }
}
