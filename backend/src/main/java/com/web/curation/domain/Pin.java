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

@Entity
@Getter @Setter
public class Pin {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "pin_id")
    private Long id;

    private Point location;

    private String address;

    @OneToMany(mappedBy = "pin", fetch = FetchType.LAZY)
    private List<Article> articles = new ArrayList<>();

    public void setLocation(double lat, double lng) {
        String wktPoint = String.format("POINT(%s %s)", lat, lng);
        Point point = null;
        try {
            point = (Point) new WKTReader().read(wktPoint);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.location = point;
    }
}
