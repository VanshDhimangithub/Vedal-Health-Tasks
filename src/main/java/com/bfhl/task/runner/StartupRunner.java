package com.bfhl.task.runner;

import com.bfhl.task.dto.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StartupRunner implements CommandLineRunner {

    private final RestTemplate restTemplate;

    public StartupRunner(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(String... args) {

        // 1️⃣ Generate Webhook
        String generateUrl =
            "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        WebhookRequest request = new WebhookRequest();
        request.setName("John Doe");
        request.setRegNo("REG12347");
        request.setEmail("john@example.com");

        ResponseEntity<WebhookResponse> response =
            restTemplate.postForEntity(
                generateUrl,
                request,
                WebhookResponse.class
            );

        WebhookResponse data = response.getBody();
        if (data == null) return;

        // 2️⃣ Extract values
        String webhookUrl = data.getWebhook();
        String token = data.getAccessToken();

        // 3️⃣ SQL QUERY (ODD regNo → Question 1)
        String sqlQuery =
            "SELECT department, COUNT(*) " +
            "FROM employees " +
            "GROUP BY department " +
            "HAVING COUNT(*) > 5;";

        // 4️⃣ Submit SQL query
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        HttpEntity<FinalQueryRequest> entity =
            new HttpEntity<>(
                new FinalQueryRequest(sqlQuery),
                headers
            );

        restTemplate.postForEntity(
            webhookUrl,
            entity,
            String.class
        );
    }
}
