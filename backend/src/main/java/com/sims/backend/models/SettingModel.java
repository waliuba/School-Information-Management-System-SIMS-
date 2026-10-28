package com.sims.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "SIMS_SETTING", uniqueConstraints = @UniqueConstraint(name = "UK_SIMS_SETTING_SCOPE_KEY", columnNames = {"SETTING_SCOPE", "OWNER_ID", "SETTING_KEY"}))
public class SettingModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SETTING_ID") private Long settingId;
    @Column(name = "SETTING_SCOPE", nullable = false, length = 20) private String scope;
    @Column(name = "OWNER_ID", nullable = false) private Long ownerId;
    @Column(name = "SETTING_KEY", nullable = false, length = 80) private String key;
    @Column(name = "SETTING_VALUE", nullable = false, length = 1000) private String value;
    @Version @Column(name = "SETTING_VERSION", nullable = false) private long version;

    protected SettingModel() {}
    public SettingModel(String scope, Long ownerId, String key, String value) { this.scope = scope; this.ownerId = ownerId; this.key = key; this.value = value; }
    public Long getSettingId() { return settingId; }
    public String getKey() { return key; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
