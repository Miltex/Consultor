package miltex.br.consultor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.SocketTimeoutException;

import miltex.br.consultor.constantes.BaseUrl;
import miltex.br.consultor.dto.ClientCode;
import miltex.br.consultor.dto.PedidoVO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProdutoDetalhe extends AppCompatActivity {

    private TextView tvNome,tvDesc,tvMarc,tvCod,tvValor;
    private EditText edQuant;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_detalhe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        view = findViewById(R.id.contView);

        tvNome = findViewById(R.id.tvNome);
        tvDesc = findViewById(R.id.tvDesc);
        tvMarc = findViewById(R.id.tvMarc);
        tvCod = findViewById(R.id.tvCod);
        tvValor = findViewById(R.id.tvValor);
        edQuant = findViewById(R.id.ed_qua);

        Bundle bundle = getIntent().getExtras();

        final Long id = bundle.getLong("ID");
        String nome = bundle.getString("NOME");
        String desc = bundle.getString("DESC");
        String marc = bundle.getString("MARC");
        String cod= bundle.getString("COD");
        String valor = bundle.getString("VALOR");

        tvNome.setText(nome);
        tvDesc.setText(desc);
        tvMarc.setText(marc);
        tvCod.setText(cod);
        tvValor.setText(valor);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantidade = edQuant.getText().toString();

                if (quantidade.isEmpty()){
                    quantidade = "1";
                }

                RealizarPedidoTask pedidoTask = new RealizarPedidoTask(Long.parseLong(getCodigoCliente()),id,Integer.parseInt(quantidade));
                pedidoTask.execute((Void)null);

            }
        });
    }

    private void uiUpdate(){
       //  Toast.makeText(getApplicationContext(), "Pedido Realizado com Sucesso.", Toast.LENGTH_LONG);
        Intent intent = new Intent();
        intent.putExtra("key","value");// O resultado é colocado aqui... para ser pego na activity que chamou
        setResult(Activity.RESULT_OK);
        finish();
    }

    private String getCodigoCliente() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("REST_DATA", Context.MODE_PRIVATE);
        ClientCode targetObject = new Gson().fromJson(pref.getString("codigo", ""), ClientCode.class);
        return targetObject.getCode().toString();
    }

    private class RealizarPedidoTask extends AsyncTask<Void, Void, Response> {

        private OkHttpClient client;
        private PedidoVO pedido;

        public RealizarPedidoTask(Long idClient, Long idProduto, Integer quant) {
            pedido = new PedidoVO(idClient, idProduto, quant);
        }

        @Override
        protected void onPreExecute() {
            client = new OkHttpClient();
        }

        @Override
        protected Response doInBackground(Void... params) {
            Response response = null;
            try {

                Gson gson = new Gson();
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(pedido));
                Request request = new Request.Builder()
                        .url(BaseUrl.POST_PEDIDO)
                        .post(body)
                        .build();
                response = client.newCall(request).execute();
                return response;

            } catch (SocketTimeoutException e) {
                // Snackbar.make(listView, R.string.servico_indisponivel, Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show();
                return response;

                // return false;
            } catch (Exception ex) {
                //  Snackbar.make(listView, R.string.falha, Snackbar.LENGTH_LONG)
                //          .setAction("Action", null).show();
                return response;
            }
        }

        @Override
        protected void onPostExecute(final Response response) {

            if (response.isSuccessful()) {
                uiUpdate();
            }
        }
    }

}
