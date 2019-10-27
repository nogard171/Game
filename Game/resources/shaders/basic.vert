uniform vec3 position;
uniform vec3 chunkPosition;

void main(void)
{
	mat4x4 pos=mat4x4(1.0);
    pos[3].x=position.x+chunkPosition.x;
    pos[3].y=position.y+chunkPosition.y;
 	gl_Position = gl_ModelViewProjectionMatrix*pos*gl_Vertex;
}