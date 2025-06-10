package com.banking.core_banking.infra.mapper.account;

import com.banking.core_banking.domain.model.dto.account.response.AccountDetailsResponse;
import com.banking.core_banking.domain.model.dto.account.response.AccountSummaryResponse;
import com.banking.core_banking.domain.model.entities.account.Account;
import com.banking.core_banking.domain.model.entities.user.AccountManager;
import com.banking.core_banking.infra.mapper.user.CustomerMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {
        TransactionMapper.class,
        CustomerMapper.class
})
public interface AccountMapper {
    AccountSummaryResponse toSummaryDto(Account account);

    @Mapping(source = "transactions", target = "recentTransactions")
    @Mapping(source = "customer", target = "customer", qualifiedByName = "toSummaryDto") // Correção 1
    @Mapping(source = "accountManager", target = "accountManagerName", qualifiedByName = "managerToFullName") // Correção 2
    AccountDetailsResponse toDetailsDto(Account account);

    @Named("managerToFullName")
    default String managerToFullName(AccountManager manager) {
        if (manager == null || manager.getEmployee() == null) {
            return null;
        }
        return manager.getEmployee().getFirstName() + " " + manager.getEmployee().getLastName();
    }
}
