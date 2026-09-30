package com.loanapp.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class GeneralLoanSchemaInitializer implements ApplicationRunner {
 private final JdbcTemplate jdbc;
 public GeneralLoanSchemaInitializer(JdbcTemplate jdbc){this.jdbc=jdbc;}
 @Override public void run(ApplicationArguments args){
  jdbc.execute("ALTER TABLE loan_applications ALTER COLUMN product_id DROP NOT NULL");
 }
}
