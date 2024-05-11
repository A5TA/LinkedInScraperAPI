package com.example.webscraperAPI.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//sample query final result: https://www.linkedin.com/jobs/search?keywords=Software%20Engineer&location=United%20States&position=1&pageNum=0
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobSearchRequest {
    String keyword; //the job name
    String location;
    int limit;
}
