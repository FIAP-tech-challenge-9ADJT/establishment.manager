package tech.challenge.establishment.manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import tech.challenge.establishment.manager.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByUserId(Long userId);
}
