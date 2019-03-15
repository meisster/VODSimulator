package netflix.simulation.subscription;

import java.math.BigDecimal;

public class PremiumSubscription extends Subscription {

    private static PremiumSubscription INSTANCE;

    private PremiumSubscription(SubscriptionType type, BigDecimal price) {
        super(type, price);
    }

    public static PremiumSubscription getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PremiumSubscription(SubscriptionType.PREMIUM, BigDecimal.valueOf(30));
        }
        return INSTANCE;
    }
}
