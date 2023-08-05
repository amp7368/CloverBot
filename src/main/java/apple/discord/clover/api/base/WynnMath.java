package apple.discord.clover.api.base;

import java.math.BigDecimal;

public class WynnMath {

    /**
     * @param wynnMinutes minutes according to Wynn's API
     * @return real minutes according to the world's standards
     */
    public static long playtime(long wynnMinutes) {
        return BigDecimal.valueOf(wynnMinutes).multiply(BigDecimal.valueOf(4.7)).longValue();
    }
}
