package com.sample.leantech.transfer.controller.jira;

import com.sample.leantech.transfer.service.jira.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jira")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping("/transfer")
    public void transfer() {
        transferService.transfer();
    }

}