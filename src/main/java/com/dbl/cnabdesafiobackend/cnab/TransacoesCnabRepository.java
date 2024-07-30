package com.dbl.cnabdesafiobackend.cnab;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacoesCnabRepository extends JpaRepository<TransacoesCnab, Long> {
}
