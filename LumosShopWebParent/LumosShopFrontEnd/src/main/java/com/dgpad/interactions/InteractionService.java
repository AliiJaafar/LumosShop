package com.dgpad.interactions;

import com.lumosshop.common.entity.interactions.Interaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InteractionService {

    @Autowired
    private InteractionRepository interactionRepository;


    public List<Interaction> getAllInteractions() {
        return interactionRepository.findAll();
    }


}
