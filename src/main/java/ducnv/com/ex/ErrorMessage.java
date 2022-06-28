package ducnv.com.ex;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {
    private String code;
    private String message;
}
