package pl.training.shop.commons;

import org.javamoney.moneta.FastMoney;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class FastMoneyConverter implements AttributeConverter<FastMoney, String> {

    @Override
    public String convertToDatabaseColumn(FastMoney fastMoney) {
        return fastMoney != null ? fastMoney.toString() : null;
    }

    @Override
    public FastMoney convertToEntityAttribute(String text) {
        return text != null ? FastMoney.parse(text) : LocalMoney.zero();
    }

}
