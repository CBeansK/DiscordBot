package com.bot.command.commands;

import com.bot.command.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.menudocs.paste.PasteClient;
import org.menudocs.paste.PasteClientBuilder;
import org.menudocs.paste.PasteHost;

import java.util.List;

public class PasteCommand implements ICommand {
    private final PasteClient client = new PasteClientBuilder()
            .setUserAgent("Bean Discord Bot")
            .setDefaultExpiry("10m")
            .setPasteHost(PasteHost.MENUDOCS) // Optional
            .build();

    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        System.out.println(args.size());
        if (args.size() < 2){
            channel.sendMessage("Missing arguments").queue();
            return;
        }
        System.out.println("paste received");

        // 
        final String language = args.get(0);
        final String contentRaw = ctx.getMessage().getContentRaw();
        final int index = contentRaw.indexOf(language) + language.length();
        final String body = contentRaw.substring(index).trim();

        System.out.println(language);
        System.out.println(body);

        //paste formatting
        client.createPaste(language, body).async(
                (id) -> client.getPaste(id).async(paste -> {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setTitle("Paste " + id, paste.getPasteUrl())
                            .setDescription("```")
                            .appendDescription(paste.getLanguage().getId())
                            .appendDescription("\n")
                            .appendDescription(paste.getBody())
                            .appendDescription("```");

                    channel.sendMessage(builder.build()).queue();
                })
        );

    }

    @Override
    public String getName() {
        return "paste";
    }

    @Override
    public String getHelp() {
        return "Creates a paste on paste.menudocs.org\n" +
                "Usage: `!!paste [language] [text]`";
    }
}
