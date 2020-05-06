package ar.edu.itba.paw.interfaces.service;

public interface CryptoService {

    boolean checkValidRecoveryToken(long id, String token);
    String generateTOTP(String userEmail, String password);
}
