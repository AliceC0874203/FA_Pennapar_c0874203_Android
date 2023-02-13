package com.example.fa_pennapar_c0874203_android.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductRepository {

    private static ProductDao productDao;
    private LiveData<List<Product>> allProduct;
    private Executor executor = Executors.newSingleThreadExecutor();

    ProductRepository(Application application) {
        ProductDatabase database = ProductDatabase.getDatabase(application);
        productDao = database.employeeDao();
        allProduct = productDao.getAllProducts();
    }

    LiveData<List<Product>> getAllProducts() {
        return allProduct;
    }

    public void insert(Product product) {
        executor.execute(() -> productDao.insert(product));
    }

    public void delete(Product product) {
        executor.execute(() -> productDao.delete(product));
    }

    public void update(Product product) {
        executor.execute(() -> productDao.update(product));
    }
}
