package br.com.alice.calllistapi.services;

import br.com.alice.calllistapi.models.*;
import br.com.alice.calllistapi.payloads.*;
import br.com.alice.calllistapi.repositories.*;
import br.com.alice.calllistapi.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService extends GenericService<IUserRepository, User, UserPayload> {

    private static final long EXPIRE_INTERVAL_IN_HOURS = 1L;

    private JavaMailSender emailSender;
    private IProfileRepository profileRepository;
    private IRecoveryRepository recoveryRepository;
    private IViewContactRepository viewContactRepository;
    private IViewMeetingRepository viewMeetingRepository;

    @Autowired
    public UserService(JavaMailSender emailSender,
                       IUserRepository userRepository,
                       IProfileRepository profileRepository,
                       IRecoveryRepository recoveryRepository,
                       IViewContactRepository viewContactRepository,
                       IViewMeetingRepository viewMeetingRepository) {
        super(userRepository);

        this.emailSender = emailSender;
        this.profileRepository = profileRepository;
        this.recoveryRepository = recoveryRepository;
        this.viewContactRepository = viewContactRepository;
        this.viewMeetingRepository = viewMeetingRepository;
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

    public boolean recoveryPassword(RecoveryPasswordRequest request) {
        Optional<Profile> profile = profileRepository.findByEmailAndActiveTrue(request.getEmail()).stream().findFirst();

        if (!profile.isPresent()) {
            return false;
        }

        User user = profile.get().getUser();
        Recovery recovery = new Recovery();
        recovery.setUserId(user.getId());
        recovery.setExpireAt(LocalDateTime.now().plusHours(EXPIRE_INTERVAL_IN_HOURS));

        recoveryRepository.save(recovery);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("Password Recovery");
        message.setText(recovery.getId());

        emailSender.send(message);

        return true;
    }

    public UserPayload validatePasswordReset(String recoveryId) {
        Optional<Recovery> recoveryInfo = recoveryRepository.findById(recoveryId);

        if (!recoveryInfo.isPresent() || LocalDateTime.now().isAfter(recoveryInfo.get().getExpireAt())) {
            return null;
        }

        recoveryRepository.delete(recoveryInfo.get());

        return new UserPayload(recoveryInfo.get().getUser());
    }

    public UserPayload resetPassword(long userId, ResetPasswordRequest request) {
        Optional<User> user = repository.findById(userId);

        if (!user.isPresent()) {
            return null;
        }

        String saltString = PasswordUtils.generateSaltString();
        String finalPassword = PasswordUtils.generateHashedPassword(saltString, request.getPassword());

        user.get().setSalt(saltString);
        user.get().setPassword(finalPassword);

        User updatedUser = repository.save(user.get());

        return new UserPayload(updatedUser);
    }

    public List<MeetingPayload> findMeetings(long userId) {
        List<ViewMeeting> viewMeetings = viewMeetingRepository.findByUser(userId);
        List<MeetingPayload> meetings = new ArrayList<>();

        for (int i = 0; i < viewMeetings.size(); i++) {
            meetings.add(new MeetingPayload(viewMeetings.get(i)));
        }

        return meetings;
    }

    public List<ContactPayload> findContacts(long userId) {
        List<ViewContact> viewContacts = viewContactRepository.findByUser(userId);
        List<ContactPayload> contacts = new ArrayList<>();

        for (int i = 0; i < viewContacts.size(); i++) {
            ViewContact viewContact = viewContacts.get(i);
            ContactPayload contact = new ContactPayload(viewContact.getUserId(), viewContact.getUserName());
            int index = contacts.indexOf(contact);

            if (index > -1) {
                contact = contacts.get(index);
                contact.getDetails().add(new ContactDetailsPayload(viewContact));
            } else {
                contact.getDetails().add(new ContactDetailsPayload(viewContact));
                contacts.add(contact);
            }
        }

        return contacts;
    }
}
