package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.User;

public interface CryptoService {

    boolean checkValidTOTP(User user, String code);
    String generateTOTP(String userEmail, String password);
    String generateRecoverToken(String code);
    boolean checkValidRecoverToken(User user, String token);
}
