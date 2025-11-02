package com.abhi.financialsystem.config;

import com.abhi.financialsystem.model.OperationType;
import com.abhi.financialsystem.repository.OperationTypeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class OperationTypeInitializer {

    private final OperationTypeRepository repo;

    public OperationTypeInitializer(OperationTypeRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    public void seed() {
        if (repo.count() == 0) {
            repo.save(new OperationType(1L, "PURCHASE"));
            repo.save(new OperationType(2L, "INSTALLMENT PURCHASE"));
            repo.save(new OperationType(3L, "WITHDRAWAL"));
            repo.save(new OperationType(4L, "PAYMENT"));
            System.out.println("✅ Operation types seeded");
        }
    }
}

