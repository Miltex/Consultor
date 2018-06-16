package miltex.br.consultor.adapters;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import miltex.br.consultor.R;
import miltex.br.consultor.dto.PedidosVO;

public class PedidosAdapter extends BaseAdapter {

    private List<PedidosVO> pedidos;
    private Activity activity;

    public PedidosAdapter(List<PedidosVO> prods, Activity act) {
        pedidos = prods;
        activity = act;
    }

    @Override
    public int getCount() {
        return pedidos.size();
    }

    @Override
    public Object getItem(int i) {
        return pedidos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View prodsView = activity.getLayoutInflater().inflate(R.layout.lista_pedidos_adaptada, viewGroup, false);

        PedidosVO pedido = pedidos.get(position);


        TextView nome = prodsView.findViewById(R.id.nome_ped);
        TextView marc = prodsView.findViewById(R.id.marc_ped);
        TextView cod = prodsView.findViewById(R.id.cod_ped);
        TextView val = prodsView.findViewById(R.id.valor_ped);

        nome.setText(pedido.getProduto().getNome());
        marc.setText(pedido.getProduto().getMarca());
        cod.setText(pedido.getProduto().getCodigo());
        val.setText("R$ "+pedido.getProduto().getValor().multiply(new BigDecimal(pedido.getQuantidade()))
                .toString().replace(".",","));

        return prodsView;

    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
