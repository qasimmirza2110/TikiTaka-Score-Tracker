// Initialize service worker for PWA
if ('serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('/sw.js')
      .then(registration => {
        console.log('ServiceWorker registration successful');
      })
      .catch(err => {
        console.log('ServiceWorker registration failed: ', err);
      });
  });
}

// Request notification permission
function requestNotificationPermission() {
  if ('Notification' in window) {
    Notification.requestPermission().then(permission => {
      if (permission === 'granted') {
        console.log('Notification permission granted');
      }
    });
  }
}

// Check auth status on protected pages
function checkAuth() {
  const protectedRoutes = ['/app/dashboard.html', '/app/profile.html'];
  if (protectedRoutes.some(route => window.location.pathname.endsWith(route))) {
    if (!localStorage.getItem('token')) {
      window.location.href = '/auth/login.html';
    }
  }
}

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
  requestNotificationPermission();
  checkAuth();
});