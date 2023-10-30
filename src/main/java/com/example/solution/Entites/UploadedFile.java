package com.example.solution.Entites;

import com.example.solution.security.entities.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data // Combines @Getter, @Setter, @EqualsAndHashCode, and @ToString
@NoArgsConstructor // Generate a no-argument constructor
@AllArgsConstructor // Generate an all-argument constructor@Table(name = "uploaded_files")
public class UploadedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType="xls";
    @Temporal(TemporalType.DATE)
    private Date registrationDate;
    @Lob
    @Column(name = "file_data", columnDefinition = "LONGBLOB")
    private byte[] fileData=null;
    @Lob
    @Column(name = "file_carre", columnDefinition = "LONGBLOB")
    private byte[] fileCarre=null;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
}
