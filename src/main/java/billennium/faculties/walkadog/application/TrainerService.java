package billennium.faculties.walkadog.application;

import billennium.faculties.walkadog.domain.Trainer;
import billennium.faculties.walkadog.infrastructure.TrainerRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class TrainerService implements UserDetailsService {
    private final TrainerRepository trainerRepository;
    private static final String USER_NOT_FOUND = "User with email %s was not found";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return trainerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
    }

    public List<Trainer> getAll(){
        return trainerRepository.findAll();
    }


}


