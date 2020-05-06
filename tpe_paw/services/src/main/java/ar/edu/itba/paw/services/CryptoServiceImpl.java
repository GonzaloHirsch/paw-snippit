package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.CryptoService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.helpers.crypto.HashGenerator;
import ar.edu.itba.paw.services.helpers.crypto.WebappCrypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CryptoServiceImpl implements CryptoService {

    @Autowired
    private UserService userService;

    @Override
    public boolean checkValidRecoveryToken(long id, String token) {
        Optional<User> userOpt = userService.findUserById(id);
        if(!userOpt.isPresent()) {
            // TODO Resource Not Found
        }
        User user = userOpt.get();
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