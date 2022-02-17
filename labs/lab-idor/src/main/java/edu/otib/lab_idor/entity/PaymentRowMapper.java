package edu.otib.lab_idor.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentRowMapper implements RowMapper<Payment> {
    @Override
    public Payment mapRow(ResultSet resultSet, int i) throws SQLException {
        Payment payment = new Payment();
        payment.setId(resultSet.getInt("id"));
        payment.setUser(resultSet.getString("username"));
        payment.setAmount(resultSet.getInt("amount"));
        payment.setDescription(resultSet.getString("description"));
        return payment;
    }
}
