package br.com.alice.calllistapi.services;

import br.com.alice.calllistapi.models.Profile;
import br.com.alice.calllistapi.payloads.CreateProfileRequest;
import br.com.alice.calllistapi.payloads.ProfilePayload;
import br.com.alice.calllistapi.payloads.UpdateProfileRequest;
import br.com.alice.calllistapi.repositories.IProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileService extends GenericService<IProfileRepository, Profile, ProfilePayload> {

    @Autowired
    public ProfileService(IProfileRepository profileRepository) {
        super(profileRepository);
    }

    @Transactional
    public List<ProfilePayload> findByUser(long userId) {
        return repository.findByUserIdAndActiveTrue(userId).stream().map(entity -> new ProfilePayload(entity)).collect(Collectors.toList());
    }

    @Transactional
    public boolean delete(long profileId) {
        Optional<Profile> optional = repository.findById(profileId);

        if (!optional.isPresent()) {
            return false;
        }

        Profile profile = optional.get();
        profile.setActive(false);

        repository.save(profile);

        return true;
    }

    @Transactional
    public ProfilePayload create(long userId, CreateProfileRequest request) {
        // So pode haver um perfil principal, por isso se o perfil a ser criado
        // for marcado como principal os demais perfis devem ser desmarcados
        if (request.isMain()) {
            repository.unmarkAsMainByUserId(userId);
        }

        Profile entity = new Profile();
        entity.setActive(true);
        entity.setCompany(request.getCompany());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setRole(request.getRole());
        entity.setUserId(userId);
        entity.setMain(request.isMain());

        entity = repository.save(entity);

        return new ProfilePayload(entity);
    }

    @Transactional
    public ProfilePayload update(long userId, UpdateProfileRequest request) {
        Optional<Profile> profile = repository.findById(userId);

        // Nao deixa inserir um usuario com mesmo username
        if (!profile.isPresent()) {
            return null;
        }

        // So pode haver um perfil principal, por isso se o perfil a ser criado
        // for marcado como principal os demais perfis devem ser desmarcados
        if (request.isMain()) {
            repository.unmarkAsMainByUserId(userId);
        }

        profile.get().setMain(request.isMain());
        profile.get().setCompany(request.getCompany());
        profile.get().setEmail(request.getEmail());
        profile.get().setPhone(request.getPhone());
        profile.get().setRole(request.getRole());

        Profile updatedProfile = repository.save(profile.get());

        return new ProfilePayload(updatedProfile);
    }
}
