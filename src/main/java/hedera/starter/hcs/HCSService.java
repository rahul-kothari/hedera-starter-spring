package hedera.starter.hcs;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.consensus.*;
import com.hedera.hashgraph.sdk.mirror.MirrorConsensusTopicQuery;
import hedera.starter.utilities.EnvUtils;
import hedera.starter.utilities.HederaClient;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
public class HCSService {

    public Client client = HederaClient.getHederaClientInstance();

    //create HCS Topic
    public String createTopic() throws HederaStatusException {
        var tx = new ConsensusTopicCreateTransaction()
                .setAdminKey(EnvUtils.getOperatorKey().publicKey)
                .execute(client);
        ConsensusTopicId topicId = tx.getReceipt(client).getConsensusTopicId();
        System.out.println("New topic created: " + topicId);

        return topicId.toString();
    }

    //Delete a HCS Topic
    public boolean deleteTopic(String topicId) throws HederaStatusException {
        var tx = new ConsensusTopicDeleteTransaction()
                .setTopicId(ConsensusTopicId.fromString(topicId))
                .execute(client)
                .getReceipt(client); //get reciept to confirm the deletion.
        System.out.println("Deleted topic " + topicId);
        return true;
    }

    //get info on a hcs topic
    public ConsensusTopicInfo getTopicInfo(String topicId) throws HederaStatusException {

        long cost = new ConsensusTopicInfoQuery()
                .setTopicId(ConsensusTopicId.fromString(topicId))
                .getCost(client);
        ConsensusTopicInfo info = new ConsensusTopicInfoQuery()
                .setTopicId(ConsensusTopicId.fromString(topicId))
                .setQueryPayment(cost + cost / 50) //add 2% to estimated cost
                .execute(client);
        return info;
    }

    /**
     * Subscribe to messages on the topic, printing out the received message and metadata as it is published by the
     * Hedera mirror node.
     */
    public boolean subscribeToTopic(String topicId) {
        new MirrorConsensusTopicQuery()
                .setTopicId(ConsensusTopicId.fromString(topicId))
                .setStartTime(Instant.ofEpochSecond(0))
                .subscribe(HederaClient.getMirrorClient(), message -> {
                            System.out.println("Received message from MirrorNode: " + new String(message.message, StandardCharsets.UTF_8)
                                    + "\n\t consensus timestamp: " + message.consensusTimestamp
                                    + "\n\t topic sequence number: " + message.sequenceNumber);
                        },
                        // On gRPC error, print the stack trace
                        Throwable::printStackTrace);
        System.out.println("New messages in this topics will be printed to the console.");
        return true;
    }

    // submit a message to a hcs topic
    public boolean submitMessage(String topicId, String message) throws HederaStatusException {
        System.out.println("Submitting message to " + topicId);
        new ConsensusMessageSubmitTransaction()
                .setTopicId(ConsensusTopicId.fromString(topicId))
                .setMessage(message)
                .execute(client)
                .getReceipt(client);
        return true;
    }
}
