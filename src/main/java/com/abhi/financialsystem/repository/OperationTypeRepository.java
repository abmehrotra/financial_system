package com.abhi.financialsystem.repository;

import com.abhi.financialsystem.model.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationTypeRepository extends JpaRepository<OperationType, Long> { }
