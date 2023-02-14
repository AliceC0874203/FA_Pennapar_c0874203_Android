package com.example.fa_pennapar_c0874203_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.fa_pennapar_c0874203_android.adapter.ProductAdapter;
import com.example.fa_pennapar_c0874203_android.db.Product;
import com.example.fa_pennapar_c0874203_android.db.ProductViewModel;
import com.example.fa_pennapar_c0874203_android.swipe_cell.SwipeToDeleteCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListProductActivity extends AppCompatActivity {

    public static final int ADD_PRODUCT_REQUEST = 1;
    public static final int EDIT_PRODUCT_REQUEST = 2;

    private ProductViewModel productViewModel;

    private RecyclerView recyclerView;
    private ProductAdapter adapter;

    private FloatingActionButton buttonAddProduct;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        setRecyclerViewAndAdapter();
        enableProductViewModel();
        enableAddProductClickLisener();
        enableOnItemCellClickListener();
        enableSwipeToDelete();
        enableSearch();
    }

    private void addDummyData() {
        if (productViewModel.getAllProducts().getValue().size() == 0) {
            for (int i = 1; i <= 10; i++) {
                productViewModel.insert(new Product("Product " + i, "Description for product " + i, 10 * i, 34.0 + i, 56.0 + i));
            }
        }
    }

    private void setRecyclerViewAndAdapter() {
        recyclerView = findViewById(R.id.recycler_view_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ProductAdapter(new ArrayList<Product>());
        recyclerView.setAdapter(adapter);
    }

    private void enableProductViewModel() {
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                addDummyData();
                adapter.setProducts(products);
            }
        });
    }

    private void enableAddProductClickLisener() {
        buttonAddProduct = findViewById(R.id.floating_action_button_add_product);
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListProductActivity.this, AddProductActivity.class);
                startActivityForResult(intent, ADD_PRODUCT_REQUEST);
            }
        });
    }

    private void enableOnItemCellClickListener() {
        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(ListProductActivity.this, AddProductActivity.class);
                intent.putExtra(AddProductActivity.EXTRA_ID, product.getId());
                intent.putExtra(AddProductActivity.EXTRA_NAME, product.getName());
                intent.putExtra(AddProductActivity.EXTRA_DESCRIPTION, product.getDescription());
                intent.putExtra(AddProductActivity.EXTRA_PRICE, product.getPrice());
                intent.putExtra(AddProductActivity.EXTRA_LAT, product.getLatitude());
                intent.putExtra(AddProductActivity.EXTRA_LONG, product.getLongitude());
                startActivityForResult(intent, EDIT_PRODUCT_REQUEST);
            }
        });
    }

    private void enableSearch() {
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void enableSwipeToDelete() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListProductActivity.this);
                builder.setMessage("Are you sure you want to delete this product?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                productViewModel.delete(adapter.getProductAt(viewHolder.getAdapterPosition()));
                                Toast.makeText(ListProductActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .create()
                        .show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PRODUCT_REQUEST && resultCode == RESULT_OK) {
            String productName = data.getStringExtra(AddProductActivity.EXTRA_NAME);
            String productDescription = data.getStringExtra(AddProductActivity.EXTRA_DESCRIPTION);
            double productPrice = data.getDoubleExtra(AddProductActivity.EXTRA_PRICE, 0);
            double productLatitude = data.getDoubleExtra(AddProductActivity.EXTRA_LAT, 0);
            double productLongitude = data.getDoubleExtra(AddProductActivity.EXTRA_LONG, 0);

            Product product = new Product(productName, productDescription, productPrice, productLatitude, productLongitude);
            productViewModel.insert(product);

            Toast.makeText(this, "Product saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_PRODUCT_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddProductActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Product can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String productName = data.getStringExtra(AddProductActivity.EXTRA_NAME);
            String productDescription = data.getStringExtra(AddProductActivity.EXTRA_DESCRIPTION);
            double productPrice = data.getDoubleExtra(AddProductActivity.EXTRA_PRICE, 0);
            double productLatitude = data.getDoubleExtra(AddProductActivity.EXTRA_LAT, 0);
            double productLongitude = data.getDoubleExtra(AddProductActivity.EXTRA_LONG, 0);

            Product product = new Product(productName, productDescription, productPrice, productLatitude, productLongitude);
            product.setId(id);
            productViewModel.update(product);

            Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Product not saved", Toast.LENGTH_SHORT).show();
        }
    }
}