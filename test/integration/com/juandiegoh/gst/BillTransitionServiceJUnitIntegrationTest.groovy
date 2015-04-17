package com.juandiegoh.gst

import grails.transaction.Transactional
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class BillTransitionServiceJUnitIntegrationTest {

    static transactional = false

    BillTransitionService billTransitionService
    def sessionFactory

    @Test
    void updateAllOpenedBillsToStatusClosedClearingSession() {
        createAndSaveOpenBill()
        Number rowsUpdated = transitionBillsWithSessionClearance(true)

        assertTrue rowsUpdated == 1

        def bills = Bill.findAll()
        assertTrue bills.status.every { it == 'CLOSED' }
    }

    @Test
    void updateAllOpenedBillsToStatusClosedWithoutClearingSession() {
        createAndSaveOpenBill()
        Number rowsUpdated = transitionBillsWithSessionClearance(false)

        assertTrue rowsUpdated == 1

        def bills = Bill.findAll()
        assertTrue bills.status.every { it == 'CLOSED' }
    }

    private Number transitionBillsWithSessionClearance(Boolean sessionClearance) {
        def rowsUpdated = billTransitionService.transitionOpenToClosed()

        if (sessionClearance) {
            sessionFactory.getCurrentSession().clear()
        }
        rowsUpdated
    }

    @Transactional
    def createAndSaveOpenBill() {
        new Bill(status: 'OPEN').save(failOnError: true)
    }

    @After
    @Transactional
    void deleteAllBills() {
        Bill.findAll()*.delete()
    }
}