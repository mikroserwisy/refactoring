package pl.training.shop.payments;

import javax.ejb.Local;

@Local
public interface PaymentIdGenerator {

    String getNext();

}
