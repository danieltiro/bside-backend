package com.bside.demo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.descriptor.jdbc.ObjectNullAsNullTypeJdbcType;

import com.bside.demo.utility.Constants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Table(name = Constants.DB_PREFIX + "_student", uniqueConstraints = {
		@UniqueConstraint(name = "idxStudent_Unique", columnNames = { Constants.DB_PREFIX + "_curp"})
})
@Entity
public class Student implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = Constants.DB_PREFIX + "_uuid", updatable = false)
	@UuidGenerator
	private UUID id;

	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = Constants.DB_PREFIX + "_name", length = 255)
	private String name;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = Constants.DB_PREFIX + "_lastname", length = 255)
	private String lastname;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = Constants.DB_PREFIX + "_birthday")
	private Date birthday;
	
	@NotNull
	@Column(name = Constants.DB_PREFIX + "_active")
	private Boolean active;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = Constants.DB_PREFIX + "_created")
	private Date created = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = Constants.DB_PREFIX + "_deleted")
	private Date deleted;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = Constants.DB_PREFIX + "_modified")
	private Date modified;
	
	@NotNull
	@Size(min = 18, max = 18)
	@Column(name = Constants.DB_PREFIX + "_curp", length = 18)
	private String curp;
	
	public Student() {
		
	}
	
	public Student(String name, String lastname, String curp, Boolean active, Date created, Date birthday) {
		this.name = name;
		this.lastname = lastname;
		this.curp = curp;
		this.active = active;
		this.created = created;
		this.birthday = birthday;
	}
	
	@Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Student other = (Student) obj;
        if (curp == null) {
            if (other.curp != null)
                return false;
        } else if (!curp.equals(curp))
            return false;
        return true;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
        int result = 1;
        result = prime * result + ((curp == null) ? 0 : curp.hashCode());
        return result;
	}

}
