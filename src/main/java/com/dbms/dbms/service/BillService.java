package com.dbms.dbms.service;

import com.dbms.dbms.dao.BillDao;
import com.dbms.dbms.dto.Bill;
import com.dbms.dbms.dto.BillEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillService {

    private final BillDao billDao;

    @Autowired
    public BillService(BillDao billDao) {
        this.billDao = billDao;
    }

    public void createBill(Bill bill) {
        bill.setTime(LocalDateTime.now());
        billDao.createNewBill(bill);
    }

    public List<Bill> getBillsForPatient(int patientId) {
        return billDao.viewBillForPatient(patientId);
    }

    public List<BillEntry> getDetailedBill(int billId) {
        return billDao.getBillEntries(billId);
    }

    public void pay(int billId, int amt) {
        billDao.addPayment(billId, amt);
    }
}
