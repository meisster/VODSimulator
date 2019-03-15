package netflix.simulation.subscription;

import java.math.BigDecimal;

public class BasicSubscription extends Subscription {

    private static BasicSubscription INSTANCE;

    private BasicSubscription(SubscriptionType type, BigDecimal price) {
        super(type, price);
    }

    public static BasicSubscription getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BasicSubscription(SubscriptionType.BASIC, BigDecimal.valueOf(10));
        }
        return INSTANCE;
    }
}
