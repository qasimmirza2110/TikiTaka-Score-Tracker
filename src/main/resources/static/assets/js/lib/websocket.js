let stompClient = null;

export function connectWebSocket() {
  const socket = new SockJS('/ws');
  stompClient = Stomp.over(socket);
  
  stompClient.connect({}, () => {
    stompClient.subscribe('/topic/scores', (message) => {
      const scores = JSON.parse(message.body);
      updateScores(scores);
    });
    
    stompClient.subscribe('/user/queue/notifications', (message) => {
      showNotification(JSON.parse(message.body));
    });
  });
}

function updateScores(scores) {
  scores.forEach(newScore => {
    const scoreElement = document.querySelector(`[data-match-id="${newScore.id}"] .match-score`);
    if (scoreElement) {
      scoreElement.textContent = newScore.score;
      scoreElement.classList.add('score-update');
      setTimeout(() => scoreElement.classList.remove('score-update'), 300);
    }
  });
}

function showNotification(notification) {
  if ('Notification' in window && Notification.permission === 'granted') {
    new Notification(notification.title, {
      body: notification.message,
      icon: '/assets/images/logo.png'
    });
  }
}