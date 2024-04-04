package org.example.simpleapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.example.simpleapi.domain.RewardData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reward")
@Log4j2
public class RewardController {

    @Operation(description = "Retrieve customer's reward by customerId and year month")
    @GetMapping("id-date/{customerId}")
    public ResponseEntity<RewardData> getRewardPointsByCustomerId(@PathVariable(name = "customerId") Integer customerId) {
        //Todo: add implementation
        RewardData reward = null;
        return ResponseEntity.ok(reward);
    }

    @Operation(description = "Retrieve all customer's rewards")
    @GetMapping("id-date/all")
    public ResponseEntity<List<RewardData>> getRewardPointsAllCustomers() {
        //Todo: add implementation
        List<RewardData> rewards = null;
        return ResponseEntity.ok(rewards);
    }
}
