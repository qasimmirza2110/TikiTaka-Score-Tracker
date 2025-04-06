import { api } from '../lib/api.js';

document.addEventListener('DOMContentLoaded', () => {
  loadFavorites();
  setupDarkModeToggle();
  setupEventListeners();
});

async function loadFavorites() {
  try {
    const favorites = await api.get('/favorites');
    renderFavorites(favorites);
  } catch (error) {
    showError(error.message);
  }
}

function renderFavorites(favorites) {
  const container = document.getElementById('favoritesContainer');
  
  if (favorites.length === 0) {
    container.innerHTML = `
      <div class="empty-state">
        <i class="far fa-star fa-3x"></i>
        <p>No favorite teams yet</p>
        <a href="/app/dashboard.html" class="btn btn-primary mt-1">
          Browse Matches
        </a>
      </div>
    `;
    return;
  }

  container.innerHTML = favorites.map(match => `
    <div class="score-card" data-match-id="${match.id}">
      <div class="team-info">
        <img src="/assets/images/teams/${match.team1.toLowerCase()}.png" alt="${match.team1}">
        <span>${match.team1}</span>
      </div>
      
      <div class="match-details">
        <div class="match-score ${match.isLive ? 'live' : ''}">
          ${match.score}
        </div>
        <button class="btn-favorite active" onclick="toggleFavorite('${match.id}')">
          <i class="fas fa-star"></i>
        </button>
      </div>
      
      <div class="team-info">
        <img src="/assets/images/teams/${match.team2.toLowerCase()}.png" alt="${match.team2}">
        <span>${match.team2}</span>
      </div>
    </div>
  `).join('');
}

function setupEventListeners() {
  document.getElementById('refreshBtn').addEventListener('click', loadFavorites);
}

function setupDarkModeToggle() {
  const toggle = document.getElementById('darkModeToggle');
  const currentTheme = localStorage.getItem('theme') || 'light';
  
  if (currentTheme === 'dark') {
    document.documentElement.setAttribute('data-theme', 'dark');
    toggle.checked = true;
  }
  
  toggle.addEventListener('change', (e) => {
    if (e.target.checked) {
      document.documentElement.setAttribute('data-theme', 'dark');
      localStorage.setItem('theme', 'dark');
    } else {
      document.documentElement.removeAttribute('data-theme');
      localStorage.setItem('theme', 'light');
    }
  });
}

function showError(message) {
  const container = document.getElementById('favoritesContainer');
  container.innerHTML = `
    <div class="error-card">
      <p>${message}</p>
      <button class="btn btn-primary" onclick="loadFavorites()">Retry</button>
    </div>
  `;
}

// Global function for favorite toggle
window.toggleFavorite = async (matchId) => {
  try {
    await api.toggleFavorite(matchId);
    loadFavorites(); // Refresh the list after toggle
  } catch (error) {
    console.error('Favorite toggle failed:', error);
  }
};