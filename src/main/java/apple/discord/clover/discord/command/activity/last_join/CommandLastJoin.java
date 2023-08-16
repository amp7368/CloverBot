package apple.discord.clover.discord.command.activity.last_join;

import apple.discord.clover.discord.command.activity.GuiInactivity;
import apple.discord.clover.discord.command.activity.base.BaseActivitySubCommand;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandLastJoin extends BaseActivitySubCommand {

    @Override
    public SubcommandData getData() {
        return new SubcommandData("last_join", "Create an activity report containing the last time a player joined")
            .addOptions(GUILD_OPTION);
    }

    @Override
    protected void onProgressCompletion(GuiInactivity gui) {
        gui.addSubPage(new MessageActivityLastJoin(gui));
    }
}
