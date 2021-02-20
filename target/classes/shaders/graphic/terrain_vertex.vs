#version 330

layout(location = 0) in vec3 in_position;
layout(location = 1) in vec4 in_colour;
layout(location = 2) in vec4 in_normal;

flat out vec3 pass_colour;

uniform vec3 lightDirection;
uniform vec3 lightColour;
uniform vec2 lightBias;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;

vec3 calculateLighting(){
	vec3 normal = in_normal.xyz * 2.0 - 1.0;
	float brightness = max(dot(-lightDirection, normal), 0.0);

	brightness = brightness;
	return (lightColour * lightBias.x) + (brightness * lightColour * lightBias.y);
}

void main(void){

    vec4 initPos = vec4(in_position, 1.0);

    mat4 modelViewMatrix =  viewMatrix * modelMatrix;
    vec4 mvPos = modelViewMatrix * initPos;

    gl_Position = projectionMatrix * mvPos;

	vec3 lighting = calculateLighting();
	pass_colour = in_colour.rgb;// * lighting;
}