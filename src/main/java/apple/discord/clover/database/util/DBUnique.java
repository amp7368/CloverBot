package apple.discord.clover.database.util;

import io.ebean.DB;
import io.ebean.Transaction;
import io.ebean.plugin.Property;
import io.javalin.http.ConflictResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DBUnique {

    public static void checkUniqueness(Transaction transaction, Object... beans) {
        Map<Object, Set<Property>> invalidProperties = new HashMap<>();
        for (Object bean : beans) {
            Set<Property> unique = DB.checkUniqueness(bean, transaction);
            if (!unique.isEmpty())
                invalidProperties.put(bean, unique);
        }
        if (invalidProperties.isEmpty()) return;
        Map<String, String> details = new HashMap<>();
        for (Entry<Object, Set<Property>> beanProperties : invalidProperties.entrySet()) {
            Object bean = beanProperties.getKey();
            Set<Property> properties = beanProperties.getValue();
            for (Property property : properties) {
                details.put("%s.%s".formatted(bean.getClass().getSimpleName(), property.name()),
                    "Value of '%s' is already present.".formatted(property.value(bean)));
            }
        }
        throw new ConflictResponse("Conflict", details);
    }
}
