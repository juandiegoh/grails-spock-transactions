package com.juandiegoh.gst

class TransitionJob {

    BillTransitionService billTransitionService

    static triggers = {
        simple name:'transitionTrigger', startDelay:0, repeatCount: 0
    }

    def execute() {
        billTransitionService.transitionOpenToClosed()
    }
}
