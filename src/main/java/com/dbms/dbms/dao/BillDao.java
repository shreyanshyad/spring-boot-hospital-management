package com.dbms.dbms.dao;

import com.dbms.dbms.dto.Bill;
import com.dbms.dbms.dto.BillEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class BillDao {
    private static final Logger logger = LoggerFactory.getLogger(BillDao.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BillDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createNewBill(Bill bill) {
        String sql = "insert into bill(patient_id, amount, amount_paid, time) " +
                "values(?,?,?,?)";
        Timestamp time = Timestamp.valueOf(bill.getTime());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String finalSql = sql;

        jdbcTemplate.update((PreparedStatementCreator) connection -> {
            PreparedStatement ps = connection.prepareStatement(finalSql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, bill.getPatientId());
            ps.setInt(2, (int) bill.getAmount());
            ps.setInt(3, (int) bill.getAmountPaid());
            ps.setTimestamp(4, time);
            return ps;
        }, keyHolder);

        int billId = keyHolder.getKey().intValue();

        for (BillEntry entry : bill.getEntries()) {
            sql = "insert into bill_cost(bill_id, name, quantity, cost) " +
                    "values(?,?,?,?)";
            jdbcTemplate.update(sql, billId, entry.getName(), entry.getQuantity(), entry.getCost());
        }
    }

    public List<Bill> viewBillForPatient(int patientId) {
        String sql = "select * from bill where patient_id=?";
        return jdbcTemplate.query(sql, mapBillFromDb(), patientId);
    }

    private RowMapper<Bill> mapBillFromDb() {
        return (resultSet, i) -> {
            Bill bill = new Bill();
            bill.setPatientId(resultSet.getInt("patient_id"));
            bill.setAmount(resultSet.getDouble("amount"));
            bill.setAmountPaid(resultSet.getDouble("amount_paid"));
            bill.setTime(resultSet.getTimestamp("time").toLocalDateTime());
            bill.setId(resultSet.getInt("bill_id"));
            return bill;
        };
    }

    public List<BillEntry> getBillEntries(int billId) {
        String sql = "select * from bill_cost where bill_id=?";
        return jdbcTemplate.query(sql, mapBillEntryFromDb(), billId);
    }

    private RowMapper<BillEntry> mapBillEntryFromDb() {
        return (resultSet, i) -> {
            BillEntry b = new BillEntry();
            b.setName(resultSet.getString("name"));
            b.setCost(resultSet.getDouble("cost"));
            b.setQuantity(resultSet.getInt("quantity"));
            return b;
        };
    }

    public void addPayment(int bill_id, int amt) {
        String sql = "update bill set amount_paid=? where bill_id=?";
        jdbcTemplate.update(sql, amt, bill_id);
    }
}

