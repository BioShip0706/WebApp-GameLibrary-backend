package it.BioShip.GameLibrary.repository;

import it.BioShip.GameLibrary.entity.Genre;
import it.BioShip.GameLibrary.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre,Long>
{

    boolean existsByName(String name);
}
