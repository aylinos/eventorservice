package com.eventor.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

//    Log out

//    @Autowired
//    JwtUtils jwtUtils;
//@PostMapping("/logout")
//@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
//public String logout(@Valid @RequestBody User user) {
//
////    Authentication authentication = authenticationManager.authenticate(
////            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
////
////    SecurityContextHolder.getContext().setAuthentication(authentication);
//
//    SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
//    return "logged out";
//
//}
}
