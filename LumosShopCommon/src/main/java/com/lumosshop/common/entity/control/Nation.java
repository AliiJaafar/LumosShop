package com.lumosshop.common.entity.control;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "nation")
public class Nation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 65)
    private String name;

    @Column(nullable = false, length = 10)
    private String code;

    @OneToMany(mappedBy = "nation")
    private Set<City> cities;


    public Nation() {
    }

    public Nation(Integer id) {
        this.id = id;
    }

    public Nation(String name) {
        this.name = name;
    }

    public Nation(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Nation(String name, String code, Set<City> cities) {
        this.name = name;
        this.code = code;
        this.cities = cities;
    }

    public Nation(Integer id, String name, String code, Set<City> cities) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.cities = cities;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
