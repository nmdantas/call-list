package br.com.unknow.calllistapi.controllers.rest;

import br.com.unknow.calllistapi.models.Meeting;
import br.com.unknow.calllistapi.payloads.CreateParticipantPayload;
import br.com.unknow.calllistapi.payloads.MeetingPayload;
import br.com.unknow.calllistapi.payloads.ParticipantPayload;
import br.com.unknow.calllistapi.payloads.ResponseWrapper;
import br.com.unknow.calllistapi.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/meetings")
public class MeetingController {
    private MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @RequestMapping(value="", method=RequestMethod.GET)
    public ResponseEntity<?> find(Meeting filter, Pageable pagination, PagedResourcesAssembler<MeetingPayload> assembler) {
        Page<MeetingPayload> meetings = meetingService.find(filter, pagination);
        PagedResources<Resource<MeetingPayload>> response = assembler.toResource(meetings);

        return ResponseEntity.ok().body(new ResponseWrapper<>(response));
    }

    @RequestMapping(value="/{meetingId}", method=RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable long meetingId) {
        MeetingPayload meeting = meetingService.findById(meetingId);

        if (meeting != null) {
            Link selfLink = linkTo(MeetingController.class).slash(meeting.getId()).withSelfRel();
            Link codesLink = linkTo(methodOn(MeetingController.class).getCodes(meeting.getId())).withRel("codes");
            Link participantsLink = linkTo(methodOn(MeetingController.class).getParticipants(meeting.getId())).withRel("participants");
            Resource<MeetingPayload> response = new Resource<>(meeting, selfLink, codesLink, participantsLink);

            return ResponseEntity.ok().body(new ResponseWrapper<>(response));
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value="/{meetingId}/codes", method=RequestMethod.GET)
    public ResponseEntity<?> getCodes(@PathVariable long meetingId) {
        List<String> codes = meetingService.findCodesByMeetingId(meetingId);

        return ResponseEntity.ok().body(new ResponseWrapper<>(codes));
    }

    @RequestMapping(value="/{meetingId}/codes", method=RequestMethod.POST)
    public ResponseEntity<?> generateNewCode(@PathVariable long meetingId) {
        String newAccessCode = meetingService.generateNewAccessCode(meetingId);

        if (newAccessCode == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(new ResponseWrapper<>(newAccessCode));
    }

    @RequestMapping(value="/{meetingId}/participants", method=RequestMethod.GET)
    public ResponseEntity<?> getParticipants(@PathVariable long meetingId) {
        List<ParticipantPayload> participants = meetingService.findParticipantsByMeetingId(meetingId);

        return ResponseEntity.ok().body(new ResponseWrapper<>(participants));
    }

    @RequestMapping(value="/{meetingId}/participants", method=RequestMethod.POST)
    public ResponseEntity<?> addParticipant(@PathVariable long meetingId, @RequestBody CreateParticipantPayload createParticipantPayload) {
        boolean success = meetingService.addParticipant(meetingId, createParticipantPayload);

        if (!success) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/{meetingId}/participants", method=RequestMethod.DELETE)
    public ResponseEntity<?> removeParticipant(@PathVariable long meetingId, @RequestBody CreateParticipantPayload createParticipantPayload) {
        boolean success = meetingService.removeParticipant(meetingId, createParticipantPayload);

        if (!success) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}
