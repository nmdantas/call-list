package br.com.alice.calllistapi.services;

import br.com.alice.calllistapi.models.User;
import br.com.alice.calllistapi.payloads.CreateUserRequest;
import br.com.alice.calllistapi.payloads.LoginRequest;
import br.com.alice.calllistapi.payloads.UpdateUserRequest;
import br.com.alice.calllistapi.payloads.UserPayload;
import br.com.alice.calllistapi.repositories.IUserRepository;
import br.com.alice.calllistapi.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService extends GenericService<IUserRepository, User, UserPayload> {

    @Autowired
    public UserService(IUserRepository userRepository) {
        super(userRepository);
    }

    @Transactional
    public UserPayload create(CreateUserRequest request) {
        // TODO: Validacoes de campos obrigatorios, verificar @Valid do Spring

        boolean userExists = repository.existsByUsername(request.getUsername());

        // Nao deixa inserir um usuario com mesmo username
        if (userExists) {
            return null;
        }

        String saltString = PasswordUtils.generateSaltString();
        String finalPassword = PasswordUtils.generateHashedPassword(saltString, request.getPassword());

        User entity = new User();
        entity.setUsername(request.getUsername());
        entity.setName(request.getName());
        entity.setSalt(saltString);
        entity.setPassword(finalPassword);
        entity.setCreateDate(LocalDateTime.now());

        entity = repository.save(entity);

        return new UserPayload(entity);
    }

    @Transactional
    public UserPayload update(long userId, UpdateUserRequest request) {
        Optional<User> user = repository.findById(userId);

        // Nao deixa inserir um usuario com mesmo username
        if (!user.isPresent()) {
            return null;
        }

        // Caso haja alteracao de username deve validar se o novo
        // valor ja nao esta sendo utilizado
        if (!user.get().getUsername().equals(request.getUsername()) &&
                repository.existsByUsername(request.getUsername())) {

            return null;
        }

        user.get().setName(request.getName());
        user.get().setUsername(request.getUsername());

        User updatedUser = repository.save(user.get());

        return new UserPayload(updatedUser);
    }

    @Transactional
    public UserPayload validatePassword(LoginRequest request) {
        User user = repository.findByUsername(request.getUsername());

        if (user == null) {
            return null;
        }

        String saltString = user.getSalt();
        String passwordToValidate = PasswordUtils.generateHashedPassword(saltString, request.getPassword());

        // Senha invalida
        if (!passwordToValidate.equals(user.getPassword())) {
            return null;
        }

        return new UserPayload(user);
    }
}
