package ui;

import datastructures.mesh.Triangle;
import datastructures.mesh.TriangleMesh;
import datastructures.mesh.TriangleMeshTools;
import datastructures.mesh.Vertex;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;
import datastructures.halfedgemesh.HalfEdge;
import datastructures.halfedgemesh.HalfEdgeFacet;
import datastructures.halfedgemesh.HalfEdgeMesh;
import datastructures.halfedgemesh.HalfEdgeVertex;

/**
 * Tools for triangle meshes
 */
public class JMonkeyTools extends TriangleMeshTools {

  public enum Shading {
    PER_VERTEX, PER_FACET
  }

  private static PhongMaterialProps defaultPhongMatProps = new PhongMaterialProps(
          ColorRGBA.DarkGray, ColorRGBA.White, ColorRGBA.DarkGray, 20.0f);

  public static Material makePhongMaterial(AssetManager assetManager, PhongMaterialProps phongMaterialProps) {
    Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    mat.setColor("Diffuse", phongMaterialProps.diffuseColor);
    mat.setColor("Ambient", phongMaterialProps.ambientColor);
    mat.setFloat("Shininess", phongMaterialProps.shininess);
    mat.setColor("Specular", phongMaterialProps.specularColor);
    mat.setBoolean("UseMaterialColors", true);
    mat.setBoolean("UseVertexColor", true);
    return mat;
  }


