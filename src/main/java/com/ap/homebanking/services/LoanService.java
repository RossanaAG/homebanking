package com.ap.homebanking.services;

import com.ap.homebanking.dtos.LoanDTO;
import com.ap.homebanking.models.Loan;

import java.util.List;
import java.util.Set;

public interface LoanService {

    List<Loan> findAll();

    Set<LoanDTO> convertToLoanDTO(List<Loan> loans);

    Loan findById(Long id);

    void saveLoan(Loan loan);
}