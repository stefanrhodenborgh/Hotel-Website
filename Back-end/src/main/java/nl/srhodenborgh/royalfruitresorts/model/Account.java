package nl.srhodenborgh.royalfruitresorts.model;

import java.util.ArrayList;
import java.util.List;

import nl.srhodenborgh.royalfruitresorts.enums.Role;
import jakarta.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private int loyaltyPoints;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private long hotelId; // USER_HOTEL_ID, OWNER_HOTEL_ID, of hotelId = staff van hotel ${hotelId}

    @Column(length = 100, unique = true)
    private String token;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private List<Review> reviews = new ArrayList<>();



    public static final long USER_HOTEL_ID = -1L;
    public static final long OWNER_HOTEL_ID = 0L;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> review) {
        this.reviews = review;
    }
    
    public String getToken() {
		return token;
	}
    
    public void setToken(String token) {
		this.token = token;
	}
}
