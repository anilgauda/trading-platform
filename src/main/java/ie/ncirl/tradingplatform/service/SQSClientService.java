package ie.ncirl.tradingplatform.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SQSClientService {


    private AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

    @Value("${aws.sqs.url}")
    private String queueUrl;

    public SendMessageResult send() {
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl).withMessageBody("hello world")
                .withMessageGroupId("messageGroup")
                .withMessageDeduplicationId(LocalDate.now().toString());
        return sqs.sendMessage(sendMessageRequest);
    }

}
