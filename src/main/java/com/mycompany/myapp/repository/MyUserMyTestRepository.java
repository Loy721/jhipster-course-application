package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MyUserMyTest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MyUserMyTest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MyUserMyTestRepository extends JpaRepository<MyUserMyTest, Long> {}
