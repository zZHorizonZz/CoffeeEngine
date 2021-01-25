package com.horizon.engine.component.component.light;

import com.horizon.engine.component.Component;
import com.horizon.engine.component.ComponentType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

public abstract class Light extends Component {

    @Getter @Setter private LightType lightType;

    @Getter @Setter private Vector3f color;
    @Getter @Setter private Vector3f position;

    @Getter @Setter protected float intensity;
    @Getter @Setter private PointLightComponent.Attenuation attenuation;

    public Light(LightType lightType) {
        super(ComponentType.LIGHT);

        this.lightType = lightType;
    }

    @Override
    public void update() {

    }

    public @Data static class Attenuation {

        private float constant, linear, exponent;

        public Attenuation(float constant, float linear, float exponent) {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }
    }
}
