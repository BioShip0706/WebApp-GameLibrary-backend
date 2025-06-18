package it.BioShip.GameLibrary.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameSearchResponse
{
    private long id;
    private String title;
    private String developer;
    private LocalDate releaseDate;
    private Double score;
}
