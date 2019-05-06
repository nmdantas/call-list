package br.com.anonymous.calllistapi.services;

import br.com.anonymous.calllistapi.models.Profile;
import br.com.anonymous.calllistapi.models.User;
import br.com.anonymous.calllistapi.payloads.CreateUserRequest;
import br.com.anonymous.calllistapi.payloads.UserPayload;
import br.com.anonymous.calllistapi.repositories.IProfileRepository;
import br.com.anonymous.calllistapi.repositories.IUserRepository;
import br.com.anonymous.calllistapi.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private IUserRepository userRepository;

    private IProfileRepository profileRepository;

    @Autowired
    public UserService(IUserRepository userRepository,
                       IProfileRepository profileRepository) {
        super();

        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    public Page<UserPayload> find(User filter, Pageable pagination) {
        Example<User> example = Example.of(filter);

        return userRepository.findAll(example, pagination).map(user -> new UserPayload(user));
    }

    public Optional<UserPayload> findById(Long id) {
        return userRepository.findById(id).map(user -> Optional.of(new UserPayload(user))).orElse(Optional.empty());
    }

    public Optional<Profile> findProfileById(Long id) {
        return profileRepository.findById(id);
    }

    public UserPayload create(CreateUserRequest request) {
        // TODO: Validacoes de campos obrigatorios, verificar @Valid do Spring

        String saltString = PasswordUtils.generateSaltString();
        String finalPassword = PasswordUtils.generateHashedPassword(saltString, request.getPassword());

        User userModel = new User();
        userModel.setUsername(request.getUsername());
        userModel.setName(request.getName());
        userModel.setSalt(saltString);
        userModel.setPassword(finalPassword);
        userModel.setCreateDate(LocalDateTime.now());

        userModel = userRepository.save(userModel);

        return new UserPayload(userModel);
    }
}
