package apple.discord.clover.database.auth.authentication;

import apple.discord.clover.database.auth.identity.DAuthIdentity;

public interface IsAuthentication {

    DAuthentication getAuthentication();

    default DAuthIdentity getIdentity() {
        return getAuthentication().getIdentity();
    }

}
