package apple.discord.clover.api.character.response;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CharacterStatsListResponse {

    public Instant requestedStart;
    public Instant requestedEnd;

    public List<CharacterStats> characters = new ArrayList<>();

    public CharacterStatsListResponse(Instant start, Instant end) {
        this.requestedStart = start;
        this.requestedEnd = end;
    }

    public void addCharacter(CharacterStats character) {
        this.characters.add(character);
    }
}
