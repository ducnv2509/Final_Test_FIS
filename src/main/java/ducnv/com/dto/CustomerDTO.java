package ducnv.com.dto;

import com.sun.istack.NotNull;
import ducnv.com.entity.Customer;
import ducnv.com.entity.Order;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerDTO {
    private Long id;

    private String name;

    private String mobile;

    private String address;

    private String username;

    private String password;


    public static class Mapper {
        public static CustomerDTO fromEntity(Customer customer) {
            return CustomerDTO.builder()
                    .id(customer.getId())
                    .name(customer.getName())
                    .mobile(customer.getMobile())
                    .address(customer.getAddress())
                    .username(customer.getUsername())
                    .password(customer.getPassword())
                    .build();
        }

        public static CustomerDTO formCustomerID(Customer customer) {
            return CustomerDTO.builder()
                    .id(customer.getId())
                    .name(customer.getName())
                    .mobile(customer.getMobile())
                    .address(customer.getAddress())
                    .username(customer.getUsername())
                    .build();
        }
    }

}
