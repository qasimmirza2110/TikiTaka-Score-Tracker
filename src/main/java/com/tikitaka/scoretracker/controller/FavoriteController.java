package com.tikitaka.scoretracker.controller;

import com.tikitaka.scoretracker.dto.FavoriteRequest;
import com.tikitaka.scoretracker.service.UserService;
import com.tikitaka.scoretracker.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping
    public ResponseEntity<List<String>> getFavorites(
            @RequestHeader("Authorization") String token) {
        String username = jwtTokenUtil.extractUsername(token.substring(7));
        return ResponseEntity.ok(userService.getFavorites(username));
    }

    @PostMapping("/toggle")
    public ResponseEntity<Void> toggleFavorite(
            @RequestHeader("Authorization") String token,
            @RequestBody FavoriteRequest request) {
        String username = jwtTokenUtil.extractUsername(token.substring(7));
        userService.toggleFavorite(username, request.getTeamId());
        return ResponseEntity.ok().build();
    }
}