package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TechnologyComponentsId implements Serializable {

    @Column(name = "technology_id")
    private Long technologyId;

    @Column(name = "component_id")
    private Long componentId;

    public TechnologyComponentsId() {
    }

    public TechnologyComponentsId(Long technologyId, Long componentId) {
        this.technologyId = technologyId;
        this.componentId = componentId;
    }

    public Long getTechnologyId() {
        return technologyId;
    }

    public void setTechnologyId(Long technologyId) {
        this.technologyId = technologyId;
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TechnologyComponentsId)) return false;
        TechnologyComponentsId that = (TechnologyComponentsId) o;
        return Objects.equals(technologyId, that.technologyId) &&
                Objects.equals(componentId, that.componentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(technologyId, componentId);
    }
}
