package it.BioShip.GameLibrary.repository;

import it.BioShip.GameLibrary.entity.Game;
import it.BioShip.GameLibrary.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatformRepository extends JpaRepository<Platform,Long>
{
    boolean existsByName(String name);

    List<Platform> findAllByOrderByNameAsc();
}
