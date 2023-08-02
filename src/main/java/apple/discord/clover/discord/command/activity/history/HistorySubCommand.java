package apple.discord.clover.discord.command.activity.history;

import apple.discord.clover.discord.command.activity.GuiInactivity;
import apple.discord.clover.discord.command.activity.base.BaseActivitySubCommand;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class HistorySubCommand extends BaseActivitySubCommand {


    @Override
    public SubcommandData getData() {
        return new SubcommandData("history", "Create an activity report containing recent playtime data")
            .addOptions(GUILD_OPTION);
    }

    @Override
    protected void onProgressCompletion(GuiInactivity gui) {
        throw new RuntimeException("todo");
    }
}
