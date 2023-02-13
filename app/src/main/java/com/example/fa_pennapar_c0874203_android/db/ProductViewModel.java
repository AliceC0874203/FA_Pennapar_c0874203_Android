package com.example.fa_pennapar_c0874203_android.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private ProductRepository repository;

    private LiveData<List<Product>> allEmployees;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        repository = new ProductRepository(application);
        allEmployees = repository.getAllProducts();
    }

    LiveData<List<Product>> getAllProducts() {
        return allEmployees;
    }

    public void insert(Product product) {
        repository.insert(product);
    }

    public void update(Product product) {
        repository.update(product);
    }

    public void delete(Product product) {
        repository.delete(product);
    }
}
