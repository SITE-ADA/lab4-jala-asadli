package az.edu.ada.wm2.lab4.service;

import az.edu.ada.wm2.lab4.model.Product;
import az.edu.ada.wm2.lab4.repository.ProductRepository;
import az.edu.ada.wm2.lab4.repository.ProductRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // IMPORTANT: signature must match interface
    @Override
    public Product updateProduct(Product product) {
        UUID id = product.getId();
        if (id == null || !productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getProductsExpiringBefore(LocalDate date) {
        return productRepository.findAll()
                .stream()
                .filter(p -> p.getExpirationDate() != null &&
                        p.getExpirationDate().isBefore(date))
                .toList();
    }

    @Override
    public List<Product> getProductsByPriceRange(BigDecimal min, BigDecimal max) {
        return productRepository.findAll()
                .stream()
                .filter(p -> p.getPrice() != null &&
                        p.getPrice().compareTo(min) >= 0 &&
                        p.getPrice().compareTo(max) <= 0)
                .toList();
    }
}