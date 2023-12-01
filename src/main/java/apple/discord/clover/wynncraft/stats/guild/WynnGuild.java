package apple.discord.clover.wynncraft.stats.guild;


import apple.discord.clover.wynncraft.WynncraftModule;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class WynnGuild {

    public String name;
    public String prefix;
    public long level;
    public int territories;
    public int wars;
    public Date created;
    protected Map<String, JsonElement> members;
    private transient List<WynnGuildMember> memberList;

    @Override
    public String toString() {
        return "WynnGuild{" +
            "name='" + name + '\'' +
            ", prefix='" + prefix + '\'' +
            ", level=" + level +
            ", created=" + created +
            '}';
    }

    public List<WynnGuildMember> getMembers() {
        return memberList;
    }

    public void initialize() {
        memberList = new ArrayList<>();
        Gson gson = WynncraftModule.gson();
        for (Entry<String, JsonElement> membersByRank : members.entrySet()) {
            String rank = membersByRank.getKey();
            JsonElement unknown = membersByRank.getValue();
            if (!unknown.isJsonObject()) continue;
            addMembersOfRank(unknown.getAsJsonObject(), gson, rank);
        }
        memberList = List.copyOf(memberList);
    }

    private void addMembersOfRank(JsonObject uuidToMember, Gson gson, String rank) {
        for (String uuid : uuidToMember.keySet()) {
            JsonElement memberJson = uuidToMember.get(uuid);
            WynnGuildMember member = gson.fromJson(memberJson, WynnGuildMember.class);
            member.setRank(rank);
            member.setUUID(UUID.fromString(uuid));
            memberList.add(member);
        }
    }
}
