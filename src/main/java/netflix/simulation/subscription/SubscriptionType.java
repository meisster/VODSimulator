package netflix.simulation.subscription;

import lombok.Getter;

@Getter
public enum SubscriptionType {

    BASIC("720x720", 1), FAMILY("1024x1024", 3), PREMIUM("1920x1920", 4), NONE("0x0", 0);

    private String resolution;
    private int availableDevices;

    SubscriptionType(String resolution, int availableDevices) {
        this.resolution = resolution;
        this.availableDevices = availableDevices;
    }

}

