package org.example.simpleapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.example.simpleapi.domain.RewardData;
import org.example.simpleapi.service.RewardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reward")
@Log4j2
public class RewardController {
    private final Marker mk = MarkerManager.getMarker("REWARD_CONTROLLER");
    private final RewardService rewardService;

    RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @Operation(description = "Retrieve customer's reward by customerId")
    @GetMapping("/{customerId}")
    public ResponseEntity<RewardData> getRewardPointsByCustomerId(@PathVariable(name = "customerId") Integer customerId) {
        RewardData reward = rewardService.getCustomerRewards(
                mk,
                customerId,
                new RewardData(customerId)
        );
        return ResponseEntity.ok(reward);
    }

    //Todo: Add implementation. This is not a requirement but good to have
    @Operation(description = "Retrieve all customer's reward records")
    @GetMapping("/all")
    public ResponseEntity<List<RewardData>> getRewardPointsAllCustomers() {
        List<RewardData> rewards = null;
        return ResponseEntity.ok(rewards);
    }
}
