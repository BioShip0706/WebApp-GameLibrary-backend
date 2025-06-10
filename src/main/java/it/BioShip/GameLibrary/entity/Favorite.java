package it.BioShip.GameLibrary.entity;

import it.BioShip.GameLibrary.entity.common.Creation;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Favorite extends Creation
{
    @EmbeddedId
    private FavoriteId favoriteId;
}
