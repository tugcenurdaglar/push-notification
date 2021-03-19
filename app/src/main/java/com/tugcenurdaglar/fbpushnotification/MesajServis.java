package com.tugcenurdaglar.fbpushnotification;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class MesajServis extends FirebaseMessagingService {
    private NotificationCompat.Builder builder;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        //mesaj burada alınıyor
        /*Manifest te
        * <action android:name="com.google.firebase.MESSAGING_EVENT"/>
        * bu yapı sayesinde önplandayken gelen bildirimi dinleriz
        * yani uygulama açıkken bildirim alabiliriz
         */
        super.onMessageReceived(remoteMessage);

        String baslik = remoteMessage.getNotification().getTitle();
        String icerik = remoteMessage.getNotification().getBody();

        Log.e("Başlık", baslik);
        Log.e("İçerik", icerik);

        durumaBagli(baslik,icerik);

    }

    public void durumaBagli(String baslik, String icerik){

        NotificationManager bildirimYoneticisi =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //bildirim üzerine tıklanıldığında nereye gitmesini istersek:

        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent gidilecekIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//OREO sürümü için bu kod çalışacak

            String kanalId = "kanalId";
            //amacı bildirim geldiği zaman, bildirimleri bir arada toplayabilmek için aynı kanalda olması isteniyor

            String kanalAd = "kanalAd";
            String kanalTanım = "kanalTanım";

            int kanalOnceligi = NotificationManager.IMPORTANCE_HIGH;//yüksek öncelikli olarak gösterilsin demek

            //Notification kanalını oluştur

            NotificationChannel kanal = bildirimYoneticisi.getNotificationChannel(kanalId);

            if (kanal == null){ //daha yeni oluşturulmuşsa

                kanal = new NotificationChannel(kanalId,kanalAd,kanalOnceligi);
                kanal.setDescription(kanalTanım);
                bildirimYoneticisi.createNotificationChannel(kanal);
            }

            builder = new NotificationCompat.Builder(this,kanalId);

            builder.setContentTitle(baslik);
            builder.setContentText(icerik);
            builder.setSmallIcon(R.drawable.resim);
            builder.setAutoCancel(true); //gelen bildirime tıklanldığında kendini otomatik olarak kapatıyor
            builder.setContentIntent(gidilecekIntent);




        }else {//OREO sürümü haricinde bu kod çalışacak

            builder = new NotificationCompat.Builder(this);

            builder.setContentTitle(baslik);
            builder.setContentText(icerik);
            builder.setSmallIcon(R.drawable.resim);
            builder.setAutoCancel(true); //gelen bildirime tıklanldığında kendini otomatik olarak kapatıyor
            builder.setContentIntent(gidilecekIntent);
            builder.setPriority(Notification.PRIORITY_HIGH);

        }

        bildirimYoneticisi.notify(1,builder.build());

    }

}
