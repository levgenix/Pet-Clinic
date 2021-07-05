package com.vet24.web.config;

import com.github.database.rider.spring.DBRiderTestExecutionListener;
import org.springframework.core.Ordered;

public class ClinicDBRiderTestExecutionListener extends DBRiderTestExecutionListener {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
