package br.com.corfio.lecodbarra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity {

    private ZXingScannerView scannerView;
    private EditText inputCodBarra;
    private TextView lblCodBarra;
    private Boolean vlPermissaoCamera;

    private static final int ZXING_CAMERA_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        atribuiVar();
        vlPermissaoCamera = false;
        verifPermissaoCamera();

        lblCodBarra.setText("Aguardando leitura...");
    }

    private void atribuiVar(){
        setContentView(R.layout.activity_main);

        inputCodBarra = findViewById(R.id.inputCodBarra);
        lblCodBarra = findViewById(R.id.lblCodBarra);
    }

    public void clickLimpar(View view){
        inputCodBarra.setText("");
        lblCodBarra.setText("Aguardando leitura...");
    }

    public void clickBtnLeCodBarra(View view){
        scannerView = new ZXingScannerView(this);
        //scannerView.resumeCameraPreview((ZXingScannerView.ResultHandler) this);
        scannerView.setResultHandler(new ZXingScannerResultHandler());

        setContentView(scannerView);
        scannerView.startCamera();

    }


    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler
    {
        @Override
        public void handleResult(com.google.zxing.Result result) {

            recebeLido(result.toString());

            scannerView.stopCamera();
        }
    }

    private void recebeLido(String codLido){

        atribuiVar();

        if (codLido == null)
            Toast.makeText(this,"Erro na leitura",Toast.LENGTH_LONG).show();
        else{
            codLido = codLido.replaceAll("\\s","");
            inputCodBarra.setText(codLido.toString());
            lblCodBarra.setText("Cod.Lido:" + codLido.toString() + " Tamanho:" + String.valueOf(codLido.toString().length()));
        }

    }

    public void verifPermissaoCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //Mostra Pergunta se APP pode utilizar camera:
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else    // Já possui permissão.
            vlPermissaoCamera = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    vlPermissaoCamera = true;
                } else {
                    Toast.makeText(this, "Sem a permissão p/ a utilização da câmera o software não vai funcionar corretamente.", Toast.LENGTH_SHORT).show();
                    vlPermissaoCamera = false;
                }
        }
    }


    @Override
    public void onBackPressed() {
        if (scannerView != null)
            scannerView.stopCamera();

        super.onBackPressed();
    }
}
