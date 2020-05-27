package com.devjpah.appbst;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    Lienzo lienzo;
    EditText et_input;
    FloatingActionButton fab_add, fab_delete, fab_search, fab_help;
    BinarySearchTree tree;
    Paint paintCircle;
    Paint paintText;
    Paint paintLines;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        conectar();

        tree = new BinarySearchTree();
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nodosInput = et_input.getText().toString().trim();
                if (!nodosInput.isEmpty()) {
                    String[] nodos = nodosInput.split(",");
                    if (nodos.length < 10) {
                        try {
                            int[] nodosInt = toInt(nodos);
                            for (int i = 0; i < nodosInt.length; i++) {
                                int cont = 0;
                                for (int j = 0; j < nodosInt.length; j++) {
                                    cont = (nodosInt[i] == nodosInt[j]) ? cont + 1 : cont;
                                }
                                if (cont == 1) {
                                    tree.Add(nodosInt[i]);
                                } else {
                                    throw new Exception("NO SE PUEDEN INGRESAR DATOS REPETIDOS");
                                }
                            }
                            lienzo.resetLienzo();
                            drawTree();
                            lienzo.invalidate();
                            Toasty.success(getApplicationContext(), "NODOS INGRESADOS CORRECTAMENTE", Toasty.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            Toasty.error(getApplicationContext(), "ERROR: " + ex.getMessage(), Toasty.LENGTH_LONG).show();
                        }
                    } else {
                        Toasty.error(getApplicationContext(), "NO SE PERMITEN INGRESAR MÁS DE 9 NODOS", Toasty.LENGTH_SHORT).show();
                    }
                } else {
                    Toasty.error(getApplicationContext(), "POR FAVOR INGRESE NODOS", Toasty.LENGTH_SHORT).show();
                }
                et_input.setText("");
            }
        });
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String dato = et_input.getText().toString().trim();
                if (!dato.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Eliminar Nodo");
                    builder.setMessage("¿Está seguro de que desea eliminar el nodo '" + dato + "' ?")
                            .setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        int datoInt = Integer.parseInt(dato);
                                        tree.Delete(datoInt);
                                        lienzo.resetLienzo();
                                        drawTree();
                                    } catch (Exception ex) {
                                        Toasty.error(getApplicationContext(), "ERROR: " + ex.getMessage(), Toasty.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                    et_input.setText("");
                } else {
                    Toasty.error(getApplicationContext(), "POR FAVOR INGRESE UN NODO", Toasty.LENGTH_SHORT).show();
                }
            }
        });
        fab_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dato = et_input.getText().toString().trim();
                BinaryNode busqueda = null;
                String info = "";
                if (!dato.isEmpty()) {
                    try {
                        final int datoInt = Integer.parseInt(dato);
                        busqueda = tree.Search(datoInt);
                    } catch (Exception ex) {
                        Toasty.error(getApplicationContext(), "ERROR: " + ex.getMessage());
                    }
                    if(busqueda != null) {
                        info += "El valor ha sido encontrado";
                    } else {
                        info += "El valor no se ha encontrado";
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Búsqueda");
                    builder.setMessage("Resultado de la búsqueda: \n" + info)
                            .setPositiveButton("LISTO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    et_input.setText("");
                                }
                            }).show();
                } else {
                    Toasty.error(getApplicationContext(), "POR FAVOR INGRESE UN NODO", Toasty.LENGTH_SHORT).show();
                }
            }
        });
        fab_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_help, null);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();
                TextView textView = view.findViewById(R.id.textview);
                Button btnDialog = view.findViewById(R.id.btnDialog);
                textView.setText(R.string.instructions);
                btnDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });

        //Configuracion de propiedades de estilo de circulos
        paintCircle = new Paint();
        paintCircle.setColor(Color.BLACK);
        paintCircle.setAntiAlias(true);
        paintCircle.setStrokeWidth(8);
        paintCircle.setStyle(Paint.Style.STROKE);

        //Configuracion de propiedades de estilo de texto
        paintText = new Paint();
        paintText.setColor(getResources().getColor(R.color.colorPrimaryDark));
        paintText.setAntiAlias(true);
        paintText.setTextSize(50);

        //Configuracion de propiedades de estilo de las lineas
        paintLines = new Paint();
        paintLines.setColor(getResources().getColor(R.color.colorPrimary));
        paintLines.setAntiAlias(true);
        paintLines.setStyle(Paint.Style.STROKE);
        paintLines.setStrokeWidth(10);
    }

    private void conectar() {
        lienzo = findViewById(R.id.lienzo);
        et_input = findViewById(R.id.et_input);
        fab_add = findViewById(R.id.fab_add);
        fab_delete = findViewById(R.id.fab_delete);
        fab_search = findViewById(R.id.fab_search);
        fab_help = findViewById(R.id.fab_help);
    }

    private void drawTree() {
        //Variables de tamaño de la interfaz
        int xInterface = lienzo.getMeasuredWidth();
        int yInterface = lienzo.getMeasuredHeight();

        //Coordenadas Iniciales de la Raiz
        int xRoot = xInterface / 2;
        int yRoot = yInterface / 8;

        //Draw Nodes and lines
        tree.getRoot().setX(xRoot);
        tree.getRoot().setY(yRoot);
        drawNodes(tree.getRoot());
        drawLines(tree.getRoot());
    }

    private ArrayList<int[]> nodosXNiveles() {
        ArrayList<int[]> arrayNiveles = new ArrayList<>();
        String[] niveles = tree.imprimirNivel();
        for (int i = 0; i < niveles.length; i++) {
            niveles[i] = niveles[i].substring(0, niveles[i].length() - 1);
            String[] nodos = niveles[i].split(",");
            int[] nodosInt = toInt(nodos);
            arrayNiveles.add(nodosInt);
        }
        return arrayNiveles;
    }

    private void posicionarNodo(BinaryNode currentRoot) {
        ArrayList<int[]> nodosXNiveles = nodosXNiveles();
        //nivel-> Nivel en el que se encuentra el nodo actual
        //aum-> Aumento de la posición en X
        int nivel = 0, aum = 0;
        for (int i = 0; i < nodosXNiveles.size(); i++) {
            for (int j = 0; j < nodosXNiveles.get(i).length; j++) {
                if (currentRoot.getData() == nodosXNiveles.get(i)[j]) {
                    nivel = i;
                }
            }
        }

        switch (nivel) {
            case 0:
                aum = 500;
                break;
            case 1:
                aum = 285;
                break;
            case 2:
                aum = 135;
                break;
            case 3:
                aum = 85;
                break;
            case 4:
                aum = 80;
                break;
            case 5:
                aum = 70;
                break;
            case 6:
                aum = 70;
                break;
            case 7:
                aum = 70;
                break;
            case 8:
                aum = 70;
                break;
        }
        if (currentRoot.getLeft() != null) {
            int xLeft = currentRoot.getX() - aum;
            int yLeft = currentRoot.getY() + 100;
            /*if (currentRoot == tree.getRoot()) {
                xLeft += -400;
            } else {
                xLeft += -100;
            }*/
            currentRoot.getLeft().setX(xLeft);
            currentRoot.getLeft().setY(yLeft);
        }
        if (currentRoot.getRight() != null) {
            int xRight = currentRoot.getX() + aum;
            int yRight = currentRoot.getY() + 100;
            /*if (currentRoot == tree.getRoot()) {
                xRight += 400;
            } else {
                xRight += 100;
            }*/
            currentRoot.getRight().setX(xRight);
            currentRoot.getRight().setY(yRight);
        }
    }

    private void drawNodes(BinaryNode currentRoot) {
        if (currentRoot != null) {
            posicionarNodo(currentRoot);
            String data = String.valueOf(currentRoot.getData());
            int x = currentRoot.getX();
            int y = currentRoot.getY();
            lienzo.drawCanvas.drawCircle(x, y, 40, paintCircle);
            lienzo.drawCanvas.drawText(data, x - 25, y + 20, paintText);
            drawNodes(currentRoot.getLeft());
            drawNodes(currentRoot.getRight());
        }
    }

    private void drawLines(BinaryNode currentRoot) {
        if (currentRoot != null) {
            int xCurrent = currentRoot.getX();
            int yCurrent = currentRoot.getY();
            if (currentRoot.getLeft() != null) {
                lienzo.drawPath.moveTo(xCurrent - 30, yCurrent + 30);
                int x = currentRoot.getLeft().getX() + 30;
                int y = currentRoot.getLeft().getY() - 30;
                lienzo.drawPath.lineTo(x, y);
                lienzo.drawCanvas.drawPath(lienzo.drawPath, paintLines);
            }
            if (currentRoot.getRight() != null) {
                lienzo.drawPath.moveTo(xCurrent + 30, yCurrent + 30);
                int x = currentRoot.getRight().getX() - 30;
                int y = currentRoot.getRight().getY() - 30;
                lienzo.drawPath.lineTo(x, y);
                lienzo.drawCanvas.drawPath(lienzo.drawPath, paintLines);
            }
            lienzo.drawPath.reset();
            drawLines(currentRoot.getLeft());
            drawLines(currentRoot.getRight());
        }
    }

    private int[] toInt(String[] nodos) {
        int[] nodosInt = new int[nodos.length];
        for (int i = 0; i < nodos.length; i++) {
            nodosInt[i] = Integer.parseInt(nodos[i]);
        }
        return nodosInt;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        int idAction = 0;
        String recorrido = "", help = "";
        switch (id) {
            case R.id.action_inorden:
                idAction = R.string.action_inorden;
                recorrido = tree.InOrden();
                help = "I  -  R  -  D";
                mostrarRecorrido(idAction, recorrido, help);
                break;
            case R.id.action_preorden:
                idAction = R.string.action_preorden;
                recorrido = tree.PreOrden();
                help = "R  -  I  -  D";
                mostrarRecorrido(idAction, recorrido, help);
                break;
            case R.id.action_postorden:
                idAction = R.string.action_postorden;
                recorrido = tree.Postorden();
                help = "I  -  D  -  R";
                mostrarRecorrido(idAction, recorrido, help);
                break;
            case R.id.action_new_tree:

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Nuevo Árbol");
                    builder.setMessage("¿Está seguro de borrar el árbol actual y crear uno nuevo? ")
                            .setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tree = new BinarySearchTree();
                                    lienzo.resetLienzo();
                                }
                            })
                            .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarRecorrido(int idAction, String recorrido, String help) {
        if(tree.getRoot() != null) {
            recorrido = recorrido.substring(0,recorrido.length() - 2);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(idAction);
            builder.setMessage("El recorrido es el siguiente:   " + help + "\n" + recorrido)
                    .setPositiveButton("LISTO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
        } else {
            Toasty.warning(getApplicationContext(), "NO PODEMOS MOSTRAR EL RECORRIDO DE UN ARBOL VACÍO",Toasty.LENGTH_LONG).show();
        }


    }
}
