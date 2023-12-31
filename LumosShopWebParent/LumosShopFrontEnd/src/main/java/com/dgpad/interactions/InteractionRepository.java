package com.dgpad.interactions;

import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.interactions.Interaction;
import com.lumosshop.common.entity.interactions.InteractionType;
import com.lumosshop.common.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {

    public List<Interaction> findAllByCustomer(Customer customer);
    public List<Interaction> findAllByProduct(Product product);

    public List<Interaction> findAllByInteractionType(InteractionType interactionType);


    default void saveOrIncrementValue(Interaction interaction) {
        Optional<Interaction> existingInteraction = findByProductIdAndCustomerIdAndInteractionType(
                interaction.getProduct().getId(),
                interaction.getCustomer().getId(),
                interaction.getInteractionType()
        );

        if (existingInteraction.isPresent()) {
            // Increment value of existing interaction
            existingInteraction.get().setValue(existingInteraction.get().getValue() + interaction.getValue());
            save(existingInteraction.get());
        } else {
            // Save new interaction
            save(interaction);
        }
    }

    @Query("SELECT i FROM Interaction i WHERE i.product.id = :productId AND i.customer.id = :customerId AND i.interactionType = :interactionType")
    Optional<Interaction> findByProductIdAndCustomerIdAndInteractionType(Integer productId, Integer customerId, InteractionType interactionType);

}
