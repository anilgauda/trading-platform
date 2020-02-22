package services

import akka.http.scaladsl.model.DateTime
import com.amazonaws.services.sqs.model.SendMessageResult

class SQSClient {

  import com.amazonaws.services.sqs.{AmazonSQS, AmazonSQSClientBuilder}
  import com.amazonaws.services.sqs.model.SendMessageRequest

  val sqs: AmazonSQS = AmazonSQSClientBuilder.defaultClient
  val queueUrl = "https://sqs.us-east-1.amazonaws.com/800086971207/test.fifo"

  def send: SendMessageResult = {
    val send_msg_request: SendMessageRequest = new SendMessageRequest()
      .withQueueUrl(queueUrl).withMessageBody("hello world")
      .withMessageGroupId("messageGroup")
      .withMessageDeduplicationId(DateTime.now.toIsoDateTimeString())
    sqs.sendMessage(send_msg_request)
  }
}
