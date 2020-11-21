package hedera.starter.hcs;

import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.consensus.ConsensusTopicInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("Handles management of Hedera Consensus Services")
@RequestMapping(path = "/hcs")
public class HCSController {
    /**
     * Topic Create
     * Topic Delete
     * Topic Info
     * TODO: Topic Update
     * Topic Subscribe
     * Submit Message
     */

    @Autowired
    HCSService hcsService;

    public HCSController() {
        hcsService = new HCSService();
    }

    @PostMapping("/topic")
    @ApiOperation("Create a HCS Topic")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Topic ID")})
    public String createTopic() throws HederaStatusException {
        return hcsService.createTopic();
    }

    @DeleteMapping("/topic/{topicId}")
    @ApiOperation("Delete a topic")
    @ApiImplicitParam(name = "topicId", required = true, type = "String", example = "0.0.2117")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success or failure")})
    public boolean deleteTopic(@PathVariable String topicId) throws HederaStatusException {
        return hcsService.deleteTopic(topicId);
    }

    @GetMapping("/topic/info/{topicId}")
    @ApiOperation("Get info on a topic")
    @ApiImplicitParam(name = "topicId", required = true, type = "String", example = "0.0.2117")
    public ConsensusTopicInfo getTopicInfo(@PathVariable String topicId) throws HederaStatusException {
        return hcsService.getTopicInfo(topicId);
    }

    @PostMapping("/subscribe")
    @ApiOperation("Subscribe Mirror Node to a topicId. Updates to topics will be printed to the console.")
    @ApiImplicitParam(name = "topicId", required = true, type = "String", example = "0.0.2117")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success or failure")})
    public String submitMessage(@RequestParam String topicId) {
        boolean res = hcsService.subscribeToTopic(topicId);
        return (res ? "New messages in this topics will be printed to the console." : "Subscription failed");
    }

    @PostMapping("/submitMessage")
    @ApiOperation("submit a message to a topic")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicId", required = true, type = "String", example = "0.0.2117"),
            @ApiImplicitParam(name = "message", required = true, type = "String", example = "test")
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success or failure")})
    public boolean submitMessage(@RequestParam String topicId, @RequestParam String message) throws HederaStatusException {
        return hcsService.submitMessage(topicId, message);
    }
}
