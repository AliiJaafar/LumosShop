package com.dgpad.admin.user;

import com.lumosshop.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends CrudRepository<User, Integer>, PagingAndSortingRepository<User, Integer>  {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User getUserByEmail(@Param("email") String email);

    public Long countById(Integer id);

    @Query("select u " +
            "from User u " +
            "where concat(u.id,' ',u.email,' ',u.firstName,' ',u.lastName,' ') like %?1%")
    public Page<User> findAll(String keyword, Pageable pageable);
    @Modifying
    @Query("update User u set u.enabled = ?2 where u.id = ?1")
    public void updateEnableStatus(Integer id, boolean status);

}
