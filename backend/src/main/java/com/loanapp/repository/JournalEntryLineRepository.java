package com.loanapp.repository;
import com.loanapp.model.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine,Long>{List<JournalEntryLine> findByEntry(JournalEntry entry);}
