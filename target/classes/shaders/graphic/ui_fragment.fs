#version 330

in vec2 outTexCoord;
in vec3 mvPos;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec4 colour;
uniform int hasTexture;

uniform float uiWidth;
uniform float uiHeight;
uniform float radius;

const float smoothness = 0.7;

void main()
{
    float alphaValue = 1.0f;
    vec2 pixelPosition = outTexCoord * vec2(uiWidth, uiHeight);

    if(radius < 0.0){
        vec2 pixelPosition = outTexCoord * vec2(uiWidth, uiHeight);
        float xMax = uiWidth - radius;
        float yMax = uiHeight - radius;

        if(pixelPosition.x < radius && pixelPosition.y < radius){
            alphaValue *= 1.0 - smoothstep(radius - smoothness, radius + smoothness, length(pixelPosition - vec2(radius, radius)));
        }else if(pixelPosition.x < radius && pixelPosition.y > yMax) {
            alphaValue *= 1.0 - smoothstep(radius - smoothness, radius + smoothness, length(pixelPosition - vec2(radius, yMax)));
        }else if(pixelPosition.x > xMax && pixelPosition.y > yMax) {
            alphaValue *= 1.0 - smoothstep(radius - smoothness, radius + smoothness, length(pixelPosition - vec2(xMax, yMax)));
        }else if(pixelPosition.x > xMax && pixelPosition.y < radius) {
            alphaValue *= 1.0 - smoothstep(radius - smoothness, radius + smoothness, length(pixelPosition - vec2(yMax, radius)));
        }
    }

    if ( hasTexture == 1 ){
        fragColor = colour * texture(texture_sampler, outTexCoord);
        //fragColor.a *= alphaValue;
    }
    else{
        fragColor = colour;
        //fragColor.a *= alphaValue;
    }

    //if(pixelPosition.y > 0.01){
    //    fragColor = vec4(1.0f,1.0f,1.0f,1.0f);
    //}
}