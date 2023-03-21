package apple.discord.clover.api.character.response;

import apple.discord.clover.database.character.DCharacter;
import java.util.List;
import java.util.UUID;

public class CharacterStats {

    public UUID id;
    public String type;
    public List<CharacterTerm> terms;
    public CharacterSnapshot startingSnapshot;
    public CharacterSnapshot endingSnapshot;

    public CharacterStats(UUID id) {
        this.id = id;
    }

    public void setTerms(List<CharacterTerm> terms) {
        this.terms = terms;
    }

    public void setFirst(DCharacter first) {
        this.type = first.type;
        this.startingSnapshot = new CharacterSnapshot(first, true);
    }

    public void setLast(DCharacter last) {
        this.endingSnapshot = new CharacterSnapshot(last, false);
    }

}
