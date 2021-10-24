# App BST (Binary Search Tree)

BST es una aplicación nativa para Android. Su funcionamiento se basa en las estructuras de árboles binarios, y con esta aplicación se consigue representar gráficamente los árboles binarios. 

![Demo](https://i.ibb.co/RDqgNs1/AppBST.jpg)

Permite añadir, eliminar y buscar nodos. Además que muestra diferentes recorridos del árbol.

## Lienzo

Lienzo es la clase con la que se parte para poder hacer todo el trabajo de graficado del árbol.
```java
public class Lienzo extends View {

    public Path drawPath;  
    private static Paint drawPaint;
    private Paint canvasPaint;
    private static int paintColor = 0xFFFF0000;
    public Canvas drawCanvas;
    private Bitmap canvasBitmap;

    public Lienzo(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }
}
```