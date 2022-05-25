package fw.partnershipservice.feign;

import fw.partnershipservice.model.CheckoutPayment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;

@FeignClient(name="fw-payment-service", url="${app.services.payment.url}")
public interface PaymentRestConsumer {

    @PostMapping("/api/payment")
    HttpServletResponse payPartnership(CheckoutPayment checkoutPayment);
}
