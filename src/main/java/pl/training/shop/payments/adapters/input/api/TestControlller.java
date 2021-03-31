package pl.training.shop.payments.adapters.input.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("test")
public class TestControlller {

    @GET
    public String test() {
        return "ok";
    }

}
