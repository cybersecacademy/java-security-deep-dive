package edu.otib.lab_idor.service;

import edu.otib.lab_idor.dao.IPaymentDAO;
import edu.otib.lab_idor.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService implements IPaymentService {
    @Autowired
    IPaymentDAO paymentDAO;

    @Override
    public List<Payment> getPaymentsByUser(String username) {
        return paymentDAO.getPaymentsByUser(username);
    }

    @Override
    public Payment getPaymentById(Integer id) {
        return paymentDAO.getPaymentById(id);
    }
}
