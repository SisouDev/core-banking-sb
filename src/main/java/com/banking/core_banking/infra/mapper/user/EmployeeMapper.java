package com.banking.core_banking.infra.mapper.user;

import com.banking.core_banking.domain.model.dto.user.request.EmployeeCreateRequest;
import com.banking.core_banking.domain.model.dto.user.request.EmployeeUpdateRequest;
import com.banking.core_banking.domain.model.dto.user.response.EmployeeResponse;
import com.banking.core_banking.domain.model.entities.user.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.role", target = "role")
    EmployeeResponse toDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationCode", ignore = true)
    @Mapping(target = "hireDate", ignore = true)
    @Mapping(target = "user", ignore = true)
    Employee toEntity(EmployeeCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "registrationCode", ignore = true)
    @Mapping(target = "hireDate", ignore = true)
    void updateFromDto(EmployeeUpdateRequest dto, @MappingTarget Employee entity);
}
