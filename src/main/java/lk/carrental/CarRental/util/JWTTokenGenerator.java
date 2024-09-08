package lk.carrental.CarRental.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lk.carrental.CarRental.model.Customer;
import lk.carrental.CarRental.model.User;
import lk.carrental.CarRental.service.CustomerService;
import lk.carrental.CarRental.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTTokenGenerator {

    private static final Logger logger = LoggerFactory.getLogger(JWTTokenGenerator.class);

    @Value("${carrental.app.jwtSecret}")
    private String jwtSecret;

    @Value("${carrental.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private final UserService userService;
    private final CustomerService customerService;


    @Autowired
    public JWTTokenGenerator(UserService userService, CustomerService customerService) {
        this.userService = userService;
        this.customerService = customerService;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateJwtTokenForUser(User user) {

        return Jwts.builder()
                .setId(String.valueOf(user.getUserId()))
                .setSubject((user.getUserName()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateJwtTokenForCustomer(Customer customer) {

        return Jwts.builder()
                .setId(String.valueOf(customer.getCustomerId()))
                .setSubject((customer.getUserName()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        String jwtToken = authToken.substring("Bearer ".length());
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(jwtToken);
            return true;
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        }

        return false;
    }

    public User getUserFromJwtToken(String token) {
        String jwtToken = token.substring("Bearer ".length());
        String id = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(jwtToken).getBody().getId();
        return userService.getUserById(id);
    }

    public Customer getCustomerFromJwtToken(String token) {
        String jwtToken = token.substring("Bearer ".length());
        String id = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(jwtToken).getBody().getId();
        return customerService.getCustomerById(id);
    }
}
