package com.example.webscraperAPI.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobsDTO {
    String name;
    String company;
    String location;
    String link;
    String details;
}
