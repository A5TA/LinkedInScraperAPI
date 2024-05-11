package com.example.webscraperAPI.services;

import com.example.webscraperAPI.models.JobSearchRequest;
import com.example.webscraperAPI.models.JobsDTO;
import com.example.webscraperAPI.models.PostDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class ScraperServiceImpl implements ScraperService {

    @Value("#{'${website.urls}'.split(',')}")
    List<String> urls;
    int delaySeconds = 10; // Adjust delay time as needed


    @Override
    public Set<JobsDTO> findAllJobs(JobSearchRequest jobSearchRequest) {
        //This will get all the posts it finds when the site is loaded
        Set<JobsDTO> currentJobs = new HashSet<>();
        for (String url: urls) {
            if (url.contains("linkedin")) {
                performJobSearch(url, jobSearchRequest, currentJobs);
            }
//            else {
//                System.out.println("This Url is not implemented");
//            }

        }
        return currentJobs;
    }

    private void performJobSearch(String url, JobSearchRequest jobSearchRequest, Set<JobsDTO>  currentJobs) {
        try {
            // Encode parameters for the job search query
            String keyword = URLEncoder.encode(jobSearchRequest.getKeyword(), StandardCharsets.UTF_8);
            String location = URLEncoder.encode(jobSearchRequest.getLocation(), StandardCharsets.UTF_8);

            // Concatenate URL with encoded parameters
            String completeURL = url + "keywords=" + keyword + "&location=" + location + "&position=1&pageNum=0";
            System.out.println("connecting to " + completeURL);
            Document document = Jsoup.connect(completeURL).get();

            Elements jobsList = document.getElementsByClass("jobs-search__results-list");
            Elements jobsListLi = jobsList.select("li");

            //                System.out.println("The list is " + jobsList.toArray().length);
            int count = jobSearchRequest.getLimit(); //this is a limit set by the requester
            for (Element li: jobsListLi ) {
                if (count <= 0) {
                    break;
                }
                String jobLink = li.getElementsByClass("base-card__full-link").attr("href");
                String jobDetails = getJobDetails(jobLink);

                JobsDTO job = JobsDTO.builder()
                        .name(li.getElementsByClass("base-search-card__title").text())
                        .company(Objects.requireNonNull(Objects.requireNonNull(li.getElementsByClass("base-search-card__subtitle").first()).getElementsByTag("a").first()).text())
                        .location(li.getElementsByClass("job-search-card__location").text())
                        .link(jobLink)
                        .details(jobDetails)
                        .build();
//                System.out.println(job.toString());
                currentJobs.add(job);
                count--;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getJobDetails(String url) throws InterruptedException, IOException {
        Document document = Jsoup.connect(url).get();
        Thread.sleep(delaySeconds * 1000L); // Add a delay to stop load on servers
        Element descriptionElement = document.select("div.show-more-less-html__markup").first();
        if (descriptionElement != null) {
            return descriptionElement.text();
        } else {
            return "No Description Available";
        }
    }

    private void getRedditPostData(Set<PostDTO> currentPosts, String url) {
        try {
            Document document = Jsoup.connect(url).maxBodySize(0).get();
            Thread.sleep(delaySeconds * 1000L); // Convert seconds to milliseconds

            Element feedElements = document.getElementsByTag("shreddit-feed").first(); //first ensures it's the actual feed

            if (feedElements != null) {
                //get all the posts inside the feed
                Elements postElements = feedElements.getElementsByTag("shreddit-post");
                for (Element postElement: postElements) {
                    //Get the Title First
                    Element titleElement = postElement.select("a[slot=title]").first();
                    if (titleElement != null) {
                        System.out.println("First post is called: " + titleElement.text());
                    }
                    //Get the Text Body
                    Element bodyElement = postElement.select("a[slot=text-body]").first();
                    if (bodyElement != null) {
                        Elements paragraphElements = bodyElement.select("p"); //get the paragraph elements which can be multipl lines
                        StringBuilder completeText = new StringBuilder(); //Make a String Builder to append paragraphs
                        for (Element paragraphElement : paragraphElements) {
                            // Get the text content of the paragraph
                            String paragraphText = paragraphElement.text();

                            // Add the element to completeText
                            completeText.append(paragraphText);
                        }
                        System.out.println("Paragraph is: " + completeText);
                    } else {
                        System.out.println("No Paragraph For this Post");


                    }
//                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
