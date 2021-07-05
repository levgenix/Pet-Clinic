package com.vet24.models.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.PrimaryKeyJoinColumn;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class CommentReaction{

    @EmbeddedId
    @EqualsAndHashCode.Include
    private CommentReactionId id;

    @ManyToOne(fetch= FetchType.LAZY)
    @PrimaryKeyJoinColumn
    @MapsId("commentId")
    private Comment comment;


    @ManyToOne(fetch= FetchType.LAZY)
    @PrimaryKeyJoinColumn
    @MapsId("clientId")
    private Client client;

    @Column(nullable = false)
    private Boolean positive;

    public CommentReaction(Comment comment, Client client, Boolean positive) {
        this.id = new CommentReactionId(comment.getId(),client.getId());
        this.comment = comment;
        this.client = client;
        this.positive = positive;
    }
}
