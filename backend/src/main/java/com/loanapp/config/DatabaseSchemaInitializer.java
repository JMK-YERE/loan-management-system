package com.loanapp.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * Small production-safe compatibility migration for databases created by older
 * versions of the application. It is intentionally idempotent.
 */
@Component
public class DatabaseSchemaInitializer implements ApplicationRunner {
    private final JdbcTemplate jdbc;

    public DatabaseSchemaInitializer(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(ApplicationArguments args) {
        jdbc.execute("ALTER TABLE repayment_schedules ADD COLUMN IF NOT EXISTS last_reminder_at TIMESTAMP");
        jdbc.execute("CREATE TABLE IF NOT EXISTS password_reset_tokens (id BIGSERIAL PRIMARY KEY, token VARCHAR(100) NOT NULL UNIQUE, user_id BIGINT NOT NULL REFERENCES users(id), expiry_date TIMESTAMP NOT NULL, used BOOLEAN NOT NULL DEFAULT FALSE, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
        jdbc.execute("CREATE INDEX IF NOT EXISTS idx_password_reset_tokens_user_id ON password_reset_tokens(user_id)");
        jdbc.execute("CREATE INDEX IF NOT EXISTS idx_password_reset_tokens_expiry ON password_reset_tokens(expiry_date)");
        jdbc.execute("ALTER TABLE loan_applications ADD COLUMN IF NOT EXISTS duration INTEGER");
        jdbc.execute("ALTER TABLE loan_applications ADD COLUMN IF NOT EXISTS duration_unit VARCHAR(20)");
        jdbc.execute("ALTER TABLE loan_applications ADD COLUMN IF NOT EXISTS interest_snapshot NUMERIC(15,2) DEFAULT 0");
        jdbc.execute("ALTER TABLE loan_applications ADD COLUMN IF NOT EXISTS processing_fee_snapshot NUMERIC(15,2) DEFAULT 0");
        jdbc.execute("ALTER TABLE loan_applications ADD COLUMN IF NOT EXISTS late_fee_snapshot NUMERIC(15,2) DEFAULT 0");
        jdbc.execute("ALTER TABLE loan_applications ADD COLUMN IF NOT EXISTS total_repayment_snapshot NUMERIC(15,2) DEFAULT 0");
        jdbc.execute("ALTER TABLE loan_applications ADD COLUMN IF NOT EXISTS installment_amount_snapshot NUMERIC(15,2) DEFAULT 0");
        jdbc.execute("ALTER TABLE loan_applications ADD COLUMN IF NOT EXISTS installment_count_snapshot INTEGER DEFAULT 1");
        jdbc.execute("ALTER TABLE loan_applications ADD COLUMN IF NOT EXISTS grace_period_days_snapshot INTEGER DEFAULT 0");
        jdbc.execute("ALTER TABLE loan_applications ADD COLUMN IF NOT EXISTS interest_type_snapshot VARCHAR(30)");
        jdbc.execute("ALTER TABLE loan_applications ADD COLUMN IF NOT EXISTS repayment_frequency_snapshot VARCHAR(30)");
        jdbc.execute("ALTER TABLE loan_applications ADD COLUMN IF NOT EXISTS terms_version VARCHAR(100)");
        jdbc.execute("ALTER TABLE loan_applications ADD COLUMN IF NOT EXISTS terms_accepted BOOLEAN DEFAULT FALSE");
        jdbc.execute("ALTER TABLE loan_applications ADD COLUMN IF NOT EXISTS terms_accepted_at TIMESTAMP");
        jdbc.execute("ALTER TABLE announcements ADD COLUMN IF NOT EXISTS image_url VARCHAR(500)");
        jdbc.execute("ALTER TABLE guarantors ADD COLUMN IF NOT EXISTS remote_token_hash VARCHAR(128)");
        jdbc.execute("ALTER TABLE guarantors ADD COLUMN IF NOT EXISTS remote_expires_at TIMESTAMP");
        jdbc.execute("ALTER TABLE guarantors ADD COLUMN IF NOT EXISTS remote_signed_at TIMESTAMP");
        jdbc.execute("ALTER TABLE guarantors ADD COLUMN IF NOT EXISTS remote_signature_data TEXT");
        jdbc.execute("ALTER TABLE signatures ADD COLUMN IF NOT EXISTS signature_hash VARCHAR(128)");
        jdbc.execute("ALTER TABLE signatures ADD COLUMN IF NOT EXISTS agreement_hash VARCHAR(128)");
        jdbc.execute("ALTER TABLE signatures ADD COLUMN IF NOT EXISTS consent_hash VARCHAR(128)");
        jdbc.execute("ALTER TABLE signatures ADD COLUMN IF NOT EXISTS consent_version VARCHAR(100)");
        jdbc.execute("ALTER TABLE signatures ADD COLUMN IF NOT EXISTS signed_by_name VARCHAR(200)");
        jdbc.execute("ALTER TABLE signatures ADD COLUMN IF NOT EXISTS signed_by_email VARCHAR(200)");
        jdbc.execute("ALTER TABLE signatures ADD COLUMN IF NOT EXISTS consent_accepted BOOLEAN DEFAULT FALSE");
        jdbc.execute("ALTER TABLE signatures ALTER COLUMN user_id DROP NOT NULL");
    }
}
