package apple.discord.clover.database.primitive;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IncrementalString {

    @Column
    public int last;
    @Column
    public int next;
}
