importScripts('https://www.gstatic.com/firebasejs/9.22.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.22.0/firebase-messaging-compat.js');
firebase.initializeApp({
  apiKey: "AIzaSyBo1bK5XeAWVdB005Z_98OMeXh0CXhdSLE",
  authDomain: "ultiamterdt.firebaseapp.com",
  databaseURL: "https://ultiamterdt-default-rtdb.asia-southeast1.firebasedatabase.app",
  projectId: "ultiamterdt",
  storageBucket: "ultiamterdt.firebasestorage.app",
  messagingSenderId: "1005368296850",
  appId: "1:1005368296850:web:c090db95774f30b63c6d4b"
});
const messaging = firebase.messaging();
messaging.onBackgroundMessage(function(payload) {
  const d = payload.data || {};
  const title = d.title || payload.notification?.title || 'New Tournament!';
  let body = d.body || payload.notification?.body || '';
  if (d.roomId) body += '\n🔑 Room ID: ' + d.roomId;
  if (d.roomPassword) body += '\n🔒 Password: ' + d.roomPassword;
  self.registration.showNotification(title, {
    body: body, icon: '/icon.png', image: d.image || '',
    tag: 'tournament', renotify: true, requireInteraction: true,
    data: { url: '/', tournamentId: d.tournamentId, roomId: d.roomId, roomPassword: d.roomPassword }
  });
});
self.addEventListener('notificationclick', function(event) {
  event.notification.close();
  event.waitUntil(clients.matchAll({type:'window',includeUncontrolled:true}).then(function(list) {
    for (let c of list) { if ('focus' in c) return c.focus(); }
    if (clients.openWindow) return clients.openWindow('/');
  }));
});
