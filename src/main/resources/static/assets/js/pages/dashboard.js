import { api } from '../lib/api.js';
import { connectWebSocket } from '../lib/websocket.js';

document.addEventListener('DOMContentLoaded', () => {
  // Initialize
  loadScores();
  connectWebSocket();
  setupDarkModeToggle();
  setupEventListeners();
});

async function loadScores() {
  try {
    const scores = await api.get('/scores/live');
    renderScores(scores);
  } catch (error) {
    showError(error.message);
  }
}

function renderScores(scores) {
  const container = document.getElementById('scoreContainer');
  container.innerHTML = scores.map(score => `
    <div class="score-card" data-match-id="${score.id}">
      <div class="team-info">
        <img src="/assets/images/teams/${score.team1.toLowerCase()}.png" alt="${score.team1}">
        <span>${score.team1}</span>
      </div>
      
      <div class="match-details">
        <div class="match-score ${score.isLive ? 'live' : ''}">
          ${score.score}
        </div>
        <button class="btn-favorite ${score.isFavorite ? 'active' : ''}" 
                onclick="toggleFavorite('${score.id}')">
          <i class="far fa-star"></i>
        </button>
      </div>
      
      <div class="team-info">
        <img src="/assets/images/teams/${score.team2.toLowerCase()}.png" alt="${score.team2}">
        <span>${score.team2}</span>
      </div>
    </div>
  `).join('');
}

function setupEventListeners() {
  document.getElementById('refreshBtn').addEventListener('click', loadScores);
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
  const container = document.getElementById('scoreContainer');
  container.innerHTML = `
    <div class="error-card">
      <p>${message}</p>
      <button class="btn btn-primary" onclick="loadScores()">Retry</button>
    </div>
  `;
}

// Global function for favorite toggle
window.toggleFavorite = async (matchId) => {
  try {
    await api.toggleFavorite(matchId);
    const btn = document.querySelector(`[data-match-id="${matchId}"] .btn-favorite`);
    btn.classList.toggle('active');
  } catch (error) {
    console.error('Favorite toggle failed:', error);
  }
};