package DTOs;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDTO {
    private String username;
    private double latitude;
    private double longitude;
}
