package com.ap.homebanking.repositories;

import com.ap.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface LoanRepository extends JpaRepository<Loan,Long> {
}