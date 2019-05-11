package br.com.anonymous.calllistapi.services;

import br.com.anonymous.calllistapi.models.AccessCode;
import br.com.anonymous.calllistapi.models.Meeting;
import br.com.anonymous.calllistapi.models.Participant;
import br.com.anonymous.calllistapi.models.Profile;
import br.com.anonymous.calllistapi.payloads.*;
import br.com.anonymous.calllistapi.repositories.IAccessCodeRepository;
import br.com.anonymous.calllistapi.repositories.IMeetingRepository;
import br.com.anonymous.calllistapi.repositories.IParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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
    public List<MeetingPayload> findByProfile(Long profileId) {
        return repository.findByProfileId(profileId).stream().map(entity -> new MeetingPayload(entity)).collect(Collectors.toList());
    }

    @Transactional
    public List<String> findCodesByMeetingId(Long meetingId) {
        return accessCodeRepository.findByMeeting(new Meeting(meetingId)).stream().map(x -> x.getCode()).collect(Collectors.toList());
    }

    @Transactional
    public List<ParticipantPayload> findParticipantsByMeetingId(Long meetingId) {
        return participantRepository.findByMeeting(new Meeting(meetingId)).stream().map(x -> new ParticipantPayload(x.getProfile())).collect(Collectors.toList());
    }

    @Transactional
    public String generateNewAccessCode(Long meetingId) {
        long count = repository.countById(meetingId);

        if (count == 0) {
            return null;
        }

        String randomCode = UUID.randomUUID().toString().substring(0, 8);

        AccessCode accessCode = new AccessCode();
        accessCode.setCreateDate(LocalDateTime.now());
        accessCode.setCode(randomCode);
        accessCode.setMeeting(new Meeting(meetingId));

        accessCodeRepository.save(accessCode);

        return randomCode;
    }

    public boolean addParticipant(Long meetingId, CreateParticipantPayload createParticipantPayload) {
        Participant participant = participantRepository.findByProfileAndMeeting(new Profile(createParticipantPayload.getProfileId()), new Meeting(meetingId));

        if (participant != null) {
           return false;
        }

        Participant newParticipant = new Participant();
        newParticipant.setMeeting(new Meeting(meetingId));
        newParticipant.setProfileId(createParticipantPayload.getProfileId());

        participantRepository.save(newParticipant);

        return true;
    }

    public boolean removeParticipant(Long meetingId, CreateParticipantPayload createParticipantPayload) {
        Participant participant = participantRepository.findByProfileAndMeeting(new Profile(createParticipantPayload.getProfileId()), new Meeting(meetingId));

        if (participant == null) {
            return false;
        }

        participantRepository.delete(participant);

        return true;
    }

    @Transactional
    public MeetingPayload create(Long profileId, CreateMeetingPayload request) {
        String randomCode = UUID.randomUUID().toString().substring(0, 8);

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
