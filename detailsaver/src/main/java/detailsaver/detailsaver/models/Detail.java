package detailsaver.detailsaver.models;

import lombok.Getter;
import lombok.Setter;

public class Detail {
    @Getter
    private Integer detailId;
    @Getter
    private Integer userId;
    @Getter
    private String note;
    @Getter
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    private long createdDate;


    public Detail(Integer detailId, Integer userId, String username, String password, String note,  long createdDate) {
        this.detailId = detailId;
        this.userId = userId;
        this.note = note;
        this.username = username;
        this.password = password;
        this.createdDate = createdDate;
    }
}
