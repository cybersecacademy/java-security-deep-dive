package edu.otib.lab_idor.dao;

import edu.otib.lab_idor.entity.Payment;

import java.util.List;

public interface IPaymentDAO {
    List<Payment> getPaymentsByUser(String username);
    Payment getPaymentById(Integer id);
}
