package it.BioShip.GameLibrary.controller;


import it.BioShip.GameLibrary.payload.request.SigninRequest;
import it.BioShip.GameLibrary.payload.request.SignupRequest;
import it.BioShip.GameLibrary.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;



    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest signupRequest)
    {
        return authenticationService.signup(signupRequest);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody @Valid SigninRequest signinRequest) throws Exception
    {
        return authenticationService.signin(signinRequest);
    }




}
