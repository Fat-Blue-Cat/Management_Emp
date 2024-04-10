package com.ncc.manage_emp.service;

import com.ncc.manage_emp.entity.Accounts;
import org.springframework.data.domain.Page;

public interface AccountService {
    Page<Accounts> getAllAccount(Integer page, Integer size, String email);
    void deleteAllAccount();
}
