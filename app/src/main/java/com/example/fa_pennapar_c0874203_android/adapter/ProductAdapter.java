package com.example.fa_pennapar_c0874203_android.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fa_pennapar_c0874203_android.ListProductActivity;
import com.example.fa_pennapar_c0874203_android.MapActivity;
import com.example.fa_pennapar_c0874203_android.R;
import com.example.fa_pennapar_c0874203_android.db.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private static List<Product> products;
    private List<Product> productsFull;
    private OnItemClickListener listener;

    private List<Product> filteredProducts;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewPrice;
        private TextView textViewLocation;
        private TextView textViewDescription;
        private Button locationBtn;

        ProductViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.name);
            textViewPrice = itemView.findViewById(R.id.price);
            textViewLocation = itemView.findViewById(R.id.location);
            textViewDescription = itemView.findViewById(R.id.description);
            locationBtn = itemView.findViewById(R.id.locationBtn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(products.get(position));
                        }
                    }
                }
            });

            locationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent i = new Intent(v.getContext(), MapActivity.class);
                    i.putExtra("lat",products.get(position).getLatitude());
                    i.putExtra("lng",products.get(position).getLongitude());
                    i.putExtra("title",products.get(position).getName());
                    v.getContext().startActivity(i);
                }
            });
        }
    }

    public ProductAdapter(List<Product> products) {
        this.products = products;
        productsFull = new ArrayList<>(products);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product currentProduct = products.get(position);
        holder.textViewName.setText(currentProduct.getName());
        holder.textViewPrice.setText(String.valueOf(currentProduct.getPrice()));
        holder.textViewLocation.setText(currentProduct.getLocation());
        holder.textViewDescription.setText(String.valueOf(currentProduct.getDescription()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public Product getProductAt(int position) {
        return products.get(position);
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        productsFull = new ArrayList<>(products);
        notifyDataSetChanged();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString();
                if (query.isEmpty()) {
                    filteredProducts = productsFull;
                } else {
                    List<Product> filteredList = new ArrayList<>();
                    for (Product product : productsFull) {
                        if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(product);
                        }
                    }
                    filteredProducts = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredProducts;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                products.clear();
                products.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
