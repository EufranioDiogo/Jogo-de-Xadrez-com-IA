package chessgameai.xadrez.gui;

import chessgameai.Alliance;
import chessgameai.xadrez.engine.pecas.Peca;
import chessgameai.xadrez.engine.tabuleiro.Movimento;
import chessgameai.xadrez.gui.Mesa.LogMovimento;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public class PainelPecasPegas extends JPanel {
    private final JPanel PAINEL_NORTE;
    private final JPanel PAINEL_SUL;
    private final String pathPecaIcon = "/home/ed/NetBeansProjects/ChessGameAI/art/";
    private final Color COR_PAINEL = Color.decode("0xFDF5E6");
    private static final Dimension DIMENSAO_PECAS_CAPTURADAS = new Dimension(40, 80);
    private static final EtchedBorder BORDA_PAINEL = new EtchedBorder(EtchedBorder.RAISED);
    
    public PainelPecasPegas() {
        super(new BorderLayout());
        setBackground(COR_PAINEL);
        setBorder(BORDA_PAINEL);
        this.PAINEL_NORTE = new JPanel(new GridLayout(8, 2));
        this.PAINEL_SUL = new JPanel(new GridLayout(8, 2));
        this.PAINEL_NORTE.setBackground(COR_PAINEL);
        this.PAINEL_SUL.setBackground(COR_PAINEL);
        
        this.add(PAINEL_NORTE, BorderLayout.NORTH);
        this.add(PAINEL_NORTE, BorderLayout.SOUTH);
    }
    
    public void remontar(final LogMovimento logsMovimentos) {
        this.PAINEL_SUL.removeAll();
        this.PAINEL_NORTE.removeAll();
        
        final ArrayList<Peca> pecasBrancas = new ArrayList<>();
        final ArrayList<Peca> pecasPretas = new ArrayList<>();
        
        
        for (final Movimento move : logsMovimentos.getMovimentos()) {
            if (move.isAtaque()) {
                final Peca pecaPega = move.getPecaAtacada();
                
                if (null == pecaPega.getAlliancePeca()) {
                    throw new RuntimeException("erro, peca a ser atacada não identificada");
                } else switch (pecaPega.getAlliancePeca()) {
                    case WHITE:
                        pecasBrancas.add(pecaPega);
                        break;
                    case BLACK:
                        pecasPretas.add(pecaPega);
                        break;
                    default:
                        throw new RuntimeException("erro, peca a ser atacada não identificada");
                }
            }
        }
        
        Collections.sort(pecasBrancas, new Comparator<Peca> () {
            @Override
            public int compare(Peca o1, Peca o2) {
                return Integer.compare(o1.getValorPeca(), o2.getValorPeca());
            }
        });
        
        Collections.sort(pecasBrancas, new Comparator<Peca> () {
            @Override
            public int compare(Peca o1, Peca o2) {
                return Integer.compare(o1.getValorPeca(), o2.getValorPeca());
            }
        });
        
        pecasBrancas.forEach((pecaPega) -> {
            try {
                final String nomeDaPeca = pecaPega.getAlliancePeca().toString() +
                        pecaPega.toString().toUpperCase() + ".gif"; 
                final File novaImagem = new File(pathPecaIcon + nomeDaPeca);
                
                final BufferedImage image = ImageIO.read(novaImagem);
                final JLabel label = new JLabel(new ImageIcon(image));
                PAINEL_NORTE.add(label);
            } catch (Exception e) {
            }
        });
        
        pecasPretas.forEach((pecaPega) -> {
            try {
                final String nomeDaPeca = pecaPega.getAlliancePeca().toString() +
                        pecaPega.toString().toUpperCase() + ".gif"; 
                final File novaImagem = new File(pathPecaIcon + nomeDaPeca);
                
                final BufferedImage image = ImageIO.read(novaImagem);
                final JLabel label = new JLabel(new ImageIcon(image));
                PAINEL_SUL.add(label);
            } catch (Exception e) {
            }
        });
        
        validate();
    }
}
