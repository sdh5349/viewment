package com.web.curation.domain.connection;

import com.web.curation.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="FOLLOW_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name="FROM_ID")
    private User from;

    @ManyToOne
    @JoinColumn(name="TO_ID")
    private User to;
}
