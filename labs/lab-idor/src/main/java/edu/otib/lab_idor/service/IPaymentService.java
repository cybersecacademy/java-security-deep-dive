package edu.otib.lab_idor.service;

import edu.otib.lab_idor.entity.Payment;

import java.util.List;

public interface IPaymentService {
    List<Payment> getPaymentsByUser(String username);
    Payment getPaymentById(Integer id);
}
