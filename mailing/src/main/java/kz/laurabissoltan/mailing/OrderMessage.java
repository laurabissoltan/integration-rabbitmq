package kz.laurabissoltan.mailing;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMessage {
    private Long orderId;
    private String email;
    private Boolean status;
}