package com.yildizholding.ocean.meyeremployeeinfokkdotomat.service;

import com.yildizholding.ocean.meyeremployeeinfokkdotomat.config.MeyerApiConfig;
import com.yildizholding.ocean.meyeremployeeinfokkdotomat.model.TokenModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeyerService {
    private final RestTemplate restTemplate;
    private final MeyerApiConfig meyerApiConfig;

    public ResponseEntity<String> getToken(TokenModel tokenModel) {
        String url = meyerApiConfig.getBaseUrl() + "/API/TokenServisi/Token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TokenModel> entity = new HttpEntity<>(tokenModel, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return ResponseEntity.status(response.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.getBody());
    }

    public ResponseEntity<String> getSicil(Object object, String oceanAuth, String meyerToken) {
        if (meyerToken == null || meyerToken.trim().isEmpty()) {
            log.warn("Token gönderilmedi!!!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("İşlem için token gereklidir.");
        }

        String url = meyerApiConfig.getBaseUrl() + "/API/SicilServisi/getSicil";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", oceanAuth);
        headers.set("auth", meyerToken);
        HttpEntity<Object> entity = new HttpEntity<>(object, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            return ResponseEntity.status(response.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.getBody());
        } catch (HttpStatusCodeException e) {
            log.error("Meyer API hata döndü. HttpStatus: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            log.error("Meyer API'sine ulaşılamıyor. Bağlantı hatası: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Arka plandaki Meyer Servisine şu an ulaşılamıyor.");
        } catch (Exception e) {
            log.error("Sicil sorgulanırken beklenmeyen bir hata oluştu --> {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sistemimizde beklenmeyen hata oluştu");
        }
    }
}
