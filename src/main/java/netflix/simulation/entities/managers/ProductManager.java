package netflix.simulation.entities.managers;

import lombok.Getter;
import netflix.simulation.entities.Video;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class ProductManager implements Serializable {
    private static ProductManager INSTANCE;
    private volatile List<Video> productList = new ArrayList<>();

    public static synchronized ProductManager getInstance() {
        if (INSTANCE == null) {
            return INSTANCE = new ProductManager();
        }
        return INSTANCE;
    }

    /**
     * It doesn't kill products, as they are not running.
     * Name is an effect of keeping code consistent.
     */
    public void killAll() {
        productList = new ArrayList<>();
    }


    /**
     * Adds given product given that it doesn't exist already in the collection
     *
     * @param product the product to be added
     */
    public void addProduct(Video product) {
        if (!productList.contains(product)) {
            productList.add(product);
        } else System.out.println("Product " + product.getTitle() + "already exists...");
    }

    /**
     * Removes product from the list.
     * Doesn't check for existence.
     *
     * @param video the product to be removed
     */
    public void removeFromList(Video video) {
        productList.remove(video);
    }

    /**
     * Gets product by index, checks only for index out of range
     *
     * @param index the index of the item to be extracted
     * @return product if found, null otherwise
     */
    public Video getProductByIndex(int index) {
        if (productList.size() > index) {
            return productList.get(index);
        }
        return null;
    }

    /**
     * Gets product by unique ID
     *
     * @param ID the param by which product is found
     * @return product if found, null otherwise
     */
    public Video getProductByID(String ID) {
        Optional<Video> first = productList.stream().filter(video -> video.getID().equals(ID)).findFirst();
        return first.orElse(null);
    }

    public Video getProductByTitle(String title) {
        Optional<Video> first = productList.
                stream().
                filter(video -> video.getTitle().equals(title)).
                findFirst();
        return first.orElse(null);
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
            INSTANCE = (ProductManager) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
