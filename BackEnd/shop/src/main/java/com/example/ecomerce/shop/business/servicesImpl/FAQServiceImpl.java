package com.example.ecomerce.shop.business.servicesImpl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ecomerce.shop.business.services.FAQService;
import com.example.ecomerce.shop.dao.entity.FAQ;
import com.example.ecomerce.shop.dao.entity.Product;
import com.example.ecomerce.shop.dao.repository.FAQRepository;
import com.example.ecomerce.shop.dao.repository.ProductRepository;
import com.example.ecomerce.shop.web.dto.FAQDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService  {
    private final FAQRepository faqRepository;
    private final ProductRepository productRepository;

    public FAQDto postFAQ(Long productId,FAQDto faqDto){
        Optional<Product>optionalProduct=productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            FAQ faq = new FAQ();
            faq.setQuestion(faqDto.getQuestion());
            faq.setAnswer(faqDto.getAnswer());
            faq.setProduct(optionalProduct.get());

            return faqRepository.save(faq).getFAQDto();
        }
        return null;
    }
}