package com.yildizholding.ocean.meyeremployeeinfokkdotomat.controller;

import com.yildizholding.ocean.meyeremployeeinfokkdotomat.model.TokenModel;
import com.yildizholding.ocean.meyeremployeeinfokkdotomat.service.MeyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MainController {
    private final MeyerService meyerService;

    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestBody TokenModel tokenModel) {
        return meyerService.getToken(tokenModel);
    }

    @PostMapping("/getSicil")
    public ResponseEntity<String> getSicil(@RequestBody Object requestBody, 
                                           @RequestHeader("Authorization") String authorization, 
                                           @RequestHeader("auth") String auth) {
        return meyerService.getSicil(requestBody, authorization, auth);
    }
}
