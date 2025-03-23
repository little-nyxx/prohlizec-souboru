import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProhlizecOkno extends JFrame{
    private JButton btnOpen;
    private JPanel pnMain;
    private JTextArea txtAVypis;

    private List<String> seznam = new ArrayList<>();
    private String cesta;

    public ProhlizecOkno() {
        initComponents();
    }

    private void initComponents() {
        setContentPane(pnMain);
        setTitle("Text file view");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        btnOpen.addActionListener(e -> {
            try {
                open();
            } catch (ProhlizecException ex) {
                throw new RuntimeException(ex);
            }
        });

        initMenu();


    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuFile = new JMenu("File");
        menuBar.add(menuFile);

        JMenuItem miOpen = new JMenuItem("Open");
        miOpen.addActionListener(e -> {
            try {
                open();
            } catch (ProhlizecException ex) {
                throw new RuntimeException(ex);
            }
        });
        menuFile.add(miOpen);

        JMenuItem miSave = new JMenuItem("Save");
        miSave.addActionListener(e -> {
            try {
                save(cesta);
            } catch (ProhlizecException ex) {
                throw new RuntimeException(ex);
            }
        });
        menuFile.add(miSave);

        JMenuItem miSaveAs = new JMenuItem("Save as..");
        miSaveAs.addActionListener(e -> {
            try {
                save(null);
            } catch (ProhlizecException ex) {
                throw new RuntimeException(ex);
            }
        });
        menuFile.add(miSaveAs);

    }

    private void save(String cesta) throws ProhlizecException {
        if (cesta != null) {
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(cesta)))){
                writer.println(txtAVypis.getText());
            } catch (IOException e) {
                System.err.println("Nastala chyba!");
            }
        } else {
            JFileChooser fc = new JFileChooser("resources/");
            int result = fc.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                cesta = file.getPath();
                try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(cesta)))){
                    writer.println(txtAVypis.getText());
                } catch (IOException e) {
                    System.err.println("Nastala chyba!");
                }
            }
        }
    }

   /* public void zapisSoubor(String nazevSouboru) throws ProhlizecException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(nazevSouboru)))){
            for(String string : seznam) {
                writer.write(string + "\n");
            }
        } catch (FileNotFoundException e) {
            throw new ProhlizecException("Soubor "+nazevSouboru+" nebyl nalezen!");
        } catch (IOException e) {
            throw new ProhlizecException("Nastala chyba!");
        }
    }*/

    private void open() throws ProhlizecException {
        try {
            JFileChooser fc = new JFileChooser();

            int result = fc.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fc.getSelectedFile();
                nactiSoubor(String.valueOf(selectedFile));

            }
        } catch (ProhlizecException e) {
            throw new ProhlizecException("Něco se pokazilo :(");
        }
    }

    public void nactiSoubor(String nazev) throws ProhlizecException {
        int aktualniIndex = 0;
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader(nazev)))){
            while (sc.hasNextLine()) {
                String radek = sc.nextLine();
                pridejRadek(radek + "\n");
                nactiDoOkna();
                txtAVypis.append(seznam.get(aktualniIndex));
                aktualniIndex++;

            }
        } catch (FileNotFoundException e) {
            throw new ProhlizecException("Chyba v názvu souboru: "+nazev);
        }

    }

    private void pridejRadek(String radek) {
        seznam.add(radek);
    }

    private void nactiDoOkna() {
        ;
    }


}
