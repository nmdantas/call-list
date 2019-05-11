package br.com.unknow.calllistapi.services;

import br.com.unknow.calllistapi.models.User;
import br.com.unknow.calllistapi.payloads.CreateUserRequest;
import br.com.unknow.calllistapi.payloads.LoginRequest;
import br.com.unknow.calllistapi.payloads.UserPayload;
import br.com.unknow.calllistapi.repositories.IUserRepository;
import br.com.unknow.calllistapi.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService extends GenericService<IUserRepository, User, UserPayload> {

    @Autowired
    public UserService(IUserRepository userRepository) {
        super(userRepository);
    }

    @Transactional
    public UserPayload create(CreateUserRequest request) {
        // TODO: Validacoes de campos obrigatorios, verificar @Valid do Spring

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
