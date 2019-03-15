package netflix.simulation.subscription;

import java.math.BigDecimal;

public class FamilySubscription extends Subscription {

    private static FamilySubscription INSTANCE;

    private FamilySubscription(SubscriptionType type, BigDecimal price) {
        super(type, price);
    }

    public static FamilySubscription getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FamilySubscription(SubscriptionType.FAMILY, BigDecimal.valueOf(20));
        }
        return INSTANCE;
    }
}
