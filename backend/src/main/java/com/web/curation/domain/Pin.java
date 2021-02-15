package com.web.curation.domain;

import com.web.curation.domain.article.Article;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * com.web.curation.domain
 * Pin.java
 * @date    2021-01-26 오전 9:31
 * @author  이주희
 *
 * @변경이력
 **/

@Entity
@Getter @Setter
public class Pin {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "pin_id")
    private Long pinId;

    private Point location;

    private String address;

    @OneToMany(mappedBy = "pin", fetch = FetchType.LAZY)
    private List<Article> articles = new ArrayList<>();

    public void setLocation(double lat, double lng) {
        String wktPoint = String.format("POINT(%s %s)", lng, lat);
        Point point = null;
        try {
            point = (Point) new WKTReader().read(wktPoint);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.location = point;
    }

    public Long getTrendArticleId() {
        AtomicReference<Long> id = new AtomicReference<>(0L);
        AtomicInteger max = new AtomicInteger();
        articles.stream()
                .forEach(article -> {
                    if(max.get() <= article.getLikes().size()) {
                        max.set(article.getLikes().size());
                        id.set(article.getArticleId());
                    }
                });
        return id.get();
    }
}
