package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Payment;
import za.ac.cput.repository.PaymentRepository;
import za.ac.cput.domain.Customer;
import za.ac.cput.domain.CustomerOrder;
import za.ac.cput.repository.CustomerRepository;
import za.ac.cput.repository.CustomerOrderRepository;
import java.time.LocalDateTime;

import java.util.List;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Override
    public Payment create(Payment payment) {
        if (payment.getCustomer() == null || payment.getCustomer().getUserId() == null) {
            throw new IllegalArgumentException("Customer information missing in payment request.");
        }
        // Fetch customer
        Customer customer = customerRepository.findById(payment.getCustomer().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found."));
        // Create linked order
        CustomerOrder customerOrder = new CustomerOrder.Builder()
                .setOrderDate(LocalDateTime.now())
                .setStatus("PAID")
                .build();
        // Link both sides
        payment = new Payment.Builder()
                .copy(payment)
                .setCustomer(customer)
                .setCustomerOrder(customerOrder)
                .build();
        // Persist order first, then payment
        customerOrderRepository.save(customerOrder);
        Payment savedPayment = paymentRepository.save(payment);

        return savedPayment;
    }
    @Override
    public Payment read(Long paymentId) {
        return paymentRepository.findById(paymentId).orElse(null);
    }

    @Override
    public Payment update(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public boolean delete(Long paymentId) {
        paymentRepository.deleteById(paymentId);
        return true;
    }

    @Override
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }
}
