package com.bookserver.controller;

import com.bookserver.business.UserBusiness;
import com.bookserver.dto.OffsetLimitRequest;
import com.bookserver.dto.ResponseMeta;
import com.bookserver.model.User;

import java.util.Optional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = {"/v1/bookserver/user", "/v2/bookserver/user"},
    produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController extends BaseController {

    @Autowired
    private UserBusiness userBusiness;

    public UserController() {
        super(User.class);
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<ResponseMeta> get(@Valid final String name, @Valid final OffsetLimitRequest pageable) {
        final Page<User> page = this.userBusiness.findAll(name, pageable);

        return super.buildResponse(HttpStatus.OK, page, pageable);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMeta> post(@RequestBody @Valid final User user) {
        final Optional<User> optionalUser = this.userBusiness.create(user);

        return super.buildResponse(HttpStatus.CREATED, optionalUser.orElse(null), null);
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<ResponseMeta> getById(@PathVariable final Long id) {
        final Optional<User> optionalUser = this.userBusiness.findById(id);

        return super.buildResponse(HttpStatus.OK, optionalUser.orElse(null), null);
    }

    @PutMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<ResponseMeta> put(
        @PathVariable final Long id, @RequestBody @Valid final User user) {

        final Optional<User> optionalUser = this.userBusiness.update(id, user);

        return super.buildResponse(HttpStatus.OK, optionalUser.orElse(null), null);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseMeta> deleteById(@PathVariable final Long id) {
        this.userBusiness.deleteById(id);

        return super.buildResponse(HttpStatus.NO_CONTENT, null, null);
    }

}
