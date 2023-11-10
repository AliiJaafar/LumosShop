package com.dgpad.controlCenter;

import com.lumosshop.common.entity.control.City;
import com.lumosshop.common.entity.control.Nation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CityRestController {

    @Autowired
    private CityRepository cityRepository;

    @GetMapping("/controls/listCities/{id}")
    public List<City> listAllByCertainNation(@PathVariable("id") Integer nationId) {
        List<City> cityList = cityRepository.findByNationOrderByNameAsc(new Nation(nationId));

        return cityList;
    }

}
