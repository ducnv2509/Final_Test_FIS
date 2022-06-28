package ducnv.com.ex;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuantityProductException extends RuntimeException{
    private String message;
}
