package apple.discord.clover.api.base;

import java.math.BigDecimal;

public class WynnMath {

    public static long playtime(long wynnMinutes) {
        return BigDecimal.valueOf(wynnMinutes).multiply(BigDecimal.valueOf(4.7)).longValue();
    }
}
