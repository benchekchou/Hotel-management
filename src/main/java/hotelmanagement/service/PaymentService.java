package hotelmanagement.service;

import hotelmanagement.model.Payment;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface PaymentService {
    void addPayment(Payment payment);
    void deletePayment(int paymentId);
    void updatePayment(Payment payment);
    List<Payment> getAllPayments();
    Payment getPaymentById(int paymentId);
}