package org.example.simpleapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.example.simpleapi.domain.RewardData;
import org.example.simpleapi.service.RewardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for reward data request and response
 */
@RestController
@RequestMapping("api/v2/reward")
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
}
