package ducnv.com.service.impl;

import ducnv.com.entity.Customer;
import ducnv.com.entity.security.Role;
import ducnv.com.repository.CustomerRepository;
import ducnv.com.service.CustomerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
class CustomerServiceImplTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findAll() {
        Customer customer1 = Customer.builder()
                .id(14L)
                .address("ha Noi1")
                .mobile("033242182")
                .name("Duc")
                .password("123123")
                .username("Ducnv3").build();

        Customer customer2 = Customer.builder()
                .id(15L)
                .address("ha Noi2")
                .mobile("0332123182")
                .name("Duc")
                .password("123123")
                .username("Ducnv3").build();

        List<Customer> customers = new ArrayList<>();
        customers.add(customer2);
        customers.add(customer1);
        Mockito.when(this.customerRepository.findAll()).thenReturn(customers);
        Assertions.assertThat(this.customerService.findAll()).isEqualTo(customers);
    }

    @Test
    void createUser() {
        Customer customer = Customer.builder()
                .id(15L)
                .address("ha Noi")
                .mobile("033242182")
                .name("Duc")
                .password("123123")
                .username("Ducnv3").build();
        Mockito.when(this.customerRepository.save(customer)).thenReturn(customer);
        Assertions.assertThat(this.customerService.addCustomer(customer)).isEqualTo(customer);
    }



    @Test
    void updateCustomer() {
        Customer customer = Customer.builder()
                .id(15L)
                .address("ha Noi")
                .mobile("033242182")
                .name("Duc")
                .password("123123")
                .username("Ducnv3").build();
        Mockito.when(this.customerRepository.findById(15L)).thenReturn(Optional.ofNullable(customer));
        customer.setName("AAAAAA");
        Mockito.when(this.customerRepository.save(customer)).thenReturn(customer);
        Assertions.assertThat(this.customerService.updateCustomer(15L, customer)).isEqualTo(customer);
    }

    @Test
    void testDelete() {
        Customer customer = Customer.builder()
                .id(15L)
                .address("ha Noi")
                .mobile("033242182")
                .name("Duc")
                .password("123123")
                .username("Ducnv3").build();
        Mockito.when(this.customerRepository.findById(15L)).thenReturn(Optional.of(customer));
        Mockito.when(this.customerRepository.existsById(customer.getId())).thenReturn(false);
        assertFalse(this.customerRepository.existsById(customer.getId()));
    }
}