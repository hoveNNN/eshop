package com.example.ecomerce.shop.business.servicesImpl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.ecomerce.shop.business.services.AdminProductService;
import com.example.ecomerce.shop.dao.entity.Category;
import com.example.ecomerce.shop.dao.entity.Product;
import com.example.ecomerce.shop.dao.repository.CategoryRepository;
import com.example.ecomerce.shop.dao.repository.ProductRepository;
import com.example.ecomerce.shop.web.dto.ProductDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductDto addProduct(ProductDto productDto) throws IOException {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());

        // Convert MultipartFile to byte[] and set it
        if (productDto.getImg() != null) {
            product.setImg(productDto.getImg().getBytes());
        } else {
            product.setImg(productDto.getByteImg());
        }

        Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow();
        product.setCategory(category);
        return productRepository.save(product).getDto();
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }
    public List<ProductDto> getProductByName(String name) {
        List<Product> products = productRepository.findByName(name);
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public ProductDto  getProductById(Long productId){
        Optional<Product>  optionalProduct=productRepository.findById(productId);
        if(optionalProduct.isPresent()){
            return optionalProduct.get().getDto();
        }else{
            return null;
        }
    }

    public ProductDto updateProduct(Long productId,ProductDto productDto){
        Optional<Product>  optionalProduct=productRepository.findById(productId);
        Optional<Category> optionalCategory= categoryRepository.findById(productDto.getCategoryId());

        if(optionalProduct.isPresent()&& optionalCategory.isPresent()){
            Product product=optionalProduct.get();

            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());
            product.setCategory(optionalCategory.get());
            if (productDto.getImg()!=null) {
                try {
                    product.setImg(productDto.getImg().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return productRepository.save(product).getDto();

    }else{
        return null;
    }
}

}
