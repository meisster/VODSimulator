package netflix.simulation.entities;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.io.Serializable;

public class Person extends RecursiveTreeObject<Person> implements Serializable {

    private String ID;
    private boolean allowed = true;

    private Video watching;

    public Person() {
    }

    public Person(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void kill() {
        this.allowed = false;
    }

    public boolean isAllowed() {
        return allowed;
    }

}
