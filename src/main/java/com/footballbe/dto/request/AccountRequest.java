package com.footballbe.dto.request;

import com.footballbe.entity.Account;
import lombok.Data;

import java.util.List;

@Data
public class AccountRequest {
    List<Account> accounts;
}
