package pl.training.shop.payments.adapters.input.api;

import lombok.Setter;
import pl.training.shop.payments.ports.input.ProcessPaymentUseCase;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("payments")
@Setter
public class ProcessPaymentController {

    @Context
    private UriInfo uriInfo;
    @Inject
    private ProcessPaymentUseCase processPaymentUseCase;
    @Inject
    private ApiMapper apiMapper;

    @POST
    public Response processPayment(PaymentRequestDto paymentRequestDto) {
        var paymentRequest = apiMapper.toDomain(paymentRequestDto);
        var payment = processPaymentUseCase.process(paymentRequest);
        var paymentDto = apiMapper.toDto(payment);
        var locationUri = currentUriWithId(payment.getId());
        return Response.created(locationUri)
                .entity(paymentDto)
                .build();
    }

    private URI currentUriWithId(String id) {
        return uriInfo.getAbsolutePathBuilder().path(id).build();
    }

}
