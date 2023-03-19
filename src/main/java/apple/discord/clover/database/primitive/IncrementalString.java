package apple.discord.clover.database.primitive;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IncrementalString {

    @Column
    public String last;
    @Column
    public String next;
}
