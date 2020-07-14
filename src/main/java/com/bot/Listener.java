package com.bot;

import com.bot.command.CommandManager;
import com.bot.command.commands.game.XPManager;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/*
*   @class Listener
*   Contains handler functions for events such as:
*   Startup, Messages, Guild join, and VC join
 */
public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();
    private final XPManager xpSystem = new XPManager(1);

    // Called on startup when the Bot is ready to receive commands
    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    /*
    *   This handles all messages received in the specified or default text channel.
    *   If the messages have the set prefix, they will be interpreted as commands and
    *   handled in CommandManager.
     */
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        String prefix = Config.get("prefix");
        String raw = event.getMessage().getContentRaw();
        String channel = event.getChannel().getId();

        // Validate set channel id
        //if (!channel.equals(Config.get("CHANNEL_ID"))){
        //    return;
        //}

        // Prevent webhooks from triggering a command more than once
        if (user.isBot() || event.isWebhookMessage()){
            return;
        }

        // Shutdown command
        if (raw.equalsIgnoreCase(prefix + "shutdown")
                && user.getId().equals(Config.get("owner_id"))){
            LOGGER.info("Shutting down");
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());

            return;
        }

        // Handle command
        if (raw.startsWith(prefix)){
            manager.handle(event);
        }
    }

    /*
    *   Whenever a user joins a voice channel, they will start accumulating xp.
    *   This function checks whether the user can earn xp, then starts their timer in XpSystem
     */
    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event){
        Member member = event.getMember();

        if (xpSystem.canGetXp(member)){

            // Make sure new players are added
            if (xpSystem.newPlayer(member)){
                xpSystem.initializePlayer(member);
            }

            // start xp gains
            LOGGER.info(member.getEffectiveName() + " is now gaining xp.");
            xpSystem.startTimer(member);

            // Testing
            LOGGER.info("" + xpSystem.getPlayerXp(member));
        }
    }
}
