package br.com.alice.calllistapi.controllers.rest;

import br.com.alice.calllistapi.models.User;
import br.com.alice.calllistapi.payloads.*;
import br.com.alice.calllistapi.services.ProfileService;
import br.com.alice.calllistapi.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;
    private ProfileService profileService;

    @Autowired
    public UserController(UserService userService,
                          ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }

    @ApiOperation(value = "List users", notes = "List all users using pagination", response = UserPayload.class, responseContainer = "org.springframework.hateoas.PagedResources")
    @RequestMapping(value="", method=RequestMethod.GET)
    public ResponseEntity<?> find(User filter, Pageable pagination, PagedResourcesAssembler<UserPayload> assembler) {
        Page<UserPayload> users = userService.find(filter, pagination);
        PagedResources<Resource<UserPayload>> response = assembler.toResource(users);

        return ResponseEntity.ok().body(new ResponseWrapper<>(response));
    }

    @ApiOperation(value = "Create new user", notes = "Create new user", response = UserPayload.class)
    @RequestMapping(value="", method=RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        UserPayload user = userService.create(request);

        // Caso nao seja possivel criar o usuario retorna um erro
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        Link selfLink = ControllerLinkBuilder.linkTo(UserController.class).slash(user.getUserId()).withSelfRel();

        user.add(selfLink);

        return ResponseEntity.ok().body(user);
    }

    @ApiOperation(value = "Login", notes = "Login user to aplication", response = UserPayload.class)
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        UserPayload user = userService.validatePassword(request);

        if (user != null) {
            Link selfLink = ControllerLinkBuilder.linkTo(UserController.class).slash(user.getUserId()).withSelfRel();
            Link profilesLink = linkTo(methodOn(UserController.class).findProfilesByUserId(user.getUserId())).withRel("profiles");

            user.add(selfLink);
            user.add(profilesLink);

            return ResponseEntity.ok().body(user);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ApiOperation(value = "Recovery", notes = "Recovery password")
    @RequestMapping(value="/recovery", method=RequestMethod.POST)
    public ResponseEntity<?> recoveryPassword(@RequestBody RecoveryPasswordRequest request) {
        boolean success = userService.recoveryPassword(request);

        if (success) {
            return ResponseEntity.ok().body(new ResponseWrapper<>(true));
        }

        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Reset", notes = "Get info to reset password", response = UserPayload.class)
    @RequestMapping(value="/reset", method=RequestMethod.GET)
    public ResponseEntity<?> validateResetPassword(@RequestParam(name="recovery_id") String recoveryId) {
        UserPayload user = userService.validatePasswordReset(recoveryId);

        if (user != null) {
            Link selfLink = ControllerLinkBuilder.linkTo(UserController.class).slash(user.getUserId()).withSelfRel();
            Link profilesLink = linkTo(methodOn(UserController.class).findProfilesByUserId(user.getUserId())).withRel("profiles");

            user.add(selfLink);
            user.add(profilesLink);

            return ResponseEntity.ok().body(user);
        }

        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Find user", notes = "Find user by user id", response = UserPayload.class)
    @RequestMapping(value="/{userId}", method=RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable long userId) {
        UserPayload user = userService.findById(userId);

        if (user != null) {
            Link selfLink = ControllerLinkBuilder.linkTo(UserController.class).slash(user.getUserId()).withSelfRel();
            Link profilesLink = linkTo(methodOn(UserController.class).findProfilesByUserId(user.getUserId())).withRel("profiles");

            user.add(selfLink);
            user.add(profilesLink);

            return ResponseEntity.ok().body(user);
        }

        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Update user", notes = "Update user", response = UserPayload.class)
    @RequestMapping(value="/{userId}", method=RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable long userId, @RequestBody UpdateUserRequest request) {
        UserPayload user = userService.update(userId, request);

        if (user != null) {
            Link selfLink = ControllerLinkBuilder.linkTo(UserController.class).slash(user.getUserId()).withSelfRel();
            Link profilesLink = linkTo(methodOn(UserController.class).findProfilesByUserId(user.getUserId())).withRel("profiles");

            user.add(selfLink);
            user.add(profilesLink);

            return ResponseEntity.ok().body(user);
        }

        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Reset", notes = "Reset password", response = UserPayload.class)
    @RequestMapping(value="/{userId}/reset", method=RequestMethod.PATCH)
    public ResponseEntity<?> resetPassword(@PathVariable long userId, @RequestBody ResetPasswordRequest request) {
        UserPayload user = userService.resetPassword(userId, request);

        if (user != null) {
            Link selfLink = ControllerLinkBuilder.linkTo(UserController.class).slash(user.getUserId()).withSelfRel();
            Link profilesLink = linkTo(methodOn(UserController.class).findProfilesByUserId(user.getUserId())).withRel("profiles");

            user.add(selfLink);
            user.add(profilesLink);

            return ResponseEntity.ok().body(user);
        }

        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "List user profiles", notes = "List all user profiles", responseContainer = "Array", response = ProfilePayload.class)
    @RequestMapping(value="/{userId}/profiles", method=RequestMethod.GET)
    public ResponseEntity<?> findProfilesByUserId(@PathVariable long userId) {
        List<ProfilePayload> profiles = profileService.findByUser(userId);

        for (int i = 0; i < profiles.size(); i++) {
            Link selfLink = linkTo(methodOn(ProfileController.class).findById(profiles.get(i).getProfileId())).withRel("details");

            profiles.get(i).add(selfLink);
        }

        return ResponseEntity.ok().body(profiles);
    }

    @ApiOperation(value = "Create new user profile", notes = "Create new user profile", response = ProfilePayload.class)
    @RequestMapping(value="/{userId}/profiles", method=RequestMethod.POST)
    public ResponseEntity<?> createProfile(@PathVariable long userId, @RequestBody CreateProfileRequest request) {
        ProfilePayload profile = profileService.create(userId, request);
        Link selfLink = linkTo(methodOn(ProfileController.class).findById(profile.getProfileId())).withSelfRel();

        profile.add(selfLink);

        return ResponseEntity.ok().body(profile);
    }

    @ApiOperation(value = "List user meetings", notes = "List all meetings", responseContainer = "Array", response = MeetingPayload.class)
    @RequestMapping(value="/{userId}/meetings", method=RequestMethod.GET)
    public ResponseEntity<?> findMeetingsByUserId(@PathVariable long userId) {
        List<MeetingPayload> meetings = userService.findMeetings(userId);

        for (int i = 0; i < meetings.size(); i++) {
            Link selfLink = linkTo(methodOn(MeetingController.class).findById(meetings.get(i).getMeetingId())).withRel("details");

            meetings.get(i).add(selfLink);
        }

        return ResponseEntity.ok().body(meetings);
    }

    @ApiOperation(value = "List user contacts", notes = "List all contacts", responseContainer = "Array", response = ContactPayload.class)
    @RequestMapping(value="/{userId}/contacts", method=RequestMethod.GET)
    public ResponseEntity<?> findContactsByUserId(@PathVariable long userId) {
        List<ContactPayload> contacts = userService.findContacts(userId);

        return ResponseEntity.ok().body(contacts);
    }
}
