package com.itsol.mockup.web.dto.response;

import com.itsol.mockup.utils.Constants;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
public class ShareStatusDTO {
    private Long fileId;
    private Long userId;
    private Timestamp dayShare;
    private String userShare;
    private String note;

    public ShareStatusDTO() {
    }

    public ShareStatusDTO(Long fileId, Long userId, Timestamp dayShare, String userShare, String note){
        this.fileId = fileId;
        this.userId = userId;
        this.dayShare = dayShare;
        this.userShare = userShare;
        this.note = note;
    }

}
