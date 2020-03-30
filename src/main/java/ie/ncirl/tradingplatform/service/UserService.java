package ie.ncirl.tradingplatform.service;

import ie.ncirl.tradingplatform.dto.UserRegisterRequest;
import ie.ncirl.tradingplatform.model.User;
import ie.ncirl.tradingplatform.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public void createUser(UserRegisterRequest userRegisterRequest) {
        User user = User.builder()
                .username(userRegisterRequest.getUsername())
                .displayName(userRegisterRequest.getUsername())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword())).build();

        userRepo.save(user);
    }
}
