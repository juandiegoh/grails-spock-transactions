package com.juandiegoh.gst

import grails.transaction.Transactional
import org.springframework.transaction.annotation.Propagation

@Transactional
class BillTransitionService {

    def transitionOpenToClosed() {
        Bill.where { status == 'OPEN' }.updateAll(status: 'CLOSED')
    }

    def transitionOpenToClosedLoop() {
        def billsToClose = Bill.findAllByStatus('OPEN')
        billsToClose*.status = 'CLOSED'
        return billsToClose.size()
    }
}
