package billennium.faculties.walkadog.application;

import billennium.faculties.walkadog.application.dto.UserAccountDetailsChangeDto;
import billennium.faculties.walkadog.application.dto.UserDto;
import billennium.faculties.walkadog.domain.ConfirmationToken;
import billennium.faculties.walkadog.domain.Users;
import billennium.faculties.walkadog.infrastructure.TrainerRepository;
import billennium.faculties.walkadog.infrastructure.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UsersService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final TrainerRepository trainerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final static String USER_NOT_FOUND = "User with %s was not found";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<Users> userDetailsOptional = usersRepository.findByUsername(username);
//        Optional<Trainer> trainerOptional = trainerRepository.findByUsername(username);
//        if(userDetailsOptional.isPresent()){
//            return userDetailsOptional.get();
//        }
//        if(trainerOptional.isPresent()){
//            return trainerOptional.get();
//        }
//
//        throw new UsernameNotFoundException(String.format(USER_NOT_FOUND, username));
        return usersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
    }



    //todo userrepo find user name
    //
    public String signUpUser(Users user) {
        boolean emailExist = usersRepository.findByEmail(user.getEmail()).isPresent();
        if (emailExist) {
            throw new IllegalStateException("Email already taken");
            //TODO RESEND ACTIVACTION EMAIL
        }
        boolean usernameExists = usersRepository.findByUsername(user.getUsername()).isPresent();
        if (usernameExists) {
            throw new IllegalStateException("Username already taken");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        usersRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .createdAt(LocalDateTime.now())
                .users(user)
                .build();
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public String resendActivationLink(String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalStateException("Email not found"));
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .createdAt(LocalDateTime.now())
                .users(user)
                .build();
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public void enableUser(String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalStateException("Wrong email"));
        user.setEnabled(true);
    }

    public String updateAccountDetails(UserAccountDetailsChangeDto userAccountDetailsChangeDto, Long userId) {

        Users users = usersRepository.getById(userId);
        if (userAccountDetailsChangeDto.getEmail() != null && !userAccountDetailsChangeDto.getEmail().equals(users.getEmail())) {
            boolean emailExist = usersRepository.findByEmail(userAccountDetailsChangeDto.getEmail()).isPresent();
            if (emailExist) {
                throw new IllegalStateException("Email already taken");
            } else {
                users.setEmail(userAccountDetailsChangeDto.getEmail());
            }
        }
        if (userAccountDetailsChangeDto.getFirstName() != null && !userAccountDetailsChangeDto.getEmail().equals(users.getName())) {
            users.setName(userAccountDetailsChangeDto.getFirstName());
        }
        if (userAccountDetailsChangeDto.getLastName() != null && !userAccountDetailsChangeDto.getEmail().equals(users.getLastName())) {
            users.setLastName(userAccountDetailsChangeDto.getLastName());
        }
        if (userAccountDetailsChangeDto.getPhoneNumber() != null && !userAccountDetailsChangeDto.getEmail().equals(users.getPhoneNumber())) {
            users.setPhoneNumber(userAccountDetailsChangeDto.getPhoneNumber());
        }
        if (userAccountDetailsChangeDto.getPassword() != null) {
            String encodedPassword = bCryptPasswordEncoder.encode(userAccountDetailsChangeDto.getPassword());
            users.setPassword(encodedPassword);
        }
        if (userAccountDetailsChangeDto.getAvatar() != null && !userAccountDetailsChangeDto.getEmail().equals(users.getAvatar())) {
            users.setAvatar(userAccountDetailsChangeDto.getAvatar());
        }
        usersRepository.save(users);
        return "Account details saved propertly";
    }

    public List<UserDto> getUserData(Long userId) {
        Users user = usersRepository.getById(userId);
        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(UserDto.builder()
                .firstName(user.getName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .avatar(user.getAvatar())
                .userId(user.getId())
                .build());
        return userDtoList;
    }
}
