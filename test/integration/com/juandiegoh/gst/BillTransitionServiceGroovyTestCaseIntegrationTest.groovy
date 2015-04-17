package com.juandiegoh.gst

import grails.transaction.Transactional

class BillTransitionServiceGroovyTestCaseIntegrationTest extends GroovyTestCase {

    static transactional = false

    BillTransitionService billTransitionService
    def sessionFactory

    @Override
    protected void tearDown() throws Exception {
        super.tearDown()
        deleteAllBills()
    }

    void testUpdateAllOpenedBillsToStatusClosedClearingSession() {
        createAndSaveOpenBill()
        Number rowsUpdated = transitionBillsWithSessionClearance(true)

        assert 1 == rowsUpdated

        def bills = Bill.findAll()
        assert bills.status.every { it == 'CLOSED' }
    }

    void testUpdateAllOpenedBillsToStatusClosedWithoutClearingSession() {
        createAndSaveOpenBill()
        Number rowsUpdated = transitionBillsWithSessionClearance(false)

        assert 1 == rowsUpdated

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

    @Transactional
    void deleteAllBills() {
        Bill.findAll()*.delete()
    }
}
