package chessgameai.xadrez.engine.pecas;

import chessgameai.Alliance;
import chessgameai.xadrez.engine.tabuleiro.Movimento;
import chessgameai.xadrez.engine.tabuleiro.Quadrado;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro;
import chessgameai.xadrez.engine.tabuleiro.TabuleiroUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public class Bispo extends Peca {
    private static final int[] possiveisOffsetsDoBispo = {-7, -9, 7, 9};
    
    public Bispo(int posicaoPeca, Alliance alliancePeca) {
        super(posicaoPeca, alliancePeca, TipoPeca.BISPO, true);
    }
    
    public Bispo(int posicaoPeca, Alliance alliancePeca, final boolean primeiroMovimento) {
        super(posicaoPeca, alliancePeca, TipoPeca.BISPO, primeiroMovimento);
    }

    @Override
    public ArrayList<Movimento> calcularPossiveisMovimentos(Tabuleiro tabuleiro) {
        ArrayList<Movimento> movimentosPossiveis = new ArrayList<>();
        
       for (final int currentCandidateOffset : possiveisOffsetsDoBispo) {
            int candidateDestinationCoordinate = this.posicaoPeca;
            
            while (TabuleiroUtils.isCoordenadaValida(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate) ||
                    isEighthColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentCandidateOffset;
                if (TabuleiroUtils.isCoordenadaValida(candidateDestinationCoordinate)) {
                    final Peca pieceAtDestination = tabuleiro.getQuadrado(candidateDestinationCoordinate).getPeca();
                    if (pieceAtDestination == null) {
                        movimentosPossiveis.add(new Movimento.MovimentoSemAtaque(tabuleiro, this, candidateDestinationCoordinate));
                    }
                    else {
                        final Alliance pieceAlliance = pieceAtDestination.getAlliancePeca();
                        if (this.alliancePeca != pieceAlliance) {
                            movimentosPossiveis.add(new Movimento.MajorAttackMove(tabuleiro, this, candidateDestinationCoordinate));
                        }
                        break;
                    }
                }
            }
        }
        
        return movimentosPossiveis;
    }

    @Override
    public String toString() {
        return TipoPeca.BISPO.toString();
    }

    @Override
    public Bispo movimentarPeca(Movimento move) {
        return new Bispo(move.getCoordenadaDestino(), move.getPecaMovimentada().getAlliancePeca());
    }
    private static boolean isFirstColumnExclusion(final int currentCandidate,
                                                  final int candidateDestinationCoordinate) {
        return (TabuleiroUtils.PRIMEIRA_COLUNA[candidateDestinationCoordinate] &&
                ((currentCandidate == -9) || (currentCandidate == 7)));
    }

    private static boolean isEighthColumnExclusion(final int currentCandidate,
                                                   final int candidateDestinationCoordinate) {
        return TabuleiroUtils.OITAVA_COLUNA[candidateDestinationCoordinate] &&
                        ((currentCandidate == -7) || (currentCandidate == 9));
    }
}
