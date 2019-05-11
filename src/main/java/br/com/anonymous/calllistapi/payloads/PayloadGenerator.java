package br.com.anonymous.calllistapi.payloads;

import br.com.anonymous.calllistapi.models.Meeting;
import br.com.anonymous.calllistapi.models.Profile;
import br.com.anonymous.calllistapi.models.User;

public class PayloadGenerator {
    public static <TPayload, TEntity> TPayload generate(TEntity entity) {
        TPayload payload;

        if (entity instanceof User) {
            payload = (TPayload)new UserPayload((User) entity);
        } else if (entity instanceof  Profile) {
            payload = (TPayload)new ProfilePayload((Profile) entity);
        } else if (entity instanceof Meeting) {
            payload = (TPayload) new MeetingPayload((Meeting) entity);
        } else {
            return (TPayload)null;
        }

        return payload;
    }
}
