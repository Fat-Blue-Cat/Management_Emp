package com.ncc.manage_emp.service.impl;

import com.ncc.manage_emp.entity.Accounts;
import com.ncc.manage_emp.repository.AccountRepository;
import com.ncc.manage_emp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;


    @Override
    public Page<Accounts> getAllAccount(Integer page, Integer size, String email) {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Accounts> accountsList = new PageImpl<>(new ArrayList<>());
        if (email != null) {
            accountsList= accountRepository.findByEmailContaining(email,pageable);
        }else{
            accountsList =  accountRepository.findAll(pageable);
        }



        return new PageImpl<>(accountsList.getContent(), pageable, accountsList.getTotalElements());
    }

    public void deleteAllAccount() {
        accountRepository.deleteAll();
    }
}
