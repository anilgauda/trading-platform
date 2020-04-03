package ie.ncirl.tradingplatform.service;

import ie.ncirl.tradingplatform.dto.AccountDetailsDTO;
import ie.ncirl.tradingplatform.dto.UserRegisterRequest;
import ie.ncirl.tradingplatform.model.Account;
import ie.ncirl.tradingplatform.model.Stock;
import ie.ncirl.tradingplatform.model.User;
import ie.ncirl.tradingplatform.repo.AccountRepo;
import ie.ncirl.tradingplatform.repo.StockRepo;
import ie.ncirl.tradingplatform.repo.UserRepo;
import ie.ncirl.tradingplatform.util.UserUtil;
import ie.ncirl.tradingplatform.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    StockRepo stockRepo;

    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Transactional
    public void createUser(UserRegisterRequest userRegisterRequest) {
        User user = User.builder()
                .username(userRegisterRequest.getUsername())
                .displayName("".equals(userRegisterRequest.getDisplayName()) ? userRegisterRequest.getUsername() : userRegisterRequest.getDisplayName())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword())).build();

        userRepo.save(user);

        String accountNumber = Util.generateRandomId();
        while (accountRepo.findAccountByNumber(accountNumber).isPresent()) {
            accountNumber = Util.generateRandomId();   // To regenerate if duplicate found by pigeon hole principle
        }

        Account account = Account.builder()
                .number(accountNumber)
                .balance(1000d)
                .user(user)
                .build();

        accountRepo.save(account);
    }

    public Account getActiveAccount() {
        return userRepo.findByUsername(UserUtil.getCurrentUser().getUsername()).get().getAccounts().get(0);
    }

    public void addBalance(Account account, Double balance) {
        account.setBalance(account.getBalance() + balance);
        accountRepo.save(account);
    }

    public AccountDetailsDTO getAccountDetails(Account account) {
        List<Stock> stocks = stockRepo.findAllByAccount(account);
        Double earnings = stocks.stream().flatMap(stock -> stock.getStockTransactions().stream())
                .mapToDouble(stockTransaction -> stockTransaction.getSellPrice() == null ? 0 : stockTransaction.getSellPrice())
                .sum();

        return AccountDetailsDTO.builder()
                .balance(Math.round(account.getBalance() * 100.0) / 100.0)
                .totalEarnings(Math.round(earnings * 100.0) / 100.0)
                .numStocks(stocks.size())
                .build();
    }

}
