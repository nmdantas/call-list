package br.com.anonymous.calllistapi.controllers.rest;

import br.com.anonymous.calllistapi.models.User;
import br.com.anonymous.calllistapi.payloads.CreateUserRequest;
import br.com.anonymous.calllistapi.payloads.ResponseWrapper;
import br.com.anonymous.calllistapi.payloads.UserPayload;
import br.com.anonymous.calllistapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value="", method=RequestMethod.GET)
    public ResponseEntity<?> find(User filter, Pageable pagination, PagedResourcesAssembler assembler) {
        Page<UserPayload> users = userService.find(filter, pagination);
        PagedResources<Resource<UserPayload>> response = assembler.toResource(users);

        return ResponseEntity.ok().body(new ResponseWrapper<>(response));
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable long id) {
        return userService.findById(id).map(user -> {
            Link selfLink = ControllerLinkBuilder.linkTo(UserController.class).slash(user.getId()).withSelfRel();
            Link profilesLink = linkTo(methodOn(UserController.class).findProfilesByUserId(user.getId())).withRel("profiles");

            Resource<UserPayload> response = new Resource(user, selfLink, profilesLink);

            return ResponseEntity.ok().body(new ResponseWrapper<>(response));
        }).orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(value="/{userId}/profiles", method=RequestMethod.GET)
    public ResponseEntity<?> findProfilesByUserId(@PathVariable long userId) {
        return userService.findById(userId).map(user -> {
            Link selfLink = linkTo(UserController.class).slash(user.getId()).withSelfRel();
            Resource<UserPayload> response = new Resource(user, selfLink);

            return ResponseEntity.ok().body(new ResponseWrapper<>(response));
        }).orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(value="/{userId}/profiles/{profileId}", method=RequestMethod.GET)
    public ResponseEntity<?> findProfileByUserId(@PathVariable long id, @PathVariable long profileId) {
        return userService.findProfileById(id).map(profile -> {
            Link selfLink = ControllerLinkBuilder.linkTo(UserController.class).slash(profile.getId()).withSelfRel();
            Resource<User> response = new Resource(profile, selfLink);

            return ResponseEntity.ok().body(new ResponseWrapper<>(response));
        }).orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody CreateUserRequest request, PagedResourcesAssembler assembler) {
        UserPayload user = userService.create(request);
        Link selfLink = ControllerLinkBuilder.linkTo(UserController.class).slash(user.getId()).withSelfRel();
        Resource<UserPayload> response = new Resource(user, selfLink);

        return ResponseEntity.ok().body(new ResponseWrapper<>(response));
    }
}
