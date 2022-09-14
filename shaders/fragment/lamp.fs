#version 330 core

in vec3 aPos;
in vec3 aNormal;

out vec4 fragColor;
 
uniform vec3 viewPos;

struct DirLight {
    vec3 direction;
  
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
}; 
uniform DirLight directLight[8];
uniform int numOfDirectLights;

struct PointLight {
    vec3 position;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;

    float constant;
    float linear;
    float quadratic;
};

uniform PointLight pointLight[8];
uniform int numOfPointLights;

struct SpotLight {
    vec3 position;
    vec3 direction;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;

    float innerAngleCos;
    float outerAngleCos;
};

uniform SpotLight spotLight[8];
uniform int numOfSpotLights;


struct Material {
	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
	float shininess;
}; 
  
uniform Material material;

vec3 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir){
    
    vec3 lightDir = normalize(-light.direction);
    
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    
    // combine results
    vec3 ambient  = light.ambient  * material.ambient;
    vec3 diffuse  = light.diffuse  * diff * material.diffuse;
    vec3 specular = light.specular * spec * material.specular;
    return (ambient + diffuse + specular);
}


vec3 CalcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir){
    
    vec3 lightDir = normalize(light.position - fragPos);
    
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    
    // attenuation
    float distance    = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + 
  			     light.quadratic * (distance * distance));    
    
    // combine results
    vec3 ambient  = light.ambient  * material.ambient;
    vec3 diffuse  = light.diffuse  * diff * material.diffuse;
    vec3 specular = light.specular * spec * material.specular;
    ambient  *= attenuation;
    diffuse  *= attenuation;
    specular *= attenuation;
    return (ambient + diffuse + specular);
} 

vec3 CalcSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDir){

    vec3 lightDir = normalize(light.position - fragPos);
    
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);

    //intensity
    float theta     = dot(lightDir, normalize(-light.direction));
    float epsilon   = light.innerAngleCos - light.outerAngleCos;
    float intensity = clamp((theta - light.outerAngleCos) / epsilon, 0.0, 1.0);
    
    // combine results
    vec3 ambient  = light.ambient  * material.ambient;
    vec3 diffuse  = light.diffuse  * diff * material.diffuse;
    vec3 specular = light.specular * spec * material.specular;
    ambient  *= intensity;
    diffuse  *= intensity;
    specular *= intensity;
    return (ambient + diffuse + specular);
}

void main() {
	
	vec3 norm = normalize(aNormal);
	vec3 viewDir = normalize(viewPos - aPos);
	vec3 result = vec3(0.0);
	for(int i = 0 ; i< numOfDirectLights ; i++){
		result += CalcDirLight(directLight[i],norm, viewDir);
	}

	for(int i = 0 ; i< numOfPointLights ; i++){
		result += CalcPointLight(pointLight[i],norm,aPos,viewDir);
	}

    for(int i = 0 ; i< numOfSpotLights ; i++){
		result += CalcSpotLight(spotLight[i],norm,aPos,viewDir);
	}

	fragColor = vec4(result, 1.0);
}

