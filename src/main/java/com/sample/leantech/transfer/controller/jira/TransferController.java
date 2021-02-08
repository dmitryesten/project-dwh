package com.sample.leantech.transfer.controller.jira;

import com.sample.leantech.transfer.service.jira.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    @Autowired
    @Qualifier("jiraService")
    private TransferService jiraTransferService;

    @PostMapping("/jira")
    public void transfer() {
        jiraTransferService.transfer();
    }

}