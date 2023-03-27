package apple.discord.clover.database.query.character;

import apple.discord.clover.api.character.term.request.CharacterRequest;
import apple.discord.clover.api.character.term.response.CharacterStats;
import apple.discord.clover.api.character.term.response.CharacterStatsListResponse;
import apple.discord.clover.database.character.query.QDCharacter;
import java.util.List;
import java.util.UUID;

public class CharacterQuery {

    public static CharacterStatsListResponse queryCharacters(CharacterRequest request) {
        CharacterStatsListResponse response = new CharacterStatsListResponse(request.start(), request.end());
        List<UUID> characters = CharacterQuery.queryCharacterIds(request.player);
        for (UUID character : characters) {
            response.addCharacter(queryOneCharacter(request, character));
        }
        return response;
    }

    private static CharacterStats queryOneCharacter(CharacterRequest request, UUID id) {
        CharacterStats character = new CharacterStats(id);
        character.setTerms(CharacterTermsQuery.queryCharacterTerms(request, id));
        character.setFirst(CharacterSnapshotQuery.queryCharacterFirst(request, id));
        character.setLast(CharacterSnapshotQuery.queryCharacterLast(request, id));
        return character;
    }

    public static List<UUID> queryCharacterIds(UUID playerId) {
        QDCharacter a = QDCharacter.alias();
        return new QDCharacter()
            .select(a.characterId)
            .setDistinct(true)
            .where().session.player.uuid.eq(playerId)
            .findSingleAttributeList();
    }
}
