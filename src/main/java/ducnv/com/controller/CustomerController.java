package ducnv.com.controller;

import ducnv.com.dto.CustomerDTO;
import ducnv.com.entity.Customer;
import ducnv.com.entity.security.Role;
import ducnv.com.entity.security.UserRole;
import ducnv.com.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/customer")
@Slf4j
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/")
    public Page<CustomerDTO> findAll(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        log.info("Request All Customer. PageNumber: {}, PageSize: {}", pageNumber, pageSize);
        return this.customerService.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @GetMapping("/{customerId}")
    public CustomerDTO findById(@PathVariable Long customerId) {
        return this.customerService.getCustomer(customerId);
    }

    @PutMapping("/")
    public ResponseEntity<?> create(@RequestBody Customer customer) throws Exception {
        customer.setPassword(this.bCryptPasswordEncoder.encode(customer.getPassword()));
        Set<UserRole> roleSet = new HashSet<>();
        Role role = new Role();
        role.setRoleId(45L);
        role.setRoleName("NORMAL");
        UserRole userRole = new UserRole();
        userRole.setCustomer(customer);
        userRole.setRole(role);
        roleSet.add(userRole);
        this.customerService.createUser(customer, roleSet);
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return ResponseEntity.ok(this.customerService.findAllCustomer());
    }

//    @PutMapping("/")
//    public ResponseEntity<?> create(@RequestBody Customer customer) {
//        this.customerService.addCustomer(customer);
//        CustomerDTO customerDTO = new CustomerDTO();
//        BeanUtils.copyProperties(customer, customerDTO);
//        return ResponseEntity.ok(this.customerService.findAllCustomer());
//    }

    @PostMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> create(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO) {
        Customer customerRequest = modelMapper.map(customerDTO, Customer.class);
        Customer customer = this.customerService.updateCustomer(customerId, customerRequest);
        CustomerDTO customerResponse = modelMapper.map(customer, CustomerDTO.class);
        return ResponseEntity.ok().body(customerResponse);
    }

    @DeleteMapping("/{customerId}")
    public void delete(@PathVariable("customerId") Long customerId) {
        this.customerService.delete(customerId);
    }

    @GetMapping("/ok")
    public List<Customer> findAll() {
        return this.customerService.findAll();
    }
}
