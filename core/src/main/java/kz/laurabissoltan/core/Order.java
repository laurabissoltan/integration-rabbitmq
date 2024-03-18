package kz.laurabissoltan.core;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="'Order'")
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private Double amount;
    private String email;
    private boolean paid;
    private boolean created;
}
