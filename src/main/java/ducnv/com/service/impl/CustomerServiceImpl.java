package ducnv.com.service.impl;

import ducnv.com.dto.CustomerDTO;
import ducnv.com.entity.Customer;
import ducnv.com.entity.security.UserRole;
import ducnv.com.ex.CustomerNotFoundException;
import ducnv.com.repository.CustomerRepository;
import ducnv.com.repository.RoleRepository;
import ducnv.com.service.CustomerService;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Page<CustomerDTO> findAll(Pageable pageable) {
        return this.customerRepository.findAll(pageable).map(CustomerDTO.Mapper::fromEntity);
    }

    @Override
    public Customer createUser(Customer user, Set<UserRole> userRoles) throws Exception {
        Customer local = customerRepository.findByUsername(user.getUsername());
        if (local != null) {
            System.out.println("User is already there");
            throw new IllegalArgumentException();
        } else {
            for (UserRole ur : userRoles) {
                roleRepository.save(ur.getRole());
            }
            user.getUserRoles().addAll(userRoles);
            local = this.customerRepository.save(user);
        }
        return local;
    }

    @Override
    public List<Customer> findAll() {
        return this.customerRepository.findAll();
    }

    @Override
    public List<CustomerDTO> findAllCustomer() {
        return this.customerRepository.findAll().stream().map(CustomerDTO.Mapper::formCustomerID).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long id) {
        if (this.customerRepository.findById(id).isEmpty()) {
            throw new CustomerNotFoundException("Customer Not Found: " + id);
        }
        return this.customerRepository.findById(id).map(CustomerDTO.Mapper::formCustomerID).get();
    }

    @Override
    public Customer addCustomer(Customer customer) {
        return this.customerRepository.save(customer);

    }

    @Override
    public Customer updateCustomer(Long id, Customer customerRequest) {
        Customer customer = this.customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer Not Found: " + id));
        customer.setAddress(customerRequest.getAddress());
        customer.setMobile(customerRequest.getMobile());
        return this.customerRepository.save(customer);
    }

    @Override
    public void delete(Long id) {
        if (this.customerRepository.findById(id).isPresent())
            throw new CustomerNotFoundException("Customer Not Found: " + id);
        Customer customer = new Customer();
        customer.setId(id);
        this.customerRepository.delete(customer);
    }
}
