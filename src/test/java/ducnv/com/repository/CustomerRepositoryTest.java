package ducnv.com.repository;

import ducnv.com.TestFinalApplication;
import ducnv.com.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = TestFinalApplication.class)
class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void testFindById() {
        Customer customer = getCustomer();
        this.customerRepository.save(customer);
        Customer result = this.customerRepository.findById(customer.getId()).get();
        assertEquals(customer.getId(), result.getId());
    }

    @Test
    public void testSave() {
        Customer customer = getCustomer();
        this.customerRepository.save(customer);
        Customer found = this.customerRepository.findById(customer.getId()).get();
        assertEquals(customer.getId(), found.getId());
    }


    @Test
    public void testDeleteById() {
        Customer customer = getCustomer();
        this.customerRepository.save(customer);
        this.customerRepository.deleteById(customer.getId());
        List<Customer> result = new ArrayList<>(this.customerRepository.findAll());
        assertEquals(result.size(), 1);
    }

    @Test
    public void testFindAll() {
        Customer customer = getCustomer();
        this.customerRepository.save(customer);
        List<Customer> result = new ArrayList<>(this.customerRepository.findAll());
        assertEquals(result.size(), 2);
    }

    private Customer getCustomer() {
        return Customer.builder()
                .address("Ha Noi")
                .mobile("1234567890")
                .name("Duc")
                .password("123123")
                .username("Ducnama")
                .build();
    }
}