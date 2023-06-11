package billennium.faculties.walkadog.presentation;

import billennium.faculties.walkadog.application.RegistrationService;
import billennium.faculties.walkadog.application.UsersService;
import billennium.faculties.walkadog.application.dto.RegistrationRequestDto;
import billennium.faculties.walkadog.application.dto.UserAccountDetailsChangeDto;
import billennium.faculties.walkadog.application.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "${app.rest.wad-path}/users")
@AllArgsConstructor
public class UsersController {
    private final RegistrationService registrationService;
    private final UsersService usersService;


    @PostMapping("/registration")
    public String register(@RequestBody RegistrationRequestDto request) {
        return registrationService.register(request);
    }

    @GetMapping("/registration/confirm")
    public String confirmAccount(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

    //todo add edit endpoint
    @GetMapping("/registration/resend")
    public String resendEmail(@RequestParam("email") String email) {
        return registrationService.resendActivationEmail(email);
    }

    @PostMapping("/edit")
    public String editAccountById(@RequestBody UserAccountDetailsChangeDto userAccountDetailsChangeDto, @RequestParam Long userId) {
        return usersService.updateAccountDetails(userAccountDetailsChangeDto, userId);
    }

    @GetMapping("/user")
    public List<UserDto> getUserById(@RequestParam Long userId) {
        return usersService.getUserData(userId);
    }


}