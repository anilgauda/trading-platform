package ie.ncirl.tradingplatform.controller;

import ie.ncirl.tradingplatform.dto.JwtRequest;
import ie.ncirl.tradingplatform.dto.JwtResponse;
import ie.ncirl.tradingplatform.dto.UserRegisterRequest;
import ie.ncirl.tradingplatform.model.User;
import ie.ncirl.tradingplatform.repo.UserRepo;
import ie.ncirl.tradingplatform.service.JwtUserDetailsService;
import ie.ncirl.tradingplatform.service.UserService;
import ie.ncirl.tradingplatform.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest userRegisterRequest) throws Exception {

        if (userService.findByUsername(userRegisterRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("username already exists");
        }
        userService.createUser(userRegisterRequest);
        return ResponseEntity.ok("created");
    }

    @PostMapping("/user/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }


    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}

