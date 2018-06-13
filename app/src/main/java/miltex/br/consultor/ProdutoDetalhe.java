package miltex.br.consultor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.SocketTimeoutException;

import miltex.br.consultor.constantes.BaseUrl;
import miltex.br.consultor.dto.PedidoVO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProdutoDetalhe extends AppCompatActivity {

    private RealizarPedidoTask pedidoTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_detalhe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }



    private class RealizarPedidoTask extends AsyncTask<Void,Void,Response> {

        private OkHttpClient client;
        private PedidoVO pedido;

        public RealizarPedidoTask(Long idClient,Long idProduto,Integer quant){
            pedido = new PedidoVO(idClient,idProduto,quant);
        }

        @Override
        protected void onPreExecute() {
            client = new OkHttpClient();
        }

        @Override
        protected Response doInBackground(Void... params) {
            Response response=null;
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
            } catch (Exception ex){
                //  Snackbar.make(listView, R.string.falha, Snackbar.LENGTH_LONG)
                //          .setAction("Action", null).show();
                return response;
            }
        }

        @Override
        protected void onPostExecute(final Response response) {

            if (response.isSuccessful()) {
                Toast.makeText(getApplicationContext(),"Pedido Realizado com Sucesso.",Toast.LENGTH_LONG);
            }
        }
    }

}
