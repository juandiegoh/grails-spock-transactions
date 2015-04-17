package com.juandiegoh.gst

import grails.transaction.Transactional
import org.junit.After
import org.junit.Test

public class BillTransitionServiceJUnitIntegrationTest {

    static transactional = false

    BillTransitionService billTransitionService
    def sessionFactory

    @Test
    void updateAllOpenedBillsToStatusClosedClearingSession() {
        createAndSaveOpenBill()
        Number rowsUpdated = transitionBillsWithSessionClearance(true)

        assert 1, rowsUpdated

        def bills = Bill.findAll()
        assert bills.status.every { it == 'CLOSED' }
    }

    @Test
    void updateAllOpenedBillsToStatusClosedWithoutClearingSession() {
        createAndSaveOpenBill()
        Number rowsUpdated = transitionBillsWithSessionClearance(false)

        assert 1, rowsUpdated

        def bills = Bill.findAll()
        assert bills.status.every { it == 'CLOSED' }
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