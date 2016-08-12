package com.example.manisharana.todoapp.Services;

public class FirebaseMessageService {
//    extends
//} FirebaseMessagingService {
//
//        private static final String TAG = "StartingAndroid";
//
//        @Override
//        public void onMessageReceived(RemoteMessage remoteMessage) {
//
//            //It is optional
//            Log.e(TAG, "From: " + remoteMessage.getFrom());
//            Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
//
//            //Calling method to generate notification
//            sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
//        }
//
//        //This method is only generating push notification
//    private void sendNotification(String title, String messageBody) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0, notificationBuilder.build());
//    }
}
