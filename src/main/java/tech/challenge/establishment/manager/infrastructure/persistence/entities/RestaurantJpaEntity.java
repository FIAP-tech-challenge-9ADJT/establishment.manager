package tech.challenge.establishment.manager.infrastructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToOne(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, optional = false)
    @JsonManagedReference
    private RestaurantAddressJpaEntity restaurantAddress;
    @Column(name = "kitchen_type", nullable = false)
    private String kitchenType;
    @Column(name = "start_operation", nullable = false)
    private LocalTime startOperation;
    @Column(name = "end_operation", nullable = false)
    private LocalTime endOperation;
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;
}
