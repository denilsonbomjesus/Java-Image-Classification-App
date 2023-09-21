import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;

public class ImageSelectionGUI extends JFrame {
    private JButton openButton; // Botão para abrir o seletor de arquivos
    private JButton processButton; // Novo botão para chamar o script Python
    private JLabel nameLabel; // Novo rótulo para exibir o nome da imagem
    private JLabel imageLabel; // Rótulo para exibir a imagem selecionada
    private String selectedImagePath; // Variável para armazenar o caminho da imagem

    public ImageSelectionGUI() {
        setTitle("Image Selection GUI");
        setSize(500, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Configura a localização da janela para o centro da tela
        setLocationRelativeTo(null);
        
        processButton = new JButton("Processar Imagem"); // Botão para chamar o script Python
        processButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processImage(); // Chama o método para processar a imagem com o script Python
            }
        });

        // Inicialização do botão
        openButton = new JButton("Selecionar Imagem");
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectImage(); // Chamado quando o botão é clicado
            }
        });

        nameLabel = new JLabel(); // Inicializa o novo rótulo
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Inicialização do rótulo da imagem
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        // Configuração do layout da janela
        Container conteudo = new JPanel();
        conteudo.setLayout(new GridLayout(0, 1));
        conteudo.add(nameLabel); // Adiciona o rótulo do nome
        conteudo.add(imageLabel); // Coloca o rótulo da imagem abaixo
        
        
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(openButton, BorderLayout.NORTH);
        c.add(BorderLayout.CENTER, conteudo);
        c.add(processButton, BorderLayout.SOUTH);
    }

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();

        // Configura para permitir a seleção apenas de arquivos de imagem
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File file) {
                String filename = file.getName().toLowerCase();
                return filename.endsWith(".jpg") || filename.endsWith(".jpeg") ||
                       filename.endsWith(".png") || file.isDirectory();
            }

            public String getDescription() {
                return "Arquivos de Imagem (.jpg, .jpeg, .png)";
            }
        });

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath(); // Armazena o caminho na variável
            displaySelectedImage(selectedFile); // Exibe a imagem selecionada
            
         // Exibe selectedImagePath no console
            System.out.println("Caminho da imagem selecionada: " + selectedImagePath);
        }
    }

    private void displaySelectedImage(File file) {
        try {
            Image image = ImageIO.read(file); // Lê a imagem do arquivo
            if (image != null) {
                // Define a largura e a altura preferenciais para a imagem no JLabel
                int preferredWidth = 300; // Especifica a largura desejada
                int preferredHeight = 200; // Especifica a altura desejada

                // Redimensiona a imagem para se ajustar ao tamanho preferencial
                ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(preferredWidth, preferredHeight, Image.SCALE_SMOOTH));
                
                nameLabel.setText("Nome da Imagem: " + file.getName()); // Define o texto do rótulo do nome
                
                imageLabel.setIcon(imageIcon); // Define a imagem no rótulo
                imageLabel.setText(null); // Limpa qualquer texto de aviso
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("");
                nameLabel.setText("Arquivo não é uma imagem válida");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void processImage() {
        if (selectedImagePath != null) {
            try {
                // Comando para executar o script Python (substitua o caminho pelo caminho real do arquivo)
                String pythonCommand = "java_ml_cats_and_dogs.py";
                
                // Inicializa um processo para executar o comando
                ProcessBuilder processBuilder = new ProcessBuilder("python", pythonCommand, selectedImagePath);
                processBuilder.redirectErrorStream(true);

                Process process = processBuilder.start();

                //comando em python passado
                System.out.println("Comando de chamada do Python: " + process);
                
                // Captura a saída padrão do processo Python
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String output;
                StringBuilder prediction = new StringBuilder(); // Para armazenar a classificação
                
                while ((output = reader.readLine()) != null) {
                	prediction.append(output); // Captura a saída do Python
                }

                // Aguarda o término do processo Python
                int exitCode = process.waitFor();
                System.out.println("Código de Saída do Python: " + exitCode);
                
                // Agora, a variável "prediction" contém a classificação retornada pelo Python
                System.out.println("Classificação da Imagem: " + prediction.toString());

            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Nenhuma imagem selecionada para processar.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ImageSelectionGUI gui = new ImageSelectionGUI();
                gui.setVisible(true); // Torna a interface visível
            }
        });        

    }
}
