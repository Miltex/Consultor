package miltex.br.consultor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import miltex.br.consultor.adapters.PedidosAdapter;
import miltex.br.consultor.adapters.ProdutosAdapter;
import miltex.br.consultor.constantes.BaseUrl;
import miltex.br.consultor.dto.ClientCode;
import miltex.br.consultor.dto.PedidosVO;
import miltex.br.consultor.dto.ProdutoVO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListarPedidos extends AppCompatActivity {

    private static final int POST_CRIA_PEDIDO = 1;
    private List<PedidosVO> produtoList = new ArrayList<>();
    private View principalView;
    private ListView listView;
    private ListaPedidosTask listaTask = null;
    private PedidosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pedidos);
        try {
            initList();
            listView = (ListView) findViewById(R.id.list_ped);
           // addClickListener();
        } catch (IOException e) {
            //indisponibilidade da conexão
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /*public void addClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ProdutoVO item = (ProdutoVO) adapter.getItem(position);

                Bundle bundle = new Bundle();
                bundle.putLong("ID",item.getId());
                bundle.putString("NOME",item.getNome());
                bundle.putString("DESC",item.getDescricao());
                bundle.putString("MARC",item.getMarca());
                bundle.putString("COD",item.getCodigo());
                bundle.putString("VALOR",item.getValor().toString());

                Intent intent = new Intent(ListarProdutos.this,ProdutoDetalhe.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,POST_CRIA_PEDIDO);
            }
        });
    }*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == POST_CRIA_PEDIDO){

            if (resultCode == RESULT_OK) {
                Snackbar.make(listView, "Pedido Realizado.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else{
                Snackbar.make(listView, R.string.falha, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }
    }*/

    private void uiUpdate() {
        adapter = new PedidosAdapter(produtoList, this);
        listView.setAdapter(adapter);
    }

    private void initList() throws IOException {
        listaTask = new ListaPedidosTask();
        listaTask.execute((Void) null);
    }

    private String getCodigoCliente() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("REST_DATA", Context.MODE_PRIVATE);
        ClientCode targetObject = new Gson().fromJson(pref.getString("codigo", ""), ClientCode.class);
        return targetObject.getCode().toString();
    }


    private class ListaPedidosTask extends AsyncTask<Void, Void, List<PedidosVO>> {

        @Override
        protected List<PedidosVO> doInBackground(Void... voids) {
            try {
                return getProdutos(requestGetProdutos());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<PedidosVO> prods) {
            listaTask = null;
            if (prods != null) {
                produtoList = prods;
                uiUpdate();
            } else {
                this.cancel(true);
                Snackbar.make(principalView, R.string.servico_indisponivel, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        private List<PedidosVO> getProdutos(String json) {
            Gson gson = new Gson();
            return gson.fromJson(json, new TypeToken<List<PedidosVO>>() {
            }.getType());
        }

        private String requestGetProdutos() throws IOException {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(BaseUrl.GET_PEDIDOS + getCodigoCliente())
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }
}
