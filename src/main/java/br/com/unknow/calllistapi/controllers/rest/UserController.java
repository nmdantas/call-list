package br.com.unknow.calllistapi.controllers.rest;

import br.com.unknow.calllistapi.models.User;
import br.com.unknow.calllistapi.payloads.*;
import br.com.unknow.calllistapi.services.ProfileService;
import br.com.unknow.calllistapi.services.UserService;
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

    @RequestMapping(value="", method=RequestMethod.GET)
    public ResponseEntity<?> find(User filter, Pageable pagination, PagedResourcesAssembler<UserPayload> assembler) {
        Page<UserPayload> users = userService.find(filter, pagination);
        PagedResources<Resource<UserPayload>> response = assembler.toResource(users);

        return ResponseEntity.ok().body(new ResponseWrapper<>(response));
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        UserPayload user = userService.create(request);
        Link selfLink = ControllerLinkBuilder.linkTo(UserController.class).slash(user.getId()).withSelfRel();
        Resource<UserPayload> response = new Resource<>(user, selfLink);

        return ResponseEntity.ok().body(new ResponseWrapper<>(response));
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        UserPayload user = userService.validatePassword(request);

        if (user != null) {
            Link selfLink = ControllerLinkBuilder.linkTo(UserController.class).slash(user.getId()).withSelfRel();
            Link profilesLink = linkTo(methodOn(UserController.class).findProfilesByUserId(user.getId())).withRel("profiles");

            Resource<UserPayload> response = new Resource<>(user, selfLink, profilesLink);

            return ResponseEntity.ok().body(new ResponseWrapper<>(response));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @RequestMapping(value="/{userId}", method=RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable long userId) {
        UserPayload user = userService.findById(userId);

        if (user != null) {
            Link selfLink = ControllerLinkBuilder.linkTo(UserController.class).slash(user.getId()).withSelfRel();
            Link profilesLink = linkTo(methodOn(UserController.class).findProfilesByUserId(user.getId())).withRel("profiles");

            Resource<UserPayload> response = new Resource<>(user, selfLink, profilesLink);

            return ResponseEntity.ok().body(new ResponseWrapper<>(response));
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value="/{userId}/profiles", method=RequestMethod.GET)
    public ResponseEntity<?> findProfilesByUserId(@PathVariable long userId) {
        List<ProfilePayload> profiles = profileService.findByUser(userId);

        return ResponseEntity.ok().body(new ResponseWrapper<>(profiles));
    }

    @RequestMapping(value="/{userId}/profiles", method=RequestMethod.POST)
    public ResponseEntity<?> createProfile(@PathVariable long userId, @RequestBody CreateProfileRequest request) {
        ProfilePayload profile = profileService.create(userId, request);
        Link selfLink = linkTo(methodOn(ProfileController.class).findById(profile.getId())).withSelfRel();
        Resource<ProfilePayload> response = new Resource<>(profile, selfLink);

        return ResponseEntity.ok().body(new ResponseWrapper<>(response));
    }
}
