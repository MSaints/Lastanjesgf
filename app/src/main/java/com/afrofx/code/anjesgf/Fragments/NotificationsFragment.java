package com.afrofx.code.anjesgf.Fragments;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.NotificationControl;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.Recyclers.RecyclerNotificacoes;
import com.afrofx.code.anjesgf.models.NotificationModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private View v;

    private List<NotificationModel> notificationModelList;

    private DatabaseHelper db;

    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
    Calendar calendar = Calendar.getInstance();

    public NotificationsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(getActivity());

        notificationModelList = new ArrayList<>();

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getContext());

        boolean value1 = (mSharedPreference.getBoolean("activa_notificacao", false));

        if(value1){
            stockNotification();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.fragment_notifications, container, false);

        final RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewNotifications);
        recyclerView.setHasFixedSize(true);
        final RecyclerNotificacoes recyclerNotificacoes = new RecyclerNotificacoes(getContext(), notificationModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerNotificacoes);

        return v;
    }



    private void dividaNotification(){

    }


    private void validadeNotification(){

    }


    private void stockNotification(){
        db = new DatabaseHelper(getContext());

        Cursor cursor = db.listProduto();

       /* Intent intent = new Intent(getContext(), MainScreenActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
*/

        if(cursor.getCount()==0){
        }else {
            while (cursor.moveToNext()){
                double minima = cursor.getDouble(cursor.getColumnIndex("produto_quanti_minima"));
                double total = cursor.getDouble(cursor.getColumnIndex("produto_quantidade"));

                String msg, data, hora, tipo;

                msg = "Estado do Stock";

                hora = dateFormat.format(calendar.getTime());
                data = dateFormat2.format(calendar.getTime());
                NotificationModel notificationModel;

                if(total == 0){
                    tipo = cursor.getString(cursor.getColumnIndex("produto_nome"))+ " Acabou";

                    NotificationControl notificationControl;
                    notificationControl = new NotificationControl(getContext());
                    notificationControl.sendNotification(msg,tipo);

                    notificationModel = new NotificationModel(tipo, msg, hora, data);
                    notificationModelList.add(notificationModel);
                }
                if(total<=minima & total != 0){
                    tipo = "Ficaram apenas "+total+" "+cursor.getString(cursor.getColumnIndex("produto_nome"));
                    hora = dateFormat.format(calendar.getTime());
                    data = dateFormat2.format(calendar.getTime());

                    NotificationControl notificationControl;
                    notificationControl = new NotificationControl(getContext());
                    notificationControl.sendNotification(msg,tipo);

                    notificationModel = new NotificationModel(tipo, msg, hora, data);
                    notificationModelList.add(notificationModel);

                }

            }
        }
    }


    private void analiseNotification(){

    }



/*    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");

        Intent notificationIntent = new Intent(getContext(), MainScreenActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private Object getSystemService(String notificationService) {

    }*/


}
