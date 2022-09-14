#version 330 core

in vec3 aPos;


out vec4 fragColor;
 
uniform vec3 viewPos;
uniform float tint;
uniform vec4 tintColor;
uniform samplerCube first_texture;

struct Material {
	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
	float shininess;
}; 
  
uniform Material material;

void main() {
	
	
	vec3 viewDir = normalize(viewPos - aPos);
	vec4 textureColor = texture(first_texture,viewDir);
	vec4 tintColorReduced = mix(vec4(1,1,1,1),tintColor,tint);
	fragColor = textureColor * tintColorReduced;

}