  public static Material makeMaterialUnshaded(AssetManager assetManager) {
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.DarkGray);
    mat.getAdditionalRenderState().setWireframe(true);
    mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
    return mat;
  }

  public static Geometry createGeometry(AssetManager assetManager, TriangleMesh triangleMesh) {
    return createGeometry(assetManager, triangleMesh, new PhongMaterialProps(), null, Shading.PER_FACET);
  }

  /**
   * Create a geometry object for a triangle mesh.
   */
  public static Geometry createGeometry(AssetManager assetManager, TriangleMesh triangleMesh,
                                        PhongMaterialProps phongMaterialProps,
                                        String normalMapFilename,
                                        Shading shading) {
    Mesh mesh = createMesh(triangleMesh, shading);
    Geometry geom = new Geometry("triangle mesh", mesh);
    if (phongMaterialProps == null) {
      phongMaterialProps = defaultPhongMatProps;
    }

    Material mat = makePhongMaterial(assetManager, phongMaterialProps);
    // Unshaded
    //Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    //mat.setBoolean("VertexColor", true);

    // Texture
    if (triangleMesh.hasTexture()) {
      String textureFilename = triangleMesh.getTextureName();
      Texture texture = assetManager.loadTexture(textureFilename);
      mat.setTexture("DiffuseMap", texture);
      texture.setMagFilter(Texture.MagFilter.Bilinear);
      texture.setMinFilter(Texture.MinFilter.BilinearNearestMipMap);
    }

    // Normal map
    if (normalMapFilename != null) {
      TangentBinormalGenerator.generate(geom);
      Texture normalMap = assetManager.loadTexture(normalMapFilename);
      normalMap.setMagFilter(Texture.MagFilter.Bilinear);
      normalMap.setMinFilter(Texture.MinFilter.BilinearNearestMipMap);
      mat.setTexture("NormalMap", normalMap);
    }

    geom.setMaterial(mat);

    return geom;
  }

  public static Mesh createMesh(TriangleMesh triangleMesh, Shading shading) {
    Mesh mesh = new Mesh();
    mesh.setMode(Mesh.Mode.Triangles);

    int size = triangleMesh.getNumberOfTriangles() * 3;

    float[] positionBuffer = new float[size * 3];
    float[] colorBuffer = new float[size * 4];
    float[] normalBuffer = new float[size * 3];
    float[] texCoordsBuffer = new float[size * 2];
    int[] indexBuffer = new int[size];

    // Fill vertex and color buffer
    for (int triangleIndex = 0; triangleIndex < triangleMesh.getNumberOfTriangles(); triangleIndex++) {

      Triangle t = triangleMesh.getTriangle(triangleIndex);
      ColorRGBA color = t.getColorIdx() >= 0 ?
              triangleMesh.getColor(t.getColorIdx()) :
              ColorRGBA.Orange;
      Vector3f normal = t.getNormal();

      for (int vertexInTriangleIndex = 0; vertexInTriangleIndex < 3; vertexInTriangleIndex++) {
        int vertexIndex = t.getVertexIndex(vertexInTriangleIndex);
        int texCoordIndex = t.getTextureCoordinate(vertexInTriangleIndex);
        Vertex vertex = triangleMesh.getVertex(vertexIndex);
        Vector3f pos = vertex.getPosition();
        if (shading == Shading.PER_VERTEX) {
          normal = vertex.getNormal();
        }

        Vector2f texCoord = texCoordIndex >= 0 ? triangleMesh.getTextureCoordinate(texCoordIndex) :
                new Vector2f(0, 0);

        // Position
        positionBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3] = pos.get(0);
        positionBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 1] = pos.get(1);
        positionBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 2] = pos.get(2);

        // Normal
        normalBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3] = normal.get(0);
        normalBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 1] = normal.get(1);
        normalBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 2] = normal.get(2);

        // Color
        colorBuffer[triangleIndex * 12 + vertexInTriangleIndex * 4] = color.r;
        colorBuffer[triangleIndex * 12 + vertexInTriangleIndex * 4 + 1] = color.g;
        colorBuffer[triangleIndex * 12 + vertexInTriangleIndex * 4 + 2] = color.b;
        colorBuffer[triangleIndex * 12 + vertexInTriangleIndex * 4 + 3] = color.a;

        // Texture coords
        texCoordsBuffer[triangleIndex * 6 + vertexInTriangleIndex * 2] = texCoord.x;
        texCoordsBuffer[triangleIndex * 6 + vertexInTriangleIndex * 2 + 1] = texCoord.y;
      }
    }
    for (int i = 0; i < size; i++) {
      indexBuffer[i] = i;
    }

    mesh.setBuffer(Type.Position, 3, positionBuffer);
    mesh.setBuffer(Type.Index, 1, indexBuffer);
    mesh.setBuffer(Type.Color, 4, colorBuffer);
    mesh.setBuffer(Type.Normal, 3, normalBuffer);
    mesh.setBuffer(Type.TexCoord, 2, texCoordsBuffer);
    mesh.updateBound();
    return mesh;
  }

  public static Geometry createGeometry(AssetManager assetManager, HalfEdgeMesh heMesh) {
    Mesh mesh = new Mesh();

    mesh.setMode(Mesh.Mode.Triangles);

    int size = heMesh.getNumberOfFacets() * 3;

    float[] positionBuffer = new float[size * 3];
    float[] colorBuffer = new float[size * 4];
    float[] normalBuffer = new float[size * 3];
    float[] texCoordsBuffer = new float[size * 2];
    int[] indexBuffer = new int[size];

    // Fill vertex and color buffer
    for (int triangleIndex = 0; triangleIndex < heMesh.getNumberOfFacets(); triangleIndex++) {

      HalfEdgeFacet t = heMesh.getFacet(triangleIndex);
      HalfEdge he = t.getHalfEdge();

      for (int vertexInTriangleIndex = 0; vertexInTriangleIndex < 3; vertexInTriangleIndex++) {
        HalfEdgeVertex vertex = he.getStartVertex();
        Vector3f pos = vertex.getPosition();
        Vector3f normal = t.getNormal();
        ColorRGBA color = heMesh.getColor(t);
        Vector2f texCoord = new Vector2f(0, 0);

        // Position
        positionBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3] = (float) pos.get(0);
        positionBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 1] = (float) pos.get(1);
        positionBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 2] = (float) pos.get(2);

        // Normal
        normalBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3] = (float) normal.get(0);
        normalBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 1] = (float) normal.get(1);
        normalBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 2] = (float) normal.get(2);

        // Color
        colorBuffer[triangleIndex * 12 + vertexInTriangleIndex * 4] = color.r;
        colorBuffer[triangleIndex * 12 + vertexInTriangleIndex * 4 + 1] = color.g;
        colorBuffer[triangleIndex * 12 + vertexInTriangleIndex * 4 + 2] = color.b;
        colorBuffer[triangleIndex * 12 + vertexInTriangleIndex * 4 + 3] = color.a;

        // Texture coords
        texCoordsBuffer[triangleIndex * 6 + vertexInTriangleIndex * 2] = texCoord.x;
        texCoordsBuffer[triangleIndex * 6 + vertexInTriangleIndex * 2 + 1] = texCoord.y;

        he = he.getNext();
      }
    }
    for (int i = 0; i < size; i++) {
      indexBuffer[i] = i;
    }

    mesh.setBuffer(Type.Position, 3, positionBuffer);
    mesh.setBuffer(Type.Index, 1, indexBuffer);
    mesh.setBuffer(Type.Color, 4, colorBuffer);
    mesh.setBuffer(Type.Normal, 3, normalBuffer);
    mesh.setBuffer(Type.TexCoord, 2, texCoordsBuffer);
    mesh.updateBound();

    Geometry geom = new Geometry("triangle mesh", mesh);

    Material mat = new Material(assetManager,
            "Common/MatDefs/Light/Lighting.j3md");
    mat.setColor("Diffuse", ColorRGBA.White);
    mat.setBoolean("UseVertexColor", true);

    geom.setMaterial(mat);

    return geom;
  }
}
