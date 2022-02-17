package edu.otib.lab_idor.dao;

import edu.otib.lab_idor.entity.Payment;
import edu.otib.lab_idor.entity.PaymentRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class PaymentDAO implements IPaymentDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    final static String SQL_GET_PAYMENTS_BY_USER = "SELECT * FROM payment WHERE username = ?";
    final static String SQL_GET_PAYMENT_BY_ID = "SELECT * FROM payment WHERE id = ?";

    @Override
    public List<Payment> getPaymentsByUser(String username) {
        RowMapper rowMapper = new PaymentRowMapper();
        return jdbcTemplate.query(SQL_GET_PAYMENTS_BY_USER, new Object[] { username }, rowMapper);
    }

    @Override
    public Payment getPaymentById(Integer id) {
        RowMapper rowMapper = new PaymentRowMapper();
        Payment payment = (Payment)jdbcTemplate.queryForObject(SQL_GET_PAYMENT_BY_ID, new Object[] {id}, rowMapper);
        return payment;
    }
}
