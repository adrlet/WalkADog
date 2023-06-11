package billennium.faculties.walkadog.presentation;

import billennium.faculties.walkadog.application.dto.LoginRequest;
import billennium.faculties.walkadog.application.dto.LoginTrainerResponse;
import billennium.faculties.walkadog.application.dto.LoginUserResponse;
import billennium.faculties.walkadog.domain.Trainer;
import billennium.faculties.walkadog.domain.Users;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/login")
@AllArgsConstructor
public class AppController {

    @Qualifier("trainer")
    @Autowired
    private AuthenticationManager authenticationTrainerManager;


    @Qualifier("user")
    @Autowired
    private AuthenticationManager authenticationUserManager;



    @PostMapping("/trainer")
    public ResponseEntity<?> trainerLogin(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationTrainerManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Trainer trainer = (Trainer) authentication.getPrincipal();
        LoginTrainerResponse response = new LoginTrainerResponse();
        response.setTrainerId(trainer.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationUserManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Users users = (Users) authentication.getPrincipal();
        LoginUserResponse response = new LoginUserResponse();
        response.setUserId(users.getId());
        return ResponseEntity.ok(response);
    }
}
