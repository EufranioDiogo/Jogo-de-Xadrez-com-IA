package chessgameai.xadrez.gui;

import chessgameai.xadrez.engine.pecas.Peca;
import chessgameai.xadrez.engine.tabuleiro.Movimento;
import chessgameai.xadrez.engine.tabuleiro.Quadrado;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro;
import chessgameai.xadrez.engine.tabuleiro.TabuleiroUtils;
import chessgameai.xadrez.engine.player.MoveTransition;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.lang.model.element.Element;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public class Mesa {
    private final JFrame gameFrame;
    private final static int  WIDTH = 600, HEIGHT = 600;
    private final Dimension DIMENSAO_PAINEL_TABULEIRO = new Dimension(400, 350);
    private final Dimension DIMENSAO_PAINEL_QUADRADO = new Dimension(10, 10);
    private final PainelTabuleiro painelTabuleiro;
    private Tabuleiro tabuleiro;
    private final String pathPecaIcon = "/home/ed/NetBeansProjects/ChessGameAI/art/";
    private Quadrado origemQuadrado;
    private Quadrado destinoQuadrado;
    private Peca pecaMovidaPeloHumano;
    
    public Mesa() {
        gameFrame = new JFrame();
        this.gameFrame.setLayout(new BorderLayout());
        
        final JMenuBar menuBar = construirMenuBar();
        
        tabuleiro = Tabuleiro.criarJogoPadrao();
        
        this.painelTabuleiro = new PainelTabuleiro();
        this.gameFrame.add(this.painelTabuleiro, BorderLayout.CENTER);
        
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gameFrame.setJMenuBar(menuBar);
        this.gameFrame.setLocationRelativeTo(null);
        this.gameFrame.setResizable(false);
        this.gameFrame.setSize(WIDTH, HEIGHT);
        this.gameFrame.setVisible(true);
    }

    private JMenuBar construirMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createExitGame());
        
        return menuBar;
    }

    private JMenu createFileMenu() {
       final JMenu fileMenu = new JMenu("File");
       final JMenuItem openPGN = new JMenuItem("Load PGN file");
       
       openPGN.addActionListener((ActionEvent e) -> {
           System.out.println("Open PGN");
       });
       
       fileMenu.add(openPGN);
       return fileMenu;
    }

    private JMenuItem createExitGame() {
        final JMenuItem exitGame = new JMenuItem("Sair");
        
        exitGame.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        return exitGame;
    }
    
    private class PainelTabuleiro extends JPanel {
        final List<PainelQuadrado> quadradosTabuleiro;
        
        public PainelTabuleiro() {
            super(new GridLayout(8, 8));
            
            this.quadradosTabuleiro = new ArrayList<>();
            Color cor;
            Color corBranca = new Color(255, 255, 255);
            Color corNegra = new Color(33, 33, 33);
            boolean alternadorCor = true;
            boolean corDeInicio = true;
            
            for (int i = 0; i < TabuleiroUtils.NUM_QUADRADOS; i++) {
                if (corDeInicio) {
                    if (alternadorCor) {
                        cor = corBranca;
                        alternadorCor = false;
                    } else {
                        cor = corNegra;
                        alternadorCor = true;
                    }
                } else {
                    if (alternadorCor) {
                        cor = corNegra;
                        alternadorCor = false;
                    } else {
                        cor = corBranca;
                        alternadorCor = true;
                    }
                }
                final PainelQuadrado quadrado = new PainelQuadrado(this, i, cor);
                this.quadradosTabuleiro.add(quadrado);
                add(quadrado);
                
                if ((i + 1) % TabuleiroUtils.NUM_QUADRADOS_POR_LINHA == 0) {
                    if (corDeInicio == true) {
                        corDeInicio = false;
                    } else {
                        corDeInicio = true;
                    }
                }
            }
            setPreferredSize(DIMENSAO_PAINEL_TABULEIRO);
            validate();
        }

        private void drawTabuleiro(Tabuleiro tabuleiro) throws IOException {
            removeAll();
            for (PainelQuadrado quadrado : this.quadradosTabuleiro) {
                quadrado.drawQuadrado(tabuleiro);
                add(quadrado);
            }
            validate();
            repaint();
        }
    }
    
    public static class LogMovimento {
        private final ArrayList<Movimento> movimentos;

        public LogMovimento() {
            this.movimentos = new ArrayList<>();
        }
        
        public ArrayList<Movimento> getMovimentos() {
            return this.movimentos;
        }
        
        public void adicionarMovimento(Movimento movimento) {
            this.movimentos.add(movimento);
        }
        
        public int size() {
            return this.movimentos.size();
        }
        
        public void clear() {
            this.movimentos.clear();
        }
        
        public Movimento removerMovimento(int index) {
            return this.movimentos.remove(index);
        }
        
        public boolean removerMovimento(final Movimento movimento) {
            return this.movimentos.remove(movimento);
        }
    }
    
    
    private class PainelQuadrado extends JPanel {
        private final int idQuadrado;

        public PainelQuadrado(final PainelTabuleiro painelTabuleiro, final int idQuadrado, Color cor) {
            //super(new GridBagLayout());
            super.setBackground(Color.RED);
            this.idQuadrado = idQuadrado;
            
            try {
                colocarIconsPeca(tabuleiro);
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
            
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(SwingUtilities.isRightMouseButton(e)) {
                        origemQuadrado = null;
                        destinoQuadrado = null;
                        pecaMovidaPeloHumano = null;
                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        Component elementoPressionado = e.getComponent().getComponentAt(e.getPoint());
                        if (elementoPressionado instanceof PainelQuadrado) {
                            
                            if (origemQuadrado  == null) {
                                origemQuadrado = tabuleiro.getQuadrado(idQuadrado);
                                pecaMovidaPeloHumano = origemQuadrado.getPeca();

                                if (pecaMovidaPeloHumano == null) {
                                    origemQuadrado = null;
                                }
                            } else {

                                if (origemQuadrado.getPeca().getPosicaoPeca() == idQuadrado) {
                                    origemQuadrado = null;
                                    destinoQuadrado = null;
                                    pecaMovidaPeloHumano = null;
                                } else {
                                    final Movimento movimento = Movimento.MovimentoFactory.criarMovimento(tabuleiro,
                                    origemQuadrado.getPeca().getPosicaoPeca(), idQuadrado);

                                    final MoveTransition movimentacao = tabuleiro.getJogadorActual().fazerMovimento(movimento);

                                    if (movimentacao.getEstadoMovimento().isDone()) {
                                        tabuleiro = movimentacao.getTabuleiro();
                                        
                                        //Move Log
                                    }

                                    SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                            try {
                                                painelTabuleiro.drawTabuleiro(tabuleiro);
                                            } catch (IOException ex) {
                                                Logger.getLogger(Mesa.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    });

                                    destinoQuadrado = null;
                                    origemQuadrado = null;
                                    pecaMovidaPeloHumano = null;
                                }
                            }
                        }
                    }
                    
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    
                }
            });
            setPreferredSize(DIMENSAO_PAINEL_QUADRADO);
            setBackground(cor);
            validate();
        }

        private void adicionarCorQuadrado() {
            
        }
        
        private void colocarIconsPeca(final Tabuleiro tabuleiro) throws IOException {
            this.removeAll();
            
            if (tabuleiro.getQuadrado(this.idQuadrado).isQuadradoOcupado()) {
                final String nomeDaPeca = tabuleiro.getQuadrado(this.idQuadrado).getPeca().getAlliancePeca().toString() + 
                      tabuleiro.getQuadrado(this.idQuadrado).getPeca().toString().toUpperCase() + ".gif"; 
                final File novaImagem = new File(pathPecaIcon + nomeDaPeca);
                
                final BufferedImage image = ImageIO.read(novaImagem);
                final JLabel label = new JLabel(new ImageIcon(image));
                label.setBackground(Color.red);
                
                add(label);
            }
        }

        public void drawQuadrado(Tabuleiro tabuleiro) throws IOException {
            colocarIconsPeca(tabuleiro);
            validate();
            repaint();
        }
    }
}
