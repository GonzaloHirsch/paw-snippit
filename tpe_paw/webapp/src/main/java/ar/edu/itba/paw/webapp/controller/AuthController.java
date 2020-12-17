package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.LoginDto;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Component
@Path("/auth")
public class AuthController {

    @POST
    @Path("/login")
    public Response login(LoginDto loginDto) {
        return Response.ok().build();
    }
}
