package com.example.djc.kanquimaniapark.Admin.GestionProductos;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Admin.ItemClickListener;
import com.example.djc.kanquimaniapark.Clases.Producto;
import com.example.djc.kanquimaniapark.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by dewyn on 10/26/2016.
 */
public class ProductsRecyclerAdapter extends RecyclerView.Adapter <ProductsVH> {

    private Dialog dialog;
    private List<Producto> productos;
    private Context context;
    private CRUDProductsFireBaseHelper crudProducts;
    private View focusView = null;


    public ProductsRecyclerAdapter(Context context, List list){
        this.productos = list;
        this.context = context;
    }

    @Override
    public ProductsVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_productos_list, parent, false);
        return new ProductsVH(v);
    }

    @Override
    public void onBindViewHolder(ProductsVH holder, final int position) {
        holder.tTitulo.setText(productos.get(position).get_titulo());
        final String precio = "RD$ " + productos.get(position).get_precio();
        holder.tPrecio.setText(precio);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClickListener(View v, int pos) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.edit_product_dialog);
                dialog.setTitle("Manejo de productos");

                //<editor-fold desc="Initialize Widgets">
                crudProducts = new CRUDProductsFireBaseHelper();
                final EditText tituloET = (EditText)dialog.findViewById(R.id.show_prodname);
                final EditText precioET = (EditText)dialog.findViewById(R.id.show_prodprice);
                final Button editButton = (Button)dialog.findViewById(R.id.edit_product);
                final Button deleteButton = (Button)dialog.findViewById(R.id.delete_product);
                final Button acceptButton = (Button)dialog.findViewById(R.id.aceptar_product);
                //</editor-fold>

                //<editor-fold desc="Seeting Values">
                tituloET.setText(productos.get(position).get_titulo());
                precioET.setText(productos.get(position).get_precio());
                //</editor-fold>

                //<editor-fold desc="Edit Button Listener">
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tituloET.setEnabled(true);
                        precioET.setEnabled(true);
                        editButton.setVisibility(View.GONE);
                        acceptButton.setVisibility(View.VISIBLE);
                    }
                });
                //</editor-fold>

                //<editor-fold desc="Delete Button Listener">
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        crudProducts.deleteProduct(productos.get(position));
                        dialog.dismiss();
                    }
                });
                //</editor-fold>

                //<editor-fold desc="Accept Button Listener">
                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean cancel = false;
                        String titulo, precio;

                        if (tituloET.getText().toString().equals("")){
                            cancel = true;
                            tituloET.setError(context.getString(R.string.vacio));
                            focusView = tituloET;
                        } else if (precioET.getText().toString().equals("")){
                            cancel = true;
                            precioET.setError(context.getString(R.string.vacio));
                            focusView = precioET;
                        }

                        for (Producto p : productos){
                            if (!p.get_id().equals(productos.get(position).get_id())){
                                if (p.get_titulo().equals(tituloET.getText().toString())){
                                    cancel = true;
                                    tituloET.setError(context.getString(R.string.usuario_existe));
                                    focusView = tituloET;
                                }
                            }
                        }

                        if (cancel){
                            focusView.requestFocus();
                        } else {
                            titulo = tituloET.getText().toString();
                            precio = precioET.getText().toString();

                            Producto p = new Producto(productos.get(position).get_id(), titulo, precio);
                            crudProducts.updateProduct(p);
                            dialog.dismiss();
                            Toast.makeText(context, context.getString(R.string.cambio_realizado), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //</editor-fold>
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }
}
