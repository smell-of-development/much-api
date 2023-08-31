package much.api.repository;

import much.api.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Set<Tag> findAllByNameIn(Set<String> tags);

}
