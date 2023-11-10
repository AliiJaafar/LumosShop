package com.dgpad.admin.control;

import com.lumosshop.common.entity.control.Nation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class NationRestController {
    private final Logger LOGGER = LoggerFactory.getLogger(NationRestController.class);

    @Autowired
    private NationRepository nationRepository;


    @GetMapping("/nations/list")
    public List<Nation> list() {
        return nationRepository.findAllByOrderByNameAsc();
    }

    @PostMapping("/nations/store")
    public String store(@RequestBody Nation nation) {
        Nation targetNation = nationRepository.save(nation);
        return String.valueOf(targetNation.getId());
    }

    @DeleteMapping("/nations/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        nationRepository.deleteById(id);
    }


}
