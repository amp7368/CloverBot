package apple.discord.clover.database.query.raid;

import apple.discord.clover.api.character.raid.request.CharacterRaidRequest;
import apple.discord.clover.api.character.raid.response.PlayerRaidListResponse;
import apple.discord.clover.api.character.raid.response.term.PlayerRaidTerm;
import apple.discord.clover.database.player.run.query.QDRaidRun;
import apple.discord.clover.database.query.character.CharacterQuery;
import java.util.List;
import java.util.UUID;

public class RaidRunQuery {


    public static PlayerRaidListResponse raidRunQuery(CharacterRaidRequest request) {
        PlayerRaidListResponse response = new PlayerRaidListResponse(request.start(), request.end());

        List<UUID> characterIds = CharacterQuery.queryCharacterIds(request.getPlayer().uuid());
        List<String> raidNames = queryRaidNames();

        List<PlayerRaidTerm> terms = RaidRunTermsQuery.raidRunQueryTerms(request);
        response.setTerms(terms);
        response.setStart(RaidRunSnapshotQuery.queryRaidsSnapshot(request, characterIds, raidNames, true));
        response.setLast(RaidRunSnapshotQuery.queryRaidsSnapshot(request, characterIds, raidNames, false));
        return response;
    }


    private static List<String> queryRaidNames() {
        return new QDRaidRun().select(QDRaidRun.alias().name).setDistinct(true).findSingleAttributeList();
    }
}
