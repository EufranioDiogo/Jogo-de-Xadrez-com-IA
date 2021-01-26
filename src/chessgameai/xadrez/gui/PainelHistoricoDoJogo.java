package chessgameai.xadrez.gui;
import static chessgameai.Alliance.BLACK;
import static chessgameai.Alliance.WHITE;
import chessgameai.xadrez.engine.player.Jogador;
import chessgameai.xadrez.engine.tabuleiro.Movimento;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro;
import chessgameai.xadrez.gui.Mesa.LogMovimento;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public class PainelHistoricoDoJogo extends JPanel {

    private final DataModel modelo;
    private final JScrollPane scrollPane;
    private static final Dimension DIMENSAO_PAINEL_HISTORICO_DO_JOGO = new Dimension(100, 400);
    
    public PainelHistoricoDoJogo() {
        this.setLayout(new BorderLayout());
        this.modelo = new DataModel();
        final JTable tabela = new JTable(modelo);
        tabela.setRowHeight(15);
        this.scrollPane = new JScrollPane(tabela);
        this.scrollPane.setColumnHeaderView(tabela.getTableHeader());
        this.scrollPane.setPreferredSize(DIMENSAO_PAINEL_HISTORICO_DO_JOGO);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void remontar(final Tabuleiro tabuleiro, final LogMovimento movimentoLog) {
        int linhaActual = 0;
        this.modelo.clear();
        for (final Movimento movimento : movimentoLog.getMovimentos()) {
            final String moveText = movimento.toString();
            
            if (null == movimento.getPecaMovimentada().getAlliancePeca()) {
                throw new RuntimeException("Peca movimentada com Alliance diferente que branco e preto");
            } else switch (movimento.getPecaMovimentada().getAlliancePeca()) {
                case WHITE:
                    this.modelo.setValueAt(moveText, linhaActual, 0);
                    break;
                case BLACK:
                    this.modelo.setValueAt(moveText, linhaActual, 1);
                    linhaActual++;
                    break;
                default:
                    throw new RuntimeException("Peca movimentada com Alliance diferente que branco e preto");
            }
        }
        
        if (movimentoLog.getMovimentos().size() > 0) {
            final Movimento ultimoMovimento = movimentoLog.getMovimentos().get(movimentoLog.size() - 1);
            final String moveText = ultimoMovimento.toString();
            
            if (ultimoMovimento.getPecaMovimentada().getAlliancePeca() == WHITE) {
                this.modelo.setValueAt(moveText + calcularCheckECheckMateHash(tabuleiro), linhaActual, 0);
            } else if (ultimoMovimento.getPecaMovimentada().getAlliancePeca() == BLACK) {
                this.modelo.setValueAt(moveText + calcularCheckECheckMateHash(tabuleiro), linhaActual - 1, 1);
            }
        }
        
        final JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
        validate();
    }

    private String calcularCheckECheckMateHash(final Tabuleiro tabuleiro) {
        Jogador jogadorActual = tabuleiro.getJogadorActual();
        
        if (jogadorActual.isEmCheckMate()) {
            return "#";
        }
        if (jogadorActual.isEmCheck()) {
            return "+";
        }
        return "";
    }
    
    private static class DataModel extends DefaultTableModel {
        private final List<Linha> values;
        private static final String[] HEADER = {"Brancas", "Pretas"};

        public DataModel() {
            this.values = new ArrayList<>();
        }
        
        public void clear() {
            this.values.clear();
            setRowCount(0);
        }
        
        @Override
        public int getRowCount() {
            if (this.values == null) {
                return 0;
            }
            return this.values.size();
        }
        
        @Override
        public int getColumnCount() {
            return HEADER.length;
        }
        
        @Override
        public Object getValueAt(final int linha, final int coluna) {
            final Linha linhaActual = this.values.get(linha);
            switch (coluna) {
                case 0:
                    return linhaActual.getMovimentoBranca();
                case 1:
                    return linhaActual.getMovimentoPreta();
                default:
                    throw new RuntimeException("Coluna Inválida");
            }
        }
        
        @Override
        public void setValueAt(final Object valor, final int linha, final int coluna) {
            final Linha linhaActual;
            
            if (this.values.size() <= linha) {
                linhaActual = new Linha();
                this.values.add(linhaActual);
            } else {
                linhaActual = this.values.get(linha);
            }
            
            
            if (coluna == 0) {
                linhaActual.setMovimentoBranca((String)valor);
                fireTableRowsInserted(linha, coluna);
            } else if (coluna == 1) {
                linhaActual.setMovimentoPreta((String) valor);
                fireTableCellUpdated(linha, coluna);
            }
        }
        
        @Override
        public Class<?> getColumnClass(final int coluna) {
            return Movimento.class;
        }
        
        @Override
        public String getColumnName(final int coluna) {
            return HEADER[coluna];
        }
        
    }
    
    private static class Linha {
        private String movimentoBranca;
        private String movimentoPreta;
        
        Linha() {
            
        }
        
        public String getMovimentoBranca() {
            return movimentoBranca;
        }
        
        public String getMovimentoPreta() {
            return movimentoPreta;
        }

        public void setMovimentoBranca(String movimentoBranca) {
            this.movimentoBranca = movimentoBranca;
        }

        public void setMovimentoPreta(String movimentoPreta) {
            this.movimentoPreta = movimentoPreta;
        }
    }
}
