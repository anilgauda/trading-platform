package ie.ncirl.tradingplatform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "stocks")
public class Stock implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String symbol;

    @OneToOne
    private Account account;

    // Avg. buy price and sell price will be calculated from transactions
    @OneToMany(mappedBy = "stock")
    private List<StockTransaction> stockTransactions;
}
