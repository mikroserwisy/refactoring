package pl.training.shop.payments.application;

import javax.ejb.Local;

@Local
public interface PaymentIdGenerator {

    String getNext();

}
