package billennium.faculties.walkadog.application.utils;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
@Service
public class EmailValidator implements Predicate<String> {
    @Override
    public boolean test(String s) {
        // TODO EMAIL VALIDATOR
        return true;
    }
}
