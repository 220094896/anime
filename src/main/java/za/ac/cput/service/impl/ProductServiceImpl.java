package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.domain.Product;
import za.ac.cput.repository.ProductRepository;
import za.ac.cput.service.ProductService;
import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Override
    public Product create(Product product) {
        return repository.save(product);
    }

    public Product saveProductWithImage(Product product, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            product.setProductImage(imageFile.getBytes());
        }
        return repository.save(product);
    }
    @Override
    public Product update(Product product) {
        return repository.save(product);
    }
    @Override
    public Product getById(Long id) {
        return repository.findById(id).orElse(null);
    }
    @Override
    public List<Product> getAll() {
        return repository.findAll();
    }
    @Override
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}