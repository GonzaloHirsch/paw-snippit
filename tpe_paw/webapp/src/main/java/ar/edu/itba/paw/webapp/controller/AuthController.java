package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.JwtTokenAuthenticationService;
import ar.edu.itba.paw.webapp.auth.JwtTokenFactory;
import ar.edu.itba.paw.webapp.dto.LoginDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.utility.Constants.PATH_PARAM_ID;

@Component
@Path("/auth")
public class AuthController {

    @Autowired
    private JwtTokenAuthenticationService tokenAuthenticationService;
    @Autowired
    private JwtTokenFactory tokenFactory;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @POST
    @Path("/login")
    public Response login(LoginDto loginDto) {
        return Response.ok().build();
    }
}
