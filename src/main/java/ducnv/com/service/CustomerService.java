package ducnv.com.service;

import ducnv.com.dto.CustomerDTO;
import ducnv.com.entity.Customer;
import ducnv.com.entity.security.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface CustomerService {
    Page<CustomerDTO> findAll(Pageable pageable);

    public Customer createUser(Customer user, Set<UserRole> userRoles) throws Exception;

    List<Customer> findAll();

    List<CustomerDTO> findAllCustomer();

    CustomerDTO getCustomer(Long id);

    Customer addCustomer(Customer customer);

    Customer updateCustomer(Long id, Customer customer);

    void delete(Long id);
}
