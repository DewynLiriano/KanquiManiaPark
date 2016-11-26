package com.example.djc.kanquimaniapark.Admin.GeneraReportes;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.djc.kanquimaniapark.Clases.Atraccion;
import com.example.djc.kanquimaniapark.Clases.Factura;
import com.example.djc.kanquimaniapark.Clases.IdentificadorEntrada;
import com.example.djc.kanquimaniapark.Clases.Producto;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StreamDownloadTask;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.example.djc.kanquimaniapark.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class GenerarReporte extends Fragment {

    private static final String ID = "ID";
    private static final String NOMBRE = "Nombre";
    private static final String TIEMPO = "Tiempo";
    private static final String PRECIO = "Precio";
    private static final String TITULO = "Titulo";
    private static final String ATRACCIONES_ID = "Atracciones";
    private static final String IDENTIFICADORES = "Identificadores";
    private static final String PRODUCTOS = "Productos";
    private static final String COLORES = "Colores";

    private static final String FACTURAS = "Facturas";
    private static final String FECHA_EMISION = "Fecha_Emision";
    private static final String TOTAL_DESCONTADO = "Total_Descontado";
    private static final String TOTAL_FINAL = "Total_Final";
    private static final String PRODUCTOS_SELECCIONADOS = "Productos_Seleccionados";
    private static final String ATRACCIONES_SELECCIONADAS = "Atracciones_Seleccionadas";
    private static final String ESPECIALES_APLICADOS = "Especiales_Aplicados";
    private static final String DATEFORMAT = "dd/MM/yyyy HH:mm";
    private static final String FECHA = "Fecha";


    File root = Environment.getExternalStorageDirectory();
    String path = "/Reporte.pdf";
    File file = new File(root, path);
    String fullVIP;
    String patinajeL;
    String patinajeI;
    String inflableL;
    String inflableI;

    private DatabaseReference colorsRef, atrRef, identRef, prodRef, factRef;

    private ArrayList<Atraccion> atracciones;
    private ArrayList<Producto> productos;
    private ArrayList<Factura> facturas;
    private ArrayList<String> colores;
    private IdentificadorEntrada identificador;

    public GenerarReporte() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reporte, container, false);

        atracciones = new ArrayList<>();
        productos = new ArrayList<>();
        colores = new ArrayList<>();
        facturas = new ArrayList<>();
        identificador = new IdentificadorEntrada();

        atrRef = FirebaseDatabase.getInstance().getReference(ATRACCIONES_ID);
        identRef = FirebaseDatabase.getInstance().getReference(IDENTIFICADORES);
        prodRef = FirebaseDatabase.getInstance().getReference(PRODUCTOS);
        colorsRef = FirebaseDatabase.getInstance().getReference(COLORES);
        factRef = FirebaseDatabase.getInstance().getReference(FACTURAS);

        colorsRef.addChildEventListener(getColores);
        atrRef.addValueEventListener(getAtr);
        identRef.limitToLast(1).addValueEventListener(getIden);
        prodRef.addValueEventListener(getProd);
        factRef.addValueEventListener(getFact);


        Button generarReporteButton = (Button) view.findViewById(R.id.generarReporte);
        generarReporteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarReporte();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void generarReporte() {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
        document.open();

        //Cambiar estilo de letras para crear parrafos(estilo y tamano)
        Font f=new Font(Font.FontFamily.TIMES_ROMAN,18.0f,Font.BOLD);
        Paragraph TituloJornada = new Paragraph("Los Colores de la Jornada X Son:\n\n",f);

        TituloJornada.setAlignment(1);

        //Llamar funcion para poner headers con titulo y logos

        try {
            DrawLogos(document);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        try {
            document.add(TituloJornada);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //Crear Tabla
        PdfPTable table = new PdfPTable(2);
        //---------------------------------------------------------------------------------------------------
        PdfPCell cellOne = new PdfPCell(new Phrase("Full VIP"));
        PdfPCell cellTwo = new PdfPCell();
        try {
            cellTwo = new PdfPCell(RetornarColorImagen(fullVIP));
        } catch (IOException | BadElementException e) {
            e.printStackTrace();
        }

        //   cellOne.setBorder(Rectangle.NO_BORDER);
        cellOne.setPadding(7);
        //   cellTwo.setBorder(Rectangle.NO_BORDER);
        cellTwo.setPadding(5);
        table.addCell(cellOne);
        table.addCell(cellTwo);
        //---------------------------------------------------------------------------------------------------
        cellOne = new PdfPCell(new Phrase("Patinaje Limitado"));
        try {
            cellTwo = new PdfPCell(RetornarColorImagen(patinajeL));
        } catch (IOException | BadElementException e) {
            e.printStackTrace();
        }

        //   cellOne.setBorder(Rectangle.NO_BORDER);
        cellOne.setPadding(7);
        //   cellTwo.setBorder(Rectangle.NO_BORDER);
        cellTwo.setPadding(5);
        table.addCell(cellOne);
        table.addCell(cellTwo);
        //---------------------------------------------------------------------------------------------------
        cellOne = new PdfPCell(new Phrase("Patinaje Ilimitado"));
        try {
            cellTwo = new PdfPCell(RetornarColorImagen(patinajeI));
        } catch (IOException | BadElementException e) {
            e.printStackTrace();
        }

        //   cellOne.setBorder(Rectangle.NO_BORDER);
        cellOne.setPadding(7);
        //   cellTwo.setBorder(Rectangle.NO_BORDER);
        cellTwo.setPadding(5);

        table.addCell(cellOne);
        table.addCell(cellTwo);
        //---------------------------------------------------------------------------------------------------
        cellOne = new PdfPCell(new Phrase("Inflables Ilimitado"));
        try {
            cellTwo = new PdfPCell(RetornarColorImagen(inflableI));
        } catch (IOException | BadElementException e) {
            e.printStackTrace();
        }

        //   cellOne.setBorder(Rectangle.NO_BORDER);
        cellOne.setPadding(7);
        //   cellTwo.setBorder(Rectangle.NO_BORDER);
        cellTwo.setPadding(5);

        table.addCell(cellOne);
        table.addCell(cellTwo);
        //---------------------------------------------------------------------------------------------------
        cellOne = new PdfPCell(new Phrase("Inflables limitado"));
        try {
            cellTwo = new PdfPCell(RetornarColorImagen(inflableL));
        } catch (IOException | BadElementException e) {
            e.printStackTrace();
        }

        //   cellOne.setBorder(Rectangle.NO_BORDER);
        cellOne.setPadding(7);
        //   cellTwo.setBorder(Rectangle.NO_BORDER);
        cellTwo.setPadding(5);

        table.addCell(cellOne);
        table.addCell(cellTwo);
        //---------------------------------------------------------------------------------------------------
        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        try {
            document.add(new Paragraph("\nReporte de la Jornada:\n\n",f));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        try {
            DatosReporte(document,1,1,1,1,1,1,1,1);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();
        Uri uri = Uri.fromFile(file);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("application/pdf");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"marcanosantanajm@hotmail.com", "facturacion.kanquipark@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "From My App");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public static Image RetornarColorImagen(String Color) throws IOException, BadElementException{
        Image img;
        if(Color == "Rojo"){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/red.png");
            img = img2;
        }
        else if (Color =="Azul"){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/blue.png");
            img = img2;
        }
        else if (Color =="Negro"){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/black.png");
            img = img2;
        }
        else if (Color =="Blanco"){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/white.png");
            img = img2;
        }
        else if (Color =="Amarillo"){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/yellow.png");
            img = img2;
        }
        else if (Color =="Azul Celeste"){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/cyan.png");
            img = img2;
        }
        else if (Color =="Rosado"){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/pink.png");
            img = img2;
        }
        else if (Color =="Naranja"){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/orange.png");
            img = img2;
        }
        else if (Color =="Morado"){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/purple.png");
            img = img2;
        }
        else if (Color =="Dorado"){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/gold.png");
            img = img2;
        }
        else if (Color =="Plateado"){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/silver.png");
            img = img2;
        }
        else{
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/Green.png");
            img = img2;
        }

        img.scaleAbsoluteHeight(35);
        img.scaleAbsoluteWidth(200);

        return img;
    }

    //Funcion para definir la cabecera de los Reportes
    public static void DrawLogos(Document doc) throws DocumentException, IOException{

        //Crear Tabla
        PdfPTable table = new PdfPTable(new float[] { 5, 3 });
        table.setWidthPercentage(100);

        long ahora = System.currentTimeMillis();
        Date fecha = new Date(ahora);
        DateFormat df = new SimpleDateFormat("                                   dd/MM/yy", Locale.getDefault());
        String Generado = df.format(fecha);

        Image Header = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/KMPark.fw.png");
        Header.scaleAbsolute(150,130);
        PdfPCell cellOne = new PdfPCell(new Paragraph(Generado));
        PdfPCell CellLogo = new PdfPCell(Header);
        CellLogo.setBorder(Rectangle.NO_BORDER);
        cellOne.setBorder(Rectangle.NO_BORDER);
        cellOne.setPadding(5);
        table.addCell(CellLogo);
        table.addCell(cellOne);
        doc.add(table);
        doc.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------\n"));
    }

    //Funcion Para Generar Datos para el reporte
    public static void DatosReporte(Document doc, int inflableI, int inflablesL, int patinajeI,
                                    int patinajeL, int fullVIP, int cantfacturas, int cantclientes,
                                    int ingresos) throws DocumentException, IOException{

        //Crear Tabla
        PdfPTable table = new PdfPTable(new float[] { 3, 1 });
        table.setWidthPercentage(100);

        PdfPCell cellOne = new PdfPCell(new Paragraph("Cantidad de Boletas de Inflables Ilimitadas Vendidas:"));
        cellOne.setPadding(5);
        table.addCell(cellOne);
        table.addCell(new Paragraph(String.valueOf(inflableI)));

        cellOne = new PdfPCell(new Paragraph("Cantidad de Boletas de Inflables limitadas Vendidas:"));
        cellOne.setPadding(5);
        table.addCell(cellOne);
        table.addCell(new Paragraph(String.valueOf(inflablesL)));

        cellOne = new PdfPCell(new Paragraph("Cantidad de Boletas de Patinaje Ilimitadas Vendidas:"));
        cellOne.setPadding(5);
        table.addCell(cellOne);
        table.addCell(new Paragraph(String.valueOf(patinajeI)));

        cellOne = new PdfPCell(new Paragraph("Cantidad de Boletas de Patinaje limitadas Vendidas:"));
        cellOne.setPadding(5);
        table.addCell(cellOne);
        table.addCell(new Paragraph(String.valueOf(patinajeL)));

        cellOne = new PdfPCell(new Paragraph("Cantidad de Boletas Full VIP Vendidas:"));
        cellOne.setPadding(5);
        table.addCell(cellOne);
        table.addCell(new Paragraph(String.valueOf(fullVIP)));

        cellOne = new PdfPCell(new Paragraph("Cantidad de Facturas generadas:"));
        cellOne.setPadding(5);
        table.addCell(cellOne);
        table.addCell(new Paragraph(String.valueOf(cantfacturas)));

        cellOne = new PdfPCell(new Paragraph("Cantidad de Clientes en la Jornada:"));
        cellOne.setPadding(5);
        table.addCell(cellOne);
        table.addCell(new Paragraph(String.valueOf(cantclientes)));

        cellOne = new PdfPCell(new Paragraph("Ingresos de la Jornada:"));
        cellOne.setPadding(5);
        table.addCell(cellOne);
        table.addCell(new Paragraph(String.valueOf(ingresos)));
        doc.add(table);
    }


    private ValueEventListener getAtr = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<Map<String, Map<String, String>>> genin = new GenericTypeIndicator<Map<String, Map<String, String>>>() {};
            Map<String, Map<String,String>> map = dataSnapshot.getValue(genin);
            atracciones.clear();

            if (map != null){
                for (Map.Entry<String, Map<String, String>> entry : map.entrySet()){
                    if (entry != null){
                        HashMap value = (HashMap) entry.getValue();
                        Atraccion a = new Atraccion();
                        a.set_id((String)value.get(ID));
                        a.set_titulo((String)value.get(NOMBRE));
                        a.set_precio((String) value.get(PRECIO));
                        a.set_tiempo((String) value.get(TIEMPO));
                        atracciones.add(a);
                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.v("Error", databaseError.getMessage());
        }
    };

    private ValueEventListener getProd = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<Map<String, Map<String, String>>> genin = new GenericTypeIndicator<Map<String, Map<String, String>>>() {};
            Map<String, Map<String,String>> map = dataSnapshot.getValue(genin);
            productos.clear();

            if (map != null){
                for (Map.Entry<String, Map<String, String>> entry : map.entrySet()){
                    if (entry != null){
                        HashMap value = (HashMap) entry.getValue();
                        Producto p = new Atraccion();
                        p.set_id((String)value.get(ID));
                        p.set_titulo((String)value.get(TITULO));
                        p.set_precio((String) value.get(PRECIO));
                        productos.add(p);
                    }
                }
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ValueEventListener getIden = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            HashMap root = (HashMap)dataSnapshot.getValue();

            if (root != null){
                Collection<Object> objects = root.values();
                for (Object o : objects){
                    if (o instanceof Map){
                        HashMap<String, Object> map = (HashMap<String, Object>) o;
                        identificador.set_id((String) map.get(ID));
                        identificador.set_fecha((String) map.get(FECHA));

                        HashMap<String, String> map_atracciones = (HashMap<String, String>) map.get(ATRACCIONES_ID);
                        if (map_atracciones != null){
                            identificador.set_atraccionesID(map_atracciones);
                        }

                        HashMap<String, String> lista_colores = (HashMap<String, String>) map.get(COLORES);
                        if (lista_colores != null){
                            identificador.set_colores(new ArrayList<>(lista_colores.values()));
                        }
                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.v("Error", databaseError.getMessage());

        }
    };

    private ValueEventListener getFact = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<Map<String, Map<String, String>>> genin = new GenericTypeIndicator<Map<String, Map<String, String>>>() {};
            Map<String, Map<String,String>> map = dataSnapshot.getValue(genin);
            facturas.clear();

            if (map != null){
                for (Map.Entry<String, Map<String, String>> entry : map.entrySet()){
                    if (entry != null){
                        HashMap value = (HashMap) entry.getValue();
                        Factura f = new Factura();
                        f.set_id((String) value.get(ID));
                        f.set_fechaEmision((String)value.get(FECHA_EMISION));
                        f.set_totalDescontado((double)value.get(TOTAL_DESCONTADO));
                        f.set_totalFinal((double) value.get(TOTAL_FINAL));

                        HashMap<String, String> productos_seleccionados = (HashMap<String, String>) value.get(PRODUCTOS_SELECCIONADOS);
                        if (productos_seleccionados != null){
                            f.set_productosSeleccionados(new ArrayList<>(productos_seleccionados.values()));
                        }

                        HashMap<String, String> atracciones_seleccionadas = (HashMap<String, String>) value.get(ATRACCIONES_SELECCIONADAS);
                        if (atracciones_seleccionadas != null){
                            f.set_atraccionesSeleccionadas(new ArrayList<>(atracciones_seleccionadas.values()));
                        }

                        HashMap<String, String> especiales_aplicados = (HashMap<String, String>) value.get(ESPECIALES_APLICADOS);
                        if (especiales_aplicados != null){
                            f.set_especialesID(new ArrayList<String>(especiales_aplicados.values()));
                        }
                        facturas.add(f);
                    }
                }
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ChildEventListener getColores = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            colores.add((String) dataSnapshot.getValue());
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}