package snakeserver.dir.authetication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import snakeserver.dir.model.Player;
import snakeserver.dir.model.PlayerRepository;

@Service
public class PlayerService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final static String USER_FOUND_MSG = "%s már létező player name";

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return playerRepository.findByName(name)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, name)
                        )
                );
    }


//    public static UserDetails isUniqueUsername(String name) throws UsernameNotFoundException {
//        return playerRepository.findByName(name)
//                .orElseThrow(
//                        () -> new UsernameNotFoundException(
//                                String.format(USER_FOUND_MSG, name)
//                        )
//                );
//    }
}
