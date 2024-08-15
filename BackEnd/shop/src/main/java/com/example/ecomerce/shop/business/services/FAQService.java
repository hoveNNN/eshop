package com.example.ecomerce.shop.business.services;

import com.example.ecomerce.shop.web.dto.FAQDto;

public interface FAQService {
    FAQDto postFAQ(Long productId,FAQDto faqDto);
}