package billennium.faculties.walkadog.application.configuration;

import billennium.faculties.walkadog.application.TrainerService;
import billennium.faculties.walkadog.application.UsersService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
public class LoginConfiguration {


    @Configuration
    @Order(1)
    public static class App1ConfigurationAdapter extends WebSecurityConfigurerAdapter {
        public App1ConfigurationAdapter(UsersService usersService, BCryptPasswordEncoder bCryptPasswordEncoder) {
            super();
            this.usersService = usersService;
            this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        }

        private final UsersService usersService;
        private final BCryptPasswordEncoder bCryptPasswordEncoder;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(daoUserAuthenticationProvider());
        }

        @Bean
        public DaoAuthenticationProvider daoUserAuthenticationProvider() {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setPasswordEncoder(bCryptPasswordEncoder);
            provider.setUserDetailsService(usersService);
            return provider;
        }

        @Bean
        @Qualifier("user")
        @Primary
        public AuthenticationManager authenticationManagerUserBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/users/login")
                    .authorizeRequests()
                    .antMatchers("/wad/api/v*/users/**").permitAll()
                    .anyRequest()
                    .hasRole("USER")

                    .and()
                    .formLogin()
////                    .loginPage("/users/login")
////                    .loginProcessingUrl("/users/users_login")
////                    .failureUrl("/users/loginTrainer?error=loginError")
//                    .defaultSuccessUrl("/users/profile")
//
//                    .and()
//                    .logout()
//                    .logoutUrl("/users/users_logout")
//                    .logoutSuccessUrl("/protectedLinks")
//                    .deleteCookies("JSESSIONID")
//
//                    .and()
//                    .exceptionHandling()
//                    .accessDeniedPage("/403")
//
                    .and()
                    .csrf().disable();
//        }
        }

        @Configuration
        @Order(2)
        public static class App2ConfigurationAdapter extends WebSecurityConfigurerAdapter {

            public App2ConfigurationAdapter(TrainerService trainerService, BCryptPasswordEncoder bCryptPasswordEncoder) {
                super();
                this.trainerService = trainerService;
                this.bCryptPasswordEncoder = bCryptPasswordEncoder;
            }

            private final TrainerService trainerService;
            private final BCryptPasswordEncoder bCryptPasswordEncoder;

            @Bean
            @Qualifier("trainer")
            public AuthenticationManager authenticationManagerTrainerBean() throws Exception {
                return super.authenticationManagerBean();
            }

            @Override
            protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.authenticationProvider(daoTrainerAuthenticationProvider());
            }

            @Bean
            public DaoAuthenticationProvider daoTrainerAuthenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setPasswordEncoder(bCryptPasswordEncoder);
                provider.setUserDetailsService(trainerService);
                return provider;
            }


            protected void configure(HttpSecurity http) throws Exception {
                http
                        .csrf().disable()
                        .antMatcher("/trainers/login")
                        .authorizeRequests()
                        .antMatchers("/wad/api/v*/trainers/**").permitAll()
                        .anyRequest()
                        .hasRole("TRAINER")

//http://localhost:8080/wad/api/v1/trainers/login

                        .and()
                        .formLogin()
//                    .loginPage("/login/trainer").permitAll()
//                    .loginProcessingUrl("/login/trainer/proces")
////                    .failureUrl("/trainers/loginTrainer?error=loginError")
////                    .defaultSuccessUrl("/trainers/profile")
//
//
//                    .and()
//                    .logout()
//                    .logoutUrl("/trainsers/trainers_logout")
//                    .logoutSuccessUrl("/protectedLinks")
//                    .deleteCookies("JSESSIONID")
//
//
//                    .and()
//                    .exceptionHandling()
//                    .accessDeniedPage("/403")


                        .and()
                        .csrf().disable();
            }
        }
    }
}
