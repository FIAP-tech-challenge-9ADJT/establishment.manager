package tech.challenge.establishment.manager.infrastructure.persistence.repositories;

import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.repositories.RestaurantAddressRepository;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantAddressId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.infrastructure.persistence.mappers.RestaurantAddressJpaMapper;

import java.util.Optional;

public class RestaurantAddressRepositoryImpl implements RestaurantAddressRepository {

    private final RestaurantAddressJpaRepository restaurantAddressJpaRepository;

    public RestaurantAddressRepositoryImpl(RestaurantAddressJpaRepository restaurantAddressJpaRepository) {
        this.restaurantAddressJpaRepository = restaurantAddressJpaRepository;
    }

    @Override
    public Optional<RestaurantAddress> findById(RestaurantAddressId id) {
        return restaurantAddressJpaRepository.findById(id.value())
                .map(RestaurantAddressJpaMapper::toDomainEntity);
    }

    @Override
    public Optional<RestaurantAddress> findByRestaurantId(RestaurantId restaurantId) {
        return restaurantAddressJpaRepository.findByRestaurantId(restaurantId.value())
                .map(RestaurantAddressJpaMapper::toDomainEntity);
    }

    @Override
    public RestaurantAddress save(RestaurantAddress restaurantAddress) {
        var jpaEntity = RestaurantAddressJpaMapper.toJpaEntity(restaurantAddress);
        var savedEntity = restaurantAddressJpaRepository.save(jpaEntity);
        return RestaurantAddressJpaMapper.toDomainEntity(savedEntity);
    }

    @Override
    public void delete(RestaurantAddressId id) {
        restaurantAddressJpaRepository.deleteById(id.value());
    }
}
