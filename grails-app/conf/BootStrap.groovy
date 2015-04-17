import com.juandiegoh.gst.Bill
import grails.util.Environment

class BootStrap {

    def init = { servletContext ->
        if (Environment.isDevelopmentMode()) {
            new Bill(status: 'OPEN').save(failOnError: true)
        }
    }

    def destroy = {
    }
}
