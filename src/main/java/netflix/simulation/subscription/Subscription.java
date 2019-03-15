package netflix.simulation.subscription;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public abstract class Subscription implements Serializable {

    private SubscriptionType type;

    private BigDecimal price = BigDecimal.ZERO;

    public void setPrice(int price) {
        this.price = BigDecimal.valueOf(price);
    }
}
