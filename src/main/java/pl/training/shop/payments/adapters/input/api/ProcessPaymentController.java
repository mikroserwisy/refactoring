package pl.training.shop.payments.adapters.input.api;

import lombok.Setter;
import pl.training.shop.payments.ports.input.ProcessPaymentUseCase;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("payments")
@Setter
public class ProcessPaymentController {

    @Inject
    private ProcessPaymentUseCase processPaymentUseCase;
    @Inject
    private ApiMapper apiMapper;

    @POST
    public Response processPayment(PaymentRequestDto paymentRequestDto) throws URISyntaxException {
        var paymentRequest = apiMapper.toDomain(paymentRequestDto);
        var payment = processPaymentUseCase.process(paymentRequest);
        var paymentDto = apiMapper.toDto(payment);
        return Response.created(new URI("payments/" + payment.getId()))
                .entity(paymentDto)
                .build();
    }

}
