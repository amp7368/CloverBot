package apple.discord.clover.discord.command.bug;

import apple.discord.clover.discord.DiscordConfig;
import apple.discord.clover.discord.DiscordModule;
import apple.discord.clover.discord.util.FindOption;
import discord.util.dcf.slash.DCFSlashCommand;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandBug extends DCFSlashCommand implements FindOption {

    @Override
    public SlashCommandData getData() {
        return Commands.slash("bug", "Submit a bug report")
            .addOption(OptionType.STRING, "description", "A detailed description of the bug and steps to recreate", true)
            .addOption(OptionType.ATTACHMENT, "attachment", "An image of the bug");
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        String description = findOption(event, "description", OptionMapping::getAsString);
        if (description == null) return;
        if (description.length() >= MessageEmbed.DESCRIPTION_MAX_LENGTH) {
            String errorMsg = "Your description must be less than %d characters".formatted(MessageEmbed.DESCRIPTION_MAX_LENGTH);
            event.replyEmbeds(error(errorMsg)).setEphemeral(true).queue();
            return;
        }
        CompletableFuture<InteractionHook> pendingResponse = event.replyEmbeds(success("Bug report pending...")).submit();

        User user = event.getUser();
        EmbedBuilder embed = new EmbedBuilder()
            .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
            .setTimestamp(event.getTimeCreated())
            .setTitle("Bug Report")
            .setDescription(description);

        @Nullable FileUpload fileUpload = getAttachmentFileUpload(event);

        MessageCreateBuilder message = new MessageCreateBuilder();
        if (fileUpload != null) {
            message.addFiles(fileUpload);
        }
        message.addEmbeds(embed.build());
        try {
            DiscordConfig.get().getReportsChannel().sendMessage(message.build()).queue(
                e -> successResponse(description, pendingResponse),
                e -> errorResponse(description, pendingResponse));
        } catch (Exception e) {
            errorResponse(description, pendingResponse);
        }
    }

    private void successResponse(String description, CompletableFuture<InteractionHook> pendingResponse) {
        MessageEmbed response = success()
            .setTitle("Thank you!")
            .setAuthor("Bug report submitted!", null, DiscordModule.dcf.jda().getSelfUser().getEffectiveAvatarUrl())
            .setDescription(description)
            .build();
        replyToPending(pendingResponse, response);
    }

    private void errorResponse(String description, CompletableFuture<InteractionHook> pendingResponse) {
        MessageEmbed response = error()
            .addField("Error when reporting the bug!", "Please contact `appleptr16` directly.", false)
            .setDescription(description)
            .setTitle("Error!")
            .build();
        replyToPending(pendingResponse, response);
    }

    private void replyToPending(CompletableFuture<InteractionHook> pendingResponse, MessageEmbed response) {
        try {
            pendingResponse.get().editOriginalEmbeds(response).queue();
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Nullable
    private FileUpload getAttachmentFileUpload(SlashCommandInteractionEvent event) {
        FileUpload fileUpload;
        try (Attachment attachment = findOption(event, "attachment", OptionMapping::getAsAttachment, false)) {
            if (attachment == null) return null;
            InputStream fileStream = getAttachmentStream(attachment);
            fileUpload = FileUpload.fromData(fileStream, attachment.getFileName());
        }
        return fileUpload;
    }

    @NotNull
    private InputStream getAttachmentStream(@NotNull Attachment attachment) {
        try {
            return attachment.getProxy().download().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
