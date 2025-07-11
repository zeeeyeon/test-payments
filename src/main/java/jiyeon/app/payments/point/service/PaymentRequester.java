package jiyeon.app.payments.point.service;

import jiyeon.app.payments.point.config.PaymentProperties;
import jiyeon.app.payments.point.dto.PaymentConfirmRequest;
import jiyeon.app.payments.point.dto.PaymentConfirmResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class PaymentRequester {

    private final RestClient restClient;
    private final PaymentProperties paymentProperties;

    public PaymentConfirmResult requestConfirm(PaymentConfirmRequest request) {
        String encodedKey = Base64.getEncoder()
                .encodeToString((paymentProperties.getSecretKey() + ":").getBytes(StandardCharsets.UTF_8));

        return restClient.post()
                .uri(paymentProperties.getConfirmUrl())
                .header(HttpHeaders.AUTHORIZATION, "Basic " + encodedKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(PaymentConfirmResult.class)
                .getBody();
    }
}
