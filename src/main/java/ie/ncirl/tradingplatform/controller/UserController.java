package ie.ncirl.tradingplatform.controller;

import ie.ncirl.tradingplatform.dto.*;
import ie.ncirl.tradingplatform.model.User;
import ie.ncirl.tradingplatform.service.JwtUserDetailsService;
import ie.ncirl.tradingplatform.service.UserService;
import ie.ncirl.tradingplatform.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    // Shows user data of current logged in user from JWT
    @GetMapping("/user/profile")
    public ResponseEntity<?> showProfile() throws Exception {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername()).get();
        return ResponseEntity.ok(
                ProfileDTO.builder().username(user.getUsername()).displayName(user.getDisplayName()).build()
        );
    }

    @PostMapping("/user/register")
    public ResponseDTO<String> register(@RequestBody UserRegisterRequest userRegisterRequest) throws Exception {
        if (userService.findByUsername(userRegisterRequest.getUsername()).isPresent()) {
            return new ResponseDTO<String>().withError("username already exists");
        }
        userService.createUser(userRegisterRequest);
        return new ResponseDTO<String>().withData("OK");
    }

    @PostMapping("/user/authenticate")
    public ResponseDTO<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        if (authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword())) {
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(authenticationRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);
            return new ResponseDTO<JwtResponse>().withData(new JwtResponse(token));
        } else {
            return new ResponseDTO<String>().withError("Invalid username or password provided");
        }
    }


    private boolean authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

