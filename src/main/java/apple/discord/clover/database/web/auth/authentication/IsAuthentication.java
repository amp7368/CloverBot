package apple.discord.clover.database.web.auth.authentication;

import apple.discord.clover.database.web.auth.identity.DAuthIdentity;

public interface IsAuthentication {

    DAuthentication getAuthentication();

    default DAuthIdentity getIdentity() {
        return getAuthentication().getIdentity();
    }

}
