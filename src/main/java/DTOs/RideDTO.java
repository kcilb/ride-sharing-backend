package DTOs;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RideDTO {
    public BigDecimal latitude;
    public BigDecimal longitude;
}
