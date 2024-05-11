package com.example.webscraperAPI.controllers;

import com.example.webscraperAPI.models.JobSearchRequest;
import com.example.webscraperAPI.models.JobsDTO;
import com.example.webscraperAPI.services.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "api/v1/")
public class ScraperController {
    @Autowired
    private ScraperService scraperService;

    @PostMapping(path = "/findJobs")
    public Set<JobsDTO> findAllJobs(@RequestBody JobSearchRequest jobSearchRequest) {
        return scraperService.findAllJobs(jobSearchRequest);
    }

    @GetMapping(path = "/test")
    public Object test() {
        return new Object() {
            public String message = "This is working";
        };
    }
}
