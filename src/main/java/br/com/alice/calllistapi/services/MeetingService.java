package br.com.alice.calllistapi.services;

import br.com.alice.calllistapi.models.AccessCode;
import br.com.alice.calllistapi.models.Meeting;
import br.com.alice.calllistapi.models.Participant;
import br.com.alice.calllistapi.models.Profile;
import br.com.alice.calllistapi.payloads.CreateMeetingPayload;
import br.com.alice.calllistapi.payloads.CreateParticipantPayload;
import br.com.alice.calllistapi.payloads.MeetingPayload;
import br.com.alice.calllistapi.payloads.ParticipantPayload;
import br.com.alice.calllistapi.repositories.IAccessCodeRepository;
import br.com.alice.calllistapi.repositories.IMeetingRepository;
import br.com.alice.calllistapi.repositories.IParticipantRepository;
import br.com.alice.calllistapi.utils.AccessCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingService extends GenericService<IMeetingRepository, Meeting, MeetingPayload> {

    private IAccessCodeRepository accessCodeRepository;
    private IParticipantRepository participantRepository;

    @Autowired
    public MeetingService(IMeetingRepository meetingRepository,
                          IAccessCodeRepository accessCodeRepository,
                          IParticipantRepository participantRepository) {
        super(meetingRepository);

        this.accessCodeRepository = accessCodeRepository;
        this.participantRepository = participantRepository;
    }

    @Transactional
    public List<MeetingPayload> findByProfile(long profileId) {
        return repository.findByProfileId(profileId).stream().map(entity -> new MeetingPayload(entity)).collect(Collectors.toList());
    }

    @Transactional
    public List<Integer> findCodesByMeetingId(long meetingId) {
        return accessCodeRepository.findByMeeting(new Meeting(meetingId)).stream().map(x -> x.getCode()).collect(Collectors.toList());
    }

    @Transactional
    public List<ParticipantPayload> findParticipantsByMeetingId(long meetingId) {
        return participantRepository.findByMeeting(new Meeting(meetingId)).stream().map(x -> new ParticipantPayload(x.getProfile())).collect(Collectors.toList());
    }

    @Transactional
    public int generateNewAccessCode(long meetingId) {
        boolean exists = repository.existsById(meetingId);

        if (!exists) {
            return 0;
        }

        int randomCode = AccessCodeUtils.generateNew(x -> {
            return accessCodeRepository.existsByCode(x);
        });

        // Exclui qualquer outro codigo associado a reuniao
        // apenas deve ter um codigo de acesso
        accessCodeRepository.deleteByMeeting(new Meeting(meetingId));

        AccessCode accessCode = new AccessCode();
        accessCode.setCreateDate(LocalDateTime.now());
        accessCode.setCode(randomCode);
        accessCode.setMeeting(new Meeting(meetingId));

        accessCodeRepository.save(accessCode);

        return randomCode;
    }

    public boolean addParticipant(int accessCode, CreateParticipantPayload createParticipantPayload) {
        AccessCode entity = accessCodeRepository.findByCode(accessCode);

        // Se nao localizar o codigo retorna falso
        if (entity == null) {
            return false;
        }

        return addParticipant(entity.getMeeting().getId(), createParticipantPayload);
    }

    public boolean addParticipant(long meetingId, CreateParticipantPayload createParticipantPayload) {
        Participant participant = participantRepository.findByProfileAndMeeting(new Profile(createParticipantPayload.getProfileId()), new Meeting(meetingId));

        // Nao deixa adicionar duas vezes o mesmo participante
        if (participant != null) {
           return false;
        }

        Participant newParticipant = new Participant();
        newParticipant.setMeeting(new Meeting(meetingId));
        newParticipant.setProfileId(createParticipantPayload.getProfileId());

        participantRepository.save(newParticipant);

        return true;
    }

    public boolean removeParticipant(long meetingId, CreateParticipantPayload createParticipantPayload) {
        Participant participant = participantRepository.findByProfileAndMeeting(new Profile(createParticipantPayload.getProfileId()), new Meeting(meetingId));

        if (participant == null) {
            return false;
        }

        participantRepository.delete(participant);

        return true;
    }

    @Transactional
    public MeetingPayload create(long profileId, CreateMeetingPayload request) {
        int randomCode = AccessCodeUtils.generateNew(x -> {
            return accessCodeRepository.existsByCode(x);
        });

        Participant participant = new Participant();
        participant.setProfileId(profileId);

        AccessCode accessCode = new AccessCode();
        accessCode.setCreateDate(LocalDateTime.now());
        accessCode.setCode(randomCode);

        Meeting entity = new Meeting();
        entity.setDate(request.getDate());
        entity.setName(request.getName());
        entity.setProfileId(profileId);
        entity.addAccessCode(accessCode);
        entity.addParticipant(participant);

        entity = repository.save(entity);

        return new MeetingPayload(entity);
    }
}
