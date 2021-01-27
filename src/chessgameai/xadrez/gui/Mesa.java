package chessgameai.xadrez.gui;

import chessgameai.xadrez.engine.pecas.Peca;
import chessgameai.xadrez.engine.tabuleiro.Movimento;
import chessgameai.xadrez.engine.tabuleiro.Quadrado;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro;
import chessgameai.xadrez.engine.tabuleiro.TabuleiroUtils;
import chessgameai.xadrez.engine.player.MoveTransition;
import chessgameai.xadrez.engine.player.agente.EstrategiaMovimento;
import chessgameai.xadrez.engine.player.agente.MiniMax;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public class Mesa extends Observable {
    private final JFrame gameFrame;
    private final PainelHistoricoDoJogo painelHistorico;
    private final PainelPecasPegas painelPecasCapturadas;
    private final LogMovimento logMovimento;
    private final GameSetup gameSetup;
    
    
    private final static int  WIDTH = 700, HEIGHT = 600;
    private final Dimension DIMENSAO_PAINEL_TABULEIRO = new Dimension(400, 350);
    private final Dimension DIMENSAO_PAINEL_QUADRADO = new Dimension(10, 10);
    private final PainelTabuleiro painelTabuleiro;
    private static Tabuleiro tabuleiro;
    private final String pathPecaIcon = "/home/ed/NetBeansProjects/ChessGameAI/art/";
    private Quadrado origemQuadrado;
    private Quadrado destinoQuadrado;
    private Peca pecaMovidaPeloHumano;
    private Movimento movimentoComputador;
    
    
    private static final Mesa INSTANCIA_MESA = new Mesa();
    
    
    private Mesa() {
        gameFrame = new JFrame();
        this.gameFrame.setLayout(new BorderLayout());
        
        final JMenuBar menuBar = construirMenuBar();
        
        
        tabuleiro = Tabuleiro.criarJogoPadrao();
        
        this.painelTabuleiro = new PainelTabuleiro();
        this.logMovimento = new LogMovimento();
        
        this.painelHistorico = new PainelHistoricoDoJogo();
        this.painelPecasCapturadas = new PainelPecasPegas();
        
        this.gameSetup = new GameSetup(this.gameFrame, true);
        
        
        this.gameFrame.add(this.painelPecasCapturadas, BorderLayout.WEST);
        this.gameFrame.add(this.painelTabuleiro, BorderLayout.CENTER);
        this.gameFrame.add(this.painelHistorico, BorderLayout.EAST);
        
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gameFrame.setJMenuBar(menuBar);
        this.gameFrame.setLocationRelativeTo(null);
        this.gameFrame.setResizable(false);
        this.gameFrame.setSize(WIDTH, HEIGHT);
        this.gameFrame.setVisible(true);
    }
    
    private static Tabuleiro getTabuleiro() {
        return tabuleiro;
    } 
    public static Mesa get() {
        return INSTANCIA_MESA;
    }
    
    public void show() throws IOException {
        Mesa.get().getMoveLog().clear();
        Mesa.get().getPainelHistorico().remontar(tabuleiro, Mesa.get().getMoveLog());
        Mesa.get().getPainelPecasCapturadas().remontar(Mesa.get().getMoveLog());
        Mesa.get().getPainelTabuleiro().drawTabuleiro(Mesa.getTabuleiro());
    }
    
    private GameSetup getGameSetup() {
        return this.gameSetup;
    }
    
    private void setupUpdate(final GameSetup gameSetup) {
        setChanged();
        notifyObservers(gameSetup);
    }
    
    public void updateGameBoard(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }
    
    public void updateMovimentoComputador(final Movimento movimento) {
        this.movimentoComputador = movimento;
    }
    
    public LogMovimento getMoveLog() {
        return this.logMovimento;
    }
    
    public PainelHistoricoDoJogo getPainelHistorico() {
        return this.painelHistorico;
    }
    
    public PainelPecasPegas getPainelPecasCapturadas() {
        return this.painelPecasCapturadas;
    }
    
    public PainelTabuleiro getPainelTabuleiro() {
        return this.painelTabuleiro;
    }
    
    public void moveFeitoUpdate(final PlayerType tipoJogador) {
        setChanged();
        notifyObservers(tipoJogador);
        
        if (Mesa.get().getGameSetup().isAIPlayer(Mesa.getTabuleiro().getJogadorActual()) &&
                !Mesa.getTabuleiro().getJogadorActual().isEmCheckMate() &&
                !Mesa.getTabuleiro().getJogadorActual().isEmStaleMate()) {
            // Create an AI Thread and execute

            final PensamentoAI pensamentoAI = new PensamentoAI();

            final EstrategiaMovimento minimax = new MiniMax(3);
            
            Movimento melhorMovimento = minimax.executar(Mesa.getTabuleiro());
            
            if (melhorMovimento != null) {
                    Mesa.get().updateMovimentoComputador(melhorMovimento);
                    Mesa.get().updateGameBoard(Mesa.get().getTabuleiro().getJogadorActual().fazerMovimento(melhorMovimento).getTabuleiro());
                    Mesa.get().getMoveLog().adicionarMovimento(melhorMovimento);
                    Mesa.get().getPainelHistorico().remontar(Mesa.getTabuleiro(), Mesa.get().getMoveLog());
                    Mesa.get().getPainelPecasCapturadas().remontar(Mesa.get().getMoveLog());
                    Mesa.get().moveFeitoUpdate(PlayerType.COMPUTER);

                    try {
                        Mesa.get().getPainelTabuleiro().drawTabuleiro(Mesa.getTabuleiro());
                    } catch (IOException ex) {
                        Logger.getLogger(Mesa.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Erro");
                }
        }

        if (Mesa.get().getTabuleiro().getJogadorActual().isEmCheckMate()) {
            System.out.println("CHECK MATE para " + Mesa.get().getTabuleiro().getJogadorActual());
        }
        if (Mesa.get().getTabuleiro().getJogadorActual().isEmStaleMate()) {
            System.out.println("Stale Mate para " + Mesa.get().getTabuleiro().getJogadorActual());
        }
    }
    
    private static class ObservadorAI implements Observer {

        @Override
        public void update(Observable o, Object arg) {
           if (Mesa.get().getGameSetup().isAIPlayer(Mesa.getTabuleiro().getJogadorActual()) &&
                   !Mesa.getTabuleiro().getJogadorActual().isEmCheckMate() &&
                   !Mesa.getTabuleiro().getJogadorActual().isEmStaleMate()) {
               // Create an AI Thread and execute
               
               final PensamentoAI pensamentoAI = new PensamentoAI();
               
               pensamentoAI.run();
           }
           
           if (Mesa.get().getTabuleiro().getJogadorActual().isEmCheckMate()) {
               System.out.println("CHECK MATE para " + Mesa.get().getTabuleiro().getJogadorActual());
           }
           if (Mesa.get().getTabuleiro().getJogadorActual().isEmStaleMate()) {
               System.out.println("Stale Mate para " + Mesa.get().getTabuleiro().getJogadorActual());
           }
        }
    
    }
    
    
    private static class PensamentoAI extends SwingWorker<Movimento, String> {
        Movimento melhorMovimento;
        
        private PensamentoAI() {
            
        }
        
        @Override
        protected Movimento doInBackground() throws Exception {
            final EstrategiaMovimento minimax = new MiniMax(3);
            
            Movimento melhorMovimento = minimax.executar(Mesa.getTabuleiro());
            
            return melhorMovimento;
        }
        
        
        @Override
        public void done() {
            try {
                Movimento melhorMovimento = get();
                
                if (melhorMovimento != null) {
                    Mesa.get().updateMovimentoComputador(melhorMovimento);
                    Mesa.get().updateGameBoard(Mesa.get().getTabuleiro().getJogadorActual().fazerMovimento(melhorMovimento).getTabuleiro());
                    Mesa.get().getMoveLog().adicionarMovimento(melhorMovimento);
                    Mesa.get().getPainelHistorico().remontar(Mesa.getTabuleiro(), Mesa.get().getMoveLog());
                    Mesa.get().getPainelPecasCapturadas().remontar(Mesa.get().getMoveLog());
                    Mesa.get().moveFeitoUpdate(PlayerType.COMPUTER);

                    try {
                        Mesa.get().getPainelTabuleiro().drawTabuleiro(Mesa.getTabuleiro());
                    } catch (IOException ex) {
                        Logger.getLogger(Mesa.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    final PensamentoAI pensamentoAI = new PensamentoAI();
               
                    pensamentoAI.execute();
                }
                
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    private JMenu createMenuOpcoes() {
        final JMenu menuOpcoes = new JMenu("Opções");
        final JMenuItem setupGameMenuItem = new JMenuItem("Configuar Jogo");
        
        setupGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Mesa.get().getGameSetup().promptUser();
                Mesa.get().setupUpdate(Mesa.get().getGameSetup());
            }
        });
        
        menuOpcoes.add(setupGameMenuItem);
        return menuOpcoes;
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

    private JMenuBar construirMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createExitGame());
        menuBar.add(createMenuOpcoes());
        return menuBar;
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
    
    
    enum PlayerType {
        HUMAN,
        COMPUTER
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
                                        logMovimento.adicionarMovimento(movimento);
                                        //Move Log
                                    }

                                    SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                            try {
                                                painelHistorico.remontar(tabuleiro, logMovimento);
                                                painelPecasCapturadas.remontar(logMovimento);
                                                
                                                if (gameSetup.isAIPlayer(tabuleiro.getJogadorActual())) {
                                                    Mesa.get().moveFeitoUpdate(PlayerType.HUMAN);
                                                }
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
            if (TabuleiroUtils.OITAVA_COLUNA[idQuadrado] || TabuleiroUtils.SETIMA_COLUNA[idQuadrado] || 
                TabuleiroUtils.SEXTA_COLUNA[idQuadrado]) {
                setBackground(new Color(0, 0, 0));
            } else {
                setBackground(cor);    
            }
            
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
