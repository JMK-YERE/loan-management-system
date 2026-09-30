package com.loanapp.repository;
import com.loanapp.model.JournalEntry; import org.springframework.data.jpa.repository.JpaRepository; import java.util.Optional;
public interface JournalEntryRepository extends JpaRepository<JournalEntry,Long>{Optional<JournalEntry> findByReference(String reference);}
