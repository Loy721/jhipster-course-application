package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MyUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MyUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Long> {}
