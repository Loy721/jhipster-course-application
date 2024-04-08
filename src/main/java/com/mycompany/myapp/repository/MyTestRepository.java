package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MyTest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MyTest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MyTestRepository extends JpaRepository<MyTest, Long> {}
