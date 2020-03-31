package ie.ncirl.tradingplatform.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;
import ie.ncirl.tradingplatform.dto.sqs.SQSDTO;
import ie.ncirl.tradingplatform.dto.sqs.StockTransactionDTO;
import ie.ncirl.tradingplatform.model.Account;
import ie.ncirl.tradingplatform.model.Stock;
import ie.ncirl.tradingplatform.repo.AccountRepo;
import ie.ncirl.tradingplatform.repo.StockRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SQSClientService {

    private AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private StockRepo stockRepo;

    @Value("${aws.sqs.url}")
    private String queueUrl;

    /**
     * Sends message to the queue.
     *
     * @return
     */
    public SendMessageResult send(SQSDTO sqsdto) {
        Gson gson = new Gson();
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        MessageAttributeValue messageAttributeValue = new MessageAttributeValue()
                .withDataType("String")
                .withStringValue("application/json");
        messageAttributes.put("contentType", messageAttributeValue);
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl).withMessageBody(gson.toJson(sqsdto))
                .withMessageGroupId("messageGroup")
                .withMessageDeduplicationId(LocalDateTime.now().toString())
                .withMessageAttributes(messageAttributes);
        return sqs.sendMessage(sendMessageRequest);
    }

    @SqsListener("${aws.sqs.url}")
    public void listen(StockTransactionDTO transactionDTO) {
        Account account = accountRepo.getOne(transactionDTO.getAccountId());
        Stock stock = Stock.builder()
                .account(account)
                .buyPrice(transactionDTO.getBuyPrice())
                .sellPrice(transactionDTO.getSellPrice())
                .quantity(transactionDTO.getQuantity())
                .symbol(transactionDTO.getSymbol())
                .build();
        stockRepo.save(stock);
    }


}
