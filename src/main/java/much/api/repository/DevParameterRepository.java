package much.api.repository;

import much.api.entity.DevParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DevParameterRepository extends JpaRepository<DevParameter, Long> {

    Optional<DevParameter> findByName(String name);

}
