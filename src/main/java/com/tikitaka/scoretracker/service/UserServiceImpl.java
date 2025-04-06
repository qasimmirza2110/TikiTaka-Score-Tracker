package com.tikitaka.scoretracker.service;

import com.tikitaka.scoretracker.model.User;
import com.tikitaka.scoretracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void toggleFavorite(String username, String teamId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getFavoriteTeams().contains(teamId)) {
            user.getFavoriteTeams().remove(teamId);
        } else {
            user.getFavoriteTeams().add(teamId);
        }
        userRepository.save(user);
    }

    @Override
    public List<String> getFavorites(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getFavoriteTeams();
    }
}