package com.example.ecomerce.shop.web.controller.admin;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecomerce.shop.business.services.AdminProductService;
import com.example.ecomerce.shop.business.services.FAQService;
import com.example.ecomerce.shop.web.dto.FAQDto;
import com.example.ecomerce.shop.web.dto.ProductDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;
     private final FAQService faqService;

    @PostMapping("/product")
    public ResponseEntity<ProductDto> addProduct(@ModelAttribute ProductDto productDto) throws IOException {
        ProductDto productDto2 = adminProductService.addProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto2);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productDtos = adminProductService.getAllProducts();
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/products/search")
public ResponseEntity<List<ProductDto>> getAllProductByName(@RequestParam String name) {
    List<ProductDto> productDtos = adminProductService.getProductByName(name);
    return ResponseEntity.ok(productDtos);
}

@DeleteMapping("/product/{id}")
public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    adminProductService.deleteProduct(id);
    return ResponseEntity.noContent().build();
}

@PostMapping("/faq/{productId}")
public  ResponseEntity<FAQDto> postFAQ(@PathVariable Long productId, @RequestBody FAQDto faqDto){
    return ResponseEntity.status(HttpStatus.CREATED).body(faqService.postFAQ(productId, faqDto));

}

@GetMapping("/product/{productId}")
public ResponseEntity<ProductDto>getProductById(@PathVariable Long productId){
    ProductDto productDto=adminProductService.getProductById(productId);
    if(productDto!=null){
        return ResponseEntity.ok(productDto);
    }else{
        return ResponseEntity.notFound().build();
    }
}

@PutMapping("/product/{productId}")
public ResponseEntity<ProductDto>updateProduct(@PathVariable Long productId,@ModelAttribute ProductDto productDto){
    ProductDto updatedProduct=adminProductService.updateProduct(productId,productDto);
    if(updatedProduct!=null){
        return ResponseEntity.ok(updatedProduct);
    }else{
        return ResponseEntity.notFound().build();
    }
}



}
