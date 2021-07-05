package com.vet24.web.controllers.user;

import com.vet24.models.exception.RepeatedCommentException;
import com.vet24.models.user.Client;
import com.vet24.models.user.Comment;
import com.vet24.models.user.CommentReaction;
import com.vet24.models.user.Doctor;
import com.vet24.models.user.DoctorReview;
import com.vet24.models.user.User;
import com.vet24.service.user.ClientService;
import com.vet24.service.user.CommentReactionService;
import com.vet24.service.user.CommentService;
import com.vet24.service.user.DoctorReviewService;
import com.vet24.service.user.DoctorService;
import com.vet24.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/client/doctor")
@Tag(name = "doctor-controller", description = "operations with doctors")
public class ClientCommentController {

    private final DoctorService doctorService;
    private final ClientService clientService;
    private final CommentService commentService;
    private final DoctorReviewService doctorReviewService;
    private final UserService userService;
    private final CommentReactionService commentReactionService;

    @Autowired
    public ClientCommentController(DoctorService doctorService, ClientService clientService, CommentService commentService,DoctorReviewService doctorReviewService, UserService userService, CommentReactionService commentReactionService) {
        this.doctorService = doctorService;
        this.clientService = clientService;
        this.commentService = commentService;
        this.doctorReviewService = doctorReviewService;
        this.userService = userService;
        this.commentReactionService = commentReactionService;
    }

    @Operation(summary = "add comment by Client for Doctor")
    @PostMapping(value = "/{doctorId}/addComment")
    public ResponseEntity<String> persistComment(@PathVariable("doctorId") Long doctorId, String text){
        Doctor doctor = doctorService.getByKey(doctorId);
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Comment comment = null;
            DoctorReview doctorReview = null;
            User currentUser = userService.getCurrentUser();
            Long userId = currentUser.getId();
            if (doctorReviewService.getByDoctorAndClientId(doctorId,userId) == null) {
                comment = new Comment(
                        userService.getCurrentUser(), text, LocalDateTime.now()
                );
                doctorReview = new DoctorReview(comment,doctor);

                commentService.persist(comment);

                doctorReviewService.persist(doctorReview);
            } else {
                throw new RepeatedCommentException("You can add only one comment to Doctor. So you have to update or delete old one.");
            }
            return new ResponseEntity<>(comment.getContent(), HttpStatus.OK);
        }
    }


    @Operation(summary = "like or dislike a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully liked/disliked the comment"),
            @ApiResponse(responseCode = "404", description = "Comment is not found")
    })
    @PostMapping(value = "/{commentId}/{positive}")
    public ResponseEntity<Void> likeOrDislikeComment(@PathVariable Long commentId, @PathVariable boolean positive)  {

        Comment comment = commentService.getByKey(commentId);
        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Client client = clientService.getCurrentClient();
        CommentReaction commentLike = new CommentReaction(comment,client,positive);
        commentReactionService.update(commentLike);

        return new  ResponseEntity<>(HttpStatus.OK);
    }
}
