package pl.training.shop.products.domain;

import lombok.Value;

@Value
public class Score implements Comparable<Score> {

    Long value;
    Long productId;

    @Override
    public int compareTo(Score otherScore) {
        return otherScore.value.compareTo(value);
    }

}
