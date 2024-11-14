package apple.discord.clover.database;

import apple.discord.clover.api.base.BaseEntity;
import apple.discord.clover.database.query.QDVersion;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "api_version")
public class DVersion extends BaseEntity {

    public static final String UNKNOWN = "unknown";
    private static DVersion currentVersion = null;
    @Id
    public Short id;

    @Column(columnDefinition = "varchar(20)", unique = true)
    public String name;

    public DVersion(String name) {
        this.name = name;
    }

    public synchronized static DVersion version(String version) {
        if (currentVersion != null && currentVersion.name.equalsIgnoreCase(version))
            return currentVersion;

        currentVersion = new QDVersion().where().name.ieq(version).findOne();
        if (currentVersion != null) return currentVersion;

        currentVersion = new DVersion(version);
        currentVersion.save();
        return currentVersion;
    }
}
