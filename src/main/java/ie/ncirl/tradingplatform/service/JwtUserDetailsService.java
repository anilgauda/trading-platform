package ie.ncirl.tradingplatform.service;

import ie.ncirl.tradingplatform.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        ie.ncirl.tradingplatform.model.User user = userRepo.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Email " + userName + " not found"));

        return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}