package com.tikitaka.scoretracker.controller;

import com.tikitaka.scoretracker.dto.MatchDTO;
import com.tikitaka.scoretracker.dto.NotificationDTO;
import com.tikitaka.scoretracker.model.User;
import com.tikitaka.scoretracker.repository.UserRepository;
import com.tikitaka.scoretracker.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class ScoreController {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Scheduled(fixedRate = 30000)
    public void pushLiveScores() {
        try {
            List<MatchDTO> scores = fetchScoresFromAPI();
            messagingTemplate.convertAndSend("/topic/scores", scores);
            sendUserSpecificNotifications(scores);
        } catch (Exception e) {
            log.error("Error pushing live scores: {}", e.getMessage());
        }
    }

    @GetMapping("/api/scores/live")
    public ResponseEntity<List<MatchDTO>> getLiveScores(
            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            List<MatchDTO> allScores = fetchScoresFromAPI();

            if (token != null && token.startsWith("Bearer ")) {
                String username = jwtTokenUtil.extractUsername(token.substring(7));
                User user = userRepository.findByUsername(username).orElse(null);

                if (user != null && !user.getFavoriteTeams().isEmpty()) {
                    return ResponseEntity.ok(filterFavorites(allScores, user));
                }
            }
            return ResponseEntity.ok(allScores);
        } catch (Exception e) {
            log.error("Failed to fetch scores: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(List.of(new MatchDTO(null, "Service", "Unavailable", "N/A", false, null)));
        }
    }

    private List<MatchDTO> filterFavorites(List<MatchDTO> scores, User user) {
        return scores.stream()
                .filter(match -> user.getFavoriteTeams().contains(match.getTeam1()) ||
                        user.getFavoriteTeams().contains(match.getTeam2()))
                .collect(Collectors.toList());
    }

    private List<MatchDTO> fetchScoresFromAPI() {
        // Implement actual API integration here
        return List.of(
                new MatchDTO("1", "Team A", "Team B", "2-1", true, "Premier League"),
                new MatchDTO("2", "Team C", "Team D", "0-0", false, "La Liga")
        );
    }

    private void sendUserSpecificNotifications(List<MatchDTO> scores) {
        userRepository.findAll().forEach(user -> {
            List<MatchDTO> userMatches = filterFavorites(scores, user);
            if (!userMatches.isEmpty()) {
                messagingTemplate.convertAndSendToUser(
                        user.getUsername(),
                        "/queue/notifications",
                        new NotificationDTO(
                                "Favorite Team Update",
                                userMatches.size() + " of your teams are playing!"
                        )
                );
            }
        });
    }
}