package apple.discord.clover.discord.command.activity.base.player;

import apple.discord.clover.api.base.request.TimeResolution;

public record InactivePlayerQueryPlaytime(TimeResolution resolution, int termsAfter) {

}
