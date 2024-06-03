package detailsaver.detailsaver.resources;

import detailsaver.detailsaver.models.Detail;
import detailsaver.detailsaver.services.DetailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/details")
public class DetailResource {

    @Autowired
    DetailService detailService;

    @GetMapping("")
    public ResponseEntity<List<Detail>> getAllDetails(HttpServletRequest request) {
        int userId = (Integer) request.getAttribute("userId");
        List<Detail> userDetails = detailService.fetchAllDetails(userId);
        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }

    @GetMapping("/{detailId}")
    public ResponseEntity<Detail> getDetailById(HttpServletRequest request, @PathVariable("detailId") Integer detailId){
        int userId = (Integer) request.getAttribute("userId");
        Detail detail = detailService.fetchDetailById(userId, detailId);
        return new ResponseEntity<>(detail, HttpStatus.OK);
    }


    @PostMapping("")
    public ResponseEntity<Detail> addDetail(HttpServletRequest request, @RequestBody Map<String, Object> detailMap){
        int userId = (Integer) request.getAttribute("userId");
        String username = detailMap.get("username").toString();
        String password = detailMap.get("password").toString();
        String note = detailMap.get("note").toString();

        Detail detail = detailService.addDetail(userId, username, password, note);

        return new ResponseEntity<>(detail, HttpStatus.CREATED);
    }

    @PutMapping("/{detailId}")
    public ResponseEntity<Map<String, Boolean>> updateDetail(HttpServletRequest request, @RequestBody Detail detail, @PathVariable("detailId") Integer detailId){
        int userId =  (Integer) request.getAttribute("userId");
        detailService.updateDetail(userId, detailId, detail );
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{detailId}")
    public ResponseEntity<Map<String, Boolean>> deleteDetail(HttpServletRequest request, @PathVariable("detailId") Integer detailId){
        int userId = (Integer) request.getAttribute("userId");
        detailService.removeDetail(userId, detailId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
