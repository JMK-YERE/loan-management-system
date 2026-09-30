package com.loanapp.repository;
import com.loanapp.model.Loan;
import com.loanapp.model.RepaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
public interface RepaymentScheduleRepository extends JpaRepository<RepaymentSchedule,Long>{
 List<RepaymentSchedule> findByLoanOrderByInstallmentNumberAsc(Loan loan);
 List<RepaymentSchedule> findByStatusAndDueDateBefore(RepaymentSchedule.ScheduleStatus status,LocalDate date);
}