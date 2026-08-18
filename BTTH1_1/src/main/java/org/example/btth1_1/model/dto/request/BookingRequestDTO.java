package org.example.btth1_1.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class BookingRequestDTO {
    @NotBlank(message = "tên khách hàng")
    private String customerName ;
    @NotNull(message = "số lượng khách")
    @Min(value = 1, message = "số lượng khách phải từ 1 người")
    private Integer numberOfPeople;
    @NotNull(message = "ngày nhận phòng")
    private LocalDate checkInDate;
    @NotNull(message = "ngày trả phòng")
    private LocalDate checkOutDate;
    @NotNull(message = "ngân sách dự kiến")
    @DecimalMin(value = "0.0", message = "ngân sách phải lớn hơn hoặc bằng 0")
    private Double budget;
}
