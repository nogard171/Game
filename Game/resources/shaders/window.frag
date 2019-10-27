uniform sampler2D myTexture;

uniform vec4 vertColor;

out vec4 color;

void main()
{
    color = texture(myTexture, gl_TexCoord[0].st)*vertColor;
}