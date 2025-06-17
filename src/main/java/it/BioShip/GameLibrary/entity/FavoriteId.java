package it.BioShip.GameLibrary.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class FavoriteId
{
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public FavoriteId(Game game, User user) {
        this.game = game;
        this.user = user;
    }
}
