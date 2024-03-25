package nl.srhodenborgh.royalfruitresorts.model;

import jakarta.persistence.*;

@Entity
public class Setting {
    @Id
    private String name;
    @Column(nullable = false)
    private int value;


    public Setting() {
    }

    public Setting(String name, int value) {
        this.name = name;
        this.value = value;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
