package com.example.notaFraisBackend.dto;

public class SectionDTO {
    private Long id;
    private String code;
    private String libelle;
    private Boolean deleted;
    private Long serviceId;

    public SectionDTO(Long id, String code, String libelle, Boolean deleted, Long serviceId) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.deleted = deleted;
        this.serviceId = serviceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

}
