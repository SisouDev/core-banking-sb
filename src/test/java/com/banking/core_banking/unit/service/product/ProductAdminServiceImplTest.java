package com.banking.core_banking.unit.service.product;

import com.banking.core_banking.domain.model.dto.product.request.LoanProductCreateRequest;
import com.banking.core_banking.domain.model.dto.product.request.LoanProductUpdateRequest;
import com.banking.core_banking.domain.model.dto.product.response.BankingProductResponse;
import com.banking.core_banking.domain.model.entities.product.BankingProduct;
import com.banking.core_banking.domain.model.entities.product.LoanProduct;
import com.banking.core_banking.domain.repository.product.BankingProductRepository;
import com.banking.core_banking.exceptions.others.BusinessException;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import com.banking.core_banking.infra.mapper.product.ProductMapper;
import com.banking.core_banking.infra.service.product.ProductAdminServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductAdminServiceImplTest {

    @Mock
    private BankingProductRepository bankingProductRepository;
    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductAdminServiceImpl productAdminService;

    @Nested
    @DisplayName("Testes para createLoanProduct()")
    class CreateLoanProductTests {

        @Test
        void whenRequestIsValid_shouldCreateAndReturnProduct() {
            var request = mock(LoanProductCreateRequest.class);
            var newProductEntity = mock(LoanProduct.class);
            var savedProductEntity = mock(LoanProduct.class);
            var responseDto = mock(BankingProductResponse.class);

            when(productMapper.toEntity(request)).thenReturn(newProductEntity);
            when(bankingProductRepository.save(newProductEntity)).thenReturn(savedProductEntity);
            when(productMapper.toDto(savedProductEntity)).thenReturn(responseDto);

            BankingProductResponse result = productAdminService.createLoanProduct(request);

            assertNotNull(result);
            assertEquals(responseDto, result);
            verify(bankingProductRepository, times(1)).save(newProductEntity);
        }
    }

    @Nested
    @DisplayName("Testes para updateLoanProduct()")
    class UpdateLoanProductTests {

        @Test
        void whenProductIsLoanProduct_shouldUpdateSuccessfully() {
            Long productId = 1L;
            var request = mock(LoanProductUpdateRequest.class);
            var loanProductEntity = mock(LoanProduct.class);

            when(bankingProductRepository.findById(productId)).thenReturn(Optional.of(loanProductEntity));

            productAdminService.updateLoanProduct(productId, request);

            verify(productMapper, times(1)).updateFromDto(request, loanProductEntity);
            verify(bankingProductRepository, times(1)).save(loanProductEntity);
        }

        @Test
        void whenProductNotFound_shouldThrowResourceNotFoundException() {
            Long productId = 99L;
            when(bankingProductRepository.findById(productId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> {
                productAdminService.updateLoanProduct(productId, mock(LoanProductUpdateRequest.class));
            });
        }

        @Test
        void whenProductIsNotLoanProduct_shouldThrowBusinessException() {
            Long productId = 1L;
            var notALoanProduct = mock(BankingProduct.class);
            when(bankingProductRepository.findById(productId)).thenReturn(Optional.of(notALoanProduct));

            assertThrows(BusinessException.class, () -> {
                productAdminService.updateLoanProduct(productId, mock(LoanProductUpdateRequest.class));
            });
            verify(productMapper, never()).updateFromDto(any(), any());
        }
    }

    @Nested
    @DisplayName("Testes para getAllProducts()")
    class GetAllProductsTests {

        @Test
        void whenProductsExist_shouldReturnPageOfProducts() {
            var pageable = mock(Pageable.class);
            var productPage = new PageImpl<BankingProduct>(Collections.singletonList(mock(LoanProduct.class)));
            when(bankingProductRepository.findAll(pageable)).thenReturn(productPage);

            Page<BankingProductResponse> result = productAdminService.getAllProducts(pageable);

            assertNotNull(result);
            assertEquals(1, result.getContent().size());
        }
    }
}