package apple.discord.clover.database.query.character;

import apple.discord.clover.api.character.request.CharacterRequest;
import apple.discord.clover.database.character.DCharacter;
import apple.discord.clover.database.character.query.QDCharacter;
import java.util.UUID;

public class CharacterSnapshotQuery {

    public static DCharacter queryCharacterFirst(CharacterRequest request, UUID id) {
        return queryTerm(request, id)
            .orderBy().session.retrievedTime.asc()
            .findOne();
    }

    public static DCharacter queryCharacterLast(CharacterRequest request, UUID id) {
        return queryTerm(request, id)
            .orderBy().session.retrievedTime.desc()
            .findOne();
    }

    private static QDCharacter queryTerm(CharacterRequest request, UUID id) {
        return new QDCharacter()
            .where().and()
            .characterId.eq(id)
            .session.retrievedTime.between(request.startSql(), request.endSql())
            .endAnd()
            .setMaxRows(1);
    }

}
