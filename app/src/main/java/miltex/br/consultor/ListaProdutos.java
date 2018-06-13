package miltex.br.consultor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import miltex.br.consultor.constantes.BaseUrl;
import miltex.br.consultor.dto.ClientCode;
import miltex.br.consultor.dto.PedidoVO;
import miltex.br.consultor.dto.ProdutoVO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ListaProdutos extends AppCompatActivity {

    List<Map<String, String>> produtoList = new ArrayList<Map<String, String>>();
    private View principalView;
    private ListView listView;
    private ListarProdutoTask listaTask = null;
    private RealizarPedidoTask pedidoTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);
        principalView = findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            initList();
            listView = (ListView) findViewById(R.id.listView1);
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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Snackbar.make(listView, ((TextView) view).getText(), Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show();

              //  onCreateDialog((String)((TextView) view).getText()).show();

            }
        });
    }


    /*public Dialog onCreateDialog(String item) {
        String[] split = item.split(" ");
        final String idCliente = split[0];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View inflaView = inflater.inflate(R.layout.dialog_pedido, null);
        builder.setView(inflaView)

                .setPositiveButton(R.string.pedir, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Snackbar.make(listView,"cliente="+ getCodigoCliente(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // LoginDialogFragment.this.getDialog().cancel();

                    }
                });

        return builder.create();
    }*/

    private void uiUpdate() {
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, produtoList, android.R.layout.simple_list_item_1,
                new String[]{"produto"}, new int[]{android.R.id.text1});
        listView.setAdapter(simpleAdapter);
        setListViewHeightBasedOnChildren(listView);
    }

    /**
     * Muito doido peguei da net... ver como funciona...
     * Funcionou...
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(
                listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;

        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(
                        desiredWidth, RelativeLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void initList() throws IOException {
        listaTask = new ListarProdutoTask();
        listaTask.execute((Void)null);
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
                return getProdutos(requestGetProdutos()) ;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ProdutoVO> prods) {
            listaTask=null;
            if (prods != null) {
                for (ProdutoVO produto : prods) {
                    produtoList.add(criarProduto("produto", String.valueOf(produto.getId()) + " " + produto.getNome()
                            + " " + produto.getMarca() + " R$ " + produto.getValor()));
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

        private HashMap<String, String> criarProduto(String chave, String valor) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(chave, valor);
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

    private class RealizarPedidoTask extends AsyncTask<Void,Void,Response>{

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
