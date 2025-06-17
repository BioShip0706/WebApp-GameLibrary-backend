package it.BioShip.GameLibrary.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameCardResponse
{
    private long id;
    private String title;
    private String developer;
    private String imageURL;
    private Double score;
}
