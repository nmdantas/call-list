package br.com.unknow.calllistapi.services;

import br.com.unknow.calllistapi.models.Profile;
import br.com.unknow.calllistapi.payloads.CreateProfileRequest;
import br.com.unknow.calllistapi.payloads.ProfilePayload;
import br.com.unknow.calllistapi.repositories.IProfileRepository;
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
    public List<ProfilePayload> findByUser(Long userId) {
        return repository.findByUserIdAndActiveTrue(userId).stream().map(entity -> new ProfilePayload(entity)).collect(Collectors.toList());
    }

    @Transactional
    public boolean delete(Long profileId) {
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
    public ProfilePayload create(Long userId, CreateProfileRequest request) {
        Profile entity = new Profile();
        entity.setActive(true);
        entity.setCompany(request.getCompany());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setRole(request.getRole());
        entity.setType(request.getType());
        entity.setUserId(userId);

        entity = repository.save(entity);

        return new ProfilePayload(entity);
    }
}
