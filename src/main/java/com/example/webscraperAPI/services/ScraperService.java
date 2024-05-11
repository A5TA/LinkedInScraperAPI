package com.example.webscraperAPI.services;

import com.example.webscraperAPI.models.JobSearchRequest;
import com.example.webscraperAPI.models.JobsDTO;
import com.example.webscraperAPI.models.PostDTO;

import java.util.Set;

public interface ScraperService {

    Set<JobsDTO> findAllJobs(JobSearchRequest jobSearchRequest);
}
