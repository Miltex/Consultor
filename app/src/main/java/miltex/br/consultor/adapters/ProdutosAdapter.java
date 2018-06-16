package miltex.br.consultor.adapters;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import miltex.br.consultor.R;
import miltex.br.consultor.dto.ProdutoVO;

public class ProdutosAdapter extends BaseAdapter {

    private List<ProdutoVO> produtos;
    private Activity activity;

    public ProdutosAdapter(List<ProdutoVO> prods, Activity act) {
        produtos = prods;
        activity = act;
    }

    @Override
    public int getCount() {
        return produtos.size();
    }

    @Override
    public Object getItem(int i) {
        return produtos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View prodsView = activity.getLayoutInflater().inflate(R.layout.lista_produtos_adaptada, viewGroup, false);

        ProdutoVO produto = produtos.get(position);

      //  TextView id = prodsView.findViewById(R.id.id_prod);
        TextView nome = prodsView.findViewById(R.id.nome);
     //   TextView desc = prodsView.findViewById(R.id.desc);
        TextView marc = prodsView.findViewById(R.id.marc);
        TextView cod = prodsView.findViewById(R.id.cod);
        TextView val = prodsView.findViewById(R.id.valor);

      //  id.setText(produto.getId().toString());
        nome.setText(produto.getNome());
     //   desc.setText(produto.getDescricao());
        marc.setText(produto.getMarca());
        cod.setText(produto.getCodigo());
        val.setText("R$ "+produto.getValor().toString().replace(".",","));

        return prodsView;

    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
