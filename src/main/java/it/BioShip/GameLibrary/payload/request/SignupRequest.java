package it.BioShip.GameLibrary.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    @NotBlank @Size(min = 4, max = 15)
    private String username;

    @NotBlank @Size(min = 1, max = 50)
    private String name;

    @NotBlank @Size(min = 1, max = 50)
    private String surname;

    @Email @Size(max = 254)
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9]{8,20}$",
            message="La password può contenere solo caratteri maiuscoli e minuscoli e numeri. La lunghezza deve essere compresa tra 6 e 10 caratteri")
    private String password;

}
