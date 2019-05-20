package br.com.alice.calllistapi.controllers.rest;

import br.com.alice.calllistapi.models.Profile;
import br.com.alice.calllistapi.payloads.*;
import br.com.alice.calllistapi.services.MeetingService;
import br.com.alice.calllistapi.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileController {
    private ProfileService profileService;
    private MeetingService meetingService;

    @Autowired
    public ProfileController(ProfileService profileService,
                             MeetingService meetingService) {
        this.profileService = profileService;
        this.meetingService = meetingService;
    }

    @RequestMapping(value="", method=RequestMethod.GET)
    public ResponseEntity<?> find(Profile filter, Pageable pagination, PagedResourcesAssembler<ProfilePayload> assembler) {
        Page<ProfilePayload> profiles = profileService.find(filter, pagination);
        PagedResources<Resource<ProfilePayload>> response = assembler.toResource(profiles);

        return ResponseEntity.ok().body(new ResponseWrapper<>(response));
    }

    @RequestMapping(value="/{profileId}", method=RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable long profileId) {
        ProfilePayload profile = profileService.findById(profileId);

        if (profile != null) {
            Link selfLink = linkTo(ProfileController.class).slash(profile.getProfileId()).withSelfRel();
            Link meetingsLink = linkTo(methodOn(ProfileController.class).findMeetingsByProfileId(profileId)).withRel("meetings");
            Resource<ProfilePayload> response = new Resource<>(profile, selfLink, meetingsLink);

            return ResponseEntity.ok().body(new ResponseWrapper<>(response));
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value="/{profileId}", method=RequestMethod.PUT)
    public ResponseEntity<?> updateProfile(@PathVariable long profileId, UpdateProfileRequest request) {
        ProfilePayload profile = profileService.update(profileId, request);

        if (profile != null) {
            Link selfLink = linkTo(ProfileController.class).slash(profile.getProfileId()).withSelfRel();
            Link meetingsLink = linkTo(methodOn(ProfileController.class).findMeetingsByProfileId(profileId)).withRel("meetings");
            Resource<ProfilePayload> response = new Resource<>(profile, selfLink, meetingsLink);

            return ResponseEntity.ok().body(new ResponseWrapper<>(response));
        }

        return ResponseEntity.badRequest().build();
    }

    @RequestMapping(value="/{profileId}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteProfile(@PathVariable long profileId) {
        boolean success = profileService.delete(profileId);

        if (success) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value="/{profileId}/meetings", method=RequestMethod.GET)
    public ResponseEntity<?> findMeetingsByProfileId(@PathVariable long profileId) {
        List<MeetingPayload> meetings = meetingService.findByProfile(profileId);
        List<Resource<MeetingPayload>> response = new ArrayList<>();

        for (int i = 0; i < meetings.size(); i++) {
            Link selfLink = linkTo(methodOn(MeetingController.class).findById(meetings.get(i).getMeetingId())).withSelfRel();
            Resource<MeetingPayload> resource = new Resource<>(meetings.get(i), selfLink);

            response.add(resource);
        }

        return ResponseEntity.ok().body(new ResponseWrapper<>(response));
    }

    @RequestMapping(value="/{profileId}/meetings", method=RequestMethod.POST)
    public ResponseEntity<?> createMeeting(@PathVariable long profileId, @RequestBody CreateMeetingPayload request) {
        MeetingPayload meeting = meetingService.create(profileId, request);
        Link selfLink = linkTo(methodOn(MeetingController.class).findById(meeting.getMeetingId())).withSelfRel();
        Resource<MeetingPayload> response = new Resource<>(meeting, selfLink);

        return ResponseEntity.ok().body(new ResponseWrapper<>(response));
    }
}
