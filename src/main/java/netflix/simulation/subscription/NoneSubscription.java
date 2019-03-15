package netflix.simulation.subscription;

import java.math.BigDecimal;

public class NoneSubscription extends Subscription {

    private static NoneSubscription INSTANCE;

    private NoneSubscription(SubscriptionType type, BigDecimal price) {
        super(type, price);
    }

    public static NoneSubscription getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoneSubscription(SubscriptionType.NONE, BigDecimal.ZERO);
        }
        return INSTANCE;
    }
}
