package EventsAndCommands.UtilityCommands;

import EventsAndCommands.Categories;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class QuoteWtfCommand extends Command {

    private String mention;
    private String argsTrimmed;
    MessageChannel quoteChannel;


    public QuoteWtfCommand() {
        this.name = "quotewtf";
        this.help = ">quotewtf [message_id] to quote a message";
        this.category = Categories.Utility;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        String args = commandEvent.getArgs();
        String messageId = args;
        boolean isFromDifferentChannel = false;
        final boolean hasAMentionedChannel = !commandEvent.getMessage().getMentionedChannels().isEmpty();

        if (hasAMentionedChannel) {
            quoteChannel = commandEvent.getMessage().getMentionedChannels().get(0);
            mention = commandEvent.getMessage().getMentionedChannels().get(0).getAsMention();
            messageId = args.replaceAll(mention, "").strip();

        } else {
            quoteChannel = commandEvent.getChannel();
        }


        quoteChannel.getMessageById(messageId).queue(x -> {

            final OffsetDateTime creationTime = x.getCreationTime();
            final Member member = x.getMember();


            EmbedBuilder embed = new EmbedBuilder();


            embed.setDescription(x.getContentRaw() + "\n \n [[Jump to message]](" + x.getJumpUrl() + ")  ");
            embed.appendDescription("Creation time:  " + creationTime.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE) + "\n[Quoted by " + commandEvent.getMember().getAsMention() + "]");
            embed.setColor(Color.orange);


            try {
                boolean hasImage = x.getAttachments().get(0).isImage();
                if (hasImage) {
                    embed.setImage(x.getAttachments().get(0).getUrl());
                }
            } catch (Exception e) {
                // Nothing, is not a picture
            }
            embed.setAuthor(member.getEffectiveName(), null, x.getAuthor().getEffectiveAvatarUrl());
            embed.setImage("https://i.imgur.com/z7by0Bo.jpg");

            commandEvent.reply(embed.build());
            commandEvent.getMessage().delete().queueAfter(1, TimeUnit.SECONDS);


        });


    }


}
