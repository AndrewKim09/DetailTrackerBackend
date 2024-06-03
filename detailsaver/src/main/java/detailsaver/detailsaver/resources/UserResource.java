package detailsaver.detailsaver.resources;

import detailsaver.detailsaver.Constants;
import detailsaver.detailsaver.models.User;
import detailsaver.detailsaver.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.util.Elements;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap){
        System.out.println("hi");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        System.out.println("hi");
        User user = userService.validateUser(email, password);
        System.out.println(user.getUserId());
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userMap) {
        System.out.println(userMap);
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");

        User user = userService.registerUser(email, password);

        Map<String, String> map = new HashMap<>();
        map.put("message", "registered successfully");

        return new ResponseEntity<>(map, HttpStatus.OK);

    }

    private Map<String, String> generateJWTToken(User user) {
        long timestamp = System.currentTimeMillis();


        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("userId", user.getUserId())
                .claim("email", user.getEmail())
                .compact();

        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;


    }
}
