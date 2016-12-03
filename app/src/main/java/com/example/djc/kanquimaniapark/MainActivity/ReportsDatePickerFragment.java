package com.example.djc.kanquimaniapark.MainActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.djc.kanquimaniapark.Clases.Atraccion;
import com.example.djc.kanquimaniapark.Clases.Factura;
import com.example.djc.kanquimaniapark.Clases.IdentificadorEntrada;
import com.example.djc.kanquimaniapark.Clases.Producto;
import com.example.djc.kanquimaniapark.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Created by dewyn on 9/28/2016.
 */

public class ReportsDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    //<editor-fold desc="Constantes">
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
    private static final String FECHA = "Fecha";
    //</editor-fold>

    //<editor-fold desc="Listas">
    private ArrayList<Atraccion> atracciones;
    private ArrayList<Producto> productos;
    private ArrayList<Factura> facturas;
    private ArrayList<String> colores;
    private ArrayList<IdentificadorEntrada> identificadores;
    //</editor-fold>

    private File root, file;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        root = Environment.getExternalStorageDirectory();

        atracciones = new ArrayList<>();
        productos = new ArrayList<>();
        colores = new ArrayList<>();
        facturas = new ArrayList<>();
        identificadores = new ArrayList<>();

        DatabaseReference atrRef = FirebaseDatabase.getInstance().getReference(ATRACCIONES_ID);
        DatabaseReference identRef = FirebaseDatabase.getInstance().getReference(IDENTIFICADORES);
        DatabaseReference prodRef = FirebaseDatabase.getInstance().getReference(PRODUCTOS);
        DatabaseReference colorsRef = FirebaseDatabase.getInstance().getReference(COLORES);
        DatabaseReference factRef = FirebaseDatabase.getInstance().getReference(FACTURAS);

        colorsRef.addChildEventListener(getColores);
        atrRef.addValueEventListener(getAtr);
        identRef.addValueEventListener(getIden);
        prodRef.addValueEventListener(getProd);
        factRef.addValueEventListener(getFact);

        //INICIALIZA EL CALENDARIO CON LA FECHA ACTUAL
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.style_date_picker_dialog, this, year, month, day);
        DatePicker picker = datePickerDialog.getDatePicker();
        picker.setMaxDate(c.getTimeInMillis());

        // CREA INSTANCIA DEL DIALOGO Y SE DEVUELVE
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String selectedDate = dayOfMonth + "/" + (month+1) + "/" + year;
        String formatedDate = selectedDate.replace("/", "-");
        String path = "/Reporte - " + formatedDate + ".pdf";
        file = new File(root, path);
        generarReporte(selectedDate);
    }

    private void generarReporte(String selectedDate) {
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
        Paragraph TituloJornada = new Paragraph("Colores usados:\n\n",f);

        TituloJornada.setAlignment(1);

        //Llamar funcion para poner headers con titulo y logos
        try {
            DrawLogos(document, selectedDate);
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
        for (Atraccion a : atracciones) {
            PdfPCell cellOne = new PdfPCell(new Phrase(a.get_titulo() + " -> " + a.get_tiempo() + " mins"));
            PdfPCell cellTwo = new PdfPCell();
            try {
                for (IdentificadorEntrada i : identificadores){
                    if(selectedDate.equals(i.get_fecha())) {
                        for(Map.Entry Entry : i.get_atracciones().entrySet()) {
                            if(Objects.equals(Entry.getValue(), a.get_id())) {
                                cellTwo = new PdfPCell(RetornarColorImagen(String.valueOf(Entry.getKey())));
                            }
                        }
                    }
                }

            } catch (IOException | BadElementException e) {
                e.printStackTrace();
            }

            cellOne.setPadding(7);
            cellTwo.setPadding(5);
            table.addCell(cellOne);
            table.addCell(cellTwo);
        }
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
            DatosReporte(document, selectedDate);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        document.close();

        Uri uri = Uri.fromFile(file);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("application/pdf");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"facturacion.kanquipark@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reporte - " + selectedDate);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Sistema de facturacion de KanquiMania Park");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public Image RetornarColorImagen(String Color) throws IOException, BadElementException{
        Image img;
        if(Color.equals("Rojo")){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/red.png");
            img = img2;
        }
        else if (Color.equals("Azul")){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/blue.png");
            img = img2;
        }
        else if (Color.equals("Negro")){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/black.png");
            img = img2;
        }
        else if (Color.equals("Blanco")){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/white.png");
            img = img2;
        }
        else if (Color.equals("Amarillo")){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/yellow.png");
            img = img2;
        }
        else if (Color.equals("Azul Celeste")){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/cyan.png");
            img = img2;
        }
        else if (Color.equals("Rosado")){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/pink.png");
            img = img2;
        }
        else if (Color.equals("Naranja")){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/orange.png");
            img = img2;
        }
        else if (Color.equals("Morado")){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/purple.png");
            img = img2;
        }
        else if (Color.equals("Dorado")){
            Image img2 = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/gold.png");
            img = img2;
        }
        else if (Color.equals("Plateado")){
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

    public void DrawLogos(Document doc, String selectedDate) throws DocumentException, IOException{

        //Crear Tabla
        PdfPTable table = new PdfPTable(new float[] { 5, 3 });
        table.setWidthPercentage(100);

        Image Header = Image.getInstance(String.valueOf(Environment.getExternalStorageDirectory()) + "/Imagenes/KMPark.fw.png");
        Header.scaleAbsolute(150,130);
        PdfPCell cellOne = new PdfPCell(new Paragraph(selectedDate));
        PdfPCell CellLogo = new PdfPCell(Header);
        CellLogo.setBorder(Rectangle.NO_BORDER);
        cellOne.setBorder(Rectangle.NO_BORDER);
        cellOne.setPadding(5);
        table.addCell(CellLogo);
        table.addCell(cellOne);
        doc.add(table);
        doc.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------\n"));
    }

    public void DatosReporte(Document doc, String selectedDate) throws DocumentException, IOException{
        float total = 0f;
        PdfPTable table = new PdfPTable(new float[] { 3, 1 });
        table.setWidthPercentage(100);

        PdfPCell cellOne;
        for (Atraccion a : atracciones) {
            cellOne = new PdfPCell(new Paragraph(a.get_titulo() + " -> " + a.get_tiempo() + " mins"));
            cellOne.setPadding(5);
            table.addCell(cellOne);
            int cant = 0;
            for (Factura f : facturas) {
                if(selectedDate.equals(f.get_fechaEmision())) {
                    if (f.get_atraccionesSeleccionadas() != null) {
                        for (String b : f.get_atraccionesSeleccionadas()) {
                            if(a.get_id().equals(b)) {
                                cant += 1;
                            }
                        }
                    }
                    total += Double.parseDouble(f.get_totalFinal());
                }
            }
            table.addCell(new Paragraph(String.valueOf(cant)));
        }
        cellOne = new PdfPCell(new Paragraph("Ingresos de la Jornada:"));
        cellOne.setPadding(5);
        table.addCell(cellOne);
        table.addCell(new Paragraph(String.valueOf(total)));
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
            GenericTypeIndicator<Map<String, Map<String, String>>> genin = new GenericTypeIndicator<Map<String, Map<String, String>>>() {};
            Map<String, Object> map = (Map<String, Object>)dataSnapshot.getValue();
            identificadores.clear();

            if (map != null){
                for (Map.Entry entry : map.entrySet()){
                    if (entry != null){
                        HashMap value = (HashMap) entry.getValue();
                        IdentificadorEntrada i = new IdentificadorEntrada();
                        i.set_id((String) value.get(ID));
                        i.set_fecha((String)value.get(FECHA));

                        HashMap<String, String> atraccionesHM = (HashMap<String, String>) value.get(ATRACCIONES_ID);
                        if (atraccionesHM != null){
                            i.set_atraccion_color(atraccionesHM);
                        }
                        identificadores.add(i);
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
            Map<String, Object> map = (Map<String, Object>)dataSnapshot.getValue();
            facturas.clear();

            if (map != null){
                for (Map.Entry entry : map.entrySet()){
                    if (entry != null){
                        HashMap value = (HashMap) entry.getValue();
                        Factura f = new Factura();
                        f.set_id((String) value.get(ID));
                        f.set_fechaEmision((String)value.get(FECHA_EMISION));
                        f.set_totalDescontado((String)value.get(TOTAL_DESCONTADO));
                        f.set_totalFinal((String) value.get(TOTAL_FINAL));

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
