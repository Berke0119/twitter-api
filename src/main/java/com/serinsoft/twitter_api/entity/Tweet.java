package com.serinsoft.twitter_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tweets")
@Getter
@Setter
public class Tweet extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    @Size(max = 280)
    @Column(nullable = false, length = 280)
    private String content;

    /*Relations*/

    @JsonIgnore
    @OneToMany(mappedBy = "tweet", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "tweet", fetch = FetchType.LAZY)
    private List<Retweet> retweets = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "tweet", fetch = FetchType.LAZY)
    private List<TweetLike> likes = new ArrayList<>();
}
