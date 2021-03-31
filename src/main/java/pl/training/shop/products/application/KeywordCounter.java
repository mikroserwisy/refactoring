package pl.training.shop.products.application;

import java.util.regex.Pattern;

class KeywordCounter {

    long count(String text, String keyword) {
        return Pattern.compile(keyword, Pattern.CASE_INSENSITIVE)
                .matcher(text)
                .results()
                .count();
    }

}
