package chessgameai.xadrez.engine.pecas;

import chessgameai.Alliance;
import static chessgameai.Alliance.BLACK;
import static chessgameai.Alliance.WHITE;
import chessgameai.xadrez.engine.tabuleiro.Movimento;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro;
import chessgameai.xadrez.engine.tabuleiro.TabuleiroUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public class Pinhao extends Peca {
    private static final int[] possiveisOffsetsDoPinhao = {8, 16, 7, 9};
    
    public Pinhao(final int posicaoPeca, final Alliance alliancePeca) {
        super(posicaoPeca, alliancePeca, TipoPeca.PINHAO, true);
    }
    
    public Pinhao(final int posicaoPeca, final Alliance alliancePeca, final boolean primeiroMovimento) {
        super(posicaoPeca, alliancePeca, TipoPeca.PINHAO, primeiroMovimento);
    }

    @Override
    public ArrayList<Movimento> calcularPossiveisMovimentos(Tabuleiro tabuleiro) {
        ArrayList<Movimento> movimentosPossiveis = new ArrayList<>();
        
        if (TabuleiroUtils.isCoordenadaValida(this.posicaoPeca)) {
            for (final int possivelOffset : possiveisOffsetsDoPinhao) {
                int coordenadaCandidata = this.getPosicaoPeca() + (possivelOffset * this.getAlliancePeca().getDirection());

                if (!TabuleiroUtils.isCoordenadaValida(coordenadaCandidata)) {
                    continue;
                }

                if (possivelOffset == 8 && !tabuleiro.getQuadrado(coordenadaCandidata).isQuadradoOcupado()) {
                    if (this.getAlliancePeca().isQuadradoPromocaoPinhao(coordenadaCandidata)) {
                      movimentosPossiveis.add(new Movimento.PromocaoPinhao(new Movimento.PinhaoMovimento(tabuleiro, this, coordenadaCandidata)));  
                    } else {
                        movimentosPossiveis.add(new Movimento.PinhaoMovimento(tabuleiro, this, coordenadaCandidata));
                    }
                } else if (possivelOffset == 16 && this.isPrimeiroMovimento() &&
                        ((TabuleiroUtils.QUARTA_LINHA[coordenadaCandidata] && this.getAlliancePeca() == WHITE) || 
                        (TabuleiroUtils.QUINTA_LINHA[coordenadaCandidata] && this.getAlliancePeca() == BLACK)) &&
                        !tabuleiro.getQuadrado(coordenadaCandidata).isQuadradoOcupado()) {

                        movimentosPossiveis.add(new Movimento.PinhaoSalto(tabuleiro, this, coordenadaCandidata));
                } else if (possivelOffset == 7 &&
                        !((TabuleiroUtils.OITAVA_COLUNA[this.posicaoPeca] && this.alliancePeca == WHITE ||
                        (TabuleiroUtils.PRIMEIRA_COLUNA[this.posicaoPeca] && this.alliancePeca == BLACK)))) {
                        if (this.getAlliancePeca().isQuadradoPromocaoPinhao(coordenadaCandidata)) {
                            movimentosPossiveis.add(new Movimento.PromocaoPinhao(new Movimento.PinhaoMovimento(tabuleiro, this, coordenadaCandidata)));  
                        } else {
                            if (tabuleiro.getQuadrado(coordenadaCandidata).isQuadradoOcupado() && 
                                this.alliancePeca != tabuleiro.getQuadrado(coordenadaCandidata).getPeca().getAlliancePeca()) {
                                movimentosPossiveis.add(new Movimento.PinhaoMovimentoAtaque(tabuleiro, this, coordenadaCandidata));
                            } else if (tabuleiro.getEnPassantPinhao() != null) {
                                if (tabuleiro.getEnPassantPinhao().getPosicaoPeca() == (this.getPosicaoPeca() + (this.getAlliancePeca().getDirection() * -1))) {
                                    final Peca pecaCandidata = tabuleiro.getEnPassantPinhao();

                                    if (this.getAlliancePeca() != pecaCandidata.getAlliancePeca()) {
                                        movimentosPossiveis.add(new Movimento.PinhaoEnPassantMovimentoAtaque(tabuleiro, this, coordenadaCandidata));
                                    }
                                }
                            }
                        }

                } else if (possivelOffset == 9 &&
                        !((TabuleiroUtils.PRIMEIRA_COLUNA[this.posicaoPeca] && this.alliancePeca == WHITE ||
                        (TabuleiroUtils.OITAVA_COLUNA[this.posicaoPeca] && this.alliancePeca == BLACK)))) {

                    if (this.getAlliancePeca().isQuadradoPromocaoPinhao(coordenadaCandidata)) {
                        movimentosPossiveis.add(new Movimento.PromocaoPinhao(new Movimento.PinhaoMovimento(tabuleiro, this, coordenadaCandidata)));  
                    } else {
                        if (tabuleiro.getQuadrado(coordenadaCandidata).isQuadradoOcupado() && 
                            this.alliancePeca != tabuleiro.getQuadrado(coordenadaCandidata).getPeca().getAlliancePeca()) {
                            movimentosPossiveis.add(new Movimento.PinhaoMovimento(tabuleiro, this, coordenadaCandidata));
                        }
                    }
                }
            }   
        }
        
        return movimentosPossiveis;
    }
    @Override
    public String toString() {
        return TipoPeca.PINHAO.toString();
    }
    @Override
    public Pinhao movimentarPeca(Movimento move) {
        return new Pinhao(move.getCoordenadaDestino(), move.getPecaMovimentada().getAlliancePeca());
    }
    
    public Rainha getPromocaoPeca() {
        return new Rainha(this.getPosicaoPeca(), this.getAlliancePeca());
    }
}
