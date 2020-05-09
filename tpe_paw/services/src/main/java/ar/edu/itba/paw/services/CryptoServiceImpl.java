package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.CryptoService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.helpers.crypto.HashGenerator;
import ar.edu.itba.paw.services.helpers.crypto.WebappCrypto;
import org.springframework.stereotype.Service;

@Service
public class CryptoServiceImpl implements CryptoService {

    @Override
    public boolean checkValidTOTP(User user, String code) {
        // Generating secret key for TOTP
        String key = HashGenerator.getInstance().generateSecretKey(user.getEmail(), user.getPassword());
        // Generating valid TOTPS
        String[] otps = WebappCrypto.generateOtps(key);
        String base64Token;
        boolean pass = false;
        for (int i = 0; i < 3; i++) {
            pass = pass || code.equals(otps[i]);
        }
        return pass;
    }

    @Override
    public String generateTOTP(String userEmail, String password) {
        // Generating secret key for TOTP
        String key = HashGenerator.getInstance().generateSecretKey(userEmail, password);
        // Generating totp
        return WebappCrypto.generateOtp(key);
    }
}