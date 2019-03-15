package netflix.simulation.entities.managers;

import lombok.Data;
import netflix.simulation.entities.Distributor;
import netflix.simulation.entities.Person;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class DistributorManager implements Serializable {
    private static DistributorManager INSTANCE;
    private volatile List<Distributor> distributors = new ArrayList<>();
    private int productCount;

    public static synchronized DistributorManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DistributorManager();
        }
        return INSTANCE;
    }

    /**
     * Invokes start method for each distributor instance
     */
    public void startAll() {
        distributors.forEach(Distributor::startThread);
    }

    /**
     * Invokes kill method for each distributor instance
     * then creates new empty list
     */
    public void killAll() {
        distributors.forEach(Person::kill);
        distributors = new ArrayList<>();
    }

    /**
     * Removes given distributor from list
     * then kills it to prevent it becoming an orphan and running with no pointer to it
     *
     * @param distributor distributor to be removed
     */
    public void removeFromList(Distributor distributor) {
        distributors.remove(distributor);
        distributor.kill();
    }

    /**
     * Creates a new distributor with generic name
     * adds to the distributor collection and serializes to prevent data loss
     */
    public void newDistributor() {
        distributors.add(new Distributor(productCount));
        this.serialize("runDistributors.bin");
    }

    /**
     * @return a random distributor with index number from 0 to distributors.size()
     */
    public Distributor getRandomDistributor() {
        return distributors.get(new Random().nextInt(distributors.size()));
    }


    public void serialize(String path) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(path));
            output.writeObject(INSTANCE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deserialize(String path) {
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(path));
            INSTANCE = (DistributorManager) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
        public void showDistributors() {
        distributors.forEach(System.out::println);
    }
     */
}
