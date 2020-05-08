package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.CryptoService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.helpers.crypto.HashGenerator;
import ar.edu.itba.paw.services.helpers.crypto.WebappCrypto;
import org.springframework.stereotype.Service;

@Service
public class CryptoServiceImpl implements CryptoService {

    @Override
    public boolean checkValidRecoveryToken(User user, String token) {
        String[] otps = WebappCrypto.generateOtps(WebappCrypto.TEST_KEY);
        String base64Token;
        boolean pass = false;
        for (int i = 0; i < 3; i++) {
            base64Token = HashGenerator.getInstance().generateRecoveryHash(user.getEmail(), user.getPassword(), otps[i]);
            pass = pass || token.compareTo(base64Token) == 0;
        }
        return pass;
    }

    @Override
    public String generateTOTP(String userEmail, String password) {
        String otp = WebappCrypto.generateOtp(WebappCrypto.TEST_KEY);
        return HashGenerator.getInstance().generateRecoveryHash(userEmail, password, otp);
    }
}