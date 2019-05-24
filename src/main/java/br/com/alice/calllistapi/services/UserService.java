package br.com.alice.calllistapi.services;

import br.com.alice.calllistapi.models.Profile;
import br.com.alice.calllistapi.models.Recovery;
import br.com.alice.calllistapi.models.User;
import br.com.alice.calllistapi.payloads.*;
import br.com.alice.calllistapi.repositories.IProfileRepository;
import br.com.alice.calllistapi.repositories.IRecoveryRepository;
import br.com.alice.calllistapi.repositories.IUserRepository;
import br.com.alice.calllistapi.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService extends GenericService<IUserRepository, User, UserPayload> {

    private JavaMailSender emailSender;
    private IProfileRepository profileRepository;
    private IRecoveryRepository recoveryRepository;

    @Autowired
    public UserService(JavaMailSender emailSender,
                       IUserRepository userRepository,
                       IProfileRepository profileRepository,
                       IRecoveryRepository recoveryRepository) {
        super(userRepository);

        this.emailSender = emailSender;
        this.profileRepository = profileRepository;
        this.recoveryRepository = recoveryRepository;
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

    public boolean resetPassword(ResetPasswordRequest request) {
        Optional<Profile> profile = profileRepository.findByEmailAndActiveTrue(request.getEmail()).stream().findFirst();

        if (!profile.isPresent()) {
            return false;
        }

        User user = profile.get().getUser();
        Recovery recovery = new Recovery();
        recovery.setUserId(user.getId());
        recovery.setExpireAt(LocalDateTime.now().plusHours(1L));

        recoveryRepository.save(recovery);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("Password Reset");
        message.setText(recovery.getId());

        emailSender.send(message);

        return true;
    }
}
