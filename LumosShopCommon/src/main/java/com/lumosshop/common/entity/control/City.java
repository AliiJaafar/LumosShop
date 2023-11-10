package com.lumosshop.common.entity.control;


import jakarta.persistence.*;

@Entity
@Table(name = "city")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 65)
    private String name;

    @ManyToOne
    @JoinColumn(name = "nation_id")
    private Nation nation;

    public City() {
    }

    public City(String name, Nation nation) {
        this.name = name;
        this.nation = nation;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Nation getNation() {
        return nation;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nation=" + nation +
                '}';
    }
}
