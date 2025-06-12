package com.banking.core_banking.infra.service.user;

import com.banking.core_banking.domain.model.dto.user.request.AddressCreateRequest;
import com.banking.core_banking.domain.model.dto.user.request.BusinessCustomerCreateRequest;
import com.banking.core_banking.domain.model.dto.user.request.PersonalCustomerCreateRequest;
import com.banking.core_banking.domain.model.dto.user.request.UserUpdateRequest;
import com.banking.core_banking.domain.model.dto.user.response.CustomerResponse;
import com.banking.core_banking.domain.model.entities.user.BusinessCustomer;
import com.banking.core_banking.domain.model.entities.user.Customer;
import com.banking.core_banking.domain.model.entities.user.PersonalCustomer;
import com.banking.core_banking.domain.model.entities.user.User;
import com.banking.core_banking.domain.model.utils.Address;
import com.banking.core_banking.domain.repository.user.CustomerRepository;
import com.banking.core_banking.domain.repository.user.UserRepository;
import com.banking.core_banking.domain.service.user.CustomerService;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import com.banking.core_banking.exceptions.user.EmailAlreadyExistsException;
import com.banking.core_banking.infra.mapper.user.CustomerMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CustomerMapper customerMapper
    ) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerMapper = customerMapper;
    }

    @Override
    @Transactional
    public CustomerResponse createBusinessCustomer(BusinessCustomerCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email '" + request.email() + "' is already in use.");
        }
        String hashedPassword = passwordEncoder.encode(request.password());
        User newUser = User.create(request.email(), hashedPassword);

        AddressCreateRequest addressDto = request.address();
        Address address = new Address();
        address.setStreet(addressDto.street());
        address.setNumber(addressDto.number());
        address.setComplement(addressDto.complement());
        address.setNeighborhood(addressDto.neighborhood());
        address.setCity(addressDto.city());
        address.setState(addressDto.state());
        address.setZipCode(addressDto.zipCode());
        address.setCountryCode(addressDto.countryCode());

        BusinessCustomer customer = BusinessCustomer.create(
                newUser,
                address,
                request.phone(),
                request.companyName(),
                request.registrationNumber(),
                request.tradeName()
        );

        BusinessCustomer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerResponse createPersonalCustomer(PersonalCustomerCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email '" + request.email() + "' is already in use.");
        }
        String hashedPassword = passwordEncoder.encode(request.password());
        User newUser = User.create(request.email(), hashedPassword);

        AddressCreateRequest addressDto = request.address();
        Address address = new Address();
        address.setStreet(addressDto.street());
        address.setNumber(addressDto.number());
        address.setComplement(addressDto.complement());
        address.setNeighborhood(addressDto.neighborhood());
        address.setCity(addressDto.city());
        address.setState(addressDto.state());
        address.setZipCode(addressDto.zipCode());
        address.setCountryCode(addressDto.countryCode());


        PersonalCustomer customer = PersonalCustomer.create(
                newUser,
                address,
                request.phone(),
                request.name(),
                request.registrationNumber(),
                request.birthDate()
        );

        PersonalCustomer savedCustomer = customerRepository.save(customer);

        return customerMapper.toDto(savedCustomer);
    }

    @Override
    public CustomerResponse getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        return customerMapper.toDto(customer);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomerProfile(Long customerId, UserUpdateRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        User userToUpdate = customer.getUser();

        if (request.email() != null && !request.email().isBlank()) {
            if (userRepository.existsByEmail(request.email())) {
                throw new EmailAlreadyExistsException("Email '" + request.email() + "' is already in use.");
            }
            userToUpdate.setEmail(request.email());
        }
        return customerMapper.toDto(customer);
    }
}
