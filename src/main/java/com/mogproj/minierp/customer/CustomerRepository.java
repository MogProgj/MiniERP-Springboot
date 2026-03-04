package com.mogproj.minierp.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>,
        JpaSpecificationExecutor<Customer> {

    @Query("SELECT c FROM Customer c WHERE c.id = :id AND c.deletedAt IS NULL")
    Optional<Customer> findActiveById(Long id);

    Page<Customer> findAllByDeletedAtIsNull(Pageable pageable);
}
