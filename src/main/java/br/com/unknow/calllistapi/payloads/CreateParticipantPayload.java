package br.com.unknow.calllistapi.payloads;

public class CreateParticipantPayload {
    private Long profileId;

    public CreateParticipantPayload() {
        super();
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }
}
