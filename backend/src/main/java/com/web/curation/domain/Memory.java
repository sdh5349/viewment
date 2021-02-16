package com.web.curation.domain;

import com.web.curation.domain.hashtag.Hashtag;
import com.web.curation.domain.memory.MemoryPin;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * com.web.curation.domain
 * Memory.java
 * @date    2021-01-22 오후 3:41
 * @author  김종성
 *
 * @변경이력
 **/

@Entity
@Getter
@Setter
public class Memory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MEMORY_ID")
    private Long id;

    @Column(nullable = false, name="MEMORY_NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    private Point location;

    @OneToMany(mappedBy = "memory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MemoryPin> nearbyPins = new ArrayList<>();

    @ColumnDefault("50")
    private int radius;

    public void addNearbyPins(MemoryPin memoryPin) {
        nearbyPins.add(memoryPin);
        memoryPin.setMemory(this);
    }

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

    public void resetNearbyPins() {
        nearbyPins.stream().forEach(memoryPin -> {
            memoryPin.setPin(null);
        });
    }

    public void resetUser() {
        user.removeMemory(this);
        user = null;
    }
}
