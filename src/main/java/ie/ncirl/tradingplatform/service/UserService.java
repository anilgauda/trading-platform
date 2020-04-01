package ie.ncirl.tradingplatform.service;

import ie.ncirl.tradingplatform.dto.UserRegisterRequest;
import ie.ncirl.tradingplatform.model.Account;
import ie.ncirl.tradingplatform.model.User;
import ie.ncirl.tradingplatform.repo.AccountRepo;
import ie.ncirl.tradingplatform.repo.UserRepo;
import ie.ncirl.tradingplatform.util.UserUtil;
import ie.ncirl.tradingplatform.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Transactional
    public void createUser(UserRegisterRequest userRegisterRequest) {
        User user = User.builder()
                .username(userRegisterRequest.getUsername())
                .displayName(userRegisterRequest.getUsername())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword())).build();

        userRepo.save(user);

        String accountNumber = Util.generateRandomId();
        while (accountRepo.findAccountByNumber(accountNumber).isPresent()) {
            accountNumber = Util.generateRandomId();   // To regenerate if duplicate found by pigeon hole principle
        }

        Account account = Account.builder()
                .number(accountNumber)
                .user(user)
                .build();

        accountRepo.save(account);
    }

    public Account getActiveAccount() {
        return userRepo.findByUsername(UserUtil.getCurrentUser().getUsername()).get().getAccounts().get(0);
    }

}
