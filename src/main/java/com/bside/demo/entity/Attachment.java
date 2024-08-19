package com.bside.demo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.bside.demo.utility.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@JsonPropertyOrder(alphabetic=true)
@Data
@Table(name = Constants.DB_PREFIX + "_attachment")
@Entity
public class Attachment implements Serializable {

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
	@Column(name = Constants.DB_PREFIX + "_key", length = 255)
	private String key;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = Constants.DB_PREFIX + "_content_type", length = 255)
	private String contentType;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = Constants.DB_PREFIX + "_extension", length = 255)
	private String extension;
	
	@Size(min = 1, max = 1000)
	@Column(name = Constants.DB_PREFIX + "_description", length = 1000)
	private String description;
	
	@NotNull
	@Column(name = Constants.DB_PREFIX + "_size")
	private Double size;

	@NotNull
	@Column(name = Constants.DB_PREFIX + "_active")
	private Boolean active = true;
	
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
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = Constants.DB_PREFIX + "_student")
    private Student student;
	
	public Attachment() {
		
	}
	
	public Attachment(String name, String key, String description, Double size, Boolean active, Date created) {
		this.name = name;
		this.key = key;
		this.description = description;
		this.size = size;
		this.active = active;
		this.created = created;
	}
	
	@Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Attachment other = (Attachment) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(name))
            return false;
        return true;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
	}
}
