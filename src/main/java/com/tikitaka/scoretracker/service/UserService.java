package com.tikitaka.scoretracker.service;

import java.util.List;

public interface UserService {
    void toggleFavorite(String username, String teamId);
    List<String> getFavorites(String username);
}