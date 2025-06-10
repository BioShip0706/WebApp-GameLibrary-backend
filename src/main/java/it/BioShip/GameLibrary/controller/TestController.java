package it.BioShip.GameLibrary.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("Test")
public class TestController
{
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/sayHelloTest")
    public String hello()
    {
        return "Test funzionante, GameLibrary funziona";
    }
}
