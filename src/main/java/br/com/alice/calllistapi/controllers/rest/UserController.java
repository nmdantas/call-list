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

        // Caso nao seja possivel criar o usuario retorna um erro
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        Link selfLink = ControllerLinkBuilder.linkTo(UserController.class).slash(user.getUserId()).withSelfRel();

        user.add(selfLink);

        return ResponseEntity.ok().body(user);
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
}
