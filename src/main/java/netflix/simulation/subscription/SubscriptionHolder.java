package netflix.simulation.subscription;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Getter
public class SubscriptionHolder {

    private static SubscriptionHolder INSTANCE;
    final Subscription basicSubscription = BasicSubscription.getInstance();
    final Subscription premiumSubscription = PremiumSubscription.getInstance();
    final Subscription familySubscription = FamilySubscription.getInstance();
    final Subscription noneSubscription = NoneSubscription.getInstance();
    final List<Subscription> subscriptionList = Arrays.asList(basicSubscription, premiumSubscription,
            familySubscription, noneSubscription);

    private SubscriptionHolder() {
    }

    public static SubscriptionHolder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SubscriptionHolder();
        }
        return INSTANCE;
    }

    public Subscription getRandomSubscription() {
        return subscriptionList.get(new Random().nextInt(4));
    }

}
