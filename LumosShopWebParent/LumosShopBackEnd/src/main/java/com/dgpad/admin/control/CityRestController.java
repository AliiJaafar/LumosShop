package com.dgpad.admin.control;

import com.lumosshop.common.entity.control.City;
import com.lumosshop.common.entity.control.Nation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CityRestController {

    @Autowired
    private CityRepository cityRepository;

    @GetMapping("/city/list/{id}")
    public List<City> listAllByCertainNation(@PathVariable("id") Integer nationId) {
        List<City> cityList = cityRepository.findByNationOrderByNameAsc(new Nation(nationId));

        return cityList;
    }

    @PostMapping("/city/store")
    public String store(@RequestBody City city) {
        City savedOne = cityRepository.save(city);
        return String.valueOf(savedOne.getId());
    }
    @DeleteMapping("/city/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        cityRepository.deleteById(id);
    }
}
