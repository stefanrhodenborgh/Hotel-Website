package nl.srhodenborgh.royalfruitresorts.model;

import jakarta.persistence.*;

@Entity
public class Setting {
    @Id
    private String name;
    @Column(nullable = false)
    private int value;
    private String description;


    public Setting() {
    }

    public Setting(String name, int value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
