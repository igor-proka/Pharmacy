package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "technology_components")
public class TechnologyComponents {

    @EmbeddedId
    private TechnologyComponentsId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("technologyId")
    @JoinColumn(name = "technology_id", nullable = false)
    private Technologies technology;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("componentId")
    @JoinColumn(name = "component_id", nullable = false)
    private MedicamentPackages component;

    @Column(name = "component_amount", nullable = false)
    private Integer componentAmount;

    public TechnologyComponents() {}

    public TechnologyComponents(Technologies technology, MedicamentPackages component, Integer componentAmount) {
        this.technology = technology;
        this.component = component;
        this.componentAmount = componentAmount;
        this.id = new TechnologyComponentsId(
                technology.getId(),
                component.getId()
        );
    }

    public TechnologyComponentsId getId() {
        return id;
    }

    public void setId(TechnologyComponentsId id) {
        this.id = id;
    }

    public Technologies getTechnology() {
        return technology;
    }

    public void setTechnology(Technologies technology) {
        this.technology = technology;
    }

    public MedicamentPackages getComponent() {
        return component;
    }

    public void setComponent(MedicamentPackages component) {
        this.component = component;
    }

    public Integer getComponentAmount() {
        return componentAmount;
    }

    public void setComponentAmount(Integer componentAmount) {
        this.componentAmount = componentAmount;
    }
}
