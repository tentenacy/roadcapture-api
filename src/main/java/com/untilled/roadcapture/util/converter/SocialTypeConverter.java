package com.untilled.roadcapture.util.converter;

import com.untilled.roadcapture.util.constant.SocialType;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
class SocialTypeConverter implements Converter<String, SocialType> {
    @Override
    public SocialType convert(String s) {
        return SocialType.valueOf(s.toUpperCase());
    }
}