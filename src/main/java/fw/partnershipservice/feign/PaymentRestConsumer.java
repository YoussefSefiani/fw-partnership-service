package fw.partnershipservice.feign;

import fw.partnershipservice.model.CheckoutPayment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="fw-payment-service", url="${app.services.payment.url}")
public interface PaymentRestConsumer {

    @PostMapping("/api/payment")
    void payPartnership(@PathVariable("partnershipId") Long partnershipId, @RequestBody CheckoutPayment checkoutPayment);
}
