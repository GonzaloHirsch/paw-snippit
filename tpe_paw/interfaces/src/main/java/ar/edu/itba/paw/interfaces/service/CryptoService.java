package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.User;

public interface CryptoService {

    boolean checkValidRecoveryToken(User user, String token);
    String generateTOTP(String userEmail, String password);
}
