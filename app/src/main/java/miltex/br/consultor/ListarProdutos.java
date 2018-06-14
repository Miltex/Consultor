package miltex.br.consultor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import miltex.br.consultor.constantes.BaseUrl;
import miltex.br.consultor.dto.ClientCode;
import miltex.br.consultor.dto.ProdutoVO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListarProdutos extends AppCompatActivity {

    List<Map<String, String>> produtoList = new ArrayList<Map<String, String>>();
    private View principalView;
    private ListView listView;
    private ListarProdutoTask listaTask = null;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_produtos);

        try {
            initList();
            listView = (ListView) findViewById(R.id.list_prod);
            addClickListener();
        } catch (IOException e) {
            //indisponibilidade da conexão
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void addClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Object item = simpleAdapter.getItem(position);
            }
        });
    }


    private void uiUpdate() {

        String[] de = {"id", "nome", "desc", "marc", "cod", "valor"};
        int[] para = {1, 2, 3, 4, 5, 6};

        simpleAdapter = new SimpleAdapter(this, produtoList, R.layout.content_lista_produtos,
                de, para);
        listView.setAdapter(simpleAdapter);

    }

    private void initList() throws IOException {
        listaTask = new ListarProdutoTask();
        listaTask.execute((Void) null);
    }

    private String getCodigoCliente() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("REST_DATA", Context.MODE_PRIVATE);
        ClientCode targetObject = new Gson().fromJson(pref.getString("codigo", ""), ClientCode.class);
        return targetObject.getCode().toString();
    }


    private class ListarProdutoTask extends AsyncTask<Void, Void, List<ProdutoVO>> {

        @Override
        protected List<ProdutoVO> doInBackground(Void... voids) {
            try {
                return getProdutos(requestGetProdutos());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ProdutoVO> prods) {
            listaTask = null;
            if (prods != null) {
                for (ProdutoVO produto : prods) {

                    produtoList.add(criarProduto("id",String.valueOf(produto.getId()),"nome",produto.getNome(),
                            "desc",produto.getDescricao(),"marc",produto.getMarca(),
                            "cod",produto.getCodigo(),"valor",String.valueOf(produto.getValor())));
                }
                uiUpdate();
            } else {
                this.cancel(true);
                Snackbar.make(principalView, R.string.servico_indisponivel, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        private List<ProdutoVO> getProdutos(String json) {
            Gson gson = new Gson();
            return gson.fromJson(json, new TypeToken<List<ProdutoVO>>() {
            }.getType());
        }

        private HashMap<String, String> criarProduto(String idKey, String idValor, String nomeKey, String nomeValor,
                                                     String descKey, String descValor, String marcKey, String marcValor,
                                                     String codKey, String codValor, String valorKey, String valorValor) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(idKey, idValor);
            map.put(nomeKey, nomeValor);
            map.put(descKey, descValor);
            map.put(marcKey, marcValor);
            map.put(codKey, codValor);
            map.put(valorKey, valorValor);
            return map;
        }

        private String requestGetProdutos() throws IOException {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(BaseUrl.GET_PRODUTOS + getCodigoCliente())
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }
}
