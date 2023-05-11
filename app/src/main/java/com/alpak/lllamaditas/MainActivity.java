package com.alpak.lllamaditas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final private int SOLICITUD_PERMISO_READ_CALL_LOG = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            String[] TIPO_LLAMADA = {"", "entrante", "saliente", "perdida", "mensaje de voz", "cancelada", "lista bloqueados"};
            TextView salida = (TextView) findViewById(R.id.llamada);
            Uri llamadas = Uri.parse("content://call_log/calls");
            Cursor c = getContentResolver().query(llamadas, null, null, null, null);
            while (c.moveToNext()) {
                salida.append("\n"
                        + DateFormat.format("dd/MM/yy k:mm (", c.getLong((int) c.getColumnIndex(CallLog.Calls.DATE)))
                        + c.getString((int) c.getColumnIndex(CallLog.Calls.DURATION)) + ") "
                        + c.getString((int) c.getColumnIndex(CallLog.Calls.NUMBER)) + ", "
                        + TIPO_LLAMADA[Integer.parseInt(c.getString((int) c.getColumnIndex(CallLog.Calls.TYPE)))]);
            }
        } else {
            solicitarPermisoLeerLlamadas();
        }
    }

    private void solicitarPermisoLeerLlamadas() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CALL_LOG)) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CALL_LOG}, SOLICITUD_PERMISO_READ_CALL_LOG);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SOLICITUD_PERMISO_READ_CALL_LOG) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Lista de las Lamadas", Toast.LENGTH_LONG).show();
                String[] TIPO_LLAMADA = {"", "entrante", "saliente", "perdida", "mensaje de voz", "cancelada", "lista bloqueados"};
                TextView salida = (TextView) findViewById(R.id.llamada);
                Uri llamadas = Uri.parse("content://call_log/calls");
                Cursor c = getContentResolver().query(llamadas, null, null, null, null);
                while (c.moveToNext()) {
                    salida.append("\n"
                            + DateFormat.format("dd/MM/yy k:mm (", c.getLong((int) c.getColumnIndex(CallLog.Calls.DATE)))
                            + c.getString((int) c.getColumnIndex(CallLog.Calls.DURATION)) + ") "
                            + c.getString((int) c.getColumnIndex(CallLog.Calls.NUMBER)) + ", "
                            + TIPO_LLAMADA[Integer.parseInt(c.getString((int) c.getColumnIndex(CallLog.Calls.TYPE)))]);
                }
            } else {


            }
        }
    }
}