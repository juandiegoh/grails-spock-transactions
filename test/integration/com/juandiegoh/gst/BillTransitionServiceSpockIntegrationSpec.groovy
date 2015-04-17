package com.juandiegoh.gst

import grails.test.spock.IntegrationSpec
import grails.transaction.Transactional
import spock.lang.Unroll

class BillTransitionServiceSpockIntegrationSpec extends IntegrationSpec {

    static transactional = false

    BillTransitionService billTransitionService
    def sessionFactory

    def cleanup() {
        deleteAllBills()
    }

    @Unroll
    void "update all opened Bills to status CLOSED"() {
        given:
        createAndSaveOpenBill()

        when:
        def rowsUpdated = billTransitionService."$method"()

        and:
        if (clearSession) {
            sessionFactory.getCurrentSession().clear()
        }

        then:
        rowsUpdated == 1

        then:
        def bills = Bill.findAll()
        bills.status.every { it == 'CLOSED' }

        where:
        desc                 | method                       | clearSession
        'where and update'   | 'transitionOpenToClosed'     | false
        'where and update'   | 'transitionOpenToClosed'     | true
        'findAll and update' | 'transitionOpenToClosedLoop' | false
        'findAll and update' | 'transitionOpenToClosedLoop' | true
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
