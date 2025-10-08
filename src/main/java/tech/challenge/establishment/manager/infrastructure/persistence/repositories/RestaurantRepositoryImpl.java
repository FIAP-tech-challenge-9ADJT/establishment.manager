package tech.challenge.establishment.manager.infrastructure.persistence.repositories;

import org.springframework.stereotype.Repository;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.valueobjects.Name;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantAddressJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.mappers.RestaurantJpaMapper;
import tech.challenge.establishment.manager.infrastructure.persistence.mappers.RestaurantAddressJpaMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;

    public RestaurantRepositoryImpl(RestaurantJpaRepository restaurantJpaRepository) {
        this.restaurantJpaRepository = restaurantJpaRepository;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantJpaEntity existingEntity = null;
        if (restaurant.getId() != null) {
            existingEntity = restaurantJpaRepository.findById(restaurant.getId().value()).orElse(null);
        }
        
        var jpaEntity = RestaurantJpaMapper.toJpaEntity(restaurant, existingEntity);
        var savedEntity = restaurantJpaRepository.save(jpaEntity);
        return RestaurantJpaMapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Restaurant> findById(RestaurantId id) {
        return restaurantJpaRepository.findById(id.value())
                .map(RestaurantJpaMapper::toDomainEntity);
    }

    @Override
    public boolean existsByNameAndRestaurantAddress(Name name, RestaurantAddress restaurantAddress) {
        RestaurantAddressJpaEntity restaurantAddressJpaEntity = RestaurantAddressJpaMapper.toJpaEntity(restaurantAddress);
        return restaurantJpaRepository.existsByNameAndRestaurantAddress(name.value(), restaurantAddressJpaEntity);
    }

    @Override
    public Optional<Restaurant> findByName(Name name) {
        return restaurantJpaRepository.findByName(name.value())
                .stream()
                .findFirst()
                .map(RestaurantJpaMapper::toDomainEntity);
    }

    @Override
    public List<Restaurant> findAll() {
        return restaurantJpaRepository.findAll()
                .stream()
                .map(RestaurantJpaMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(RestaurantId id) {
        return restaurantJpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(RestaurantId id) {
        restaurantJpaRepository.deleteById(id.value());
    }

    @Override
    public List<Restaurant> findByNameContainingIgnoreCase(String name) {
        return restaurantJpaRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(RestaurantJpaMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Restaurant> findByNameAndRestaurantAddress(Name name, RestaurantAddress restaurantAddress) {
        RestaurantAddressJpaEntity restaurantAddressJpaEntity = RestaurantAddressJpaMapper.toJpaEntity(restaurantAddress);
        return restaurantJpaRepository.findByNameAndRestaurantAddress(name.value(), restaurantAddressJpaEntity)
                .stream()
                .findFirst()
                .map(RestaurantJpaMapper::toDomainEntity);
    }
}
