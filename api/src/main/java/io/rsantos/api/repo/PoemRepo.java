package io.rsantos.api.repo;

import io.rsantos.api.entity.Poem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoemRepo extends JpaRepository<Poem, String> {
  Optional<Poem> findById(String id);
  Optional<Poem> findByTitle(String title);
}
w