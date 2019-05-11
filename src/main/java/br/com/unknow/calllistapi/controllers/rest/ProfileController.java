package br.com.unknow.calllistapi.controllers.rest;

import br.com.unknow.calllistapi.models.Profile;
import br.com.unknow.calllistapi.payloads.CreateMeetingPayload;
import br.com.unknow.calllistapi.payloads.MeetingPayload;
import br.com.unknow.calllistapi.payloads.ProfilePayload;
import br.com.unknow.calllistapi.payloads.ResponseWrapper;
import br.com.unknow.calllistapi.services.MeetingService;
import br.com.unknow.calllistapi.services.ProfileService;
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
            Link selfLink = linkTo(ProfileController.class).slash(profile.getId()).withSelfRel();
            Link meetingsLink = linkTo(methodOn(ProfileController.class).findMeetingsByProfileId(profileId)).withRel("meetings");
            Resource<ProfilePayload> response = new Resource<>(profile, selfLink, meetingsLink);

            return ResponseEntity.ok().body(new ResponseWrapper<>(response));
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value="/{profileId}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteById(@PathVariable long profileId) {
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
            Link selfLink = linkTo(methodOn(MeetingController.class).findById(meetings.get(i).getId())).withSelfRel();
            Resource<MeetingPayload> resource = new Resource<>(meetings.get(i), selfLink);

            response.add(resource);
        }

        return ResponseEntity.ok().body(new ResponseWrapper<>(response));
    }

    @RequestMapping(value="/{profileId}/meetings", method=RequestMethod.POST)
    public ResponseEntity<?> createMeeting(@PathVariable long profileId, @RequestBody CreateMeetingPayload request) {
        MeetingPayload meeting = meetingService.create(profileId, request);
        Link selfLink = linkTo(methodOn(MeetingController.class).findById(meeting.getId())).withSelfRel();
        Resource<MeetingPayload> response = new Resource<>(meeting, selfLink);

        return ResponseEntity.ok().body(new ResponseWrapper<>(response));
    }
}
